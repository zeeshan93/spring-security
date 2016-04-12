package com.compassites.testproject.springsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private com.compassites.testproject.utils.TokenGenerator tokenGenerator;

	@Autowired
	private Environment environment;

	@Autowired
	private AuthenticationProviderImpl demoAuthenticationProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable()
				.addFilterBefore(new TokenPreProcessingFilter(environment, tokenGenerator),
						UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests()
				.antMatchers("/operation/**").hasRole("USER");
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(demoAuthenticationProvider);

	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManager();
	}
}
