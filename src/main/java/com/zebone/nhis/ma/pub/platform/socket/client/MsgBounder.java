package com.zebone.nhis.ma.pub.platform.socket.client;

public class MsgBounder {

	public MsgBounder(){}
	public MsgBounder(Object msg,Object serviceData){
		this.msg = msg;
		this.serviceData = serviceData;
	}

	private Object msg;

	private Object serviceData;
	
	private Object msgAck;


	public Object getMsg() {
		return msg;
	}

	public void setMsg(Object msg) {
		this.msg = msg;
	}

	public Object getServiceData() {
		return serviceData;
	}

	public void setServiceData(Object serviceData) {
		this.serviceData = serviceData;
	}

	@Override
	public String toString() {
		return "MsgBounder{" +
				"msg=" + msg +
				", serviceData=" + serviceData +
				'}';
	}
	public Object getMsgAck() {
		return msgAck;
	}
	public void setMsgAck(Object msgAck) {
		this.msgAck = msgAck;
	}
	
}
