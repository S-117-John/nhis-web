package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

/**
 * 医疗保障基金结算清单信息上传入参——收费项目信息
 * @author Administrator
 *
 */
public class Input4101Iteminfo {
	
	private String	med_chrgitm;//	Y	医疗收费项目	参照医疗收费项目类别
	private String	amt;//	Y	金额	
	private String	claa_sumfee;//	Y	甲类费用合计	
	private String	clab_amt;//	Y	乙类金额	
	private String	fulamt_ownpay_amt;//	Y	全自费金额	
	private String	oth_amt;//	Y	其他金额	
	public String getMed_chrgitm() {
		return med_chrgitm;
	}
	public void setMed_chrgitm(String med_chrgitm) {
		this.med_chrgitm = med_chrgitm;
	}
	public String getAmt() {
		return amt;
	}
	public void setAmt(String amt) {
		this.amt = amt;
	}
	public String getClaa_sumfee() {
		return claa_sumfee;
	}
	public void setClaa_sumfee(String claa_sumfee) {
		this.claa_sumfee = claa_sumfee;
	}
	public String getClab_amt() {
		return clab_amt;
	}
	public void setClab_amt(String clab_amt) {
		this.clab_amt = clab_amt;
	}
	public String getFulamt_ownpay_amt() {
		return fulamt_ownpay_amt;
	}
	public void setFulamt_ownpay_amt(String fulamt_ownpay_amt) {
		this.fulamt_ownpay_amt = fulamt_ownpay_amt;
	}
	public String getOth_amt() {
		return oth_amt;
	}
	public void setOth_amt(String oth_amt) {
		this.oth_amt = oth_amt;
	}

	

	
}
