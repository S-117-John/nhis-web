package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

/**
 * 医疗保障基金结算清单信息上传入参——结算清单信息
 * @author Administrator
 *
 */
public class Input4101Setlinfo {
	
	private String	mdtrt_id;//	Y	就诊ID	
	private String	setl_id;//	Y	结算ID	
	private String	fixmedins_name;//	Y	定点医药机构名称	
	private String	fixmedins_code;//	Y	定点医药机构编号	
	private String	hi_setl_lv;//		医保结算等级	
	private String	hi_no;//		医保编号	参保人在医保系统中的唯一身份代码
	private String	medcasno;//	Y	病案号	
	private String	dcla_time;//		申报时间	结算清单上报时间
	private String	psn_name;//	Y	人员姓名	
	private String	gend;//	Y	性别	
	private String	brdy;//	Y　	出生日期	
	private String	age;//		年龄	大于1岁（含1岁）时必填，单位岁
	private String	ntly;//	Y　	国籍	
	private String	nwb_age;//		（年龄不足1周岁）年龄	小于1岁时必填，单位天
	private String	naty;//	Y　	民族	
	private String	patn_cert_type;//	Y	患者证件类别	
	private String	certno;//	Y	证件号码	患者证件号码
	private String	prfs;//	Y　	职业	
	private String	curr_addr;//		现住址	
	private String	emp_name;//		单位名称	
	private String	emp_addr;//		单位地址	
	private String	emp_tel;//		单位电话	
	private String	poscode;//		邮编	
	private String	coner_name;//	Y　	联系人姓名	
	private String	patn_rlts;//	Y　	与患者关系	
	private String	coner_addr;//	Y　	联系人地址	
	private String	coner_tel;//	Y　	联系人电话	
	private String	hi_type;//	Y　	医保类型	
	private String	insuplc;//	Y　	参保地	
	private String	sp_psn_type;//		特殊人员类型	
	private String	nwb_adm_type;//		新生儿入院类型	
	private String	nwb_bir_wt;//		新生儿出生体重	精确到10克(g)
	private String	nwb_adm_wt;//		新生儿入院体重	精确到10克(g)
	private String	opsp_diag_caty;//		门诊慢特病诊断科别	
	private String	opsp_mdtrt_date;//		门诊慢特病就诊日期	
	private String	ipt_med_type;//	Y　	住院医疗类型	
	private String	adm_way;//		入院途径	
	private String	trt_type;//		治疗类别	
	private String	adm_time;//		入院时间	
	private String	adm_caty;//	Y　	入院科别	参照科室代码（dept）
	private String	refldept_dept;//		转科科别	参照科室代码（dept），如果超过一次以上的转科，用“→”转接表示
	private String	dscg_time;//		出院时间	
	private String	dscg_caty;//	Y　	出院科别	参照科室代码（dept）
	private String	act_ipt_days;//		实际住院天数	
	private String	otp_wm_dise;//		门（急）诊西医诊断	
	private String	wm_dise_code;//		西医诊断疾病代码	
	private String	otp_tcm_dise;//		门（急）诊中医诊断	
	private String	tcm_dise_code;//		中医诊断代码	
	private String	diag_code_cnt;//		诊断代码计数	
	private String	oprn_oprt_code_cnt;//		手术操作代码计数	
	private String	vent_used_dura;//		呼吸机使用时长	格式：天数/小时数/分钟数例：1/13/24
	private String	pwcry_bfadm_coma_dura;//		颅脑损伤患者入院前昏迷时长	格式：天数/小时数/分钟数例：1/13/24
	private String	pwcry_afadm_coma_dura;//		颅脑损伤患者入院后昏迷时长	格式：天数/小时数/分钟数例：1/13/24
	private String	bld_cat;//		输血品种	
	private String	bld_amt;//		输血量	
	private String	bld_unt;//		输血计量单位	
	private String	spga_nurscare_days;//		特级护理天数	
	private String	lv1_nurscare_days;//		一级护理天数	
	private String	scd_nurscare_days;//		二级护理天数	
	private String	lv3_nurscare_days;//		三级护理天数	
	private String	dscg_way;//		离院方式	当离院方式为“2”时，如果接收患者的医疗机构明确，需要填写转入医疗机构的名称；当离院方式为“3”时，如果接收患者的社区卫生服务机构明确，需要填写社区卫生服务机构/乡镇卫生院名称当离院方式为“2”或“3”时，如果接收患者的医疗机构或社区卫生服务机构明确，需要填写机构对应的医保定点医疗机构代码
	private String	acp_medins_name;//		拟接收机构名称	
	private String	acp_optins_code;//		拟接收机构代码	
	private String	bill_code;//	Y	票据代码	
	private String	bill_no;//	Y	票据号码	
	private String	biz_sn;//	Y	业务流水号	业务流水号
	private String	days_rinp_flag_31;//		出院31天内再住院计划标志	
	private String	days_rinp_pup_31;//		出院31天内再住院目的	
	private String	chfpdr_name;//		主诊医师姓名	
	private String	chfpdr_code;//		主诊医师代码	主诊医师在《医保医师代码》中的代码，在就医地未完成标准化前，可传医师在就医地系统中的唯一编号
	private String	setl_begn_date;//	Y　	结算开始日期	
	private String	setl_end_date;//	Y　	结算结束日期	
	private String	psn_selfpay;//	Y　	个人自付	
	private String	psn_ownpay;//	Y　	个人自费	
	private String	acct_pay;//	Y　	个人账户支出	
	private String	psn_cashpay;//	Y　	个人现金支付	
	private String	hi_paymtd;//	Y　	医保支付方式	
	private String	hsorg;//	Y	医保机构	
	private String	hsorg_opter;//	Y	医保机构经办人	
	private String	medins_fill_dept;//	Y　	医疗机构填报部门	
	private String	medins_fill_psn;//	Y　	医疗机构填报人	
	public String getMdtrt_id() {
		return mdtrt_id;
	}
	public void setMdtrt_id(String mdtrt_id) {
		this.mdtrt_id = mdtrt_id;
	}
	public String getSetl_id() {
		return setl_id;
	}
	public void setSetl_id(String setl_id) {
		this.setl_id = setl_id;
	}
	public String getFixmedins_name() {
		return fixmedins_name;
	}
	public void setFixmedins_name(String fixmedins_name) {
		this.fixmedins_name = fixmedins_name;
	}
	public String getFixmedins_code() {
		return fixmedins_code;
	}
	public void setFixmedins_code(String fixmedins_code) {
		this.fixmedins_code = fixmedins_code;
	}
	public String getHi_setl_lv() {
		return hi_setl_lv;
	}
	public void setHi_setl_lv(String hi_setl_lv) {
		this.hi_setl_lv = hi_setl_lv;
	}
	public String getHi_no() {
		return hi_no;
	}
	public void setHi_no(String hi_no) {
		this.hi_no = hi_no;
	}
	public String getMedcasno() {
		return medcasno;
	}
	public void setMedcasno(String medcasno) {
		this.medcasno = medcasno;
	}
	public String getDcla_time() {
		return dcla_time;
	}
	public void setDcla_time(String dcla_time) {
		this.dcla_time = dcla_time;
	}
	public String getPsn_name() {
		return psn_name;
	}
	public void setPsn_name(String psn_name) {
		this.psn_name = psn_name;
	}
	public String getGend() {
		return gend;
	}
	public void setGend(String gend) {
		this.gend = gend;
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
	public String getNtly() {
		return ntly;
	}
	public void setNtly(String ntly) {
		this.ntly = ntly;
	}
	public String getNwb_age() {
		return nwb_age;
	}
	public void setNwb_age(String nwb_age) {
		this.nwb_age = nwb_age;
	}
	public String getNaty() {
		return naty;
	}
	public void setNaty(String naty) {
		this.naty = naty;
	}
	public String getPatn_cert_type() {
		return patn_cert_type;
	}
	public void setPatn_cert_type(String patn_cert_type) {
		this.patn_cert_type = patn_cert_type;
	}
	public String getCertno() {
		return certno;
	}
	public void setCertno(String certno) {
		this.certno = certno;
	}
	public String getPrfs() {
		return prfs;
	}
	public void setPrfs(String prfs) {
		this.prfs = prfs;
	}
	public String getCurr_addr() {
		return curr_addr;
	}
	public void setCurr_addr(String curr_addr) {
		this.curr_addr = curr_addr;
	}
	public String getEmp_name() {
		return emp_name;
	}
	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
	}
	public String getEmp_addr() {
		return emp_addr;
	}
	public void setEmp_addr(String emp_addr) {
		this.emp_addr = emp_addr;
	}
	public String getEmp_tel() {
		return emp_tel;
	}
	public void setEmp_tel(String emp_tel) {
		this.emp_tel = emp_tel;
	}
	public String getPoscode() {
		return poscode;
	}
	public void setPoscode(String poscode) {
		this.poscode = poscode;
	}
	public String getConer_name() {
		return coner_name;
	}
	public void setConer_name(String coner_name) {
		this.coner_name = coner_name;
	}
	public String getPatn_rlts() {
		return patn_rlts;
	}
	public void setPatn_rlts(String patn_rlts) {
		this.patn_rlts = patn_rlts;
	}
	public String getConer_addr() {
		return coner_addr;
	}
	public void setConer_addr(String coner_addr) {
		this.coner_addr = coner_addr;
	}
	public String getConer_tel() {
		return coner_tel;
	}
	public void setConer_tel(String coner_tel) {
		this.coner_tel = coner_tel;
	}
	public String getHi_type() {
		return hi_type;
	}
	public void setHi_type(String hi_type) {
		this.hi_type = hi_type;
	}
	public String getInsuplc() {
		return insuplc;
	}
	public void setInsuplc(String insuplc) {
		this.insuplc = insuplc;
	}
	public String getSp_psn_type() {
		return sp_psn_type;
	}
	public void setSp_psn_type(String sp_psn_type) {
		this.sp_psn_type = sp_psn_type;
	}
	public String getNwb_adm_type() {
		return nwb_adm_type;
	}
	public void setNwb_adm_type(String nwb_adm_type) {
		this.nwb_adm_type = nwb_adm_type;
	}
	public String getNwb_bir_wt() {
		return nwb_bir_wt;
	}
	public void setNwb_bir_wt(String nwb_bir_wt) {
		this.nwb_bir_wt = nwb_bir_wt;
	}
	public String getNwb_adm_wt() {
		return nwb_adm_wt;
	}
	public void setNwb_adm_wt(String nwb_adm_wt) {
		this.nwb_adm_wt = nwb_adm_wt;
	}
	public String getOpsp_diag_caty() {
		return opsp_diag_caty;
	}
	public void setOpsp_diag_caty(String opsp_diag_caty) {
		this.opsp_diag_caty = opsp_diag_caty;
	}
	public String getOpsp_mdtrt_date() {
		return opsp_mdtrt_date;
	}
	public void setOpsp_mdtrt_date(String opsp_mdtrt_date) {
		this.opsp_mdtrt_date = opsp_mdtrt_date;
	}
	public String getIpt_med_type() {
		return ipt_med_type;
	}
	public void setIpt_med_type(String ipt_med_type) {
		this.ipt_med_type = ipt_med_type;
	}
	public String getAdm_way() {
		return adm_way;
	}
	public void setAdm_way(String adm_way) {
		this.adm_way = adm_way;
	}
	public String getTrt_type() {
		return trt_type;
	}
	public void setTrt_type(String trt_type) {
		this.trt_type = trt_type;
	}
	public String getAdm_time() {
		return adm_time;
	}
	public void setAdm_time(String adm_time) {
		this.adm_time = adm_time;
	}
	public String getAdm_caty() {
		return adm_caty;
	}
	public void setAdm_caty(String adm_caty) {
		this.adm_caty = adm_caty;
	}
	public String getRefldept_dept() {
		return refldept_dept;
	}
	public void setRefldept_dept(String refldept_dept) {
		this.refldept_dept = refldept_dept;
	}
	public String getDscg_time() {
		return dscg_time;
	}
	public void setDscg_time(String dscg_time) {
		this.dscg_time = dscg_time;
	}
	public String getDscg_caty() {
		return dscg_caty;
	}
	public void setDscg_caty(String dscg_caty) {
		this.dscg_caty = dscg_caty;
	}
	public String getAct_ipt_days() {
		return act_ipt_days;
	}
	public void setAct_ipt_days(String act_ipt_days) {
		this.act_ipt_days = act_ipt_days;
	}
	public String getOtp_wm_dise() {
		return otp_wm_dise;
	}
	public void setOtp_wm_dise(String otp_wm_dise) {
		this.otp_wm_dise = otp_wm_dise;
	}
	public String getWm_dise_code() {
		return wm_dise_code;
	}
	public void setWm_dise_code(String wm_dise_code) {
		this.wm_dise_code = wm_dise_code;
	}
	public String getOtp_tcm_dise() {
		return otp_tcm_dise;
	}
	public void setOtp_tcm_dise(String otp_tcm_dise) {
		this.otp_tcm_dise = otp_tcm_dise;
	}
	public String getTcm_dise_code() {
		return tcm_dise_code;
	}
	public void setTcm_dise_code(String tcm_dise_code) {
		this.tcm_dise_code = tcm_dise_code;
	}
	public String getDiag_code_cnt() {
		return diag_code_cnt;
	}
	public void setDiag_code_cnt(String diag_code_cnt) {
		this.diag_code_cnt = diag_code_cnt;
	}
	public String getOprn_oprt_code_cnt() {
		return oprn_oprt_code_cnt;
	}
	public void setOprn_oprt_code_cnt(String oprn_oprt_code_cnt) {
		this.oprn_oprt_code_cnt = oprn_oprt_code_cnt;
	}
	public String getVent_used_dura() {
		return vent_used_dura;
	}
	public void setVent_used_dura(String vent_used_dura) {
		this.vent_used_dura = vent_used_dura;
	}
	public String getPwcry_bfadm_coma_dura() {
		return pwcry_bfadm_coma_dura;
	}
	public void setPwcry_bfadm_coma_dura(String pwcry_bfadm_coma_dura) {
		this.pwcry_bfadm_coma_dura = pwcry_bfadm_coma_dura;
	}
	public String getPwcry_afadm_coma_dura() {
		return pwcry_afadm_coma_dura;
	}
	public void setPwcry_afadm_coma_dura(String pwcry_afadm_coma_dura) {
		this.pwcry_afadm_coma_dura = pwcry_afadm_coma_dura;
	}
	public String getBld_cat() {
		return bld_cat;
	}
	public void setBld_cat(String bld_cat) {
		this.bld_cat = bld_cat;
	}
	public String getBld_amt() {
		return bld_amt;
	}
	public void setBld_amt(String bld_amt) {
		this.bld_amt = bld_amt;
	}
	public String getBld_unt() {
		return bld_unt;
	}
	public void setBld_unt(String bld_unt) {
		this.bld_unt = bld_unt;
	}
	public String getSpga_nurscare_days() {
		return spga_nurscare_days;
	}
	public void setSpga_nurscare_days(String spga_nurscare_days) {
		this.spga_nurscare_days = spga_nurscare_days;
	}
	public String getLv1_nurscare_days() {
		return lv1_nurscare_days;
	}
	public void setLv1_nurscare_days(String lv1_nurscare_days) {
		this.lv1_nurscare_days = lv1_nurscare_days;
	}
	public String getScd_nurscare_days() {
		return scd_nurscare_days;
	}
	public void setScd_nurscare_days(String scd_nurscare_days) {
		this.scd_nurscare_days = scd_nurscare_days;
	}
	public String getLv3_nurscare_days() {
		return lv3_nurscare_days;
	}
	public void setLv3_nurscare_days(String lv3_nurscare_days) {
		this.lv3_nurscare_days = lv3_nurscare_days;
	}
	public String getDscg_way() {
		return dscg_way;
	}
	public void setDscg_way(String dscg_way) {
		this.dscg_way = dscg_way;
	}
	public String getAcp_medins_name() {
		return acp_medins_name;
	}
	public void setAcp_medins_name(String acp_medins_name) {
		this.acp_medins_name = acp_medins_name;
	}
	public String getAcp_optins_code() {
		return acp_optins_code;
	}
	public void setAcp_optins_code(String acp_optins_code) {
		this.acp_optins_code = acp_optins_code;
	}
	public String getBill_code() {
		return bill_code;
	}
	public void setBill_code(String bill_code) {
		this.bill_code = bill_code;
	}
	public String getBill_no() {
		return bill_no;
	}
	public void setBill_no(String bill_no) {
		this.bill_no = bill_no;
	}
	public String getBiz_sn() {
		return biz_sn;
	}
	public void setBiz_sn(String biz_sn) {
		this.biz_sn = biz_sn;
	}
	public String getDays_rinp_flag_31() {
		return days_rinp_flag_31;
	}
	public void setDays_rinp_flag_31(String days_rinp_flag_31) {
		this.days_rinp_flag_31 = days_rinp_flag_31;
	}
	public String getDays_rinp_pup_31() {
		return days_rinp_pup_31;
	}
	public void setDays_rinp_pup_31(String days_rinp_pup_31) {
		this.days_rinp_pup_31 = days_rinp_pup_31;
	}
	public String getChfpdr_name() {
		return chfpdr_name;
	}
	public void setChfpdr_name(String chfpdr_name) {
		this.chfpdr_name = chfpdr_name;
	}
	public String getChfpdr_code() {
		return chfpdr_code;
	}
	public void setChfpdr_code(String chfpdr_code) {
		this.chfpdr_code = chfpdr_code;
	}
	public String getSetl_begn_date() {
		return setl_begn_date;
	}
	public void setSetl_begn_date(String setl_begn_date) {
		this.setl_begn_date = setl_begn_date;
	}
	public String getSetl_end_date() {
		return setl_end_date;
	}
	public void setSetl_end_date(String setl_end_date) {
		this.setl_end_date = setl_end_date;
	}
	public String getPsn_selfpay() {
		return psn_selfpay;
	}
	public void setPsn_selfpay(String psn_selfpay) {
		this.psn_selfpay = psn_selfpay;
	}
	public String getPsn_ownpay() {
		return psn_ownpay;
	}
	public void setPsn_ownpay(String psn_ownpay) {
		this.psn_ownpay = psn_ownpay;
	}
	public String getAcct_pay() {
		return acct_pay;
	}
	public void setAcct_pay(String acct_pay) {
		this.acct_pay = acct_pay;
	}
	public String getPsn_cashpay() {
		return psn_cashpay;
	}
	public void setPsn_cashpay(String psn_cashpay) {
		this.psn_cashpay = psn_cashpay;
	}
	public String getHi_paymtd() {
		return hi_paymtd;
	}
	public void setHi_paymtd(String hi_paymtd) {
		this.hi_paymtd = hi_paymtd;
	}
	public String getHsorg() {
		return hsorg;
	}
	public void setHsorg(String hsorg) {
		this.hsorg = hsorg;
	}
	public String getHsorg_opter() {
		return hsorg_opter;
	}
	public void setHsorg_opter(String hsorg_opter) {
		this.hsorg_opter = hsorg_opter;
	}
	public String getMedins_fill_dept() {
		return medins_fill_dept;
	}
	public void setMedins_fill_dept(String medins_fill_dept) {
		this.medins_fill_dept = medins_fill_dept;
	}
	public String getMedins_fill_psn() {
		return medins_fill_psn;
	}
	public void setMedins_fill_psn(String medins_fill_psn) {
		this.medins_fill_psn = medins_fill_psn;
	}
}
