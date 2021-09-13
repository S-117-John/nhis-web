package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

/**
 * 医疗保障基金结算清单信息上传入参——门诊慢特病诊断信息
 * @author Administrator
 *
 */
public class Input4101Opspdiseinfo {
	
	private String	diag_name;//	Y	诊断名称
	private String	diag_code;//	Y	诊断代码
	private String	oprn_oprt_name;//		手术操作名称
	private String	oprn_oprt_code;//		手术操作代码
	public String getDiag_name() {
		return diag_name;
	}
	public void setDiag_name(String diag_name) {
		this.diag_name = diag_name;
	}
	public String getDiag_code() {
		return diag_code;
	}
	public void setDiag_code(String diag_code) {
		this.diag_code = diag_code;
	}
	public String getOprn_oprt_name() {
		return oprn_oprt_name;
	}
	public void setOprn_oprt_name(String oprn_oprt_name) {
		this.oprn_oprt_name = oprn_oprt_name;
	}
	public String getOprn_oprt_code() {
		return oprn_oprt_code;
	}
	public void setOprn_oprt_code(String oprn_oprt_code) {
		this.oprn_oprt_code = oprn_oprt_code;
	}
}
