package com.es.tungnv.webservice;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;

import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.GcsCommon;
import com.es.tungnv.utils.GcsConstantVariables;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class GcsAsyncCallWS {

	private Common comm = new Common();
	private String URL_SERVICE;

	@SuppressWarnings("static-access")
	public GcsAsyncCallWS() {
		this.URL_SERVICE = "http://" + GcsCommon.cfgInfo.getIP_SERVICE() + "/WSGCS/Service1.asmx";
	}

	// ///////////////////////////////////////////////////

	/** Tải về 1 file bất kỳ
	 *
	 * @param _DirPath
	 * @param _FileName
	 * @return
	 */
	private String DownloadSingleFile(String _DirPath, String _FileName) {
		String METHOD_NAME = "DownloadChunk";
		try {

			int numRetries = 0;
			int maxRetries = 50;
			int Offset = 0;
			int ChunkSize = 50 * 1024; // 64 * 1024 kb

			byte[] bloc = new byte[0];

			SoapObject request = new SoapObject(GcsConstantVariables.NAMESPACE, METHOD_NAME);
			request.addProperty("FileName", _FileName);
			request.addProperty("Offset", Offset);
			request.addProperty("BufferSize", ChunkSize);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(GcsCommon.cfgInfo.getIP_SERVICE());

			androidHttpTransport.call(GcsConstantVariables.NAMESPACE + METHOD_NAME, envelope);

			SoapPrimitive resultsRequestSOAP;
			String result = "";

			File FileDownload = new File(_DirPath, _FileName);

			if (FileDownload.exists()) {
				FileDownload.delete();
			}

			long FileSize = GetFileSize(_FileName);

			while (Offset < FileSize) {
				try {

					request.setProperty(1, Offset);

					androidHttpTransport.call(GcsConstantVariables.NAMESPACE + METHOD_NAME,
							envelope);
					resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
					result = resultsRequestSOAP.toString();

					byte[] blocTmp = Base64.decode(result, Base64.DEFAULT);
					byte[] blocCopy = bloc.clone();
					bloc = new byte[blocCopy.length + blocTmp.length];

					if (Offset > 0)
						// copy mảng blocCopy vào mảng bloc
						System.arraycopy(blocCopy, 0, bloc, 0, blocCopy.length);

					// thêm mảng blocTmp vào mảng bloc
					System.arraycopy(blocTmp, 0, bloc, blocCopy.length, blocTmp.length);

					Offset += ChunkSize; // save the offset position for resume
				} catch (Exception ex) {
					// too many retries, bail out
					if (numRetries++ >= maxRetries) {
						throw new Exception();
					}
				}
			}

			if (bloc.length > 0) {
				comm.ConvertByteToFile(bloc, FileDownload.getAbsolutePath());
				return null;
			}

			return null;
		} catch (Exception e) {
			return "FAIL";
		}
	}

	/** Tải vể nhiều file bất kỳ
	 *
	 * @param _DirPath
	 * @param _arrFileName
	 * @return
	 */
	private String DownloadMultiFiles(String _DirPath, String[] _arrFileName) {

		try {
			for (String _FileName : _arrFileName) {
				DownloadSingleFile(_DirPath, _FileName);
			}
			return null;

		} catch (Exception e) {
			return "FAIL";
		}
	}

	/** Kiểm tra sổ có thể thao tác ko
	 *
	 * @param MA_DVIQLY
	 * @param TEN_FILE
	 * @return
	 */
	@SuppressWarnings("unused")
	private int CheckChotSo(String MA_DVIQLY, String TEN_FILE) {
		// kiểm tra sổ đã chốt chưa
		try {
			String METHOD_NAME = "CheckChotSo";
			SoapObject request = new SoapObject(GcsConstantVariables.NAMESPACE, METHOD_NAME);
			request.addProperty("MA_DVIQLY", MA_DVIQLY);
			request.addProperty("TEN_FILE", TEN_FILE);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					this.URL_SERVICE);

			androidHttpTransport.call(GcsConstantVariables.NAMESPACE + METHOD_NAME, envelope);

			SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope
					.getResponse();

			int result = Integer
					.parseInt(resultsRequestSOAP.toString());

			return result;
		} catch (Exception e) {
			return 1;
		}
	}

	/** Lấy danh sách sổ mà nhân viên có thể cập nhật lên server
	 *
	 * @param MA_DVIQLY
	 * @param MA_NVGCS
	 * @return
	 */
	private String[] GetMaSoGCSOfNV(String MA_DVIQLY, String MA_NVGCS) {
		String METHOD_NAME = "GetMaSoGCSOfNV";
		String[] _arrMaSoGCS;
		try {

			SoapObject request = new SoapObject(GcsConstantVariables.NAMESPACE, METHOD_NAME);
			request.addProperty("MA_DVIQLY", MA_DVIQLY);
			request.addProperty("MA_NVGCS", MA_NVGCS);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					this.URL_SERVICE);

			androidHttpTransport.call(GcsConstantVariables.NAMESPACE + METHOD_NAME, envelope);

			SoapObject yourResponseObject = (SoapObject) envelope.bodyIn;
			SoapObject arrStr = (SoapObject) yourResponseObject.getProperty(0);

			_arrMaSoGCS = new String[arrStr.getPropertyCount()];

			for (int i = 0; i < _arrMaSoGCS.length; i++) {
				_arrMaSoGCS[i] = arrStr.getProperty(i).toString();
			}

			return _arrMaSoGCS;
		} catch (Exception e) {
			return null;
		}
	}

	/** Chuyển 1 file bất kỳ lên server
	 *
	 * @param _FileName
	 * @return
	 */
	private String UploadSingleFile(String _FileName) {
		try {
			if (_FileName == null || _FileName.length() == 0)
				return "Tên file không hợp lệ";

			// upload file
			String METHOD_NAME = "UploadChunk_Mobile";
			int offset = 0;
			int ChunkSize = 100 * 1024;
			int sizeTransfer = 0;

			File _FileTransfer = new File( _FileName);//_Dir,
			if (!_FileTransfer.exists()) {
				return "File không tồn tại";
			}

			// convert file cần transfer thành byte[]
			byte[] bloc = comm.FileToByteArray(_FileName); //_Dir + "/" +
			byte[] blocChunk;

			SoapObject request = new SoapObject(GcsConstantVariables.NAMESPACE, METHOD_NAME);
			request.addProperty("FileName", _FileName);
			request.addProperty("buffer", bloc);
			request.addProperty("Offset", offset);
			request.addProperty("MA_DVIQLY", GcsCommon.getMaDviqly());
			request.addProperty("IMEI", GcsCommon.getIMEI());

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.encodingStyle = SoapEnvelope.ENC;
			envelope.setOutputSoapObject(request);
			envelope.bodyOut = request;
			// envelope.encodingStyle = SoapSerializationEnvelope.XSD;
			new MarshalBase64().register(envelope); // serialization

			HttpTransportSE androidHttpTransport = new HttpTransportSE(this.URL_SERVICE);
			// androidHttpTransport.debug = true;

			long FileSize = _FileTransfer.length();
			String result = null;
			while (offset < FileSize) {
				if (FileSize - offset >= ChunkSize) {
					sizeTransfer = ChunkSize;
				} else {
					sizeTransfer = (int) (FileSize - (long) offset);
				}
				blocChunk = new byte[sizeTransfer];
				System.arraycopy(bloc, offset, blocChunk, 0, sizeTransfer);
				request.setProperty(1, blocChunk);
				request.setProperty(2, offset);
				androidHttpTransport.call(GcsConstantVariables.NAMESPACE + METHOD_NAME, envelope);

				SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();

				if(resultsRequestSOAP != null)
					result = resultsRequestSOAP.toString();
				if(result != null)
					return result;

				offset += ChunkSize;

			}

			// androidHttpTransport.reset();

			return null;

		} catch (Exception e) {
			return "FAIL";
		}
	}

	/** Chuyển nhiều file bất kỳ lên server
	 *
	 * @param _arrFileName
	 * @return
	 */
	private String UploadMultiFiles(String[] _arrFileName) {

		try {

			int success = 0;
			for (int i = 0; i < _arrFileName.length; i++) {
				if(UploadSingleFile(_arrFileName[i]) == null)
					success++;
			}

			// Update to sv
			//success = Integer.parseInt(UpdateServerFromFile(_arrFileName));

			return "Cập nhật thành công " + success + " sổ";

		} catch (Exception e) {
			return "FAIL";
		}
	}

	/** Cập nhật db sqlite vừa đẩy lên vào sql server
	 *
	 * @param _arrFileName
	 * @return
	 * @throws Exception
	 */
	private String UpdateServerFromFile(String[] _arrFileName) throws Exception {
		if (_arrFileName == null || _arrFileName.length == 0)
			return "Không có file cập nhật";
		String METHOD_NAME = "UpdateGCS_CHISO_HHU";
		String lstFileName = comm.ConvertArray2String(_arrFileName);

		if(lstFileName == null)
			throw new Exception( "Không có sổ để cập nhật");

		SoapObject request = new SoapObject(GcsConstantVariables.NAMESPACE, METHOD_NAME);
		request.addProperty("MA_NVIEN", GcsCommon.getMaDviqly());
		request.addProperty("MA_NVGCS", GcsCommon.getMaNvgcs());
		request.addProperty("IMEI", GcsCommon.getIMEI());
		request.addProperty("lstFileName", lstFileName);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		// envelope.encodingStyle = SoapEnvelope.ENC;
		envelope.setOutputSoapObject(request);
		// envelope.bodyOut = request;
		// envelope.encodingStyle = SoapSerializationEnvelope.XSD;
		// new MarshalBase64().register(envelope); // serialization

		HttpTransportSE androidHttpTransport = new HttpTransportSE(this.URL_SERVICE);

		// androidHttpTransport.debug = true;

		androidHttpTransport.call(GcsConstantVariables.NAMESPACE + METHOD_NAME, envelope);

		// androidHttpTransport.reset();

		SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();

		int result = Integer.parseInt(resultsRequestSOAP.toString());

		return result+"";

	}

	/** Lấy kích thước file
	 *
	 * @param _FileName
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private long GetFileSize(String _FileName) throws IOException,
			XmlPullParserException {

		String METHOD_NAME = "GetFileSize";
		SoapObject request = new SoapObject(GcsConstantVariables.NAMESPACE, METHOD_NAME);
		request.addProperty("FileName", _FileName);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(
				this.URL_SERVICE);

		androidHttpTransport.call(GcsConstantVariables.NAMESPACE + METHOD_NAME, envelope);

		SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope
				.getResponse();

		long result = Long.parseLong(resultsRequestSOAP.toString());

		return result;
	}

	/** Lấy thông tin người dùng đăng nhập
	 *
	 * @param Username
	 * @param Password
	 * @return
	 */
	@SuppressWarnings("unused")
	private String UserLogin(String Username, String Password) {

		String METHOD_NAME = "UserLogin";

		try {
			SoapObject request = new SoapObject(GcsConstantVariables.NAMESPACE, METHOD_NAME);
			request.addProperty("userName", Username);
			request.addProperty("pass", Password);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					this.URL_SERVICE);

			androidHttpTransport.call(GcsConstantVariables.NAMESPACE + METHOD_NAME, envelope);

			// bước này lấy cả file XML
			SoapObject responseBody = (SoapObject) envelope.getResponse();
			// bước này bỏ các thông tin XML chỉ lấy kết quả trả về
			responseBody = (SoapObject) responseBody.getProperty(1);
			// Lấy thông tin XML của bảng trả về
			SoapObject table = (SoapObject) responseBody.getProperty(0);
			int num_row = table.getPropertyCount();
			SoapObject tableRow = null;

			// Ở đây lấy thông tin từng dòng của table, chú ý 0 là dòng đầu tiên
			tableRow = (SoapObject) table.getProperty(0);

			if (tableRow != null) {
				int LOAI_NV = Integer.parseInt(tableRow.getProperty("LOAI_NV")
						.toString());
				if (LOAI_NV == 2 || LOAI_NV == 0 || LOAI_NV == 3) {
					GcsCommon.setMaDviqly(tableRow.getProperty("MA_DVIQLY").toString());
					GcsCommon.setMaNvgcs(tableRow.getProperty("MA_NV").toString());
					GcsCommon.setMaDql(tableRow.getProperty("MA_DQL").toString());
				} else {
					return "Tài khoản không hợp lệ";
				}
			} else {
				throw new Exception("FAIL");
			}

			return "SUCCESS";
		} catch (Exception e) {
			if (e.getMessage() != null
					&& e.getMessage().contains("HTTP status: 404"))
				return "Sai địa chỉ web service";
			else if (e.getMessage() != null
					&& e.getMessage().contains("HTTP status: 500"))
				return "Kiểm tra lại kết nối";
			else
				return "Đăng nhập thất bại";
		}

	}

	/** Kiểm tra IMEI của máy gửi lên server có hợp lệ ko
	 *
	 * @param IMEI
	 * @return
	 */
	private boolean CheckIMEI(String IMEI) {
		try {
			String METHOD_NAME = "CheckIMEI";
			SoapObject request = new SoapObject(GcsConstantVariables.NAMESPACE, METHOD_NAME);
			request.addProperty("IMEI", IMEI);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					this.URL_SERVICE);

			androidHttpTransport.call(GcsConstantVariables.NAMESPACE + METHOD_NAME, envelope);

			SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope
					.getResponse();

			boolean result = Boolean
					.parseBoolean(resultsRequestSOAP.toString());

			return result;
		} catch (Exception e) {
			return false;
		}
	}


	/** Cập nhật db sqlite vừa đẩy lên vào server
	 *
	 * @param TEN_FILE danh sách sổ cần cập nhật vào server
	 * @return
	 */
	public String UploadSoGCS(String[] TEN_FILE)
	{
		try {
			if (TEN_FILE == null || TEN_FILE.length == 0)
				return "Gửi sổ lên máy chủ thất bại có thể do: \n- Chưa chọn sổ \n- Sổ định đẩy lên server có thể đang bị khóa \n- Sổ không có trên máy";

//			String LIST_TEN_FILE = comm.ConvertArray2String(TEN_FILE);

			// upload file
			String METHOD_NAME = "UploadChunk_Mobile";
			int offset = 0;
			int ChunkSize = 102400; //100 * 1024
			int sizeTransfer = 0;
//			String fileFullname = Environment.getExternalStorageDirectory().toString() + "/ESGCS/DB/ESGCS.s3db";
			String fileFullname = Environment.getExternalStorageDirectory().toString() + "/ESGCS/ESGCS.zip";
			File _FileTransfer = new File(fileFullname);//_Dir,
			if (!_FileTransfer.exists()) {
				return "File không tồn tại";
			}

			// tạo ra file db sqlite chứa các sổ upload lên server


			// convert file cần transfer thành byte[]
			byte[] bloc = comm.FileToByteArray(_FileTransfer.getAbsolutePath()); //_Dir + "/" +
			byte[] blocChunk;

			SoapObject request = new SoapObject(GcsConstantVariables.NAMESPACE, METHOD_NAME);
			request.addProperty("FileName", "ESGCS_" + GcsCommon.getIMEI() + ".zip"); // đặt tên file khi tải lên server
			request.addProperty("buffer", bloc);
			request.addProperty("Offset", offset);
			request.addProperty("MA_DVIQLY", GcsCommon.getMaDviqly());
			request.addProperty("IMEI", GcsCommon.getIMEI());

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.encodingStyle = SoapEnvelope.ENC;
			envelope.setOutputSoapObject(request);
			envelope.bodyOut = request;
			// envelope.encodingStyle = SoapSerializationEnvelope.XSD;
			new MarshalBase64().register(envelope); // serialization

			HttpTransportSE androidHttpTransport = new HttpTransportSE(this.URL_SERVICE);
			// androidHttpTransport.debug = true;

			long FileSize = _FileTransfer.length();
			String result = null;
			while (offset < FileSize) {
				if (FileSize - offset >= ChunkSize) {
					sizeTransfer = ChunkSize;
				} else {
					sizeTransfer = (int) (FileSize - (long) offset);
				}
				blocChunk = new byte[sizeTransfer];
				System.arraycopy(bloc, offset, blocChunk, 0, sizeTransfer);
				request.setProperty(1, blocChunk);
				request.setProperty(2, offset);
				androidHttpTransport.call(GcsConstantVariables.NAMESPACE + METHOD_NAME, envelope);

				SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();

				if(resultsRequestSOAP != null)
					result = resultsRequestSOAP.toString();
				if(result != null)
					return result;

				offset += ChunkSize;
			}

			// androidHttpTransport.reset();

			// Update to server
			Integer.parseInt(UpdateServerFromFile(TEN_FILE)); // cập nhật các sổ vừa đẩy lên vào server

			return "Cập nhật thành công " + TEN_FILE.length + " sổ";

		} catch (Exception e) {
			return "Gửi dữ liệu thất bại: "+ e.toString();
		}

	}

	/** Lấy sổ về thiết bị Android
	 *
	 * @param TEN_FILE
	 * @return
	 */
	public String DownloadSoGCS(String[] TEN_FILE, String FILE_NAME) {

		try {
			if (!CheckIMEI(GcsCommon.getIMEI())) {
				return "Thiết bị chưa đăng ký";
			}

			if (TEN_FILE.equals(null) && TEN_FILE.length < 1)
				return "Bạn không được phân sổ nào";

			String LIST_TEN_FILE = comm.ConvertArray2String(TEN_FILE);
			byte[] bloc = null;
			String result = null;

			String sdCardPath = Environment.getExternalStorageDirectory()
					.toString();
			String METHOD_NAME = "DownloadSoGCS_MOBILE";
			SoapPrimitive resultsRequestSOAP = null;
			SoapObject request = new SoapObject(GcsConstantVariables.NAMESPACE, METHOD_NAME);

			request.addProperty("MA_DVIQLY", GcsCommon.getMaDviqly());
			request.addProperty("MA_NVGCS", GcsCommon.getMaNvgcs());
			request.addProperty("IMEI", GcsCommon.getIMEI());
			request.addProperty("TEN_FILE", LIST_TEN_FILE);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(this.URL_SERVICE);

			androidHttpTransport.call(GcsConstantVariables.NAMESPACE + METHOD_NAME, envelope);
			resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
			if (resultsRequestSOAP != null) {
				result = resultsRequestSOAP.toString();
				bloc = Base64.decode(result, Base64.DEFAULT);
			}
			
			// create file
			File FileDownload = new File(sdCardPath + "/ESGCS/Photo/", FILE_NAME);
			if (FileDownload.exists()) {
				FileDownload.delete();
			}
			comm.ConvertByteToFile(bloc, FileDownload.getAbsolutePath());

			return "Đã tải về " + TEN_FILE.length + " sổ";

		} catch (Exception e) {
			return "Tải về thất bại";
		}
	}
	
	
	// ////////// METHOD Call WEB SERVICE ///////////////////
	public String WS_UPLOAD_SINGLE_FILE_CALL(File fileTransfer) {
		WS_UPLOAD_SINGLE_FILE ws_upload_1_file = new WS_UPLOAD_SINGLE_FILE(
				fileTransfer);
		ws_upload_1_file.execute();
		try {
			return ws_upload_1_file.get();
		} catch (InterruptedException e) {
			return "FAIL";
		} catch (ExecutionException e) {
			return "FAIL";
		}
	}

	public String WS_UPLOAD_MULTI_FILES_CALL(String[] arrFileName) {
		WS_UPLOAD_MULTI_FILES ws_upload_multi_files = new WS_UPLOAD_MULTI_FILES(arrFileName);
		ws_upload_multi_files.execute();
		try {
			return ws_upload_multi_files.get();
		} catch (InterruptedException e) {
			return "FAIL";
		} catch (ExecutionException e) {
			return "FAIL";
		}
	}

	public String WS_DOWNLOAD_SINGLE_FILE_CALL(File fileTransfer) {
		WS_DOWNLOAD_SINGLE_FILE ws_download_1_file = new WS_DOWNLOAD_SINGLE_FILE(
				fileTransfer);
		ws_download_1_file.execute();
		try {
			return ws_download_1_file.get();
		} catch (InterruptedException e) {
			return "FAIL";
		} catch (ExecutionException e) {
			return "FAIL";
		}
	}

	public String WS_DOWNLOAD_MULTI_FILES_CALL(String DirPath,
			String[] arrFileName) {
		WS_DOWNLOAD_MULTI_FILES ws_download_multi_files = new WS_DOWNLOAD_MULTI_FILES(
				DirPath, arrFileName);
		ws_download_multi_files.execute();
		try {
			return ws_download_multi_files.get();
		} catch (InterruptedException e) {
			return "FAIL";
		} catch (ExecutionException e) {
			return "FAIL";
		}
	}

	public String WS_USER_LOGIN_CALL(String username, String pass) {
		WS_USER_LOGIN ws_userLogin = new WS_USER_LOGIN(username, pass);
		ws_userLogin.execute();
		try {
			return ws_userLogin.get();
		} catch (InterruptedException e) {
			return "Đăng nhập thất bại";
		} catch (ExecutionException e) {
			return "Đăng nhập thất bại";
		}
	}

	public String WS_DOWNLOAD_SO_GCS_CALL(String MA_DVIQLY, String MA_NVGCS,
			String[] MaSo, String FILE_NAME) {
		WS_DOWNLOAD_SO_GCS ws = new WS_DOWNLOAD_SO_GCS(MaSo, FILE_NAME);
		ws.execute();
		try {
			return ws.get();
		} catch (InterruptedException e) {
			return "FAIL";
		} catch (ExecutionException e) {
			return "FAIL";
		}
	}
	
	public String WS_UPLOAD_SO_GCS_CALL(String MA_DVIQLY, String MA_NVGCS,
			String[] MaSo) {
		WS_UPLOAD_SO_GCS ws = new WS_UPLOAD_SO_GCS(MaSo);
		ws.execute();
		try {
			return ws.get();
		} catch (InterruptedException e) {
			return "FAIL";
		} catch (ExecutionException e) {
			return "FAIL";
		}
	}

	public String[] WS_GET_TEN_FILE_OF_NV_CALL(String MA_DVIQLY, String MA_NVGCS) {
		WS_GET_TEN_FILE_OF_NV ws = new WS_GET_TEN_FILE_OF_NV(MA_DVIQLY,
				MA_NVGCS);
		ws.execute();
		try {
			return ws.get();
		} catch (InterruptedException e) {
			return null;
		} catch (ExecutionException e) {
			return null;
		}
	}

	
	
	
	
	// /////////// Class Call WEB SERVICE ///////////////////
	private class WS_UPLOAD_SINGLE_FILE extends
			AsyncTask<String, Integer, String> {
		private File fileTransfer;

		public WS_UPLOAD_SINGLE_FILE(File fileTransfer) {
			this.fileTransfer = fileTransfer;
		}

		protected String doInBackground(String... params) {
			// Log.i(TAG, "doInBackground");

			return UploadSingleFile(fileTransfer.getName());
		}

		@Override
		protected void onPostExecute(String result) {
			// Log.i(TAG, "onPostExecute");
		}

		@Override
		protected void onPreExecute() {
			// Log.i(TAG, "onPreExecute");
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// Log.i(TAG, "onProgressUpdate");
		}

	}

	private class WS_UPLOAD_MULTI_FILES extends
			AsyncTask<String, Integer, String> {
		private String[] arrFileName;

		public WS_UPLOAD_MULTI_FILES(String[] arrFileName) {
			this.arrFileName = arrFileName;
		}

		protected String doInBackground(String... params) {
			// Log.i(TAG, "doInBackground");

			return UploadMultiFiles(arrFileName);
		}

		@Override
		protected void onPostExecute(String result) {
			// Log.i(TAG, "onPostExecute");
		}

		@Override
		protected void onPreExecute() {
			// Log.i(TAG, "onPreExecute");
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// Log.i(TAG, "onProgressUpdate");
		}

	}

	private class WS_DOWNLOAD_SINGLE_FILE extends
			AsyncTask<String, Integer, String> {
		private File fileTransfer;

		public WS_DOWNLOAD_SINGLE_FILE(File fileTransfer) {
			this.fileTransfer = fileTransfer;
		}

		protected String doInBackground(String... params) {
			// Log.i(TAG, "doInBackground");

			return DownloadSingleFile(fileTransfer.getAbsolutePath(),
					fileTransfer.getName());
		}

		@Override
		protected void onPostExecute(String result) {
			// Log.i(TAG, "onPostExecute");
		}

		@Override
		protected void onPreExecute() {
			// Log.i(TAG, "onPreExecute");
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// Log.i(TAG, "onProgressUpdate");
		}

	}

	private class WS_DOWNLOAD_MULTI_FILES extends
			AsyncTask<String, Integer, String> {
		private String DirPath;
		private String[] arrFileName;

		public WS_DOWNLOAD_MULTI_FILES(String DirPath, String[] arrFileName) {
			this.DirPath = DirPath;
			this.arrFileName = arrFileName;
		}

		protected String doInBackground(String... params) {
			// Log.i(TAG, "doInBackground");

			return DownloadMultiFiles(DirPath, arrFileName);
		}

		@Override
		protected void onPostExecute(String result) {
			// Log.i(TAG, "onPostExecute");
		}

		@Override
		protected void onPreExecute() {
			// Log.i(TAG, "onPreExecute");
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// Log.i(TAG, "onProgressUpdate");
		}

	}

	private class WS_USER_LOGIN extends AsyncTask<String, Integer, String> {
		String Username;
		String Password;

		public WS_USER_LOGIN(String Username, String Password) {
			this.Username = Username;
			this.Password = Password;
		}

		protected String doInBackground(String... params) {
			// Log.i(TAG, "doInBackground");

			return UserLogin(Username, Password);
		}

		@Override
		protected void onPostExecute(String result) {
			// Log.i(TAG, "onPostExecute");
		}

		@Override
		protected void onPreExecute() {
			// Log.i(TAG, "onPreExecute");
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// Log.i(TAG, "onProgressUpdate");
		}

	}

	private class WS_DOWNLOAD_SO_GCS extends AsyncTask<String, Integer, String> {
		String[] ARRAY_TEN_FILE;
		String FILE_NAME;

		public WS_DOWNLOAD_SO_GCS(String[] ARRAY_TEN_FILE, String FILE_NAME) {
			this.ARRAY_TEN_FILE = ARRAY_TEN_FILE;
			this.FILE_NAME = FILE_NAME;
		}

		protected String doInBackground(String... params) {
			// Log.i(TAG, "doInBackground");

			return DownloadSoGCS(ARRAY_TEN_FILE, FILE_NAME);
		}

		@Override
		protected void onPostExecute(String result) {
			// Log.i(TAG, "onPostExecute");
		}

		@Override
		protected void onPreExecute() {
			// Log.i(TAG, "onPreExecute");
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// Log.i(TAG, "onProgressUpdate");
		}

	}
	
	private class WS_UPLOAD_SO_GCS extends AsyncTask<String, Integer, String> {
		String[] ARRAY_TEN_FILE;

		public WS_UPLOAD_SO_GCS(String[] ARRAY_TEN_FILE) {
			this.ARRAY_TEN_FILE = ARRAY_TEN_FILE;
		}

		protected String doInBackground(String... params) {
			// Log.i(TAG, "doInBackground");

			return UploadSoGCS(ARRAY_TEN_FILE);
		}

		@Override
		protected void onPostExecute(String result) {
			// Log.i(TAG, "onPostExecute");
		}

		@Override
		protected void onPreExecute() {
			// Log.i(TAG, "onPreExecute");
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// Log.i(TAG, "onProgressUpdate");
		}

	}

	private class WS_GET_TEN_FILE_OF_NV extends
			AsyncTask<String, Integer, String[]> {
		String MA_NVGCS;
		String MA_DVIQLY;

		public WS_GET_TEN_FILE_OF_NV(String MA_DVIQLY, String MA_NVGCS) {
			this.MA_NVGCS = MA_NVGCS;
			this.MA_DVIQLY = MA_DVIQLY;
		}

		@Override
		protected String[] doInBackground(String... params) {
			// Log.i(TAG, "doInBackground");

			return GetMaSoGCSOfNV(MA_DVIQLY, MA_NVGCS);
		}

		@Override
		protected void onPostExecute(String[] result) {
			// Log.i(TAG, "onPostExecute");
		}

		@Override
		protected void onPreExecute() {
			// Log.i(TAG, "onPreExecute");
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// Log.i(TAG, "onProgressUpdate");
		}

	}

}
