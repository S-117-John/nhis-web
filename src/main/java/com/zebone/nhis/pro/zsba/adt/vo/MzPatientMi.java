package com.zebone.nhis.pro.zsba.adt.vo;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;


@Table(value="mz_patient_mi")
public class MzPatientMi {
	
	/** 患者编码 */
	@PK
	@Field(value="patient_id")
	private String patientId ;
    
	/** 住院号 */
    @Field(value="inpatient_no")
    private String inpatientNo;

	/** 身份证号 */
    @Field(value="social_no")
    private String socialNo;

	/** ？？ */
    @Field(value="hic_no")
    private String hicNo;

	/** 诊疗卡号 */
    @Field(value="p_bar_code")
    private String pBarCode;

	/** 患者姓名 */
    @Field(value="name")
    private String name;

	/** 性别编码：1男，2女 */
    @Field(value="sex")
    private String sex;

	/** 婚姻编码 */
    @Field(value="marry_code")
    private String marryCode;

	/** 国家编码 */
    @Field(value="country_code")
    private String countryCode;

	/** 名族编码 */
    @Field(value="nation_code")
    private String nationCode;

	/** 出生地址 */
    @Field(value="birth_place")
    private String birthPlace;

	/** 家庭地址-编码 */
    @Field(value="home_district")
    private String homeDistrict;

	/** 家庭地址-详细地点 */
    @Field(value="home_street")
    private String homeStreet;

	/** 家庭电话 */
    @Field(value="home_tel")
    private String homeTel;

	/** 家庭邮编 */
    @Field(value="home_zipcode")
    private String homeZipcode;

	/** 联系人名称 */
    @Field(value="relation_name")
    private String relationName;

	/** 联系人关系 */
    @Field(value="relation_code")
    private String relationCode;

	/** 联系人电话 */
    @Field(value="relation_tel")
    private String relationTel;

	/** 身份类型 */
    @Field(value="response_type")
    private String responseType;

	/**  */
    @Field(value="contract_code")
    private String contractCode;

	/**  */
    @Field(value="insurl_code")
    private String insurlCode;

	/**  */
    @Field(value="balance")
    private double  balance;

	/** 最大门诊次数 */
    @Field(value="max_times")
    private int  maxTimes;

	/** 办卡时间 */
    @Field(value="lv_data")
    private Date lvData;

	/** 出生日期 */
    @Field(value="birthday")
    private Date birthday;

	/**  */
    @Field(value="occupation_type")
    private String occupationType;

	/**  */
    @Field(value="black_flag")
    private String blackFlag;

	/** 拼音码 */
    @Field(value="py_code")
    private String pyCode;

	/**  */
    @Field(value="cpy")
    private String cpy;

	/**  */
    @Field(value="max_ledger_sn")
    private int maxLedgerSn;

	/**  */
    @Field(value="max_item_sn")
    private int maxItemSn;

	/**  */
    @Field(value="max_receipt_sn")
    private int maxReceiptSn;

	/**  */
    @Field(value="charge_type")
    private String chargeType;

	/**  */
    @Field(value="addition_no1")
    private String additionNo1;

	/**  */
    @Field(value="bayy_flag")
    private String bayyFlag;

	/** 联系人证件号 */
    @Field(value="relation_card_no")
    private String relationCardNo;

	/** 联系人证件类型 */
    @Field(value="relation_card_type")
    private String relationCardType;

	/** 个人证件类型 */
    @Field(value="self_card_type")
    private String selfCardType;

	/**  */
    @Field(value="relation_p_id")
    private String relationPId;

	/** 电子健康码-实体卡 */
    @Field(value="IcCardId")
    private String icCardId;

	/** 电子健康码-虚拟卡 */
    @Field(value="HealthCardId")
    private String healthCardId;

	/** 微信绑定id */
    @Field(value="wx_open_id")
    private String wxOpenId;

	/** 三代医保卡号 */
    @Field(value="new_yb_card")
    private String newYbCard;

