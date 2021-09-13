package com.zebone.nhis.webservice.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 银行支付节点(bankInfo)
 * @author admin
 *
 */
@XmlRootElement(name = "bankInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbBankinfoVo {
	/*
     * 商户号
     */
	@XmlElement(name="merchantId")
    private String merchantid;
	/*
     * POS终端号
     */
    @XmlElement(name="posId")
    private String posid;
    /*
     * 支付的银行卡号
     */
    @XmlElement(name="payCardNo")
    private String paycardno;
    /*
     * 交易日期
     */
    @XmlElement(name="payDate")
    private String paydate;
    /*
     * 交易时间
     */
    @XmlElement(name="payTime")
    private String paytime;
    /*
     * 批次号
     */
    @XmlElement(name="batchNo")
    private String batchno;
    /*
     * 凭证号
     */
    @XmlElement(name="vouchNo")
    private String vouchno;
    /*
     * 参考号
     */
    @XmlElement(name="referNo")
    private String referno;
    /*
     * 流水号
     */
    @XmlElement(name="flowNo")
    private String flowno;
    /*
     * 支付金额
     */
    @XmlElement(name="payAmt")
    private String payamt;
	public String getMerchantid() {
		return merchantid;
	}
	public void setMerchantid(String merchantid) {
		this.merchantid = merchantid;
	}
	public String getPosid() {
		return posid;
	}
	public void setPosid(String posid) {
		this.posid = posid;
	}
	public String getPaycardno() {
		return paycardno;
	}
	public void setPaycardno(String paycardno) {
		this.paycardno = paycardno;
	}
	public String getPaydate() {
		return paydate;
	}
	public void setPaydate(String paydate) {
		this.paydate = paydate;
	}
	public String getPaytime() {
		return paytime;
	}
	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}
	public String getBatchno() {
		return batchno;
	}
	public void setBatchno(String batchno) {
		this.batchno = batchno;
	}
	public String getVouchno() {
		return vouchno;
	}
	public void setVouchno(String vouchno) {
		this.vouchno = vouchno;
	}
	public String getReferno() {
		return referno;
	}
	public void setReferno(String referno) {
		this.referno = referno;
	}
	public String getFlowno() {
		return flowno;
	}
	public void setFlowno(String flowno) {
		this.flowno = flowno;
	}
	public String getPayamt() {
		return payamt;
	}
	public void setPayamt(String payamt) {
		this.payamt = payamt;
	}
    
}
