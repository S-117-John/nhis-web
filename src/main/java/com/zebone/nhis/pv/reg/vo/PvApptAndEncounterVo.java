package com.zebone.nhis.pv.reg.vo;

import java.util.List;

import com.zebone.nhis.common.module.sch.appt.SchAppt;

/**
 * 患者预约以及就诊信息
 * 
 * @author wangpeng
 * @date 2016年9月19日
 *
 */
public class PvApptAndEncounterVo {
	
	/** 患者预约列表 */
	private List<SchAppt> apptList;
	
	/** 历史就诊记录 */
	private List<PvOpVo> hisEncounterList;
	
	/** 当前挂号记录 */
	private List<PvOpVo> todayEncounterList;

	public List<SchAppt> getApptList() {
		return apptList;
	}

	public void setApptList(List<SchAppt> apptList) {
		this.apptList = apptList;
	}

	public List<PvOpVo> getHisEncounterList() {
		return hisEncounterList;
	}

	public void setHisEncounterList(List<PvOpVo> hisEncounterList) {
		this.hisEncounterList = hisEncounterList;
	}

	public List<PvOpVo> getTodayEncounterList() {
		return todayEncounterList;
	}

	public void setTodayEncounterList(List<PvOpVo> todayEncounterList) {
		this.todayEncounterList = todayEncounterList;
	}

}
