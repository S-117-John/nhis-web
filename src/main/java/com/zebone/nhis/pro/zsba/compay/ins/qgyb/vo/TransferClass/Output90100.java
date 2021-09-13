package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass;

/**
 * 1.16缴费查询接口【90100】回参实体类
 * @author Administrator
 *
 */
public class Output90100 {
	
	private String	poolarea_no;//	Y	统筹区代码	例：中山市的代码是：442000
	private String	poolarea_no_name;//		统筹区名称	
	private String	insutype;//	Y	险种类型代码	例：390=城乡居民基本医疗保险
	private String	insutype_name;//	Y	险种类型名称	
	private String	clct_type;//	Y	缴费类型	例：10=正常缴费
	private String	clct_type_name;//	Y	缴费类型名称	
	private String	clct_flag;//	Y	到账类型	例：0=未到账1=足额缴费
	private String	clct_flag_name;//	Y	到账类型名称	
	private String	accrym_begn;//	Y	费款所属期开始日期	例：202001
	private String	accrym_end;//	Y	费款所属期结束日期	例：202001
	private String	clct_time;//	Y	到账时间	例：2021-01-26 00:00:00
	public String getPoolarea_no() {
		return poolarea_no;
	}
	public void setPoolarea_no(String poolarea_no) {
		this.poolarea_no = poolarea_no;
	}
	public String getPoolarea_no_name() {
		return poolarea_no_name;
	}
	public void setPoolarea_no_name(String poolarea_no_name) {
		this.poolarea_no_name = poolarea_no_name;
	}
	public String getInsutype() {
		return insutype;
	}
	public void setInsutype(String insutype) {
		this.insutype = insutype;
	}
	public String getInsutype_name() {
		return insutype_name;
	}
	public void setInsutype_name(String insutype_name) {
		this.insutype_name = insutype_name;
	}
	public String getClct_type() {
		return clct_type;
	}
	public void setClct_type(String clct_type) {
		this.clct_type = clct_type;
	}
	public String getClct_type_name() {
		return clct_type_name;
	}
	public void setClct_type_name(String clct_type_name) {
		this.clct_type_name = clct_type_name;
	}
	public String getClct_flag() {
		return clct_flag;
	}
	public void setClct_flag(String clct_flag) {
		this.clct_flag = clct_flag;
	}
	public String getClct_flag_name() {
		return clct_flag_name;
	}
	public void setClct_flag_name(String clct_flag_name) {
		this.clct_flag_name = clct_flag_name;
	}
	public String getAccrym_begn() {
		return accrym_begn;
	}
	public void setAccrym_begn(String accrym_begn) {
		this.accrym_begn = accrym_begn;
	}
	public String getAccrym_end() {
		return accrym_end;
	}
	public void setAccrym_end(String accrym_end) {
		this.accrym_end = accrym_end;
	}
	public String getClct_time() {
		return clct_time;
	}
	public void setClct_time(String clct_time) {
		this.clct_time = clct_time;
	}

	
}
