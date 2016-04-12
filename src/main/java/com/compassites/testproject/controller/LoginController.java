package com.compassites.testproject.controller;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compassites.testproject.model.UserSignUpMo;
import com.compassites.testproject.service.LoginService;

@Controller
@RequestMapping(value = "/app")
public class LoginController {

	@Autowired
	private LoginService loginService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/signup"} , method = RequestMethod.POST)
	public @ResponseBody JSONObject signUp(@RequestBody UserSignUpMo request) throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException,
			UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		String authKey = loginService.authKeyGenerator(request);
		String message = loginService.signUp(request);
		JSONObject jobj = new JSONObject();
		jobj.put("auth_key", authKey);
		jobj.put("message", message);
		jobj.put("status", 0);
		return jobj;
	}
}
