package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

/**
 * 5.2.5.2【2402】出院办理入参 出院信息（节点标识：dscginfo）
 * @author Administrator
 *
 */
public class Input2402Dscginfo {
	
	private String	mdtrt_id;//	就诊ID	
	private String	psn_no;//	人员编号	
	private String	insutype;//	险种类型	
	private String	endtime;//	结束时间	"出院时间yyyy-MM-dd HH:mm:ss"
	private String	dise_codg;//	病种编码	"按照标准编码填写：按病种结算病种目录代码(bydise_setl_list_code)日间手术病种目录代码(daysrg_dise_list_code)"
	private String	dise_name;//	病种名称	
	private String	oprn_oprt_code;//	手术操作代码	日间手术病种时必填
	private String	oprn_oprt_name;//	手术操作名称	
	private String	fpsc_no;//	计划生育服务证号	
	private String	matn_type;//	生育类别	
	private String	birctrl_type;//	计划生育手术类别	
	private String	latechb_flag;//	晚育标志	
	private String	geso_val;//	孕周数	
	private String	fetts;//	胎次	
	private String	fetus_cnt;//	胎儿数	
	private String	pret_flag;//	早产标志	
	private String	birctrl_matn_date;//	计划生育手术或生育日期	yyyy-MM-dd
	private String	cop_flag;//	伴有并发症标志	
	private String	dscg_dept_codg;//	出院科室编码	
	private String	dscg_dept_name;//	出院科室名称	
	private String	dscg_bed;//	出院床位	
	private String	dscg_way;//	离院方式	
	private String	die_date;//	死亡日期	yyyy-MM-dd
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
	public String getInsutype() {
		return insutype;
	}
	public void setInsutype(String insutype) {
		this.insutype = insutype;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
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
	public String getCop_flag() {
		return cop_flag;
	}
	public void setCop_flag(String cop_flag) {
		this.cop_flag = cop_flag;
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
	public String getDie_date() {
		return die_date;
	}
	public void setDie_date(String die_date) {
		this.die_date = die_date;
	}
}