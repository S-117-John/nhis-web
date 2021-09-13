package com.zebone.nhis.bl.pub.syx.vo;

import java.util.Date;

/**
 * 关联医嘱
 * @author 
 *
 */
public class OtherRelatedOrdersVo {
	private String pk_cnord;        //医嘱主键
    private String name_ord;        //名称
    private String pk_dept;         //开立科室
    private String name_emp_ord;    //开立医生
    private Date date_start;      //开始日期
	public String getPk_cnord() {
		return pk_cnord;
	}
	public void setPk_cnord(String pk_cnord) {
		this.pk_cnord = pk_cnord;
	}
	public String getName_ord() {
		return name_ord;
	}
	public void setName_ord(String name_ord) {
		this.name_ord = name_ord;
	}
	public String getPk_dept() {
		return pk_dept;
	}
	public void setPk_dept(String pk_dept) {
		this.pk_dept = pk_dept;
	}
	public String getName_emp_ord() {
		return name_emp_ord;
	}
	public void setName_emp_ord(String name_emp_ord) {
		this.name_emp_ord = name_emp_ord;
	}
	public Date getDate_start() {
		return date_start;
	}
	public void setDate_start(Date date_start) {
		this.date_start = date_start;
	}
    
}
