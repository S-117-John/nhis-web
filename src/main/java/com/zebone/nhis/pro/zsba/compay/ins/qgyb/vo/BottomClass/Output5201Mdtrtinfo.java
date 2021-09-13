package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

public class Output5201Mdtrtinfo {
	
	private String	mdtrt_id;//	Y	就诊ID	
	private String	psn_no;//	Y	人员编号	
	private String	psn_cert_type;//	Y	人员证件类型	
	private String	certno;//	Y	证件号码	
	private String	psn_name;//	Y	人员姓名	
	private String	gend;//	Y	性别	
	private String	naty;//		民族	
	private String	brdy;//		出生日期	yyyy-MM-dd
	private String	age;//		年龄	
	private String	coner_name;//		联系人姓名	
	private String	tel;//		联系电话	
	private String	insutype;//	Y	险种类型	
	private String	psn_type;//	Y	人员类别	
	private String	maf_psn_flag;//		医疗救助对象标志	
	private String	cvlserv_flag;//	Y	公务员标志	
	private String	flxempe_flag;//		灵活就业标志	
	private String	nwb_flag;//		新生儿标志	
	private String	insu_optins;//		参保机构医保区划	
	private String	emp_name;//		单位名称	
	private String	begntime;//	Y	开始时间	yyyy-MM-dd HH:mm:ss
	private String	endtime;//	Y	结束时间	yyyy-MM-dd HH:mm:ss
	private String	mdtrt_cert_type;//		就诊凭证类型	
	private String	med_type;//	Y	医疗类别	
	private String	ars_year_ipt_flag;//		跨年度住院标志	
	private String	pre_pay_flag;//		先行支付标志	
	private String	ipt_otp_no;//		住院/门诊号	
	private String	medrcdno;//		病历号	
	private String	atddr_no;//		主治医生编码	
	private String	chfpdr_name;//		主诊医师姓名	
	private String	adm_dept_codg;//		入院科室编码	
	private String	adm_dept_name;//		入院科室名称	
	private String	adm_bed;//		入院床位	
	private String	dscg_maindiag_code;//		住院主诊断代码	
	private String	dscg_maindiag_name;//		住院主诊断名称	
	private String	dscg_dept_codg;//		出院科室编码	
	private String	dscg_dept_name;//		出院科室名称	
	private String	dscg_bed;//		出院床位	
	private String	dscg_way;//		离院方式	
	private String	main_cond_dscr;//		主要病情描述	
	private String	dise_codg;//		病种编码	参照标准编码：按病种结算病种目录代码(bydise_setl_list_code)、门诊慢特病病种目录代码(opsp_dise_cod)、日间手术病种目录代码(daysrg_dise_list_code)
	private String	dise_name;//		病种名称	
	private String	oprn_oprt_code;//		手术操作代码	
	private String	oprn_oprt_name;//		手术操作名称	
	private String	otp_diag_info;//		门诊诊断信息	
	private String	inhosp_stas;//		在院状态	
	private String	die_date;//		死亡日期	yyyy-MM-dd
	private String	ipt_days;//		住院天数	
	private String	fpsc_no;//		计划生育服务证号	
	private String	matn_type;//		生育类别	
	private String	birctrl_type;//		计划生育手术类别	
	private String	latechb_flag;//		晚育标志	
	private String	geso_val;//		孕周数	
	private String	fetts;//		胎次	
	private String	fetus_cnt;//		胎儿数	
	private String	pret_flag;//		早产标志	
	private String	birctrl_matn_date;//		计划生育手术或生育日期	yyyy-MM-dd
	private String	cop_flag;//		伴有并发症标志	
	private String	opter_id;//		经办人ID	
	private String	opter_name;//		经办人姓名	
	private String	opt_time;//		经办时间	yyyy-MM-dd HH:mm:ss
	private String	memo;//		备注	
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
	public String getConer_name() {
		return coner_name;
	}
	public void setConer_name(String coner_name) {
		this.coner_name = coner_name;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
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
	public String getMaf_psn_flag() {
		return maf_psn_flag;
	}
	public void setMaf_psn_flag(String maf_psn_flag) {
		this.maf_psn_flag = maf_psn_flag;
	}
	public String getCvlserv_flag() {
		return cvlserv_flag;
	}
	public void setCvlserv_flag(String cvlserv_flag) {
		this.cvlserv_flag = cvlserv_flag;
	}
	public String getFlxempe_flag() {
		return flxempe_flag;
	}
	public void setFlxempe_flag(String flxempe_flag) {
		this.flxempe_flag = flxempe_flag;
	}
	public String getNwb_flag() {
		return nwb_flag;
	}
	public void setNwb_flag(String nwb_flag) {
		this.nwb_flag = nwb_flag;
	}
	public String getInsu_optins() {
		return insu_optins;
	}
	public void setInsu_optins(String insu_optins) {
		this.insu_optins = insu_optins;
	}
	public String getEmp_name() {
		return emp_name;
	}
	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
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
	public String getArs_year_ipt_flag() {
		return ars_year_ipt_flag;
	}
	public void setArs_year_ipt_flag(String ars_year_ipt_flag) {
		this.ars_year_ipt_flag = ars_year_ipt_flag;
	}
	public String getPre_pay_flag() {
		return pre_pay_flag;
	}
	public void setPre_pay_flag(String pre_pay_flag) {
		this.pre_pay_flag = pre_pay_flag;
	}
	public String getIpt_otp_no() {
		return ipt_otp_no;
	}
	public void setIpt_otp_no(String ipt_otp_no) {
		this.ipt_otp_no = ipt_otp_no;
	}
	public String getMedrcdno() {
		return medrcdno;
	}
	public void setMedrcdno(String medrcdno) {
		this.medrcdno = medrcdno;
	}
	public String getAtddr_no() {
		return atddr_no;
	}
	public void setAtddr_no(String atddr_no) {
		this.atddr_no = atddr_no;
	}
	public String getChfpdr_name() {
		return chfpdr_name;
	}
	public void setChfpdr_name(String chfpdr_name) {
		this.chfpdr_name = chfpdr_name;
	}
	public String getAdm_dept_codg() {
		return adm_dept_codg;
	}
	public void setAdm_dept_codg(String adm_dept_codg) {
		this.adm_dept_codg = adm_dept_codg;
	}
	public String getAdm_dept_name() {
		return adm_dept_name;
	}
	public void setAdm_dept_name(String adm_dept_name) {
		this.adm_dept_name = adm_dept_name;
	}
	public String getAdm_bed() {
		return adm_bed;
	}
	public void setAdm_bed(String adm_bed) {
		this.adm_bed = adm_bed;
	}
	public String getDscg_maindiag_code() {
		return dscg_maindiag_code;
	}
	public void setDscg_maindiag_code(String dscg_maindiag_code) {
		this.dscg_maindiag_code = dscg_maindiag_code;
	}
	public String getDscg_maindiag_name() {
		return dscg_maindiag_name;
	}
	public void setDscg_maindiag_name(String dscg_maindiag_name) {
		this.dscg_maindiag_name = dscg_maindiag_name;
	}
	public String getDscg_dept_codg() {
		return dscg_dept_codg;
	}
	public void setDscg_dept_codg(String dscg_dept_codg) {
		this.dscg_dept_codg = dscg_dept_codg;
	}
	public String getDscg_dept_name() {
		return dscg_dept_name;
	}
	public void setDscg_dept_name(String dscg_dept_name) {
		this.dscg_dept_name = dscg_dept_name;
	}
	public String getDscg_bed() {
		return dscg_bed;
	}
	public void setDscg_bed(String dscg_bed) {
		this.dscg_bed = dscg_bed;
	}
	public String getDscg_way() {
		return dscg_way;
	}
	public void setDscg_way(String dscg_way) {
		this.dscg_way = dscg_way;
	}
	public String getMain_cond_dscr() {
		return main_cond_dscr;
	}
	public void setMain_cond_dscr(String main_cond_dscr) {
		this.main_cond_dscr = main_cond_dscr;
	}
	public String getDise_codg() {
		return dise_codg;
	}
	public void setDise_codg(String dise_codg) {
		this.dise_codg = dise_codg;
	}
	public String getDise_name() {
		return dise_name;
	}
	public void setDise_name(String dise_name) {
		this.dise_name = dise_name;
	}
	public String getOprn_oprt_code() {
		return oprn_oprt_code;
	}
	public void setOprn_oprt_code(String oprn_oprt_code) {
		this.oprn_oprt_code = oprn_oprt_code;
	}
	public String getOprn_oprt_name() {
		return oprn_oprt_name;
	}
	public void setOprn_oprt_name(String oprn_oprt_name) {
		this.oprn_oprt_name = oprn_oprt_name;
	}
	public String getOtp_diag_info() {
		return otp_diag_info;
	}
	public void setOtp_diag_info(String otp_diag_info) {
		this.otp_diag_info = otp_diag_info;
	}
	public String getInhosp_stas() {
		return inhosp_stas;
	}
	public void setInhosp_stas(String inhosp_stas) {
		this.inhosp_stas = inhosp_stas;
	}
	public String getDie_date() {
		return die_date;
	}
	public void setDie_date(String die_date) {
		this.die_date = die_date;
	}
	public String getIpt_days() {
		return ipt_days;
	}
	public void setIpt_days(String ipt_days) {
		this.ipt_days = ipt_days;
	}
	public String getFpsc_no() {
		return fpsc_no;
	}
	public void setFpsc_no(String fpsc_no) {
		this.fpsc_no = fpsc_no;
	}
	public String getMatn_type() {
		return matn_type;
	}
	public void setMatn_type(String matn_type) {
		this.matn_type = matn_type;
	}
	public String getBirctrl_type() {
		return birctrl_type;
	}
	public void setBirctrl_type(String birctrl_type) {
		this.birctrl_type = birctrl_type;
	}
	public String getLatechb_flag() {
		return latechb_flag;
	}
	public void setLatechb_flag(String latechb_flag) {
		this.latechb_flag = latechb_flag;
	}
	public String getGeso_val() {
		return geso_val;
	}
	public void setGeso_val(String geso_val) {
		this.geso_val = geso_val;
	}
	public String getFetts() {
		return fetts;
	}
	public void setFetts(String fetts) {
		this.fetts = fetts;
	}
	public String getFetus_cnt() {
		return fetus_cnt;
	}
	public void setFetus_cnt(String fetus_cnt) {
		this.fetus_cnt = fetus_cnt;
	}
	public String getPret_flag() {
		return pret_flag;
	}
	public void setPret_flag(String pret_flag) {
		this.pret_flag = pret_flag;
	}
	public String getBirctrl_matn_date() {
		return birctrl_matn_date;
	}
	public void setBirctrl_matn_date(String birctrl_matn_date) {
		this.birctrl_matn_date = birctrl_matn_date;
	}
	public String getCop_flag() {
		return cop_flag;
	}
	public void setCop_flag(String cop_flag) {
		this.cop_flag = cop_flag;
	}
	public String getOpter_id() {
		return opter_id;
	}
	public void setOpter_id(String opter_id) {
		this.opter_id = opter_id;
	}
	public String getOpter_name() {
		return opter_name;
	}
	public void setOpter_name(String opter_name) {
		this.opter_name = opter_name;
	}
	public String getOpt_time() {
		return opt_time;
	}
	public void setOpt_time(String opt_time) {
		this.opt_time = opt_time;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
}
