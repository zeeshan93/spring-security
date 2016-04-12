package com.compassites.testproject.dataoperation.impl;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.omg.CORBA.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.compassites.testproject.dataoperation.DBOperation;
import com.compassites.testproject.model.UserSignUpMo;
import com.compassites.testproject.utils.TokenGenerator;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

@Repository
public class DBOperationImpl implements DBOperation {

	@Autowired
	MongoClient mongoClient;

	@Autowired
	org.springframework.core.env.Environment env;

	@Autowired
	TokenGenerator generator;

	@Override
	public String signUp(UserSignUpMo request) throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException,
			UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {

		MongoDatabase database = mongoClient.getDatabase(env.getProperty("mongo.dataBase"));
		MongoCollection<BasicDBObject> coll = database.getCollection(env.getProperty("mongo.userCollection"),
				BasicDBObject.class);
		Gson gson = new Gson();

		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("email", Pattern.compile(request.getEmail(), Pattern.CASE_INSENSITIVE));
		FindIterable<BasicDBObject> obj = coll.find(whereQuery);

		if (obj.first() == null) {
			String password = request.getPassword();
			String encryptedPassword = generator.encrypt(env.getProperty("secretKey"), password);
			request.setPassword(encryptedPassword);
			BasicDBObject basicDBObject = (BasicDBObject) JSON.parse(gson.toJson(request));
			coll.insertOne(basicDBObject);
			return "User added into Database";
		}

		return "User already registered";
	}
	
	@Override
	public String authValidation(String userName, String email) {
		MongoDatabase database = mongoClient.getDatabase(env.getProperty("mongo.dataBase"));
		MongoCollection<BasicDBObject> coll = database.getCollection(env.getProperty("mongo.userCollection"),
				BasicDBObject.class);
		
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("email", Pattern.compile(email, Pattern.CASE_INSENSITIVE));
		whereQuery.put("name", userName);
		FindIterable<BasicDBObject> obj = coll.find(whereQuery);
		if(obj.first()!=null){
			return obj.first().getString("password");
		}
		return null;
	}

}
