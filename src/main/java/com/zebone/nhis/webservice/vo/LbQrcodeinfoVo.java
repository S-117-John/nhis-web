package com.zebone.nhis.webservice.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 扫码支付节点(qrCodeInfo)
 * @author admin
 *
 */
@XmlRootElement(name = "qrCodeInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbQrcodeinfoVo {
	/*
	 * 订单号
	 */
	@XmlElement(name="orderNo")
    private String orderno;
	/*
	 * 流水号
	 */
    @XmlElement(name="flowNo")
    private String flowno;
    /*
	 * 支付费用
	 */
    @XmlElement(name="payAmt")
    private Double payamt;
    /*
	 * 支付时间
	 */
    @XmlElement(name="payTime")
    private String paytime;
    /*
	 * 支付方账号
	 */
    @XmlElement(name="accountNo")
    private String accountno;
    /*
	 * 商户号
	 */
    @XmlElement(name="merchantId")
    private String merchantid;
    /*
	 * 第三方支付方式
	 */
    @XmlElement(name="payMethod")
    private String paymethod;
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getFlowno() {
		return flowno;
	}
	public void setFlowno(String flowno) {
		this.flowno = flowno;
	}
	public Double getPayamt() {
		return payamt;
	}
	public void setPayamt(Double payamt) {
		this.payamt = payamt;
	}
	public String getPaytime() {
		return paytime;
	}
	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}
	public String getAccountno() {
		return accountno;
	}
	public void setAccountno(String accountno) {
		this.accountno = accountno;
	}
	public String getMerchantid() {
		return merchantid;
	}
	public void setMerchantid(String merchantid) {
		this.merchantid = merchantid;
	}
	public String getPaymethod() {
		return paymethod;
	}
	public void setPaymethod(String paymethod) {
		this.paymethod = paymethod;
	}
    
    
}
