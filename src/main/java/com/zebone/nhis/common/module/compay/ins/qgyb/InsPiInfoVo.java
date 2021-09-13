package com.zebone.nhis.common.module.compay.ins.qgyb;

import java.util.List;

public class InsPiInfoVo {

    /// <summary>
    /// 基本信息
    /// </summary>
    private InsQgybMaster baseinfo;
    
    /// <summary>
    /// 参保信息
    /// </summary>
    private List<InsQgybInsutype> insuinfo;
    /// <summary>
    /// 身份信息
    /// </summary>
    private List<InsQgybIdet> idetinfo ;
    
    private String pkPv;
    private String pkPi;
	
	public List<InsQgybInsutype> getInsuinfo() {
		return insuinfo;
	}
	public void setInsuinfo(List<InsQgybInsutype> insuinfo) {
		this.insuinfo = insuinfo;
	}
	public List<InsQgybIdet> getIdetinfo() {
		return idetinfo;
	}
	public void setIdetinfo(List<InsQgybIdet> idetinfo) {
		this.idetinfo = idetinfo;
	}
	public InsQgybMaster getBaseinfo() {
		return baseinfo;
	}
	public void setBaseinfo(InsQgybMaster baseinfo) {
		this.baseinfo = baseinfo;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}
	
}
