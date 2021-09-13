package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

import com.google.gson.annotations.SerializedName;

/**
 * 5.2.4.1【2301】住院费用明细上传 回参 （节点标识：resultl）
 * @author Administrator
 *
 */
public class Output2301Result {
	
	private String	feedetl_sn;//		费用明细流水号	
	private String	det_item_fee_sumamt;//	Y	明细项目费用总额	
	private String	cnt;//	Y	数量	
	private String	pric;//	Y	单价	
	private String	pric_uplmt_amt;//	Y	定价上限金额	
	private String	selfpay_prop;//		自付比例	
	private String	fulamt_ownpay_amt;//		全自费金额	
	private String	overlmt_amt;//		超限价金额	
	private String	preselfpay_amt;//		先行自付金额	
	private String	inscp_scp_amt;//		符合政策范围金额	
	private String	chrgitm_lv;//	Y	收费项目等级	
	private String	med_chrgitm_type;//	Y	医疗收费项目类别	
	private String	bas_medn_flag;//		基本药物标志	
	private String	hi_nego_drug_flag;//		医保谈判药品标志	
	private String	chld_medc_flag;//		儿童用药标志	
	private String	list_sp_item_flag;//		目录特项标志	特检特治项目或特殊药品
	private String	lmt_used_flag;//	Y	限制使用标志	
	private String	drt_reim_flag;//		直报标志	
	private String	memo;//		备注	明细分割错误信息
	public String getFeedetl_sn() {
		return feedetl_sn;
	}
	public void setFeedetl_sn(String feedetl_sn) {
		this.feedetl_sn = feedetl_sn;
	}
	public String getDet_item_fee_sumamt() {
		return det_item_fee_sumamt;
	}
	public void setDet_item_fee_sumamt(String det_item_fee_sumamt) {
		this.det_item_fee_sumamt = det_item_fee_sumamt;
	}
	public String getCnt() {
		return cnt;
	}
	public void setCnt(String cnt) {
		this.cnt = cnt;
	}
	public String getPric() {
		return pric;
	}
	public void setPric(String pric) {
		this.pric = pric;
	}
	public String getPric_uplmt_amt() {
		return pric_uplmt_amt;
	}
	public void setPric_uplmt_amt(String pric_uplmt_amt) {
		this.pric_uplmt_amt = pric_uplmt_amt;
	}
	public String getSelfpay_prop() {
		return selfpay_prop;
	}
	public void setSelfpay_prop(String selfpay_prop) {
		this.selfpay_prop = selfpay_prop;
	}
	public String getFulamt_ownpay_amt() {
		return fulamt_ownpay_amt;
	}
	public void setFulamt_ownpay_amt(String fulamt_ownpay_amt) {
		this.fulamt_ownpay_amt = fulamt_ownpay_amt;
	}
	public String getOverlmt_amt() {
		return overlmt_amt;
	}
	public void setOverlmt_amt(String overlmt_amt) {
		this.overlmt_amt = overlmt_amt;
	}
	public String getPreselfpay_amt() {
		return preselfpay_amt;
	}
	public void setPreselfpay_amt(String preselfpay_amt) {
		this.preselfpay_amt = preselfpay_amt;
	}
	public String getInscp_scp_amt() {
		return inscp_scp_amt;
	}
	public void setInscp_scp_amt(String inscp_scp_amt) {
		this.inscp_scp_amt = inscp_scp_amt;
	}
	public String getChrgitm_lv() {
		return chrgitm_lv;
	}
	public void setChrgitm_lv(String chrgitm_lv) {
		this.chrgitm_lv = chrgitm_lv;
	}
	public String getMed_chrgitm_type() {
		return med_chrgitm_type;
	}
	public void setMed_chrgitm_type(String med_chrgitm_type) {
		this.med_chrgitm_type = med_chrgitm_type;
	}
	public String getBas_medn_flag() {
		return bas_medn_flag;
	}
	public void setBas_medn_flag(String bas_medn_flag) {
		this.bas_medn_flag = bas_medn_flag;
	}
	public String getHi_nego_drug_flag() {
		return hi_nego_drug_flag;
	}
	public void setHi_nego_drug_flag(String hi_nego_drug_flag) {
		this.hi_nego_drug_flag = hi_nego_drug_flag;
	}
	public String getChld_medc_flag() {
		return chld_medc_flag;
	}
	public void setChld_medc_flag(String chld_medc_flag) {
		this.chld_medc_flag = chld_medc_flag;
	}
	public String getList_sp_item_flag() {
		return list_sp_item_flag;
	}
	public void setList_sp_item_flag(String list_sp_item_flag) {
		this.list_sp_item_flag = list_sp_item_flag;
	}
	public String getLmt_used_flag() {
		return lmt_used_flag;
	}
	public void setLmt_used_flag(String lmt_used_flag) {
		this.lmt_used_flag = lmt_used_flag;
	}
	public String getDrt_reim_flag() {
		return drt_reim_flag;
	}
	public void setDrt_reim_flag(String drt_reim_flag) {
		this.drt_reim_flag = drt_reim_flag;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	
}
