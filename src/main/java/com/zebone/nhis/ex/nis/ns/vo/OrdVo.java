package com.zebone.nhis.ex.nis.ns.vo;

import java.util.Date;
import java.util.List;

/**
 * 用来接收收据
 * @Auther: wuqiang
 * @Date: 2018/12/13 11:40
 * @Description:
 */
public class OrdVo {

	/** 待更新的请领明细主键  */
    private List<String> pkPdapdt ;
    private String pkPdapdts ;
    /** 待更新的执行单主键  */
    private List<String> pkExocc ;
    private String pkExoccs ;
    /** 操作状态  */
    private String euResult ;
    /** 操作人主键  */
    private String pkEmp;
    /** 操作人名称 */
    private String nameEmp;
    /** 操作时间  */
    private String dateOpe;
    /** 是否取消执行单  */
    private String flagCanc;

    public List<String> getPkPdapdt() {
        return pkPdapdt;
    }

    public void setPkPdapdt(List<String> pkPdapdt) {
        this.pkPdapdt = pkPdapdt;
    }

	public List<String> getPkExocc() {
		return pkExocc;
	}

	public void setPkExocc(List<String> pkExocc) {
		this.pkExocc = pkExocc;
	}

	public String getEuResult() {
		return euResult;
	}

	public void setEuResult(String euResult) {
		this.euResult = euResult;
	}

	public String getPkEmp() {
		return pkEmp;
	}

	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}

	public String getNameEmp() {
		return nameEmp;
	}

	public void setNameEmp(String nameEmp) {
		this.nameEmp = nameEmp;
	}

	public String getDateOpe() {
		return dateOpe;
	}

	public void setDateOpe(String dateOpe) {
		this.dateOpe = dateOpe;
	}

	public String getFlagCanc() {
		return flagCanc;
	}

	public void setFlagCanc(String flagCanc) {
		this.flagCanc = flagCanc;
	}

	public String getPkPdapdts() {
		return pkPdapdts;
	}

	public void setPkPdapdts(String pkPdapdts) {
		this.pkPdapdts = pkPdapdts;
	}

	public String getPkExoccs() {
		return pkExoccs;
	}

	public void setPkExoccs(String pkExoccs) {
		this.pkExoccs = pkExoccs;
	}
    
}