	/** 电子健康码-二维码 */
    @Field(value="EleHealthCarId")
    private String eleHealthCarId;

	/** 转入nhis */
    @Field(value="flag_to_his")
    private String flagToHis;



	@Override
	public String toString() {
		return "MzPatientMi{" +
				"patientId='" + patientId + '\'' +
				", inpatientNo='" + inpatientNo + '\'' +
				", socialNo='" + socialNo + '\'' +
				", hicNo='" + hicNo + '\'' +
				", pBarCode='" + pBarCode + '\'' +
				", name='" + name + '\'' +
				", sex='" + sex + '\'' +
				", marryCode='" + marryCode + '\'' +
				", countryCode='" + countryCode + '\'' +
				", nationCode='" + nationCode + '\'' +
				", birthPlace='" + birthPlace + '\'' +
				", homeDistrict='" + homeDistrict + '\'' +
				", homeStreet='" + homeStreet + '\'' +
				", homeTel='" + homeTel + '\'' +
				", homeZipcode='" + homeZipcode + '\'' +
				", relationName='" + relationName + '\'' +
				", relationCode='" + relationCode + '\'' +
				", relationTel='" + relationTel + '\'' +
				", responseType='" + responseType + '\'' +
				", contractCode='" + contractCode + '\'' +
				", insurlCode='" + insurlCode + '\'' +
				", balance=" + balance +
				", maxTimes=" + maxTimes +
				", lvData=" + lvData +
				", birthday=" + birthday +
				", occupationType='" + occupationType + '\'' +
				", blackFlag='" + blackFlag + '\'' +
				", pyCode='" + pyCode + '\'' +
				", cpy='" + cpy + '\'' +
				", maxLedgerSn=" + maxLedgerSn +
				", maxItemSn=" + maxItemSn +
				", maxReceiptSn=" + maxReceiptSn +
				", chargeType='" + chargeType + '\'' +
				", additionNo1='" + additionNo1 + '\'' +
				", bayyFlag='" + bayyFlag + '\'' +
				", relationCardNo='" + relationCardNo + '\'' +
				", relationCardType='" + relationCardType + '\'' +
				", selfCardType='" + selfCardType + '\'' +
				", relationPId='" + relationPId + '\'' +
				", icCardId='" + icCardId + '\'' +
				", healthCardId='" + healthCardId + '\'' +
				", wxOpenId='" + wxOpenId + '\'' +
				", newYbCard='" + newYbCard + '\'' +
				", eleHealthCarId='" + eleHealthCarId + '\'' +
				", flagToHis=" + flagToHis +
				'}';
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getInpatientNo() {
		return inpatientNo;
	}

	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}

	public String getSocialNo() {
		return socialNo;
	}

	public void setSocialNo(String socialNo) {
		this.socialNo = socialNo;
	}

	public String getHicNo() {
		return hicNo;
	}

	public void setHicNo(String hicNo) {
		this.hicNo = hicNo;
	}

	public String getpBarCode() {
		return pBarCode;
	}

