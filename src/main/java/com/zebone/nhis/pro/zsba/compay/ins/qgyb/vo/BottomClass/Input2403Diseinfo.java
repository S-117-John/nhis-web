package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

import com.google.gson.annotations.SerializedName;

/**
 * 5.2.5.3住院信息变更2403入参 入院诊断信息（节点标识：diseinfo）
 * @author Administrator
 *
 */
public class Input2403Diseinfo {
	
	private String	mdtrt_id;//	Y　	就诊ID
	private String	psn_no;//	　Y	人员编号	
	private String	diag_type;//	　Y	诊断类别	
	private String	maindiag_flag;//	　Y	主诊断标志	
	private String	diag_srt_no;//	　Y	诊断排序号	
	private String	diag_code;//	　Y	诊断代码	
	private String	diag_name;//	　Y	诊断名称	
	private String	adm_cond;//		入院病情	
	private String	diag_dept;//	　Y	诊断科室	
	private String	dise_dor_no;//	　Y	诊断医生编码	
	private String	dise_dor_name;//	　Y	诊断医生姓名	
	private String	diag_time;//	　Y	诊断时间	yyyy-MM-dd HH:mm:ss
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
	public String getDiag_type() {
		return diag_type;
	}
	public void setDiag_type(String diag_type) {
		this.diag_type = diag_type;
	}

	public String getMaindiag_flag() {
		return maindiag_flag;
	}
	public void setMaindiag_flag(String maindiag_flag) {
		this.maindiag_flag = maindiag_flag;
	}
	public String getDiag_srt_no() {
		return diag_srt_no;
	}
	public void setDiag_srt_no(String diag_srt_no) {
		this.diag_srt_no = diag_srt_no;
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
	public String getAdm_cond() {
		return adm_cond;
	}
	public void setAdm_cond(String adm_cond) {
		this.adm_cond = adm_cond;
	}
	public String getDiag_dept() {
		return diag_dept;
	}
	public void setDiag_dept(String diag_dept) {
		this.diag_dept = diag_dept;
	}
	public String getDise_dor_no() {
		return dise_dor_no;
	}
	public void setDise_dor_no(String dise_dor_no) {
		this.dise_dor_no = dise_dor_no;
	}
	public String getDise_dor_name() {
		return dise_dor_name;
	}
	public void setDise_dor_name(String dise_dor_name) {
		this.dise_dor_name = dise_dor_name;
	}
	public String getDiag_time() {
		return diag_time;
	}
	public void setDiag_time(String diag_time) {
		this.diag_time = diag_time;
	}
	
	
}
