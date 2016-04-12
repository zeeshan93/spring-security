package com.compassites.testproject.config;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.compassites.testproject")
@PropertySource({ "classpath:application.properties", "classpath:persistance-mongo.properties" })
public class AppConfig extends WebMvcConfigurerAdapter {
	
	//This PropertySourcesPlaceholderConfigurer allows the properties to be accessed without the autowired environment variable
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	@Autowired
	private Environment env;

	private static final Logger logger = Logger.getLogger(AppConfig.class);

	
	@Bean(name = "dataSourceClient")
	public MongoClient dataSource() {
		/*
		 * return new MongoClient(env.getProperty("mongo.url"),
		 * Integer.valueOf(env.getProperty("mongo.port")));
		 */

		String mongoUserName = env.getProperty("mongo.userName");

		String mongoPassword = env.getProperty("mongo.password");

		String host = env.getProperty("mongo.url").toString();

		Object portOb = env.getProperty("mongo.port");
		Integer port = Integer.parseInt(portOb.toString());

		String db = env.getProperty("mongo.dataBase").toString();

		logger.info("username : " + mongoUserName + ", password : " + mongoPassword + ", host: " + host);
		if (mongoPassword == null) {
			return new MongoClient(env.getProperty("mongo.url"), Integer.valueOf(env.getProperty("mongo.port")));
		}
		MongoCredential credential = MongoCredential.createCredential(mongoUserName, db, mongoPassword.toCharArray());
		MongoClient client = new MongoClient(new ServerAddress(host, port), Arrays.asList(credential));
		return client;
	}
	
	//To allow the .com .pdf etc extensions
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.favorPathExtension(false);
	}
	
	//Servlet container's default handling of static resources. 
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
}
