package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bl.charge;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Identifier;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.PhResource;

/**
 * 记费
 */
public class BlCharge extends PhResource{

    private String resourceType;
    
    private String implicitRules;
    
    private String id;
    
    private String status;
    
    private String occurrenceDateTime;
    
    private String enteredDate;
    
    private List<Identifier> identifier;
    
    private BlSubject subject;
    
    private BlEncounter encounter;
    
    private List<BlBasedOn> basedOn;

    private BlEnterer enterer;

    private List<BlEnterer> supportingInformation;

    private List<BlPerformer> performer;
    
    private BlQuantity quantity;
    
    private BlPriceOverride priceOverride;

	public String getResourceType() {
		return resourceType;
	}

	public String getImplicitRules() {
		return implicitRules;
	}

	public String getId() {
		return id;
	}

	public String getStatus() {
		return status;
	}

	public String getOccurrenceDateTime() {
		return occurrenceDateTime;
	}

	public String getEnteredDate() {
		return enteredDate;
	}

	public List<Identifier> getIdentifier() {
		return identifier;
	}

	public BlSubject getSubject() {
		return subject;
	}

	public BlEncounter getEncounter() {
		return encounter;
	}

	public List<BlBasedOn> getBasedOn() {
		return basedOn;
	}

	public BlEnterer getEnterer() {
		return enterer;
	}

	public List<BlEnterer> getSupportingInformation() {
		return supportingInformation;
	}

	public List<BlPerformer> getPerformer() {
		return performer;
	}

	public BlQuantity getQuantity() {
		return quantity;
	}

	public BlPriceOverride getPriceOverride() {
		return priceOverride;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public void setImplicitRules(String implicitRules) {
		this.implicitRules = implicitRules;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setOccurrenceDateTime(String occurrenceDateTime) {
		this.occurrenceDateTime = occurrenceDateTime;
	}

	public void setEnteredDate(String enteredDate) {
		this.enteredDate = enteredDate;
	}

	public void setIdentifier(List<Identifier> identifier) {
		this.identifier = identifier;
	}

	public void setSubject(BlSubject subject) {
		this.subject = subject;
	}

	public void setEncounter(BlEncounter encounter) {
		this.encounter = encounter;
	}

	public void setBasedOn(List<BlBasedOn> basedOn) {
		this.basedOn = basedOn;
	}

	public void setEnterer(BlEnterer enterer) {
		this.enterer = enterer;
	}

	public void setSupportingInformation(List<BlEnterer> supportingInformation) {
		this.supportingInformation = supportingInformation;
	}

	public void setPerformer(List<BlPerformer> performer) {
		this.performer = performer;
	}

	public void setQuantity(BlQuantity quantity) {
		this.quantity = quantity;
	}

	public void setPriceOverride(BlPriceOverride priceOverride) {
		this.priceOverride = priceOverride;
	}
    
}
