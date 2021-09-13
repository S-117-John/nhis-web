package com.zebone.nhis.common.module.compay.ins.qgyb;

import java.util.Map;

public class InsQgybPreSt {


	/* 退费预结算 医保出参 */
	private OutParamHuaJia ybPreSettleInfo;

	/* 退费预结算 医保入参 */
	private Map<String, Object> yBPreIntoParam;
	
	//退费预结算 入参
	private String preIntoParam; 
	
	//门诊收费预结算 入参
	private Map<String,Object> ybPreSettleParam;
	
	private Double aggregateAmount;
	
	private Double patientsPay;
	private Double medicarePayments;



	public String getPreIntoParam() {
		return preIntoParam;
	}

	public void setPreIntoParam(String preIntoParam) {
		this.preIntoParam = preIntoParam;
	}

	public Double getAggregateAmount() {
		return aggregateAmount;
	}

	public void setAggregateAmount(Double aggregateAmount) {
		this.aggregateAmount = aggregateAmount;
	}

	public Double getPatientsPay() {
		return patientsPay;
	}

	public void setPatientsPay(Double patientsPay) {
		this.patientsPay = patientsPay;
	}

	public Double getMedicarePayments() {
		return medicarePayments;
	}

	public void setMedicarePayments(Double medicarePayments) {
		this.medicarePayments = medicarePayments;
	}

	public OutParamHuaJia getYbPreSettleInfo() {
		return ybPreSettleInfo;
	}

	public void setYbPreSettleInfo(OutParamHuaJia ybPreSettleInfo) {
		this.ybPreSettleInfo = ybPreSettleInfo;
	}

	public Map<String, Object> getyBPreIntoParam() {
		return yBPreIntoParam;
	}

	public void setyBPreIntoParam(Map<String, Object> yBPreIntoParam) {
		this.yBPreIntoParam = yBPreIntoParam;
	}

	public Map<String, Object> getYbPreSettleParam() {
		return ybPreSettleParam;
	}

	public void setYbPreSettleParam(Map<String, Object> ybPreSettleParam) {
		this.ybPreSettleParam = ybPreSettleParam;
	}


}
