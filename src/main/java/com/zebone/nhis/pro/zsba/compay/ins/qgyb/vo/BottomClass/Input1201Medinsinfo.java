package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

public class Input1201Medinsinfo {
	
	private String	fixmedins_type;//	Y 定点医疗服务机构类型
	private String	fixmedins_name;//	定点医药机构名称 输入关键字模糊查询
	private String	fixmedins_code;//	定点医药机构编号  查询定点零售药店时填写定点零售药店代码；查询定点医疗机构时填写定点医疗机构代码。
	public String getFixmedins_type() {
		return fixmedins_type;
	}
	public void setFixmedins_type(String fixmedins_type) {
		this.fixmedins_type = fixmedins_type;
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
	
	
}
