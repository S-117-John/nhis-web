package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

public class Output4160Data {
	
	private String	diag_code;//	主要诊断编码	字符型	40			
	private String	diag_name;//	主要诊断名称	字符型	255			
	private String	poolarea_no;//	统筹区	字符型	10		Y	
	private String	dise_sco;//	对照病种分值	数值型	10,2			
	private String	act_dise_sco;//	实际分组病种分值	数值型	10,2			
	private String	act_setl_sco;//	实际结算分值	数值型	10,2			
	private String	trt_way;//	治疗方式	字符型	10	Y		
	private String	trt_way_name;//	治疗方式名称	字符型	10			
	private String	fixmedins_code;//	定点医药机构编号	字符型	30		Y	
	private String	fixmedins_name;//	定点医药机构名称	字符型	200			
	private String	psn_no;//	人员编号	字符型	32			
	private String	psn_name;//	人员姓名	字符型	10			
	private String	insu_admdvs;//	参保所属医保区划	字符型	100			
	private String	insutype;//	险种类型	字符型	6	Y		
	private String	act_ipt_days;//	住院天数	数值型	10,0			
	private String	fund_payamt;//	统筹基金支付金额	数值型	16,2			
	private String	psn_selfpay_amt;//	个人自付金额	数值型	16,2		Y	
	private String	total_payamt;//	住院总费用	数值型	16,2			
	private String	inscp_amt;//	符合范围金额	数值型	16,2			
	private String	fund_pay_sumamt;//	基金支付总额	数值型	16,2			
	private String	adm_time;//	入院时间	日期时间型				yyyy-MM-dd HH:mm:ss
	private String	dscg_time;//	出院时间	日期时间型				yyyy-MM-dd HH:mm:ss
	private String	setl_end_date;//	结算结束时间	日期时间型				yyyy-MM-dd HH:mm:ss
	private String	norm_flag;//	数据标识	字符型	6	Y		
	private String	payb_setl_amt;//	应付结算金额	数值型	16,2			
	private String	act_setl_amt;//	实际结算金额	数值型	16,2			
	private String	diag_grp;//	诊断亚目分组	字符型	10			
	private String	remark;//	备注	字符型	100			
	public String getDiag_code() {
		return diag_code;
	}
	public void setDiag_code(String diag_code) {
		this.diag_code = diag_code;
	}
	public String getDiag_name() {
		return diag_name;
	}
	public void setDiag_name(String diag_name) {
		this.diag_name = diag_name;
	}
	public String getPoolarea_no() {
		return poolarea_no;
	}
	public void setPoolarea_no(String poolarea_no) {
		this.poolarea_no = poolarea_no;
	}
	public String getDise_sco() {
		return dise_sco;
	}
	public void setDise_sco(String dise_sco) {
		this.dise_sco = dise_sco;
	}
	public String getAct_dise_sco() {
		return act_dise_sco;
	}
	public void setAct_dise_sco(String act_dise_sco) {
		this.act_dise_sco = act_dise_sco;
	}
	public String getAct_setl_sco() {
		return act_setl_sco;
	}
	public void setAct_setl_sco(String act_setl_sco) {
		this.act_setl_sco = act_setl_sco;
	}
	public String getTrt_way() {
		return trt_way;
	}
	public void setTrt_way(String trt_way) {
		this.trt_way = trt_way;
	}
	public String getTrt_way_name() {
		return trt_way_name;
	}
	public void setTrt_way_name(String trt_way_name) {
		this.trt_way_name = trt_way_name;
	}
	public String getFixmedins_code() {
		return fixmedins_code;
	}
	public void setFixmedins_code(String fixmedins_code) {
		this.fixmedins_code = fixmedins_code;
	}
	public String getFixmedins_name() {
		return fixmedins_name;
	}
	public void setFixmedins_name(String fixmedins_name) {
		this.fixmedins_name = fixmedins_name;
	}
	public String getPsn_no() {
		return psn_no;
	}
	public void setPsn_no(String psn_no) {
		this.psn_no = psn_no;
	}
	public String getPsn_name() {
		return psn_name;
	}
	public void setPsn_name(String psn_name) {
		this.psn_name = psn_name;
	}
	public String getInsu_admdvs() {
		return insu_admdvs;
	}
	public void setInsu_admdvs(String insu_admdvs) {
		this.insu_admdvs = insu_admdvs;
	}
	public String getInsutype() {
		return insutype;
	}
	public void setInsutype(String insutype) {
		this.insutype = insutype;
	}
	public String getAct_ipt_days() {
		return act_ipt_days;
	}
	public void setAct_ipt_days(String act_ipt_days) {
		this.act_ipt_days = act_ipt_days;
	}
	public String getFund_payamt() {
		return fund_payamt;
	}
	public void setFund_payamt(String fund_payamt) {
		this.fund_payamt = fund_payamt;
	}
	public String getPsn_selfpay_amt() {
		return psn_selfpay_amt;
	}
	public void setPsn_selfpay_amt(String psn_selfpay_amt) {
		this.psn_selfpay_amt = psn_selfpay_amt;
	}
	public String getTotal_payamt() {
		return total_payamt;
	}
	public void setTotal_payamt(String total_payamt) {
		this.total_payamt = total_payamt;
	}
	public String getInscp_amt() {
		return inscp_amt;
	}
	public void setInscp_amt(String inscp_amt) {
		this.inscp_amt = inscp_amt;
	}
	public String getFund_pay_sumamt() {
		return fund_pay_sumamt;
	}
	public void setFund_pay_sumamt(String fund_pay_sumamt) {
		this.fund_pay_sumamt = fund_pay_sumamt;
	}
	public String getAdm_time() {
		return adm_time;
	}
	public void setAdm_time(String adm_time) {
		this.adm_time = adm_time;
	}
	public String getDscg_time() {
		return dscg_time;
	}
	public void setDscg_time(String dscg_time) {
		this.dscg_time = dscg_time;
	}
	public String getSetl_end_date() {
		return setl_end_date;
	}
	public void setSetl_end_date(String setl_end_date) {
		this.setl_end_date = setl_end_date;
	}
	public String getNorm_flag() {
		return norm_flag;
	}
	public void setNorm_flag(String norm_flag) {
		this.norm_flag = norm_flag;
	}
	public String getPayb_setl_amt() {
		return payb_setl_amt;
	}
	public void setPayb_setl_amt(String payb_setl_amt) {
		this.payb_setl_amt = payb_setl_amt;
	}
	public String getAct_setl_amt() {
		return act_setl_amt;
	}
	public void setAct_setl_amt(String act_setl_amt) {
		this.act_setl_amt = act_setl_amt;
	}
	public String getDiag_grp() {
		return diag_grp;
	}
	public void setDiag_grp(String diag_grp) {
		this.diag_grp = diag_grp;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
