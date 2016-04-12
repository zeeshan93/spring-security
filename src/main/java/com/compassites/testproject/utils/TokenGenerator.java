package com.compassites.testproject.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.stereotype.Service;

@Service
public class TokenGenerator {
	public TokenGenerator() {

	}

	/**
	 * 
	 * @param secretKey
	 *            Key used to encrypt data
	 * @param plainText
	 *            Text input to be encrypted
	 * @return Returns encrypted text
	 * 
	 */
	public String encrypt(String secretKey, String plainText) throws NoSuchAlgorithmException, InvalidKeySpecException,
			NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
			UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(secretKey);
		String myEncryptedText = textEncryptor.encrypt(plainText);
		return myEncryptedText;

	}

	/**
	 * @param secretKey
	 *            Key used to decrypt data
	 * @param encryptedText
	 *            encrypted text input to decrypt
	 * @return Returns plain text after decryption
	 */
	public String decrypt(String secretKey, String encryptedText) throws NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
			UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, IOException {

		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(secretKey);
		String myEncryptedText = textEncryptor.decrypt(encryptedText);
		return myEncryptedText;

	}
}
