package com.zebone.nhis.webservice.zhongshan.vo;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.common.support.CommonUtils;

public class PacsApplyRecordRisVo {
	private String recordSn;		// 申请单主键(非空) record_sn
	private String uid;				// 申请单号(非空) uid
	private String opeType;			// 申请单状态(非空) ope_type
	private String patientId;		// 患者id(非空) patient_id
	private String inpatientNo;		// 住院号(住院不为空) inpatient_no
	private String times;			// 就诊次数 times
	private String name;			// 患者姓名(非空) name
	private String sex;				// 患者性别代码(非空, 0女 1男) sex
	private Date birthday;			// 出生日期(非空, yyyy-mm-dd) birthday
	private String age;				// 生日 -- 根据各个系统的函数计算返回
	private String address;			// 住址 address
	private String telephone;		// 联系电话 telephone
	private String company;			// 工作单位 company
	private String bedNo;			// 床号 bed_no
	private String checkType;		// 检查类型代码(非空, 7代表心电图申请) check_type
	private String bodyOfCase;		// 检查项目 body_of_case
	private String sendType;		// 就诊类型(非空, 1门诊 2住院 3体检 4妇幼) send_type
	private String sendDept;		// 申请科室(非空 , 科室名称) send_dept
	private String docName;			// 申请医生(非空 , 医生姓名) doc_name
	private Date applyDate;			// 申请日期(非空 , yyyy-mm-dd hh:nn:ss) apply_date
	private String description;		// 临床描述 description
	private String diagnosis;		// 诊断及检查目的 diagnosis
	private double price;			// 价格(非空) price
	private String emergencyFlag;	// 加急标志(非空, 1加急 0普通）emergency_flag
	private String sendDw;			// 申请机构 (申请机构名称)send_dw
	private String sysFlag;			// 新旧系统 (申请机构来源)sys_flag
	private String idNo;			// 患者证件号 ID_NO
	private String idSn;			// 患者唯一索引号
	private String fyPno;			// 妇幼保健号
	@Override
	public String toString() {
		return "{ \"record_sn\":\"" + CommonUtils.getString(recordSn)
		    + "\",\"uid\":\"" + CommonUtils.getString(uid)
		    + "\",\"opeType\":\"" + CommonUtils.getString(opeType)
		    + "\",\"patient_id\":\"" + CommonUtils.getString(patientId)
		    + "\",\"inpatient_no\":\"" + CommonUtils.getString(inpatientNo,"")
		    + "\",\"times\":\"" + CommonUtils.getString(times,"")
		    + "\",\"name\":\"" + CommonUtils.getString(name)
		    + "\",\"sex\":\"" + CommonUtils.getString(sex)
		    + "\",\"birthday\":\"" + getFormatDate("yyyy-MM-dd",birthday)
		    + "\",\"age\":\"" + CommonUtils.getString(age,"")
		    + "\",\"address\":\"" + CommonUtils.getString(address,"")
		    + "\",\"telephone\":\"" + CommonUtils.getString(telephone,"")
		    + "\",\"company\":\"" + CommonUtils.getString(company , "")
		    + "\",\"bed_no\":\"" + CommonUtils.getString(bedNo , "") 
		    + "\",\"check_type\":\"" + CommonUtils.getString(checkType)
		    + "\",\"body_of_case\":"  + JSON.toJSONString(CommonUtils.getString(bodyOfCase))
		    + ",\"send_type\":\"" + CommonUtils.getString(sendType)
		    + "\",\"send_dept\":\"" + CommonUtils.getString(sendDept)
		    + "\",\"doc_name\":\"" + CommonUtils.getString(docName)
		    + "\",\"apply_date\":\"" + getFormatDate("yyyy-MM-dd HH:mm:ss",applyDate)
		    + "\",\"description\":" + JSON.toJSONString(CommonUtils.getString(description , "")) 
			+ ",\"diagnosis\":" + JSON.toJSONString(CommonUtils.getString(diagnosis , ""))
			+ ",\"price\":\"" + (new DecimalFormat(".##")).format(price)
		    + "\",\"emergency_flag\":\"" + CommonUtils.getString(emergencyFlag,"") 
		    + "\",\"sys_flag\":\"" + CommonUtils.getString(sysFlag,"")  
		    + "\",\"id_no\":\"" + CommonUtils.getString(idNo,"")   
		    + "\",\"id_sn\":\"" + CommonUtils.getString(idSn,"")   
		    + "\",\"fy_pno\":\"" + CommonUtils.getString(fyPno,"")  
		    + "\",\"send_dw\":\"" + CommonUtils.getString(sendDw , "") + "\"},";
	}

	public String getIdSn() {
		return idSn;
	}

	public void setIdSn(String idSn) {
		this.idSn = idSn;
	}

	public String getFyPno() {
		return fyPno;
	}

	public void setFyPno(String fyPno) {
		this.fyPno = fyPno;
	}

	public String getRecordSn() {
		return recordSn;
	}

	public void setRecordSn(String recordSn) {
			this.recordSn = recordSn;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getOpeType() {
		return opeType;
	}
	public void setOpeType(String opeType) {
		this.opeType = opeType;
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
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	public String getBodyOfCase() {
		return bodyOfCase;
	}
	public void setBodyOfCase(String bodyOfCase) {
		this.bodyOfCase = bodyOfCase;
	}
	public String getSendType() {
		return sendType;
	}
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	public String getSendDept() {
		return sendDept;
	}
	public void setSendDept(String sendDept) {
		this.sendDept = sendDept;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getEmergencyFlag() {
		return emergencyFlag;
	}
	public void setEmergencyFlag(String emergencyFlag) {
		this.emergencyFlag = emergencyFlag;
	}
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getSendDw() {
		return sendDw;
	}
	public void setSendDw(String sendDw) {
		this.sendDw = sendDw;
	}
	
	public String getSysFlag() {
		return sysFlag;
	}

	public void setSysFlag(String sysFlag) {
		this.sysFlag = sysFlag;
	}

	/**
	 * 返回某种格式的时间
	 * @param formatStyle 时间的格式
	 * @param dateTime 时间
	 * @return
	 */
	public String getFormatDate( String formatStyle, Date dateTime){
		SimpleDateFormat format = new SimpleDateFormat(formatStyle);
		return format.format(dateTime);
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
}
