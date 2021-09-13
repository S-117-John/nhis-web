package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

public class Input5201Data {
	
	private String	psn_no;//	Y	人员编号	
	private String	begntime;//	Y	开始时间 yyyy-MM-dd HH:mm:ss
	private String	endtime;//	Y	结束时间 yyyy-MM-dd HH:mm:ss
	private String	med_type;//	Y	医疗类别
	private String	mdtrt_id;//		就诊ID	
	public String getPsn_no() {
		return psn_no;
	}
	public void setPsn_no(String psn_no) {
		this.psn_no = psn_no;
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
	public String getMed_type() {
		return med_type;
	}
	public void setMed_type(String med_type) {
		this.med_type = med_type;
	}
	public String getMdtrt_id() {
		return mdtrt_id;
	}
	public void setMdtrt_id(String mdtrt_id) {
		this.mdtrt_id = mdtrt_id;
	}
	
	
}
