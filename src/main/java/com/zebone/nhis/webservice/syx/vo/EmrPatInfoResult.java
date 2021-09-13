package com.zebone.nhis.webservice.syx.vo;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



/**
 * 住院患者基本信息结果记录
 * @author Kongxuedong
 *
 */
@XmlRootElement(name = "RECORD")
public class EmrPatInfoResult {
	
	//患者编码
	private String codePi;
	//患者姓名
	private String name;
	//住院号
	private String codeIp;
	//就诊编码
	private String codePv;
	//性别
	private String nameSex;
	//年龄
	private String agePv;
	//证件号码
	private String idNo;
	//出生日期
	private String birthDate;
	//婚否
	private String dtMarry;
	//联系电话(手机)
	private String mobile;
	//家庭住址
	private String addrCur;
	//住址电话
	private String telNo;
	//职业名称
	private String occuName;
	//联系人姓名
	private String nameRel;
	//联系人地址
	private String addrRel;
	//联系人电话
	private String telRel;
	//联系人关系
	private String dtRalation;
	//住院次数
	private String ipTimes;
	//床位号
	private String bedNo;
	//床位
	private String bedNoId;
	//入院方式
	private String dtIntype;
	//病情等级
	private String dtLevelDise;
	//入院时间
	private String dateBegin;
	//入院科室编码
	private String pkDeptAdmit;
	//入院科室名称
	private String nameDeptAdmit;
	//住院科室编码
	private String pkDept;
	//住院科室名称
	private String nameDept;
	//治疗科室编码
	private String pkDeptZl;
	//治疗科室名称
	private String nameDeptZl;
	//出院日期
	private String dateEnd;
	//出院方式
	private String dtOuttype;
	
	@XmlElement(name="PATIENT_ID")
	public String getCodePi() {
		return codePi;
	}
	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}
	
	@XmlElement(name="PATIENT_NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement(name="IP_SEQNO_TEXT")
	public String getCodeIp() {
		return codeIp;
	}
	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}
	
	@XmlElement(name="IN_PATIENT_ID")
	public String getCodePv() {
		return codePv;
	}
	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}
	
	@XmlElement(name="SEX_FLAG")
	public String getNameSex() {
		return nameSex;
	}
	public void setNameSex(String nameSex) {
		this.nameSex = nameSex;
	}
	
	@XmlElement(name="AGE")
	public String getAgePv() {
		return agePv;
	}
	public void setAgePv(String agePv) {
		this.agePv = agePv;
	}
	
	@XmlElement(name="IDENTITY_CARD_NO")
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	
	@XmlElement(name="BIRTHDAY")
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	
	@XmlElement(name="MARRIAGE_FLAG")
	public String getDtMarry() {
		return dtMarry;
	}
	public void setDtMarry(String dtMarry) {
		this.dtMarry = dtMarry;
	}
	
	@XmlElement(name="CELL_PHONE")
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@XmlElement(name="HOME_ADDRESS")
	public String getAddrCur() {
		return addrCur;
	}
	public void setAddrCur(String addrCur) {
		this.addrCur = addrCur;
	}
	
	@XmlElement(name="HOME_PHONE")
	public String getTelNo() {
		return telNo;
	}
	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}
	
	@XmlElement(name="PROFESSION_NAME")
	public String getOccuName() {
		return occuName;
	}
	public void setOccuName(String occuName) {
		this.occuName = occuName;
	}
	
	@XmlElement(name="CONTACT_PERSON")
	public String getNameRel() {
		return nameRel;
	}
	public void setNameRel(String nameRel) {
		this.nameRel = nameRel;
	}
	
	@XmlElement(name="CONTACT_ADDRESS")
	public String getAddrRel() {
		return addrRel;
	}
	public void setAddrRel(String addrRel) {
		this.addrRel = addrRel;
	}
	
	@XmlElement(name="CONTACT_PHONE")
	public String getTelRel() {
		return telRel;
	}
	public void setTelRel(String telRel) {
		this.telRel = telRel;
	}
	
	@XmlElement(name="CONTACT_RELATIONSHIP_FLAG")
	public String getDtRalation() {
		return dtRalation;
	}
	public void setDtRalation(String dtRalation) {
		this.dtRalation = dtRalation;
	}
	
	@XmlElement(name="IP_TIMES")
	public String getIpTimes() {
		return ipTimes;
	}
	public void setIpTimes(String ipTimes) {
		this.ipTimes = ipTimes;
	}
	
	@XmlElement(name="SICK_BED_NO")
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	
	@XmlElement(name="SICK_BED_ID")
	public String getBedNoId() {
		return bedNoId;
	}
	public void setBedNoId(String bedNoId) {
		this.bedNoId = bedNoId;
	}
	
	@XmlElement(name="IP_MANNER_FLAG")
	public String getDtIntype() {
		return dtIntype;
	}
	public void setDtIntype(String dtIntype) {
		this.dtIntype = dtIntype;
	}
	
	@XmlElement(name="DISEASE_STATE_FLAG")
	public String getDtLevelDise() {
		return dtLevelDise;
	}
	public void setDtLevelDise(String dtLevelDise) {
		this.dtLevelDise = dtLevelDise;
	}
	
	@XmlElement(name="ADMISSION_TIME")
	public String getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(String dateBegin) {
		this.dateBegin = dateBegin;
	}
	
	@XmlElement(name="ADMISSION_DEP_NO")
	public String getPkDeptAdmit() {
		return pkDeptAdmit;
	}
	public void setPkDeptAdmit(String pkDeptAdmit) {
		this.pkDeptAdmit = pkDeptAdmit;
	}
	
	@XmlElement(name="ADMISSION_DEP_NAME")
	public String getNameDeptAdmit() {
		return nameDeptAdmit;
	}
	public void setNameDeptAdmit(String nameDeptAdmit) {
		this.nameDeptAdmit = nameDeptAdmit;
	}
	@XmlElement(name="IP_DEP_NO")
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	@XmlElement(name="IP_DEP_NAME")
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	@XmlElement(name="CURE_DEP_NO")
	public String getPkDeptZl() {
		return pkDeptZl;
	}
	public void setPkDeptZl(String pkDeptZl) {
		this.pkDeptZl = pkDeptZl;
	}
	@XmlElement(name="CURE_DEP_NAME")
	public String getNameDeptZl() {
		return nameDeptZl;
	}
	public void setNameDeptZl(String nameDeptZl) {
		this.nameDeptZl = nameDeptZl;
	}
	@XmlElement(name="OUT_DATE")
	public String getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}
	
	@XmlElement(name="OUT_TYPE_FLAG")
	public String getDtOuttype() {
		return dtOuttype;
	}
	public void setDtOuttype(String dtOuttype) {
		this.dtOuttype = dtOuttype;
	}
}