	public void setpBarCode(String pBarCode) {
		this.pBarCode = pBarCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMarryCode() {
		return marryCode;
	}

	public void setMarryCode(String marryCode) {
		this.marryCode = marryCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getNationCode() {
		return nationCode;
	}

	public void setNationCode(String nationCode) {
		this.nationCode = nationCode;
	}

	public String getBirthPlace() {
		return birthPlace;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	public String getHomeDistrict() {
		return homeDistrict;
	}

	public void setHomeDistrict(String homeDistrict) {
		this.homeDistrict = homeDistrict;
	}

	public String getHomeStreet() {
		return homeStreet;
	}

	public void setHomeStreet(String homeStreet) {
		this.homeStreet = homeStreet;
	}

	public String getHomeTel() {
		return homeTel;
	}

	public void setHomeTel(String homeTel) {
		this.homeTel = homeTel;
	}

	public String getHomeZipcode() {
		return homeZipcode;
	}

	public void setHomeZipcode(String homeZipcode) {
		this.homeZipcode = homeZipcode;
	}

	public String getRelationName() {
		return relationName;
	}

	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}

	public String getRelationCode() {
		return relationCode;
	}

	public void setRelationCode(String relationCode) {
		this.relationCode = relationCode;
	}

	public String getRelationTel() {
		return relationTel;
	}

	public void setRelationTel(String relationTel) {
		this.relationTel = relationTel;
	}

	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getInsurlCode() {
		return insurlCode;
	}

	public void setInsurlCode(String insurlCode) {
		this.insurlCode = insurlCode;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public int getMaxTimes() {
		return maxTimes;
	}

	public void setMaxTimes(int maxTimes) {
		this.maxTimes = maxTimes;
	}

	public Date getLvData() {
		return lvData;
	}

	public void setLvData(Date lvData) {
		this.lvData = lvData;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getOccupationType() {
		return occupationType;
	}

	public void setOccupationType(String occupationType) {
		this.occupationType = occupationType;
	}

	public String getBlackFlag() {
		return blackFlag;
	}

	public void setBlackFlag(String blackFlag) {
		this.blackFlag = blackFlag;
	}

	public String getPyCode() {
		return pyCode;
	}

	public void setPyCode(String pyCode) {
		this.pyCode = pyCode;
	}

	public String getCpy() {
		return cpy;
	}

	public void setCpy(String cpy) {
		this.cpy = cpy;
	}

	public int getMaxLedgerSn() {
		return maxLedgerSn;
	}

	public void setMaxLedgerSn(int maxLedgerSn) {
		this.maxLedgerSn = maxLedgerSn;
	}

	public int getMaxItemSn() {
		return maxItemSn;
	}

	public void setMaxItemSn(int maxItemSn) {
		this.maxItemSn = maxItemSn;
	}

	public int getMaxReceiptSn() {
		return maxReceiptSn;
	}

	public void setMaxReceiptSn(int maxReceiptSn) {
		this.maxReceiptSn = maxReceiptSn;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public String getAdditionNo1() {
		return additionNo1;
	}

	public void setAdditionNo1(String additionNo1) {
		this.additionNo1 = additionNo1;
	}

	public String getBayyFlag() {
		return bayyFlag;
	}

	public void setBayyFlag(String bayyFlag) {
		this.bayyFlag = bayyFlag;
	}

	public String getRelationCardNo() {
		return relationCardNo;
	}

	public void setRelationCardNo(String relationCardNo) {
		this.relationCardNo = relationCardNo;
	}

	public String getRelationCardType() {
		return relationCardType;
	}

	public void setRelationCardType(String relationCardType) {
		this.relationCardType = relationCardType;
	}

	public String getSelfCardType() {
		return selfCardType;
	}

	public void setSelfCardType(String selfCardType) {
		this.selfCardType = selfCardType;
	}

	public String getRelationPId() {
		return relationPId;
	}

	public void setRelationPId(String relationPId) {
		this.relationPId = relationPId;
	}

	public String getIcCardId() {
		return icCardId;
	}

	public void setIcCardId(String icCardId) {
		this.icCardId = icCardId;
	}

	public String getHealthCardId() {
		return healthCardId;
	}

	public void setHealthCardId(String healthCardId) {
		this.healthCardId = healthCardId;
	}

	public String getWxOpenId() {
		return wxOpenId;
	}

	public void setWxOpenId(String wxOpenId) {
		this.wxOpenId = wxOpenId;
	}

	public String getNewYbCard() {
		return newYbCard;
	}

	public void setNewYbCard(String newYbCard) {
		this.newYbCard = newYbCard;
	}

	public String getEleHealthCarId() {
		return eleHealthCarId;
	}

	public void setEleHealthCarId(String eleHealthCarId) {
		this.eleHealthCarId = eleHealthCarId;
	}

	public String getFlagToHis() {
		return flagToHis;
	}

	public void setFlagToHis(String flagToHis) {
		this.flagToHis = flagToHis;
	}
}
