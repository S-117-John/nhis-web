package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

/**
 * 医疗保障基金结算清单信息上传入参——手术操作信息
 * @author Administrator
 *
 */
public class Input4101Oprninfo {
	
	private String	oprn_oprt_type;//	Y	手术操作类别	
	private String	oprn_oprt_name;//	Y	手术操作名称	
	private String	oprn_oprt_code;//	Y	手术操作代码	
	private String	oprn_oprt_date;//	Y	手术操作日期	
	private String	anst_way;//		麻醉方式	参照麻醉-方法代码
	private String	oper_dr_name;//	Y	术者医师姓名	
	private String	oper_dr_code;//	Y	术者医师代码	
	private String	anst_dr_name;//		麻醉医师姓名	
	private String	anst_dr_code;//		麻醉医师代码	
	public String getOprn_oprt_type() {
		return oprn_oprt_type;
	}
	public void setOprn_oprt_type(String oprn_oprt_type) {
		this.oprn_oprt_type = oprn_oprt_type;
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
	public String getOprn_oprt_date() {
		return oprn_oprt_date;
	}
	public void setOprn_oprt_date(String oprn_oprt_date) {
		this.oprn_oprt_date = oprn_oprt_date;
	}
	public String getAnst_way() {
		return anst_way;
	}
	public void setAnst_way(String anst_way) {
		this.anst_way = anst_way;
	}
	public String getOper_dr_name() {
		return oper_dr_name;
	}
	public void setOper_dr_name(String oper_dr_name) {
		this.oper_dr_name = oper_dr_name;
	}
	public String getOper_dr_code() {
		return oper_dr_code;
	}
	public void setOper_dr_code(String oper_dr_code) {
		this.oper_dr_code = oper_dr_code;
	}
	public String getAnst_dr_name() {
		return anst_dr_name;
	}
	public void setAnst_dr_name(String anst_dr_name) {
		this.anst_dr_name = anst_dr_name;
	}
	public String getAnst_dr_code() {
		return anst_dr_code;
	}
	public void setAnst_dr_code(String anst_dr_code) {
		this.anst_dr_code = anst_dr_code;
	}

	
	
}
