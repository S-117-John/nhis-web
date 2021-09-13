package com.zebone.nhis.base.msg.vo;

import java.util.List;

import com.zebone.nhis.common.module.base.message.SysMessage;
import com.zebone.nhis.common.module.base.message.SysMessageSend;

public class MessageSendParam {
	
	private SysMessage message  ;
	private List<SysMessageSend> sends ;
	
	/** 需要返回列表类型 1- 发送 2-接收 */
	private String type;
	
	public SysMessage getMessage() {
		return message;
	}
	public void setMessage(SysMessage message) {
		this.message = message;
	}
	public List<SysMessageSend> getSends() {
		return sends;
	}
	public void setSends(List<SysMessageSend> sends) {
		this.sends = sends;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
