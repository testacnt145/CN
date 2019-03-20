package com.chattynotes.adapters.mediaViewer.model;

public class Media {

	private long chatId;
	private String msg;
	private String msgId;
	private int msgFlow;
	private long msgTimestamp;

	public Media() {

	}

	public long getChatId() {
		return chatId;
	}

	public void setChatId(long chatId) {
		this.chatId = chatId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public int getMsgFlow() {
		return msgFlow;
	}

	public void setMsgFlow(int msgFlow) {
		this.msgFlow = msgFlow;
	}

	public long getMsgTimestamp() {
		return msgTimestamp;
	}

	public void setMsgTimestamp(long msgTimestamp) {
		this.msgTimestamp = msgTimestamp;
	}
}
