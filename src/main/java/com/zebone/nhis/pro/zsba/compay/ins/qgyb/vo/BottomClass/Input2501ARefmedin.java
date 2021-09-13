package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

public class Input2501ARefmedin {
	
	private String	refl_old_mdtrt_id;//	Y	转院前就诊id
	private String	psn_no;//	Y	人员编号	
	private String	insutype;//		险种类型	
	private String	tel;//		联系电话	
	private String	addr;//		联系地址	
	private String	insu_optins;//		参保机构医保区划	
	private String	diag_code;//	Y　	诊断代码	
	private String	diag_name;//	Y　	诊断名称	
	private String	dise_cond_dscr;//		疾病病情描述	
	private String	reflin_medins_no;//	Y　	转往定点医药机构编号	通过【1201】交易获取医药机构管理码
	private String	reflin_medins_name;//	Y	转往医院名称	
	private String	mdtrtarea_admdvs;//	Y	就医地行政区划	转往医院所属的行政区划
	private String	hosp_agre_refl_flag;//		医院同意转院标志	
	private String	refl_type;//	Y	转院类型	
	private String	refl_date;//	Y	转院日期	yyyy-MM-dd
	private String	refl_rea;//	Y	转院原因	
	private String	refl_opnn;//	Y	转院意见	
	private String	begndate;//		开始日期	yyyy-MM-dd
	private String	enddate;//		结束日期	yyyy-MM-dd
	public String getPsn_no() {
		return psn_no;
	}
	public void setPsn_no(String psn_no) {
		this.psn_no = psn_no;
	}
	public String getInsutype() {
		return insutype;
	}
	public void setInsutype(String insutype) {
		this.insutype = insutype;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getInsu_optins() {
		return insu_optins;
	}
	public void setInsu_optins(String insu_optins) {
		this.insu_optins = insu_optins;
	}
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
	public String getDise_cond_dscr() {
		return dise_cond_dscr;
	}
	public void setDise_cond_dscr(String dise_cond_dscr) {
		this.dise_cond_dscr = dise_cond_dscr;
	}
	public String getReflin_medins_no() {
		return reflin_medins_no;
	}
	public void setReflin_medins_no(String reflin_medins_no) {
		this.reflin_medins_no = reflin_medins_no;
	}
	public String getReflin_medins_name() {
		return reflin_medins_name;
	}
	public void setReflin_medins_name(String reflin_medins_name) {
		this.reflin_medins_name = reflin_medins_name;
	}
	public String getMdtrtarea_admdvs() {
		return mdtrtarea_admdvs;
	}
	public void setMdtrtarea_admdvs(String mdtrtarea_admdvs) {
		this.mdtrtarea_admdvs = mdtrtarea_admdvs;
	}
	public String getHosp_agre_refl_flag() {
		return hosp_agre_refl_flag;
	}
	public void setHosp_agre_refl_flag(String hosp_agre_refl_flag) {
		this.hosp_agre_refl_flag = hosp_agre_refl_flag;
	}
	public String getRefl_type() {
		return refl_type;
	}
	public void setRefl_type(String refl_type) {
		this.refl_type = refl_type;
	}
	public String getRefl_date() {
		return refl_date;
	}
	public void setRefl_date(String refl_date) {
		this.refl_date = refl_date;
	}
	public String getRefl_rea() {
		return refl_rea;
	}
	public void setRefl_rea(String refl_rea) {
		this.refl_rea = refl_rea;
	}
	public String getRefl_opnn() {
		return refl_opnn;
	}
	public void setRefl_opnn(String refl_opnn) {
		this.refl_opnn = refl_opnn;
	}
	public String getBegndate() {
		return begndate;
	}
	public void setBegndate(String begndate) {
		this.begndate = begndate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getRefl_old_mdtrt_id() {
		return refl_old_mdtrt_id;
	}
	public void setRefl_old_mdtrt_id(String refl_old_mdtrt_id) {
		this.refl_old_mdtrt_id = refl_old_mdtrt_id;
	}


	
}
