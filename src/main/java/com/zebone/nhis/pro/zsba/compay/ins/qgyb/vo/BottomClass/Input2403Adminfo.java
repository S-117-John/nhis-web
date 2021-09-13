package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

/**
 * 5.2.5.3住院信息变更2403入参 入院登记信息（节点标识：adminfo）
 * @author Administrator
 *
 */
public class Input2403Adminfo {
	
	private String	mdtrt_id;//	Y　	就诊ID
	private String	psn_no;//	Y　	人员编号	
	private String	coner_name;//		联系人姓名	
	private String	tel;//		联系电话	
	private String	begntime;//	Y　	开始时间	"入院时间 yyyy-MM-dd HH:mm:ss"
	private String	endtime;//	Y　	结束时间	"入院时间 yyyy-MM-dd HH:mm:ss"
	private String	mdtrt_cert_type;//	Y　	就诊凭证类型	
	private String	med_type;//	Y　	医疗类别	
	private String	ipt_otp_no;//	Y　	住院号	院内就诊流水号
	private String	medrcdno;//		病历号	
	private String	atddr_no;//	Y　	主治医生编码	
	private String	chfpdr_name;//	Y　	主诊医师姓名	
	private String	adm_diag_dscr;//	Y　	入院诊断描述	
	private String	adm_dept_codg;//	Y　	入院科室编码	
	private String	adm_dept_name;//	Y　	入院科室名称	
	private String	adm_bed;//	Y　	入院床位	
	private String	dscg_maindiag_code;//	Y　	住院主诊断代码	
	private String	dscg_maindiag_name;//	Y　	住院主诊断名称	
	private String	main_cond_dscr;//		主要病情描述	
	private String	dise_codg;//		病种编码	"按照标准编码填写：按病种结算病种目录代码(bydise_setl_list_code)	日间手术病种目录代码(daysrg_dise_list_code)"
	private String	dise_name;//		病种名称	
	private String	oprn_oprt_code;//		手术操作代码	日间手术病种时必填
	private String	oprn_oprt_name;//		手术操作名称
	private String	fpsc_no;//		计划生育服务证号	
	private String	matn_type;//		生育类别	
	private String	birctrl_type;//		计划生育手术类别	
	private String	latechb_flag;//		晚育标志	
	private String	geso_val;//		孕周数	
	private String	fetts;//		胎次	
	private String	fetus_cnt;//		胎儿数	
	private String	pret_flag;//		早产标志	
	private String	birctrl_matn_date;//		计划生育手术或生育日期	yyyy-MM-dd
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

	public String getAdm_diag_dscr() {
		return adm_diag_dscr;
	}
	public void setAdm_diag_dscr(String adm_diag_dscr) {
		this.adm_diag_dscr = adm_diag_dscr;
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
	
	
}
