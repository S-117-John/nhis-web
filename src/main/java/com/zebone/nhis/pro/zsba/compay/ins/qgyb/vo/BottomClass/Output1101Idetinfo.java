package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

public class Output1101Idetinfo {
	
	private String	psn_idet_type;//	Y　	
	private String	psn_type_lv;//		详见残疾等级字典
	private String	memo;//		
	private String	begntime;//	Y　	yyyy-MM-dd HH:mm:ss
	private String	endtime;//		yyyy-MM-dd HH:mm:ss
	private String	emp_name;//		单位名称
	public String getPsn_idet_type() {
		return psn_idet_type;
	}
	public void setPsn_idet_type(String psn_idet_type) {
		this.psn_idet_type = psn_idet_type;
	}
	public String getPsn_type_lv() {
		return psn_type_lv;
	}
	public void setPsn_type_lv(String psn_type_lv) {
		this.psn_type_lv = psn_type_lv;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getBegntime() {
		return begntime;
	}
	public void setBegntime(String begntime) {
		this.begntime = begntime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getEmp_name() {
		return emp_name;
	}
	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
	}
}
