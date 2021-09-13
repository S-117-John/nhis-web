package com.zebone.nhis.webservice.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 现金支付节点(cashInfo)
 * @author admin
 *
 */
@XmlRootElement(name = "cashInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbCashinfoVo {
	/*
	 * 流水号
	 */
   @XmlElement(name="flowNo")
   private String flowno;
   /*
    * 支付费用
    */
   @XmlElement(name="payAmt")
   private String payamt;
   /*
    * 支付时间
    */
   @XmlElement(name="payTime")
   private String paytime;
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
   public String getPaytime() {
	  return paytime;
   }
   public void setPaytime(String paytime) {
	  this.paytime = paytime;
   }
   
   
}
