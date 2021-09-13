package com.zebone.nhis.ma.pub.platform.socket.client;

public interface CustomerHandler {

	void completeSend(MsgBounder msgBounder);

	void afterReceived(MsgBounder msgBounder,Object receivedData);
}
