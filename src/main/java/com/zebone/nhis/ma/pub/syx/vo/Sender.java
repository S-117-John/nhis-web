package com.zebone.nhis.ma.pub.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.zebone.platform.common.support.UserContext;

@XStreamAlias("sender")
public class Sender {
	private String systemId = "HIS";
	private String systemName = "HIS";
	private String senderId = UserContext.getUser().getCodeEmp();
	private String sendername = UserContext.getUser().getNameEmp();
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getSendername() {
		return sendername;
	}
	public void setSendername(String sendername) {
		this.sendername = sendername;
	}
}
