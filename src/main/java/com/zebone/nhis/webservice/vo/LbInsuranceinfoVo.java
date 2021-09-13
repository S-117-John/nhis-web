package com.zebone.nhis.webservice.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 医保支付节点(insuranceInfo)
 * @author admin
 *
 */
@XmlRootElement(name = "insuranceInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbInsuranceinfoVo {
	/**
	 * 医保交易流水号
	 */
	@XmlElement(name="flowNo")
    private String flowno;
	/**
	 * 医保支付时间
	 */
    @XmlElement(name="payTime")
    private String paytime;
    /**
	 * 支付总金额
	 */
    @XmlElement(name="payAmt")
    private String payamt;
    /**
	 * 医保卡号
	 */
    @XmlElement(name="insuranceNo")
    private String insuranceno;
    /**
	 * 医保入参
	 */
    @XmlElement(name="insuranceIn")
    private String insurancein;
    /**
	 * 医保出参
	 */
    @XmlElement(name="insuranceOut")
    private String insuranceout;
	public String getFlowno() {
		return flowno;
	}
	public void setFlowno(String flowno) {
		this.flowno = flowno;
	}
	public String getPaytime() {
		return paytime;
	}
	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}
	public String getPayamt() {
		return payamt;
	}
	public void setPayamt(String payamt) {
		this.payamt = payamt;
	}
	public String getInsuranceno() {
		return insuranceno;
	}
	public void setInsuranceno(String insuranceno) {
		this.insuranceno = insuranceno;
	}
	public String getInsurancein() {
		return insurancein;
	}
	public void setInsurancein(String insurancein) {
		this.insurancein = insurancein;
	}
	public String getInsuranceout() {
		return insuranceout;
	}
	public void setInsuranceout(String insuranceout) {
		this.insuranceout = insuranceout;
	}
    
    
}
