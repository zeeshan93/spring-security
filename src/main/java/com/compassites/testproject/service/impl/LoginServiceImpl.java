package com.compassites.testproject.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.compassites.testproject.dataoperation.DBOperation;
import com.compassites.testproject.model.UserSignUpMo;
import com.compassites.testproject.service.LoginService;
import com.compassites.testproject.utils.TokenGenerator;

@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	private TokenGenerator generator;

	@Autowired
	private Environment env;

	@Autowired
	private DBOperation dbOperation;

	@Override
	public String authKeyGenerator(UserSignUpMo request) throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException,
			UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		String plainText = request.getName() + ":" + request.getEmail() + ":" + request.getPassword();
		// String secretKey = "${secretKey}";
		String secretKey = env.getProperty("secretKey");
		String authKey = generator.encrypt(secretKey, plainText);
		return authKey;
	}

	@Override
	public String signUp(UserSignUpMo request) throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException,
			UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		String message = dbOperation.signUp(request);
		return message;
	}

	@Override
	public String authValidation(String userName, String email) {
		String password = dbOperation.authValidation(userName, email);
		try {
			password = generator.decrypt(env.getProperty("secretKey"), password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return password;
	}

}
