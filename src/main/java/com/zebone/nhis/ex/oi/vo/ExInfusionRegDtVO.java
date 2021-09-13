package com.zebone.nhis.ex.oi.vo;

import com.zebone.nhis.common.module.ex.oi.ExInfusionRegDt;

public class ExInfusionRegDtVO extends ExInfusionRegDt {
	private String pkBed;
	 // <summary>
    /// 获取或设置 occ表主键
    /// </summary>
    public String pkInfuocc;

     // <summary>
    /// 获取或设置 occ表occ_no字段
    /// </summary>
    public String occNo;

	public String getPkBed() {
		return pkBed;
	}

	public void setPkBed(String pkBed) {
		this.pkBed = pkBed;
	}

	public String getPkInfuocc() {
		return pkInfuocc;
	}

	public void setPkInfuocc(String pkInfuocc) {
		this.pkInfuocc = pkInfuocc;
	}

	public String getOccNo() {
		return occNo;
	}

	public void setOccNo(String occNo) {
		this.occNo = occNo;
	}
	
	
	

}
