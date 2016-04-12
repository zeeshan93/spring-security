package com.compassites.testproject.springsecurity;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.compassites.testproject.utils.TokenGenerator;


public class TokenPreProcessingFilter extends OncePerRequestFilter{
	private Environment environment;

	private TokenGenerator tokenGenerator;
	
	public TokenPreProcessingFilter(Environment environment,TokenGenerator tokenGenerator){
		this.environment = environment;
		this.tokenGenerator = tokenGenerator;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String xAuth = request.getHeader("auth_key");
			
			//decrypt the auth_key
			String data = tokenGenerator.decrypt(
			environment.getProperty("secretKey"), xAuth);
			
			//split the data into username,password and login type based on :
			String[] usernameEmailPassword = data.split(":");
			
			//set the username,Email and Password
			String usernameWithEmail = usernameEmailPassword[0] + ":" +usernameEmailPassword[1];
			String password = usernameEmailPassword[2];
			
			//set username,Email and password in authentication variable
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					usernameWithEmail, password);

			//set the authenticated principal and credentials in the current security context 
			SecurityContextHolder.getContext()
					.setAuthentication(authentication);

			
		} catch (Exception e) {
			/*//set username,Email and password in authentication variable
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					"default:password", "password");

			//set the authenticated principal and credentials in the current security context 
			SecurityContextHolder.getContext()
					.setAuthentication(authentication);*/
			
			//response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized Access");

		}
		filterChain.doFilter(request, response);		
	}

}
