package com.zebone.nhis.bl.hd.vo;

import java.io.Serializable;

public class RefundVo implements Serializable,Cloneable {

private static final long serialVersionUID = 1L;
	
	public Object clone() {  
		RefundVo o = null;  
        try {  
            o = (RefundVo) super.clone();  
        } catch (CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return o;  
    }  
	
	/**
	 * 退费机构
	 */
	private String pkOrg;
	/**
	 * 退费部门
	 */
	private String pkDept;
	/**
	 * 退费人员
	 */
	private String NameEmp;
	private String pkEmp;

	/**
	 * 记费主键
	 */
	private String pkCgop;
	/**
	 * 退费数量
	 */
	private Double quanRe;

	public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public String getNameEmp() {
		return NameEmp;
	}
	public void setNameEmp(String nameEmp) {
		NameEmp = nameEmp;
	}
	public String getPkEmp() {
		return pkEmp;
	}
	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}
	public String getPkCgop() {
		return pkCgop;
	}
	public void setPkCgop(String pkCgop) {
		this.pkCgop = pkCgop;
	}
	public Double getQuanRe() {
		return quanRe;
	}
	public void setQuanRe(Double quanRe) {
		this.quanRe = quanRe;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
