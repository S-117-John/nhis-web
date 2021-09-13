package com.zebone.nhis.ma.pub.sd.vo.health;

/**
 * 电子健康码 患者信息实体类 
 * @author zhangtao
 *
  */
public class EhealthCodePatiVO {

	/**
     * 电子健康卡ID
     */
    public String healthCardId;

    /**
     * 居民健康卡主索引ID
     */
    public String mindexId;

    /**
     * 电子健康卡属性 1-临时卡(电子就诊卡)；2-正式卡(电子健康卡)
     */
    public String healthCardAttr;

    /**
     * 电子健康卡状态  1-正常；2-暂停
     */
    public String healthCardStatus;

    /**
     * 电子健康卡二维码图片
     */
    public String healthCardQrcode;
    
    /**
     * 电子健康卡二维码内容。只返回静态二维码内容
     */
    public String healthCardQrcodeData;

    /**
     * 证件类型
     */
    public String idType;

    /**
     * 证件号
     */
    public String idNo;

    /**
     * 用户姓名
     */
    public String name;

    /**
     * 用户性别
     */
    public String gender;

    /**
     * 民族
     */
    public String nation;

    /**
     * 出生日期
     */
    public String birthday;

    /**
     * 手机号码
     */
    public String mobile;

    /**
     * 联系电话
     */
    public String telephone;

    /**
     * 居住地址
     */
    public String address;

    /**
     * 工作单位
     */
    public String unit;

    /**
     * 支付Token
     */
    public String token;

    /**
     * 动态码剩余成功验证次数
     */
    public String surplusNum;


    /**
     * 最后更新时间
     */
    public String updateTime;


	public String getHealthCardId() {
		return healthCardId;
	}


	public void setHealthCardId(String healthCardId) {
		this.healthCardId = healthCardId;
	}


	public String getMindexId() {
		return mindexId;
	}


	public void setMindexId(String mindexId) {
		this.mindexId = mindexId;
	}


	public String getHealthCardAttr() {
		return healthCardAttr;
	}


	public void setHealthCardAttr(String healthCardAttr) {
		this.healthCardAttr = healthCardAttr;
	}


	public String getHealthCardStatus() {
		return healthCardStatus;
	}


	public void setHealthCardStatus(String healthCardStatus) {
		this.healthCardStatus = healthCardStatus;
	}


	public String getHealthCardQrcodeData() {
		return healthCardQrcodeData;
	}


	public void setHealthCardQrcodeData(String healthCardQrcodeData) {
		this.healthCardQrcodeData = healthCardQrcodeData;
	}


	public String getIdType() {
		return idType;
	}


	public void setIdType(String idType) {
		this.idType = idType;
	}


	public String getIdNo() {
		return idNo;
	}


	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public String getNation() {
		return nation;
	}


	public void setNation(String nation) {
		this.nation = nation;
	}


	public String getBirthday() {
		return birthday;
	}


	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getTelephone() {
		return telephone;
	}


	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getUnit() {
		return unit;
	}


	public void setUnit(String unit) {
		this.unit = unit;
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public String getSurplusNum() {
		return surplusNum;
	}


	public void setSurplusNum(String surplusNum) {
		this.surplusNum = surplusNum;
	}


	public String getUpdateTime() {
		return updateTime;
	}


	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}


	public String getHealthCardQrcode() {
		return healthCardQrcode;
	}


	public void setHealthCardQrcode(String healthCardQrcode) {
		this.healthCardQrcode = healthCardQrcode;
	}
	 
}
