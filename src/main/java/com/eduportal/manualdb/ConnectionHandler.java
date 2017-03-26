package com.eduportal.manualdb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class ConnectionHandler {

	private static final MongoClient client;
	private static final MongoDatabase db;

	static {
		MongoClientURI mongoClientURI = new MongoClientURI("mongodb://root:root@ds137550.mlab.com:37550/heroku_nl9x1kkp");
		client = new MongoClient(mongoClientURI);
		db = client.getDatabase("heroku_nl9x1kkp");
	}

	public static MongoDatabase db() {
		return db;
	}

}
