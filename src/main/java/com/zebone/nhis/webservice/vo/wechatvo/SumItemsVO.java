package com.zebone.nhis.webservice.vo.wechatvo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "sumItems")
@XmlAccessorType(XmlAccessType.FIELD)
public class SumItemsVO {
	
   /**
    * 格式：yyyy-MM-dd
    */
   @XmlElement(name="billDate")
   private String billDate;
    /**
     * 微信当日费用
     */
    @XmlElement(name="wxFee")
    private String wxFee;
    /**
     * 支付宝当日费用
     */
    @XmlElement(name="zfbFee")
    private String zfbFee;
    /**
     * 医院当日总收入
     */
    @XmlElement(name="hospFee")
    private String hospFee	;
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getWxFee() {
		return wxFee;
	}
	public void setWxFee(String wxFee) {
		this.wxFee = wxFee;
	}
	public String getZfbFee() {
		return zfbFee;
	}
	public void setZfbFee(String zfbFee) {
		this.zfbFee = zfbFee;
	}
	public String getHospFee() {
		return hospFee;
	}
	public void setHospFee(String hospFee) {
		this.hospFee = hospFee;
	}
    
}
