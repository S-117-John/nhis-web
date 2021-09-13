package com.zebone.nhis.common.module.bl;

import java.io.Serializable;

/**
 * 退费参数
 * @author wujunjie
 *
 */
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
	private String pkCgip;
	/**
	 * 退费数量
	 */
	private Double quanRe;

	/**
	 * 发退主键
	 */
	private String pkOrdexdt;
	/**
	 * 取消原因
	 */
	private String noteCg;

	public String getPkOrdexdt() {
		return pkOrdexdt;
	}

	public void setPkOrdexdt(String pkOrdexdt) {
		this.pkOrdexdt = pkOrdexdt;
	}

	public Double getQuanRe() {
		return quanRe;
	}

	public void setQuanRe(Double quanRe) {
		this.quanRe = quanRe;
	}

	public String getNameEmp() {
		return NameEmp;
	}

	public void setNameEmp(String nameEmp) {
		NameEmp = nameEmp;
	}

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

	public String getPkEmp() {
		return pkEmp;
	}

	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}


	public String getPkCgip() {
		return pkCgip;
	}

	public void setPkCgip(String pkCgip) {
		this.pkCgip = pkCgip;
	}

	public String getNoteCg() { return noteCg; }

	public void setNoteCg(String noteCg) { this.noteCg = noteCg; }
}
