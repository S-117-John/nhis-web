package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("acknowledgement")
public class Acknowledgement {
	@XStreamAsAttribute
	private String typeCode;
	private TargetMessage targetMessage;
	private AcknowledgementDetail acknowledgementDetail;

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public TargetMessage getTargetMessage() {
		if(targetMessage==null)targetMessage=new TargetMessage();
		return targetMessage;
	}

	public void setTargetMessage(TargetMessage targetMessage) {
		this.targetMessage = targetMessage;
	}

	public AcknowledgementDetail getAcknowledgementDetail() {
		if(acknowledgementDetail==null)acknowledgementDetail=new AcknowledgementDetail();
		return acknowledgementDetail;
	}

	public void setAcknowledgementDetail(
			AcknowledgementDetail acknowledgementDetail) {
		this.acknowledgementDetail = acknowledgementDetail;
	}

}
