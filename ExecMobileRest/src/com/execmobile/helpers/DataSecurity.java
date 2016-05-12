package com.execmobile.helpers;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;


public class DataSecurity {
	
	String key;
	int size;
	
	public DataSecurity(String key, int size)
	{
		this.key = key;
		this.size = size;
	}
	
	public String decryptData(String encryptedString) throws Exception
	{
		try {
			SecretKeySpec keySpec = null;
			byte[] keyBytes = DatatypeConverter.parseHexBinary(key);
			keySpec = new SecretKeySpec(keyBytes, "AES");
			byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		    IvParameterSpec ivspec = new IvParameterSpec(iv);
			byte[] encrypteBytes = DatatypeConverter.parseBase64Binary(encryptedString);
			Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			aesCipher.init(Cipher.DECRYPT_MODE, keySpec, ivspec);
			byte[] decryptedBytes = aesCipher.doFinal(encrypteBytes);
			String decryptedData = new String(decryptedBytes);
			
			return decryptedData;
		} catch (Exception ex) {
			
			throw ex;
		}
	}

}
