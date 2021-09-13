package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

/**
 * 5.2.4.1【2301】住院费用明细上传 （节点标识：feedetail）
 * @author Administrator
 *
 */
public class Input2301Feedetail {
	
	private String	feedetl_sn;//	Y　	费用明细流水号	单次就诊内唯一
	private String	init_feedetl_sn;//		原费用流水号	退单时传入被退单的费用明细流水号
	private String	mdtrt_id;//	Y　	就诊ID	
	private String	drord_no;//		医嘱号	
	private String	psn_no;//	Y　	人员编号	
	private String	med_type;//	Y　	医疗类别	
	private String	fee_ocur_time;//	Y	费用发生时间	yyyy-MM-dd HH:mm:ss
	private String	med_list_codg;//	Y　	医疗目录编码	
	private String	medins_list_codg;//	Y　	医药机构目录编码	
	private String	det_item_fee_sumamt;//	Y　	明细项目费用总额	
	private String	cnt;//	Y	数量	退单时数量填写负数
	private String	pric;//	Y　	单价	
	private String	bilg_dept_codg;//	Y　	开单科室编码	
	private String	bilg_dept_name;//	Y　	开单科室名称	
	private String	bilg_dr_codg;//	Y　	开单医生编码	
	private String	bilg_dr_name;//	Y　	开单医师姓名	
	private String	acord_dept_codg;//		受单科室编码	
	private String	acord_dept_name;//		受单科室名称	
	private String	orders_dr_code;//		受单医生编码	
	private String	orders_dr_name;//		受单医生姓名	
	private String	hosp_appr_flag;//		医院审批标志	
	private String	tcmdrug_used_way;//		中药使用方式	
	private String	etip_flag;//		外检标志	
	private String	etip_hosp_code;//		外检医院编码	
	private String	dscg_tkdrug_flag;//		出院带药标志	
	private String	matn_fee_flag;//		生育费用标志	
	private String	memo;//		备注	
	public String getFeedetl_sn() {
		return feedetl_sn;
	}
	public void setFeedetl_sn(String feedetl_sn) {
		this.feedetl_sn = feedetl_sn;
	}
	public String getInit_feedetl_sn() {
		return init_feedetl_sn;
	}
	public void setInit_feedetl_sn(String init_feedetl_sn) {
		this.init_feedetl_sn = init_feedetl_sn;
	}
	public String getMdtrt_id() {
		return mdtrt_id;
	}
	public void setMdtrt_id(String mdtrt_id) {
		this.mdtrt_id = mdtrt_id;
	}
	public String getDrord_no() {
		return drord_no;
	}
	public void setDrord_no(String drord_no) {
		this.drord_no = drord_no;
	}
	public String getPsn_no() {
		return psn_no;
	}
	public void setPsn_no(String psn_no) {
		this.psn_no = psn_no;
	}
	public String getMed_type() {
		return med_type;
	}
	public void setMed_type(String med_type) {
		this.med_type = med_type;
	}
	public String getFee_ocur_time() {
		return fee_ocur_time;
	}
	public void setFee_ocur_time(String fee_ocur_time) {
		this.fee_ocur_time = fee_ocur_time;
	}
	public String getMed_list_codg() {
		return med_list_codg;
	}
	public void setMed_list_codg(String med_list_codg) {
		this.med_list_codg = med_list_codg;
	}
	public String getMedins_list_codg() {
		return medins_list_codg;
	}
	public void setMedins_list_codg(String medins_list_codg) {
		this.medins_list_codg = medins_list_codg;
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
	public String getBilg_dept_codg() {
		return bilg_dept_codg;
	}
	public void setBilg_dept_codg(String bilg_dept_codg) {
		this.bilg_dept_codg = bilg_dept_codg;
	}
	public String getBilg_dept_name() {
		return bilg_dept_name;
	}
	public void setBilg_dept_name(String bilg_dept_name) {
		this.bilg_dept_name = bilg_dept_name;
	}
	public String getBilg_dr_codg() {
		return bilg_dr_codg;
	}
	public void setBilg_dr_codg(String bilg_dr_codg) {
		this.bilg_dr_codg = bilg_dr_codg;
	}
	public String getBilg_dr_name() {
		return bilg_dr_name;
	}
	public void setBilg_dr_name(String bilg_dr_name) {
		this.bilg_dr_name = bilg_dr_name;
	}
	public String getAcord_dept_codg() {
		return acord_dept_codg;
	}
	public void setAcord_dept_codg(String acord_dept_codg) {
		this.acord_dept_codg = acord_dept_codg;
	}
	public String getAcord_dept_name() {
		return acord_dept_name;
	}
	public void setAcord_dept_name(String acord_dept_name) {
		this.acord_dept_name = acord_dept_name;
	}
	public String getOrders_dr_code() {
		return orders_dr_code;
	}
	public void setOrders_dr_code(String orders_dr_code) {
		this.orders_dr_code = orders_dr_code;
	}
	public String getOrders_dr_name() {
		return orders_dr_name;
	}
	public void setOrders_dr_name(String orders_dr_name) {
		this.orders_dr_name = orders_dr_name;
	}
	public String getHosp_appr_flag() {
		return hosp_appr_flag;
	}
	public void setHosp_appr_flag(String hosp_appr_flag) {
		this.hosp_appr_flag = hosp_appr_flag;
	}
	public String getTcmdrug_used_way() {
		return tcmdrug_used_way;
	}
	public void setTcmdrug_used_way(String tcmdrug_used_way) {
		this.tcmdrug_used_way = tcmdrug_used_way;
	}
	public String getEtip_flag() {
		return etip_flag;
	}
	public void setEtip_flag(String etip_flag) {
		this.etip_flag = etip_flag;
	}
	public String getEtip_hosp_code() {
		return etip_hosp_code;
	}
	public void setEtip_hosp_code(String etip_hosp_code) {
		this.etip_hosp_code = etip_hosp_code;
	}
	public String getDscg_tkdrug_flag() {
		return dscg_tkdrug_flag;
	}
	public void setDscg_tkdrug_flag(String dscg_tkdrug_flag) {
		this.dscg_tkdrug_flag = dscg_tkdrug_flag;
	}
	public String getMatn_fee_flag() {
		return matn_fee_flag;
	}
	public void setMatn_fee_flag(String matn_fee_flag) {
		this.matn_fee_flag = matn_fee_flag;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	
}
