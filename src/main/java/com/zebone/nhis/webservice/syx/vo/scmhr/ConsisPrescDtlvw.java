package com.zebone.nhis.webservice.syx.vo.scmhr;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 处方明细数据
 * @author jd
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name="CONSIS_PRESC_DTLVW")
public class ConsisPrescDtlvw {
	/*
	 * 医嘱编码
	 */
	@XmlElement(name="ADVICE_CODE")
	private String adviceCode;
	@XmlElement(name="DRUG_CODE")
	/*
	 * 药房编码
	 */
    private String drugCode;
	/*
	 * 数量
	 */
	@XmlElement(name="QUANTITY")
    private BigDecimal quantity;
	public String getAdviceCode() {
		return adviceCode;
	}
	public void setAdviceCode(String adviceCode) {
		this.adviceCode = adviceCode;
	}
	public String getDrugCode() {
		return drugCode;
	}
	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}
	public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	
}
