package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

public class Output1101Insuinfo {
	
	private String	balc;//	Y	余额
	private String	insutype;//	Y	险种类型
	private String	psn_type;//	Y	人员类别
	private String	cvlserv_flag;//	Y	公务员标志
	private String	insuplc_admdvs;//	Y	参保地医保区划
	private String	emp_name;//		单位名称
	private String psn_insu_stas;// 1为正常险种
	private String	psn_insu_date;//	参保时间	
	private String	paus_insu_date;//	停保时间
	public String getBalc() {
		return balc;
	}
	public void setBalc(String balc) {
		this.balc = balc;
	}
	public String getInsutype() {
		return insutype;
	}
	public void setInsutype(String insutype) {
		this.insutype = insutype;
	}
	public String getPsn_type() {
		return psn_type;
	}
	public void setPsn_type(String psn_type) {
		this.psn_type = psn_type;
	}
	public String getCvlserv_flag() {
		return cvlserv_flag;
	}
	public void setCvlserv_flag(String cvlserv_flag) {
		this.cvlserv_flag = cvlserv_flag;
	}
	public String getInsuplc_admdvs() {
		return insuplc_admdvs;
	}
	public void setInsuplc_admdvs(String insuplc_admdvs) {
		this.insuplc_admdvs = insuplc_admdvs;
	}
	public String getEmp_name() {
		return emp_name;
	}
	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
	}
	public String getPsn_insu_stas() {
		return psn_insu_stas;
	}
	public void setPsn_insu_stas(String psn_insu_stas) {
		this.psn_insu_stas = psn_insu_stas;
	}
	public String getPsn_insu_date() {
		return psn_insu_date;
	}
	public void setPsn_insu_date(String psn_insu_date) {
		this.psn_insu_date = psn_insu_date;
	}
	public String getPaus_insu_date() {
		return paus_insu_date;
	}
	public void setPaus_insu_date(String paus_insu_date) {
		this.paus_insu_date = paus_insu_date;
	}
	
}
