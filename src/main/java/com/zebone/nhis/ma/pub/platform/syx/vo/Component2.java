package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("component2")
public class Component2 {
	private SequenceNumber sequenceNumber;
	private SubstanceAdministrationRequest substanceAdministrationRequest;
	private ProcedureRequest procedureRequest;
	private SupplyRequest supplyRequest;
	private ObservationRequest observationRequest;
	
	public SequenceNumber getSequenceNumber() {
		if(sequenceNumber==null) sequenceNumber=new  SequenceNumber();
		return sequenceNumber;
	}

	public void setSequenceNumber(SequenceNumber sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public SubstanceAdministrationRequest getSubstanceAdministrationRequest() {
		if(substanceAdministrationRequest==null) substanceAdministrationRequest=new SubstanceAdministrationRequest();
		return substanceAdministrationRequest;
	}

	public void setSubstanceAdministrationRequest(
			SubstanceAdministrationRequest substanceAdministrationRequest) {
		this.substanceAdministrationRequest = substanceAdministrationRequest;
	}

	public ProcedureRequest getProcedureRequest() {
		if(procedureRequest==null)procedureRequest=new ProcedureRequest();
		return procedureRequest;
	}

	public void setProcedureRequest(ProcedureRequest procedureRequest) {
		this.procedureRequest = procedureRequest;
	}

	public SupplyRequest getSupplyRequest() {
		if(supplyRequest==null) supplyRequest=new SupplyRequest();
		return supplyRequest;
	}

	public void setSupplyRequest(SupplyRequest supplyRequest) {
		this.supplyRequest = supplyRequest;
	}

	public ObservationRequest getObservationRequest() {
		if(observationRequest==null)observationRequest=new ObservationRequest();
		return observationRequest;
	}

	public void setObservationRequest(ObservationRequest observationRequest) {
		this.observationRequest = observationRequest;
	}
	
}
