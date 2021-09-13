package com.zebone.nhis.ma.pub.sd.vo.health;

/**
 * 电子健康码 EHC01 请求类
 * @author zhangtao
 *
 */
public class EhealthCodeEHC01ReqVO {

	/**
	 * 证件类型
	 */
	private String idType;
	
	/**
	 * 证件号
	 */
	private String idNo;
	
	/**
	 * 姓名
	 */
	private String name;
	
	/**
	 * 性别
	 */
	private String gender;
	
	/**
	 * 民族
	 */
	private String nation;
	
	/**
	 * 出生日期
	 */
	private String birthday;
	
	/**
	 * 手机号码
	 */
	private String mobile;
	
	/**
	 * 联系电话
	 */
	private String telephone;
	
	/**
	 * 居住地址
	 */
	private String address;
	
	/**
	 * 工作单位
	 */
	private String unit;

	/**
	 * 是否返回二维码图片标识 默认不返回  1：返回二维码图片 
	 */
	private String qrCodePicFlag;
	
	/**
	 * 监护人姓名
	 */
	private String guardianName;
	
	/**
	 * 监护人证件类型
	 */
	private String guardianIdType;
	
	/**
	 * 监护人证件号码
	 */
	private String guardianIdNo;
	
	/**
	 * 监护人与本人关系
	 */
	private String guardianRelation;

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

	public String getQrCodePicFlag() {
		return qrCodePicFlag;
	}

	public void setQrCodePicFlag(String qrCodePicFlag) {
		this.qrCodePicFlag = qrCodePicFlag;
	}

	public String getGuardianName() {
		return guardianName;
	}

	public void setGuardianName(String guardianName) {
		this.guardianName = guardianName;
	}

	public String getGuardianIdType() {
		return guardianIdType;
	}

	public void setGuardianIdType(String guardianIdType) {
		this.guardianIdType = guardianIdType;
	}

	public String getGuardianIdNo() {
		return guardianIdNo;
	}

	public void setGuardianIdNo(String guardianIdNo) {
		this.guardianIdNo = guardianIdNo;
	}

	public String getGuardianRelation() {
		return guardianRelation;
	}

	public void setGuardianRelation(String guardianRelation) {
		this.guardianRelation = guardianRelation;
	}

}
