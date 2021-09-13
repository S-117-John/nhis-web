package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

public class Input2304Data {
	
	private String	psn_no;//	Y	人员编号	
	private String	mdtrt_cert_type;//	Y	就诊凭证类型	
	private String	mdtrt_cert_no;//		就诊凭证编号	就诊凭证类型为“01”时填写电子凭证令牌，为“02”时填写身份证号，为“03”时填写社会保障卡卡号
	private String	medfee_sumamt;//	Y	医疗费总额	
	private String	psn_setlway;//	Y	个人结算方式	
	private String	mdtrt_id;//	Y	就诊ID	
	private String	acct_used_flag;//		个人账户使用标志	
	private String	insutype;//	Y	险种类型	
	private String	invono;//		发票号	
	private String mid_setl_flag;//中途结算标志
	public String getPsn_no() {
		return psn_no;
	}
	public void setPsn_no(String psn_no) {
		this.psn_no = psn_no;
	}
	public String getMdtrt_cert_type() {
		return mdtrt_cert_type;
	}
	public void setMdtrt_cert_type(String mdtrt_cert_type) {
		this.mdtrt_cert_type = mdtrt_cert_type;
	}
	public String getMdtrt_cert_no() {
		return mdtrt_cert_no;
	}
	public void setMdtrt_cert_no(String mdtrt_cert_no) {
		this.mdtrt_cert_no = mdtrt_cert_no;
	}
	public String getMedfee_sumamt() {
		return medfee_sumamt;
	}
	public void setMedfee_sumamt(String medfee_sumamt) {
		this.medfee_sumamt = medfee_sumamt;
	}
	public String getPsn_setlway() {
		return psn_setlway;
	}
	public void setPsn_setlway(String psn_setlway) {
		this.psn_setlway = psn_setlway;
	}
	public String getMdtrt_id() {
		return mdtrt_id;
	}
	public void setMdtrt_id(String mdtrt_id) {
		this.mdtrt_id = mdtrt_id;
	}
	public String getAcct_used_flag() {
		return acct_used_flag;
	}
	public void setAcct_used_flag(String acct_used_flag) {
		this.acct_used_flag = acct_used_flag;
	}
	public String getInsutype() {
		return insutype;
	}
	public void setInsutype(String insutype) {
		this.insutype = insutype;
	}
	public String getInvono() {
		return invono;
	}
	public void setInvono(String invono) {
		this.invono = invono;
	}
	public String getMid_setl_flag() {
		return mid_setl_flag;
	}
	public void setMid_setl_flag(String mid_setl_flag) {
		this.mid_setl_flag = mid_setl_flag;
	}
	
}
