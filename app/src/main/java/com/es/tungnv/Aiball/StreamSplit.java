package com.es.tungnv.Aiball;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Hashtable;

public class StreamSplit {
	/**
	 * String section
	 */
	private boolean STREAM_END;
	public static final String BOUNDARY_MARKER_PREFIX  = "--";
	public static final String BOUNDARY_MARKER_TERM    = BOUNDARY_MARKER_PREFIX;
	/**
	 * Data section
	 */
	protected DataInputStream mDataInputStream;


	public StreamSplit(DataInputStream dis)
	{
		mDataInputStream = dis;
		STREAM_END = false;
	}


	public Hashtable readHeaders() throws IOException
	{
		Hashtable ht = new Hashtable();
		String response = null;
		boolean satisfied = false;

		do {
			response = mDataInputStream.readLine();
			if (response == null) {
				STREAM_END = true;
				break;
			} else if (response.equals("")) {
				if (satisfied) {
					break;
				} else {
					// Carry on...
				}
			} else {
				satisfied = true;
			}
			addPropValue(response, ht);
		} while (true);
		return ht;
	}

	protected static void addPropValue(String response, Hashtable ht)
	{
		int idx = response.indexOf(":");
		if (idx == -1) {
			return;
		}
		String tag = response.substring(0, idx);
		String val = response.substring(idx + 1).trim();
		ht.put(tag.toLowerCase(), val);
	}


	public static Hashtable readHeaders(URLConnection conn)
	{
		Hashtable ht = new Hashtable();
		boolean foundAny = false;
		int i = 0;
		do {
			String key = conn.getHeaderFieldKey(i);
			if (key == null) {
				if (i == 0) {
					i++;
					continue;
				} else {
					break;
				}
			}
			String val = conn.getHeaderField(i);
			ht.put(key.toLowerCase(), val);
			i++;
		} while (true);
		return ht;
	}


	public void skipToBoundary(String boundary) throws IOException
	{
		readToBoundary(boundary);
	}


	public byte[] readToBoundary(String boundary) throws IOException
	{
		ResizableByteArrayOutputStream baos = new ResizableByteArrayOutputStream();
		StringBuffer lastLine = new StringBuffer();
		int lineidx = 0;
		int chidx = 0;
		byte ch;
		do {
			try {
				ch = mDataInputStream.readByte();
			} catch (EOFException e) {
				STREAM_END = true;
				break;
			}
			if (ch == '\n' || ch == '\r') {
				//
				// End of line... Note, this will now look for the boundary
				// within the line - more flexible as it can habdle
				// arfle--boundary\n  as well as
				// arfle\n--boundary\n
				//
				String lls = lastLine.toString();
				int idx = lls.indexOf(BOUNDARY_MARKER_PREFIX);
				if (idx != -1) {
					lls = lastLine.substring(idx);
					if (lls.startsWith(boundary)) {
						//
						// Boundary found - check for termination
						//
						String btest = lls.substring(boundary.length());
						if (btest.equals(BOUNDARY_MARKER_TERM)) {
							STREAM_END = true;
						}
						chidx = lineidx+idx;
						break;
					}
				}
				lastLine = new StringBuffer();
				lineidx = chidx + 1;
			} else {
				lastLine.append((char) ch);
			}
			chidx++;
			baos.write(ch);
		} while (true);
		//
		baos.close();
		baos.resize(chidx);
		return baos.toByteArray();
	}


	public boolean isAtStreamEnd()
	{
		return STREAM_END;
	}
}


class ResizableByteArrayOutputStream extends ByteArrayOutputStream {
	public ResizableByteArrayOutputStream()
	{
		super();
	}


	public void resize(int size)
	{
		count = size;
	}
}
