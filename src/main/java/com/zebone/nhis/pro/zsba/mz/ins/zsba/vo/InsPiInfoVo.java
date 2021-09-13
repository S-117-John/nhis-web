package com.zebone.nhis.pro.zsba.mz.ins.zsba.vo;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private String insuTypes;
    
    // 险种与参保地 集合
    private Set<Map<String, Object>> insuTypeAndPlaceList = new HashSet<Map<String,Object>>();
	
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
	public String getInsuTypes() {
		return insuTypes;
	}
	public void setInsuTypes(String insuTypes) {
		this.insuTypes = insuTypes;
	}
	public Set<Map<String, Object>> getInsuTypeAndPlaceList() {
		return insuTypeAndPlaceList;
	}
	public void setInsuTypeAndPlaceList(Set<Map<String, Object>> insuTypeAndPlaceList) {
		this.insuTypeAndPlaceList = insuTypeAndPlaceList;
	}
	
	
}
