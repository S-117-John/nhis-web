package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 中山二院平台消息发送对象(不含头部信息)
 * 
 * @author yx
 * 
 */
@XStreamAlias("PRPA_IN201311UV02")
public class Request {
	@XStreamAsAttribute
	private String XMLNS_XSI = "http://www.w3.org/2001/XMLSchema-instance";//生成xml后需要替换为xmlns:xsi
	@XStreamAsAttribute 
	private String ITSVersion ="XML_1.0";
	@XStreamAsAttribute
	private String xmlns = "urn:hl7-org:v3";
	@XStreamAsAttribute 
	private String XSI_SCHEMALOCATION = "urn:hl7-org:v3 ../multicacheschemas/";//生成xml后需要替换为xsi:schemaLocation,对应的xsd文件名由构造中传入
	private Id id= new Id();// 根节点第一层id标签
	private CreationTime creationTime;
	private InteractionId interactionId;
	private ProcessingCode processingCode ;
	private ProcessingModeCode processingModeCode;
	private AcceptAckCode acceptAckCode;
	private Receiver receiver;
    private Sender sender;
    private ControlActProcess controlActProcess;
    @XStreamOmitField
    private String reqHead;
    
    public Request(){}
	public Request(String xsd) {
		super();
		this.XSI_SCHEMALOCATION = this.XSI_SCHEMALOCATION +xsd;
		this.reqHead=xsd.substring(0, xsd.indexOf("."));
	}
	
	public String getXMLNS_XSI() {
		return XMLNS_XSI;
	}

	public void setXMLNS_XSI(String xMLNS_XSI) {
		XMLNS_XSI = xMLNS_XSI;
	}

	public String getXmlns() {
		return xmlns;
	}

	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	public Id getId() {
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public InteractionId getInteractionId() {
		if(interactionId==null)interactionId=new InteractionId();
		return interactionId;
	}

	public void setInteractionId(InteractionId interactionId) {
		this.interactionId = interactionId;
	}

	public CreationTime getCreationTime() {
		if(creationTime==null) creationTime=new CreationTime();
		return creationTime;
	}

	public void setCreationTime(CreationTime creationTime) {
		this.creationTime = creationTime;
	}

	public ProcessingCode getProcessingCode() {
		if(processingCode==null)processingCode=new ProcessingCode();
		return processingCode;
	}

	public void setProcessingCode(ProcessingCode processingCode) {
		this.processingCode = processingCode;
	}

	public ProcessingModeCode getProcessingModeCode() {
		if(processingModeCode==null)processingModeCode=new ProcessingModeCode();
		return processingModeCode;
	}

	public void setProcessingModeCode(ProcessingModeCode processingModeCode) {
		this.processingModeCode = processingModeCode;
	}

	public AcceptAckCode getAcceptAckCode() {
		if(acceptAckCode==null)acceptAckCode=new AcceptAckCode();
		return acceptAckCode;
	}

	public void setAcceptAckCode(AcceptAckCode acceptAckCode) {
		this.acceptAckCode = acceptAckCode;
	}

	public Receiver getReceiver() {
		if(receiver==null) receiver=new Receiver();
		return receiver;
	}

	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}

	public Sender getSender() {
		if(sender==null)sender=new Sender();
		return sender;
	}

	public void setSender(Sender sender) {
		this.sender = sender;
	}

	public ControlActProcess getControlActProcess() {
		if(controlActProcess==null)controlActProcess=new ControlActProcess();
		return controlActProcess;
	}

	public void setControlActProcess(ControlActProcess controlActProcess) {
		this.controlActProcess = controlActProcess;
	}

	public String getReqHead() {
		return reqHead;
	}

	public void setReqHead(String reqHead) {
		this.reqHead = reqHead;
	}
	

}
