package com.dnx.model;

public class BlockedMessage {
	
	private int id, senderId;
	private String message;
	private String dataTime;
	
	public BlockedMessage(int id, int senderId, String message, String dataTime) {
		super();
		this.id = id;
		this.senderId = senderId;
		this.message = message;
		this.dataTime = dataTime;
	}

	public BlockedMessage(int senderId, String message, String dataTime) {
		super();
		this.senderId = senderId;
		this.message = message;
		this.dataTime = dataTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDataTime() {
		return dataTime;
	}

	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}
	
	
}
