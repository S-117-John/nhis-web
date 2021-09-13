package com.zebone.nhis.ma.pub.zsrm.vo;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(value = XmlAccessType.FIELD)
public class PiA1028RegVo {
	//记录序号
	@XmlElement(name = "seq_no")
	private String seqNo;
	//扫码的科室代码
	@XmlElement(name = "department_type ")
	private String departmentType ;
	//电子健康卡ID
	@XmlElement(name = "ehealth_card_id")
	private String ehealthCardId;
	//用卡时间
	@XmlElement(name = "time")
	private String time;

	//姓名
	@XmlElement(name = "pname")
	private String pname;
    //证件类型
	@XmlElement(name = "id_type")
	private String idType;
   //证件号码
   @XmlElement(name = "idcard")
	private  String idcard;

	//联系方式电话
	@XmlElement(name = "phone")
	private String phone;
	//户籍所在地
	@XmlElement(name = "domicile")
	private String domicile;
	//刷卡终端类型编号
	@XmlElement(name = "channel_code")
	private  String channelCode;

	//刷卡终端类型名称
	@XmlElement(name = "channel_name")
	private String channelName;
	//诊疗环节代码
	@XmlElement(name = "med_step_code")
	private String medStepCode;
	//诊疗环节名称
	@XmlElement(name = "med_step_name")
	private  String medStepName;

	//识读终端设备序列号
	@XmlElement(name = "channel_sn")
	private  String channelSn;

	//支付金额
	@XmlElement(name = "payAmount")
	private  String payAmount;

	//支付渠道
	@XmlElement(name = "payChannel")
	private  String payChannel;

	//线上或线下
	@XmlElement(name = "onlineOrOffline")
	private  String onlineOrOffline;

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	public String getDepartmentType() {
		return departmentType;
	}

	public void setDepartmentType(String departmentType) {
		this.departmentType = departmentType;
	}

	public String getEhealthCardId() {
		return ehealthCardId;
	}

	public void setEhealthCardId(String ehealthCardId) {
		this.ehealthCardId = ehealthCardId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDomicile() {
		return domicile;
	}

	public void setDomicile(String domicile) {
		this.domicile = domicile;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getMedStepCode() {
		return medStepCode;
	}

	public void setMedStepCode(String medStepCode) {
		this.medStepCode = medStepCode;
	}

	public String getMedStepName() {
		return medStepName;
	}

	public void setMedStepName(String medStepName) {
		this.medStepName = medStepName;
	}

	public String getChannelSn() {
		return channelSn;
	}

	public void setChannelSn(String channelSn) {
		this.channelSn = channelSn;
	}

	public String getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}

	public String getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}

	public String getOnlineOrOffline() {
		return onlineOrOffline;
	}

	public void setOnlineOrOffline(String onlineOrOffline) {
		this.onlineOrOffline = onlineOrOffline;
	}
}
