package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

public class Input2302Data {
	
	private String	feedetl_sn;//		费用明细流水号	 传入“0000”时删除全部
	private String	mdtrt_id;//	Y　	就诊ID
	private String	psn_no;//	　Y	人员编号	
	public String getFeedetl_sn() {
		return feedetl_sn;
	}
	public void setFeedetl_sn(String feedetl_sn) {
		this.feedetl_sn = feedetl_sn;
	}
	public String getMdtrt_id() {
		return mdtrt_id;
	}
	public void setMdtrt_id(String mdtrt_id) {
		this.mdtrt_id = mdtrt_id;
	}
	public String getPsn_no() {
		return psn_no;
	}
	public void setPsn_no(String psn_no) {
		this.psn_no = psn_no;
	}
	
}
