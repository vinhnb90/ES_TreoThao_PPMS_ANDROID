package com.es.tungnv.Aiball;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;

import com.es.tungnv.utils.TthtConstantVariables;

public class Util {
	public static byte[] intToByteArray_MSB(int value) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = (b.length - 1 - i) * 8;
            b[i] = (byte) ((value >>> offset) & 0xFF);
        }
        return b;
    }
	public static int byteArrayToInt_MSB(byte[] b, int offset) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i + offset] & 0x000000FF) << shift;
        }
        return value;
    }
	public static void intToByteArray_LSB(int value,byte[] abyte0)
	{
		abyte0[3] = (byte)(value / 0x1000000);
        value %= 0x1000000;
        abyte0[2] = (byte)(value / 0x10000);
        value %= 0x10000;
        abyte0[1] = (byte)(value / 256);
        value %= 256;
        abyte0[0] = (byte)value;
	}
	public static void shortToByteArray_LSB(short word0, byte abyte0[])
	{
	        abyte0[1] = (byte)(word0 >>> 8);
	        abyte0[0] = (byte)(word0 & 0xff);
	}
	public static void memSet(byte[] arr, int len,byte value)
	{
		for(int i = 0 ; i < len;i++)
		{
			arr[i] = value;
		}
	}
	
	public static String getRecordFileName() {
		String directory = getRecordDirectory();
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyddMM_HHmmss");
		return directory + File.separator + formatter.format(date) + ".avi";
	}

	public static String getSnapshotFileName(String name) {
		String directory = getRecordDirectory();
		return directory + name + ".jpg";
	}
	
	public static String getRecordDirectory() {
		String path = Environment.getExternalStorageDirectory().getPath() + TthtConstantVariables.PROGRAM_PHOTOS_PATH;
		File f = new File(path);
		if(!f.exists()) {
			f.mkdir();
		}
		return path;
	}
	
	public static File[] getRecordFiles() {
		File recordDir = new File(getRecordDirectory());
		return recordDir.listFiles();
	}
	
	public static void deleteVideo(String filename) {
		File f = new File(getRecordDirectory() + filename);
		f.delete();
	} 
}
