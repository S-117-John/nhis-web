package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

public class Output1101Baseinfo {
	
	private String	psn_no;//	Y	人员编号	
	private String	psn_cert_type;//	Y	人员证件类型	
	private String	certno;//	Y	证件号码	
	private String	psn_name;//	Y	人员姓名	
	private String	gend;//	Y	性别	
	private String	naty;//		民族	
	private String	brdy;//		出生日期	yyyy-MM-dd
	private String	age;//	Y	年龄	

	public String getPsn_no() {
		return psn_no;
	}
	public void setPsn_no(String psn_no) {
		this.psn_no = psn_no;
	}
	public String getPsn_cert_type() {
		return psn_cert_type;
	}
	public void setPsn_cert_type(String psn_cert_type) {
		this.psn_cert_type = psn_cert_type;
	}
	public String getCertno() {
		return certno;
	}
	public void setCertno(String certno) {
		this.certno = certno;
	}
	public String getPsn_name() {
		return psn_name;
	}
	public void setPsn_name(String psn_name) {
		this.psn_name = psn_name;
	}
	public String getGend() {
		return gend;
	}
	public void setGend(String gend) {
		this.gend = gend;
	}
	public String getNaty() {
		return naty;
	}
	public void setNaty(String naty) {
		this.naty = naty;
	}
	public String getBrdy() {
		return brdy;
	}
	public void setBrdy(String brdy) {
		this.brdy = brdy;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
}
