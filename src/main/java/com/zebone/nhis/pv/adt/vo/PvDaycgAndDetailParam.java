package com.zebone.nhis.pv.adt.vo;

import java.util.List;

import com.zebone.nhis.common.module.pv.PvDaycg;
import com.zebone.nhis.common.module.pv.PvDaycgDetail;

/***
 * 患者就诊-固定费用、明细
 *  
 * @author wangpeng
 * @date 2016年12月9日
 *
 */
public class PvDaycgAndDetailParam {
	
	/***
	 * 患者就诊-固定费用
	 */
	private PvDaycg pvDaycg;
	
	/**
	 * 患者就诊-固定费用-明细
	 */
	private List<PvDaycgDetail> daycgDetailList;

	public PvDaycg getPvDaycg() {
		return pvDaycg;
	}

	public void setPvDaycg(PvDaycg pvDaycg) {
		this.pvDaycg = pvDaycg;
	}

	public List<PvDaycgDetail> getDaycgDetailList() {
		return daycgDetailList;
	}

	public void setDaycgDetailList(List<PvDaycgDetail> daycgDetailList) {
		this.daycgDetailList = daycgDetailList;
	}

}
