package com.compassites.testproject.springsecurity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.compassites.testproject.service.LoginService;

@Component
public class AuthenticationProviderImpl implements AuthenticationProvider {

	private LoginService loginService;

	@Autowired
	public AuthenticationProviderImpl(LoginService loginService) {
		this.loginService = loginService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String usernameWithEmail = authentication.getPrincipal().toString();
		String password = authentication.getCredentials().toString();

		String userName = usernameWithEmail.split(":")[0];
		String email = usernameWithEmail.split(":")[1];
		int statusCode = 200;

		try {
			if (loginService.authValidation(userName, email) == null
					|| !password.equals(loginService.authValidation(userName, email))) {
				throw new BadCredentialsException("Bad credentials");
			}
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

		return new UsernamePasswordAuthenticationToken(usernameWithEmail, password, authorities);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
