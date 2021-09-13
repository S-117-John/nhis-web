package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

public class Input4160Data {
	
	private String	poolarea_no;//	统筹区	字符型	10			
	private String	fixmedins_code;//	定点医药机构编号	字符型	30		Y	
	private String	fixmedins_name;//	定点医药机构名称	字符型	200		
	private String	norm_flag;//	数据标识	字符型	6	Y		
	private String	diag_grp;//	诊断亚目分组	字符型	10
	private String	setl_end_date;//	结算结束时间（查询区间开始值）	日期时间型				yyyy-MM-dd HH:mm:ss
	private String	end_setl_end_date;//	结算结束时间（查询区间结束值）	日期时间型				yyyy-MM-dd HH:mm:ss
	private String	page_num;//	当前页数	数值型	4			
	private String	page_size;//	本页数据量	数值型	4	
	public String getPoolarea_no() {
		return poolarea_no;
	}
	public void setPoolarea_no(String poolarea_no) {
		this.poolarea_no = poolarea_no;
	}
	public String getFixmedins_code() {
		return fixmedins_code;
	}
	public void setFixmedins_code(String fixmedins_code) {
		this.fixmedins_code = fixmedins_code;
	}
	public String getFixmedins_name() {
		return fixmedins_name;
	}
	public void setFixmedins_name(String fixmedins_name) {
		this.fixmedins_name = fixmedins_name;
	}
	public String getNorm_flag() {
		return norm_flag;
	}
	public void setNorm_flag(String norm_flag) {
		this.norm_flag = norm_flag;
	}
	public String getDiag_grp() {
		return diag_grp;
	}
	public void setDiag_grp(String diag_grp) {
		this.diag_grp = diag_grp;
	}
	public String getSetl_end_date() {
		return setl_end_date;
	}
	public void setSetl_end_date(String setl_end_date) {
		this.setl_end_date = setl_end_date;
	}
	public String getEnd_setl_end_date() {
		return end_setl_end_date;
	}
	public void setEnd_setl_end_date(String end_setl_end_date) {
		this.end_setl_end_date = end_setl_end_date;
	}
	public String getPage_num() {
		return page_num;
	}
	public void setPage_num(String page_num) {
		this.page_num = page_num;
	}
	public String getPage_size() {
		return page_size;
	}
	public void setPage_size(String page_size) {
		this.page_size = page_size;
	}
	
	
}
