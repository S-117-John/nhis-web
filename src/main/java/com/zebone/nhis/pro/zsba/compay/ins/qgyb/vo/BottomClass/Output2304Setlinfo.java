package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

public class Output2304Setlinfo {
	
	private String	mdtrt_id;//	Y	就诊ID	
	private String  setl_id; // Y	结算ID
	private String	psn_no;//	Y	人员编号	
	private String	psn_name;//	Y	人员姓名	
	private String	psn_cert_type;//	Y	人员证件类型	
	private String	certno;//	Y	证件号码	
	private String	gend;//		性别	
	private String	naty;//		民族	
	private String	brdy;//		出生日期	yyyy-MM-dd
	private String	age;//		年龄	
	private String	insutype;//		险种类型	
	private String	psn_type;//	Y	人员类别	
	private String	cvlserv_flag;//	Y	公务员标志	
	private String	setl_time;//		结算时间	yyyy-MM-dd HH:mm:ss
	private String	mdtrt_cert_type;//		就诊凭证类型	
	private String	med_type;//	Y	医疗类别	
	private String	medfee_sumamt;//	Y	医疗费总额	
	private String	fulamt_ownpay_amt;//	Y	全自费金额	
	private String	overlmt_selfpay;//	Y	超限价自费费用	
	private String	preselfpay_amt;//	Y	先行自付金额	
	private String	inscp_scp_amt;//	Y	符合政策范围金额	
	private String	act_pay_dedc;//		实际支付起付线	
	private String	hifp_pay;//	Y	基本医疗保险统筹基金支出	
	private String	pool_prop_selfpay;//	Y	基本医疗保险统筹基金支付比例	
	private String	cvlserv_pay;//	Y	公务员医疗补助资金支出	
	private String	hifes_pay;//	Y　	企业补充医疗保险基金支出	
	private String	hifmi_pay;//	Y　	居民大病保险资金支出	
	private String	hifob_pay;//	Y　	职工大额医疗费用补助基金支出	
	private String	maf_pay;//	Y　	医疗救助基金支出	
	private String	oth_pay;//	Y　	其他支出	
	private String	fund_pay_sumamt;//	Y	基金支付总额	
	private String	psn_part_amt;//		个人负担总金额	
	private String	acct_pay;//	Y	个人账户支出	
	private String	psn_cash_pay;//	Y	个人现金支出	
	private String	hosp_part_amt;//		医院负担金额	
	private String	balc;//	Y	余额	
	private String	acct_mulaid_pay;//	Y	个人账户共济支付金额	
	private String	medins_setl_id;//	Y	医药机构结算ID	存放发送方报文ID
	private String	clr_optins;//		清算经办机构	
	private String	clr_way;//		清算方式	
	private String	clr_type;//	Y　	清算类别	
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
	public String getPsn_name() {
		return psn_name;
	}
	public void setPsn_name(String psn_name) {
		this.psn_name = psn_name;
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
	public String getSetl_time() {
		return setl_time;
	}
	public void setSetl_time(String setl_time) {
		this.setl_time = setl_time;
	}
	public String getMdtrt_cert_type() {
		return mdtrt_cert_type;
	}
	public void setMdtrt_cert_type(String mdtrt_cert_type) {
		this.mdtrt_cert_type = mdtrt_cert_type;
	}
	public String getMed_type() {
		return med_type;
	}
	public void setMed_type(String med_type) {
		this.med_type = med_type;
	}
	public String getMedfee_sumamt() {
		return medfee_sumamt;
	}
	public void setMedfee_sumamt(String medfee_sumamt) {
		this.medfee_sumamt = medfee_sumamt;
	}
	public String getFulamt_ownpay_amt() {
		return fulamt_ownpay_amt;
	}
	public void setFulamt_ownpay_amt(String fulamt_ownpay_amt) {
		this.fulamt_ownpay_amt = fulamt_ownpay_amt;
	}
	public String getOverlmt_selfpay() {
		return overlmt_selfpay;
	}
	public void setOverlmt_selfpay(String overlmt_selfpay) {
		this.overlmt_selfpay = overlmt_selfpay;
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
	public String getAct_pay_dedc() {
		return act_pay_dedc;
	}
	public void setAct_pay_dedc(String act_pay_dedc) {
		this.act_pay_dedc = act_pay_dedc;
	}
	public String getHifp_pay() {
		return hifp_pay;
	}
	public void setHifp_pay(String hifp_pay) {
		this.hifp_pay = hifp_pay;
	}
	public String getPool_prop_selfpay() {
		return pool_prop_selfpay;
	}
	public void setPool_prop_selfpay(String pool_prop_selfpay) {
		this.pool_prop_selfpay = pool_prop_selfpay;
	}
	public String getCvlserv_pay() {
		return cvlserv_pay;
	}
	public void setCvlserv_pay(String cvlserv_pay) {
		this.cvlserv_pay = cvlserv_pay;
	}
	public String getHifes_pay() {
		return hifes_pay;
	}
	public void setHifes_pay(String hifes_pay) {
		this.hifes_pay = hifes_pay;
	}
	public String getHifmi_pay() {
		return hifmi_pay;
	}
	public void setHifmi_pay(String hifmi_pay) {
		this.hifmi_pay = hifmi_pay;
	}
	public String getHifob_pay() {
		return hifob_pay;
	}
	public void setHifob_pay(String hifob_pay) {
		this.hifob_pay = hifob_pay;
	}
	public String getMaf_pay() {
		return maf_pay;
	}
	public void setMaf_pay(String maf_pay) {
		this.maf_pay = maf_pay;
	}
	public String getOth_pay() {
		return oth_pay;
	}
	public void setOth_pay(String oth_pay) {
		this.oth_pay = oth_pay;
	}
	public String getFund_pay_sumamt() {
		return fund_pay_sumamt;
	}
	public void setFund_pay_sumamt(String fund_pay_sumamt) {
		this.fund_pay_sumamt = fund_pay_sumamt;
	}
	public String getPsn_part_amt() {
		return psn_part_amt;
	}
	public void setPsn_part_amt(String psn_part_amt) {
		this.psn_part_amt = psn_part_amt;
	}
	public String getAcct_pay() {
		return acct_pay;
	}
	public void setAcct_pay(String acct_pay) {
		this.acct_pay = acct_pay;
	}
	public String getPsn_cash_pay() {
		return psn_cash_pay;
	}
	public void setPsn_cash_pay(String psn_cash_pay) {
		this.psn_cash_pay = psn_cash_pay;
	}
	public String getHosp_part_amt() {
		return hosp_part_amt;
	}
	public void setHosp_part_amt(String hosp_part_amt) {
		this.hosp_part_amt = hosp_part_amt;
	}
	public String getBalc() {
		return balc;
	}
	public void setBalc(String balc) {
		this.balc = balc;
	}
	public String getAcct_mulaid_pay() {
		return acct_mulaid_pay;
	}
	public void setAcct_mulaid_pay(String acct_mulaid_pay) {
		this.acct_mulaid_pay = acct_mulaid_pay;
	}
	public String getMedins_setl_id() {
		return medins_setl_id;
	}
	public void setMedins_setl_id(String medins_setl_id) {
		this.medins_setl_id = medins_setl_id;
	}
	public String getClr_optins() {
		return clr_optins;
	}
	public void setClr_optins(String clr_optins) {
		this.clr_optins = clr_optins;
	}
	public String getClr_way() {
		return clr_way;
	}
	public void setClr_way(String clr_way) {
		this.clr_way = clr_way;
	}
	public String getClr_type() {
		return clr_type;
	}
	public void setClr_type(String clr_type) {
		this.clr_type = clr_type;
	}
	public String getSetl_id() {
		return setl_id;
	}
	public void setSetl_id(String setl_id) {
		this.setl_id = setl_id;
	}
	
	

}
