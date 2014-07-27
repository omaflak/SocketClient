package com.omaflak.socketclient;

import java.io.Serializable;

public class Message implements Serializable{

	private static final long serialVersionUID = 5337831264023296552L;
	private String sender;
	private String message;
	
	Message(String sender, String message){
		this.sender = sender;
		this.message = message;
	}

	public String getSender() {
		return sender;
	}

	public String getMessage() {
		return message;
	}
	
	public String toString(){
		return this.getSender()+" : "+this.getMessage();
	}
}
