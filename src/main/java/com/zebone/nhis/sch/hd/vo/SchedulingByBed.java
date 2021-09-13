package com.zebone.nhis.sch.hd.vo;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.zebone.nhis.common.module.base.bd.code.BdCodeDateslot;
import com.zebone.nhis.common.module.sch.hd.SchSchHd;

/**
 * 基于床位视角排班
 * @author Administrator
 *
 */
public class SchedulingByBed {
	
	//床位PK
	private String pkHdbed;
	
	//床位编码
	private String codeBed;
	
	//床位名称
	private String nameBed;
	
	//关联设备
	private String msp;
		
	//治疗类型
	private String dtHdtype;
	
	//治疗类型-为校验床位是否符合患者治疗类型
	private String dtHdtypeCopy;
	
	//日期分组名称
	private String nameDateslot;
	
	private String pkDateslot;
	
	public String getPkDateslot() {
		return pkDateslot;
	}

	public void setPkDateslot(String pkDateslot) {
		this.pkDateslot = pkDateslot;
	}

	//天列表
	private List<WeekVo> oneDay;


	public String getPkHdbed() {
		return pkHdbed;
	}

	public void setPkHdbed(String pkHdbed) {
		this.pkHdbed = pkHdbed;
	}

	public String getCodeBed() {
		return codeBed;
	}

	public void setCodeBed(String codeBed) {
		this.codeBed = codeBed;
	}

	public String getNameBed() {
		return nameBed;
	}

	public void setNameBed(String nameBed) {
		this.nameBed = nameBed;
	}

	public String getMsp() {
		return msp;
	}

	public void setMsp(String msp) {
		this.msp = msp;
	}

	public String getDtHdtype() {
		return dtHdtype;
	}

	public void setDtHdtype(String dtHdtype) {
		this.dtHdtype = dtHdtype;
	}

	public String getNameDateslot() {
		return nameDateslot;
	}

	public void setNameDateslot(String nameDateslot) {
		this.nameDateslot = nameDateslot;
	}

	public List<WeekVo> getOneDay() {
		return oneDay;
	}

	public void setOneDay(List<WeekVo> oneDay) {
		this.oneDay = oneDay;
	}

	public String getDtHdtypeCopy() {
		return dtHdtypeCopy;
	}

	public void setDtHdtypeCopy(String dtHdtypeCopy) {
		this.dtHdtypeCopy = dtHdtypeCopy;
	}
	
}
