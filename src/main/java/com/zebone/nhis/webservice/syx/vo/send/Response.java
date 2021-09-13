package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 中山二院平台消息响应格式对象
 * 
 * @author yx
 * 
 */

@XStreamAlias("PRVS_IN000004UV01")
public class Response {
	@XStreamAsAttribute
	private String ITSVersion="XML_1.0";
	
	private Id id = new Id();
	private CreationTime creationTime = new CreationTime();
	private InteractionId interactionId = new InteractionId();
	private ProcessingCode processingCode = new ProcessingCode();
	private ProcessingModeCode processingModeCode = new ProcessingModeCode();
	private AcceptAckCode acceptAckCode = new AcceptAckCode();
	private Receiver receiver = new Receiver();
	private Sender sender = new Sender();
	private Acknowledgement acknowledgement = new Acknowledgement();
	private ControlActProcess controlActProcess=new ControlActProcess();
	public Id getId() {
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public CreationTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(CreationTime creationTime) {
		this.creationTime = creationTime;
	}

	public InteractionId getInteractionId() {
		return interactionId;
	}

	public void setInteractionId(InteractionId interactionId) {
		this.interactionId = interactionId;
	}

	public ProcessingCode getProcessingCode() {
		return processingCode;
	}

	public void setProcessingCode(ProcessingCode processingCode) {
		this.processingCode = processingCode;
	}

	public AcceptAckCode getAcceptAckCode() {
		return acceptAckCode;
	}

	public void setAcceptAckCode(AcceptAckCode acceptAckCode) {
		this.acceptAckCode = acceptAckCode;
	}

	public Receiver getReceiver() {
		return receiver;
	}

	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}

	public Sender getSender() {
		return sender;
	}

	public void setSender(Sender sender) {
		this.sender = sender;
	}

	public Acknowledgement getAcknowledgement() {
		return acknowledgement;
	}

	public void setAcknowledgement(Acknowledgement acknowledgement) {
		this.acknowledgement = acknowledgement;
	}

	public ProcessingModeCode getProcessingModeCode() {
		return processingModeCode;
	}

	public void setProcessingModeCode(ProcessingModeCode processingModeCode) {
		this.processingModeCode = processingModeCode;
	}

	public String getITSVersion() {
		return ITSVersion;
	}

	public void setITSVersion(String iTSVersion) {
		ITSVersion = iTSVersion;
	}

	public ControlActProcess getControlActProcess() {
		return controlActProcess;
	}

	public void setControlActProcess(ControlActProcess controlActProcess) {
		this.controlActProcess = controlActProcess;
	}
	
}
