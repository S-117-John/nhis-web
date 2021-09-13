package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bl.charge;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Identifier;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.PhResource;

import java.util.List;

/**
 * 结算
 */
public class BlSettle extends PhResource{

    private String patientId;
    
    private String patientType;

	private String codePv;

	private String euStatus;

	private String chargeOperator;

	public String getChargeOperator() {
		return chargeOperator;
	}

	public void setChargeOperator(String chargeOperator) {
		this.chargeOperator = chargeOperator;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getPatientType() {
		return patientType;
	}

	public void setPatientType(String patientType) {
		this.patientType = patientType;
	}

	public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}

	public String getEuStatus() {
		return euStatus;
	}

	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}
}
