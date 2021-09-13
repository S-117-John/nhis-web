package com.zebone.nhis.common.arch.vo;

import javax.xml.bind.annotation.XmlElement;

public class UploadHead {

	
//	<visittype>3</visittype> --就诊类型1门诊2急诊3住院4体检
//	 <docname> HIS_010293128.pdf</docname> --文件名称
//	 <doctype> </doctype> --文件类型：检查，检验，病历等
//	<sysname>HIS</sysname>  --系统名称
//	<date>2017-06-16 10:39:00</date> --报告日期
//	<user>张小青</user>   --操作员
//	<size>32768000</size> --文件大小
//	<memo>住院电子病历-出院小结</memo> --说明
//	<paticode>0123344</paticode> --患者编码
//	<patiname>王鹤松</patiname>--姓名
//	<visitcode>09238019</visitcode>--就诊编码
//	<visitdate>2017-06-14</visitdate>--就诊日期
//	<path>../2001~4000/0123344_王鹤松/09238019_20170505/</path>--文件路径
//	<status>0</status>  --0未归
	
	/*
	 * 
	<visittype>3</visittype> --就诊类型1门诊2急诊3住院4体检
  	<docname> HIS_010293128.pdf</docname> --文件名称
  	<doctype> 0016</doctype> --文件类型：检查：0016，检验0017 
	<sysname>HIS</sysname>  --系统名称
	<date>2017-06-16 10:39:00</date> --报告日期
	<user>张小青</user>   --操作员
	<sex>男</sex>
	<age>12</age>
	<dept>内科门诊</dept> --申请科室
	<deptr>CT室</deptr>报告科室
	<rid>20090907853473</rid>申请单号
	<rserial>76200</rserial>报告序列号 ，根据各系统实际情况，rid和reserial可以相同，也可不同
	<iptimes>1</iptimes>就诊次数
	<size>32768000</size> --文件大小
	<memo>住院电子病历-出院小结</memo> --说明/项目名称
	<paticode>0123344</paticode> --患者ID
	<patiname>王鹤松</patiname>--姓名
	<visitcode>09238019</visitcode>--门诊：患者ID 住院：住院号 体检：体检号
	<visitdate>2017-06-14 10:39:00</visitdate>--开单申请日期

	 */
	private String visittype;
	
	private String docname;
	
	private String doctype;
	
	private String sysname;
	
	private String date;
	
	private String user;
	
	
	private String memo;
	
	
	private String paticode;
	
	
	private String patiname;
	
	
	private String visitcode;
	
	
	private String visitdate;
	
	private String path;
	
	private String status;
	
	private String rid;
	
	private String rserial;
	
	private String dept;
	

	private String sex;
	
	private String age;
	
	private String deptr;
	
	private String iptimes;
	
	private String p_id;
	
	private String isarch;
	
	private String times;
	@XmlElement(name="age")
	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
	@XmlElement(name="deptr")
	public String getDeptr() {
		return deptr;
	}

	public void setDeptr(String deptr) {
		this.deptr = deptr;
	}
	@XmlElement(name="iptimes")
	public String getIptimes() {
		return iptimes;
	}

	public void setIptimes(String iptimes) {
		this.iptimes = iptimes;
	}

	@XmlElement(name="sex")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@XmlElement(name="dept")
	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}


	@XmlElement(name="rid")
	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}
	@XmlElement(name="rserial")
	public String getRserial() {
		return rserial;
	}

	public void setRserial(String rserial) {
		this.rserial = rserial;
	}

	@XmlElement(name="visittype")
	public String getVisittype() {
		return visittype;
	}

	public void setVisittype(String visittype) {
		this.visittype = visittype;
	}

	@XmlElement(name="docname")
	public String getDocname() {
		return docname;
	}

	public void setDocname(String docname) {
		this.docname = docname;
	}

	@XmlElement(name="doctype")
	public String getDoctype() {
		return doctype;
	}

	public void setDoctype(String doctype) {
		this.doctype = doctype;
	}

	@XmlElement(name="sysname")
	public String getSysname() {
		return sysname;
	}

	public void setSysname(String sysname) {
		this.sysname = sysname;
	}

	@XmlElement(name="date")
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@XmlElement(name="user")
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@XmlElement(name="memo")
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@XmlElement(name="paticode")
	public String getPaticode() {
		return paticode;
	}

	public void setPaticode(String paticode) {
		this.paticode = paticode;
	}

	@XmlElement(name="patiname")
	public String getPatiname() {
		return patiname;
	}

	public void setPatiname(String patiname) {
		this.patiname = patiname;
	}

	@XmlElement(name="visitcode")
	public String getVisitcode() {
		return visitcode;
	}

	public void setVisitcode(String visitcode) {
		this.visitcode = visitcode;
	}

	@XmlElement(name="visitdate")
	public String getVisitdate() {
		return visitdate;
	}

	public void setVisitdate(String visitdate) {
		this.visitdate = visitdate;
	}

	@XmlElement(name="path")
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@XmlElement(name="status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	@XmlElement(name="p_id")
	public String getP_id() {
		return p_id;
	}

	public void setP_id(String p_id) {
		this.p_id = p_id;
	}

	@XmlElement(name="isarch")
	public String getIsarch() {
		return isarch;
	}

	public void setIsarch(String isarch) {
		this.isarch = isarch;
	}

	@XmlElement(name="times")
	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}
	
	

}
