package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

/**
 * 医疗保障基金结算清单信息上传入参——住院诊断信息
 * @author Administrator
 *
 */
public class Input4101Diseinfo {
	
	private String	diag_type;//	　Y	诊断类别
	private String	diag_code;//	　Y	诊断代码
	private String	diag_name;//	　Y	诊断名称
	private String	adm_cond_type;//		入院病情类型
	private String maindiag_flag;// 主诊断标志
	public String getDiag_type() {
		return diag_type;
	}
	public void setDiag_type(String diag_type) {
		this.diag_type = diag_type;
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
	public String getAdm_cond_type() {
		return adm_cond_type;
	}
	public void setAdm_cond_type(String adm_cond_type) {
		this.adm_cond_type = adm_cond_type;
	}
	public String getMaindiag_flag() {
		return maindiag_flag;
	}
	public void setMaindiag_flag(String maindiag_flag) {
		this.maindiag_flag = maindiag_flag;
	}
	
}
