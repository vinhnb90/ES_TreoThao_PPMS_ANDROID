package com.es.tungnv.Aiball;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Hashtable;

import android.util.Log;

public class VideoStreamer implements Runnable {
	/**
	 * String section
	 */
	private boolean COLLECTING;
	private boolean ENABLE_AUDIO;
	private boolean IS_DEFUNCT; 

	private int IMG_CURRENT_INDEX;
	private int IMG_INDEX;
	public int RETRY = 0;
	private int RESET_AUDIO_BUFFER_COUNT = 0;
	private int RESET_FLAG;
	private int RESOLUTION_JPEG;
	private static final int IMG_FLUFF_FACTOR = 1;

	private long START_TIME;

	private String USER_NAME =null; 
	private String PASS_WORD=null;
	
	private final String TAG = VideoStreamer.class.getSimpleName();
	/**
	 * Data section
	 */
	private ArrayList<IVideoSink> mAlVideoSinks;
	private DataInputStream mDataInputStream;
	/**
	 * Others section
	 */
	private URL mURL;

	public VideoStreamer(URL url, String name, String pass) {
		mAlVideoSinks = new ArrayList<IVideoSink>();
		mURL = url;
		USER_NAME = name;
		PASS_WORD = pass;
	}

	public void addVideoSink(IVideoSink videoSink) {
		synchronized(mAlVideoSinks) {
			mAlVideoSinks.add(videoSink);
		}
	}

	public void removeVideoSink(IVideoSink videoSink) {
		synchronized(mAlVideoSinks) {
			if(mAlVideoSinks.contains(videoSink))
				mAlVideoSinks.remove(videoSink);
		}
	}

	public void enableAudio(boolean enableAudio) {
		ENABLE_AUDIO = enableAudio;
	}

	@Override
	public void run() {
		StreamSplit ssplit = null;
		try {
			//
			// Loop for a while until we either give up (hit m_retryCount), or
			// get a connection.... Sleep inbetween.
			//
			String connectionError = null;
			String ctype = null;
			Hashtable headers = null;
			URLConnection conn = null;
			String userpass = USER_NAME + ":" + PASS_WORD;
			String basicAuth = new String(Base64.encodeBytes(userpass.getBytes()));


			while(RETRY < 3) {
				try {
					conn = mURL.openConnection();
					String auth = "User-Agent";
					conn.addRequestProperty (auth, "Application\nAuthorization: Basic " + basicAuth);
					mDataInputStream = new DataInputStream(new BufferedInputStream(conn.getInputStream()));
					break;
				} catch(Exception ex) {
					Thread.sleep(1000);

					RETRY++;

				}
			}

			try {
				//
				// Read Headers for the main thing...
				//
				headers = StreamSplit.readHeaders(conn);
				ssplit = new StreamSplit(mDataInputStream);
			} catch(Exception ex) {
				for(IVideoSink videoSink : mAlVideoSinks) {
					videoSink.onInitError(ex.getMessage());
				}
				return;
			}

			COLLECTING = true;
			ctype = (String) headers.get("code-type");

			int bidx = ctype.indexOf("boundary=");
			String boundary = StreamSplit.BOUNDARY_MARKER_PREFIX;
			if (bidx != -1) {
				boundary = ctype.substring(bidx + 9);
				ctype = ctype.substring(0, bidx);
				//
				// Remove quotes around boundary string [if present]
				//
				if (boundary.startsWith("\"") && boundary.endsWith("\""))
				{
					boundary = boundary.substring(1, boundary.length()-1);
				}
				if (!boundary.startsWith(StreamSplit.BOUNDARY_MARKER_PREFIX)) {
					boundary = StreamSplit.BOUNDARY_MARKER_PREFIX + boundary;
				}
			}

			if (ctype.startsWith("multipart/x-mixed-replace")) {
				ssplit.skipToBoundary(boundary);
			}

			do {
				if (COLLECTING) 
				{
					if (boundary != null) 
					{
						headers = ssplit.readHeaders();
						//
						// Are we at the end of the m_stream?
						//
						if (ssplit.isAtStreamEnd()) 
						{
							break;
						}
						ctype = (String) headers.get("code-type");
						if (ctype == null) {
							throw new Exception("No part code type");
						}
					}
					//
					// Mixed Type -> just skip...
					//
					if (ctype.startsWith("multipart/x-mixed-replace")) 
					{
						//
						// Skip
						//
						bidx = ctype.indexOf("boundary=");
						boundary = ctype.substring(bidx + 9);
						ssplit.skipToBoundary(boundary);
					} else {
						byte[] data = ssplit.readToBoundary(boundary);
						if (data.length == 0) {
							break;
						}
						if (IMG_INDEX > IMG_FLUFF_FACTOR && START_TIME == 0) {
							START_TIME = System.currentTimeMillis();
						}

						byte[] img;
						RESET_FLAG = data[0];
						int iLength = Util.byteArrayToInt_MSB(data, 1);
						IMG_CURRENT_INDEX = Util.byteArrayToInt_MSB(data, 5);
						int resolutionJpeg = data[9];
						RESET_AUDIO_BUFFER_COUNT = data[10];
						byte[] outPCM = null;

						if(iLength > 0)
						{		
							byte[] adpcm = new byte[iLength];
							System.arraycopy(data,1 + 4 + 4 + 1 + 1 , adpcm, 0, iLength);

							if(ENABLE_AUDIO) {
								outPCM = ADPCMDecoder.decode(adpcm);
							}			/*
									System.arraycopy(outPCM,0, m_Audio, 0, outPCM.length);
									m_audioLength = outPCM.length;
							 */
							int imageDataLen =data.length - (1 + 4 + 4 + 1 + 1 + iLength); 
							img = new byte[imageDataLen];
							System.arraycopy(data,1 + 4 + 4 + 1 + 1 + iLength,img,0,img.length);
							//m_count_test = 0;
						}else{
							int imageDataLen = data.length - (1 + 4 + 4 + 1 + 1);
							img = new byte[imageDataLen];
							System.arraycopy(data,1 + 4 + 4 + 1 + 1,img,0,img.length);
						}

						//m_count_test++;
						updateImage(ctype, img, outPCM);
						/*
							}else{

								_resolutionJpeg = data[0];
								byte[] img = new byte[data.length - 1];
								System.arraycopy(data,1,img,0,img.length);
								updateImage(ctype,img);
					//		}
						 */
					}
				}
				/*
					try {
						Thread.sleep(20);
					} catch (InterruptedException ie) {
					}
				 */
			} while (!IS_DEFUNCT);
		} catch (Exception e) {
		} finally {
			stop();
		}
	}

	public void stop()
	{
		COLLECTING = false;
		IS_DEFUNCT = true;
		try {
			if (mDataInputStream != null)
			{
				mDataInputStream.close();
			}
			mDataInputStream = null;
		} catch (Exception e) {
		}
	}

	private void updateImage(String content_type, byte[] image, byte[] pcm) {
		/**
		 * Used IP address of Trek Ai-ball is okay,
		 * If use external IP address, need modify something
		 */
		try{
			IMG_INDEX++;
			for(IVideoSink videoSink : mAlVideoSinks) {
				videoSink.onFrame(image, pcm);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public int getResetFlag() {
		return RESET_FLAG;
	}

	public int getResetAudioBufferCount() {
		return RESET_AUDIO_BUFFER_COUNT;
	}

	public int getImgCurrentIndex()
	{
		return IMG_CURRENT_INDEX;
	}
}