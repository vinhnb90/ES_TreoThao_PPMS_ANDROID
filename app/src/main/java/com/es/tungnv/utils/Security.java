package com.es.tungnv.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.annotation.SuppressLint;
import android.util.Base64;

public class Security {
	// private final String Key = "Esolutions@123456Esolutions@123456";

	// MD5
	private final String ALGORITHM = "md5";
	private final String DIGEST_STRING = "HG58YZ3CR9";
	private final String ASCII = "ASCII";
	private final String SECRET_KEY_ALGORITHM = "DESede";
	private final String TRANSFORMATION_PADDING = "DESede/CBC/PKCS5Padding";

	MessageDigest md;
	byte[] digestOfPassword = null;
	byte[] keyBytes = null;
	SecretKey keyMD;
	IvParameterSpec iv;
	// MD5

	// Base 64
	private final String UNICODE_FORMAT = "UTF8";
	private final String DESEDE_ENCRYPTION_SCHEME = "DESede";
	private KeySpec ks;
	private SecretKeyFactory skf;
	private Cipher cipher;
	byte[] arrayBytes;
	private String myEncryptionKey;
	private String myEncryptionScheme;
	SecretKey keyBase64;

	// Base64

	public Security() {
		// initMD5();
		initBase64();
	}

	@SuppressLint("NewApi")
	public void initMD5() {

		try {
			md = MessageDigest.getInstance(ALGORITHM);
			digestOfPassword = md.digest(DIGEST_STRING.getBytes(ASCII));
			keyBytes = Arrays.copyOf(digestOfPassword, 24);
			for (int j = 0, k = 16; j < 8;) {
				keyBytes[k++] = keyBytes[j++];
			}
			keyMD = new SecretKeySpec(keyBytes, SECRET_KEY_ALGORITHM);
			iv = new IvParameterSpec(new byte[8]);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
		}

	}

	private void initBase64() {
		myEncryptionKey = "Esolutions@123456Esolutions@123456";
		myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;

		try {
			arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
			ks = new DESedeKeySpec(arrayBytes);
			skf = SecretKeyFactory.getInstance(myEncryptionScheme);
			cipher = Cipher.getInstance(myEncryptionScheme);
			keyBase64 = skf.generateSecret(ks);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
		}
	}

	/* Encryption Method */
	@SuppressLint("NewApi")
	public String encrypt_MD5(String message) {

		Cipher cipher;
		String result = null;
		final byte[] plainTextBytes;
		final byte[] cipherText;

		try {
			cipher = Cipher.getInstance(TRANSFORMATION_PADDING);
			cipher.init(Cipher.ENCRYPT_MODE, keyMD, iv);
			plainTextBytes = message.getBytes(ASCII);
			cipherText = cipher.doFinal(plainTextBytes);
			result = new String(cipherText);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/* Decryption Method */
	@SuppressLint("NewApi")
	public String decrypt_MD5(String message) {

		Cipher decipher;
		String result = null;
		byte[] plainText = null;
		try {
			decipher = Cipher.getInstance(TRANSFORMATION_PADDING);
			decipher.init(Cipher.DECRYPT_MODE, keyMD, iv);
			plainText = decipher.doFinal(message.getBytes());
			result = new String(plainText, ASCII);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String encrypt_Base64(String unencryptedString) {
		String encryptedString = null;
		try {
			cipher.init(Cipher.ENCRYPT_MODE, keyBase64);
			byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
			byte[] encryptedText = cipher.doFinal(plainText);
			int flags = Base64.NO_WRAP; //| Base64.URL_SAFE;
			encryptedString = new String(Base64.encode(encryptedText, flags));
		} catch (Exception e) {
			return null;
		}
		return encryptedString;
	}

	public String decrypt_Base64(String encryptedString) {
		String decryptedText = null;
		try {
			cipher.init(Cipher.DECRYPT_MODE, keyBase64);
			int flags = Base64.NO_WRAP;// | Base64.URL_SAFE;
			byte[] encryptedText = Base64.decode(encryptedString.getBytes(), flags);
			byte[] plainText = cipher.doFinal(encryptedText);
			decryptedText = new String(plainText);
			return decryptedText;
		} catch (Exception e) {
			return null;
		}
	}

	public String decodeString(String in) throws Exception {
		byte[] out = Base64.decode(in, 0);
		return new String(out);
	}
}
