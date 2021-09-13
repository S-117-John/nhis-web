package com.zebone.nhis.webservice.vo.picardvo;

import javax.xml.bind.annotation.XmlElement;

/**
 * 6.1.查询是否存在就诊卡 
 * 查询患者是否存在在用就诊卡。
 * @ClassName: ResPiCardVo   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: zhangheng 
 * @date: 2019年4月18日 下午3:01:06     
 * @Copyright: 2019
 */
public class ResPiCardVo {
	// 卡类型(身份证: 01)
	private String dtCardtype;
	// 卡号
	private String cardNo;
	// 有效开始时间
	private String dateBegin;
	// 有效截止时间
	private String dateEnd;
	// 卡状态(0 在用,1挂失,2到期,8退卡, 9销卡)
	private String euStatus;
	// 是否启用(1启用,0未启用)
	private String flagActive;

	@XmlElement(name = "flagActive")
	public String getFlagActive() {
		return flagActive;
	}

	public void setFlagActive(String flagActive) {
		this.flagActive = flagActive;
	}

	@XmlElement(name = "dtCardtype")
	public String getDtCardtype() {
		return dtCardtype;
	}

	public void setDtCardtype(String dtCardtype) {
		this.dtCardtype = dtCardtype;
	}

	@XmlElement(name = "cardNo")
	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	@XmlElement(name = "dateBegin")
	public String getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(String dateBegin) {
		this.dateBegin = dateBegin;
	}

	@XmlElement(name = "dateEnd")
	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

	@XmlElement(name = "euStatus")
	public String getEuStatus() {
		return euStatus;
	}

	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}

}
