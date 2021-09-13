package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public abstract class PubRequest<T> {

	private Id id = new Id();
	private CreationTime creationTime=new CreationTime();  
	private InteractionId interactionId=new InteractionId();
	private ProcessingCode processingCode=new ProcessingCode();
	private ProcessingModeCode processingModeCode=new ProcessingModeCode();
	private AcceptAckCode acceptAckCode=new AcceptAckCode();
	private Receiver receiver=new Receiver();
	private Sender sender=new Sender();
	
	protected T control;
	
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

	public ProcessingModeCode getProcessingModeCode() {
		return processingModeCode;
	}

	public void setProcessingModeCode(ProcessingModeCode processingModeCode) {
		this.processingModeCode = processingModeCode;
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
	
	public abstract T getControl();
	
	public abstract void setControl(T control);
	
	@XStreamAlias("id")
	class Id extends PubElement {
		private Item item=new Item();

		public Item getItem() {
			return item;
		}

		public void setItem(Item item) {
			this.item = item;
		}
		
	}

	@XStreamAlias("creationTime")
	class CreationTime extends PubElement {

	}

	@XStreamAlias("interactionId")
	class InteractionId extends PubElement {

	}

	@XStreamAlias("processingCode")
	class ProcessingCode extends PubElement {

	}
	
	@XStreamAlias("processingModeCode")
	class ProcessingModeCode extends PubElement {

	}

	@XStreamAlias("acceptAckCode")
	class AcceptAckCode extends PubElement {

	}
	@XStreamAlias("receiver")
	class Receiver extends PubElement {
		private Device device=new Device();

		public Device getDevice() {
			return device;
		}

		public void setDevice(Device device) {
			this.device = device;
		}
	}
	@XStreamAlias("device")
	class Device extends PubElement {
		private Id id=new Id();

		public Id getId() {
			return id;
		}

		public void setId(Id id) {
			this.id = id;
		}
	}
	@XStreamAlias("item")
	class Item extends PubElement {

	}
	
	@XStreamAlias("sender")
	class Sender extends PubElement {
		private Device device=new Device();

		public Device getDevice() {
			return device;
		}

		public void setDevice(Device device) {
			this.device = device;
		}
	}
}
