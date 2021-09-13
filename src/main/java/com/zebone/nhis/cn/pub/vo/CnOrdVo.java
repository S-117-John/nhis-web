package com.zebone.nhis.cn.pub.vo;

import java.util.List;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;

/**
 * 医嘱保存参数vo
 * 
 * @author yangxue
 * 
 */
public class CnOrdVo {
	//界面医嘱集合
	private List<CnOrder> ordList;
	// 删除集合pkcnord
	private List<Object[]> delList;
	//医技医嘱查询用
	private String flagMedOrd;
	//医技医嘱查询用
	private String ordsnParent;

	
	public List<Object[]> getDelList() {
		return delList;
	}

	public void setDelList(List<Object[]> delList) {
		this.delList = delList;
	}

	public List<CnOrder> getOrdList() {
		return ordList;
	}

	public void setOrdList(List<CnOrder> ordList) {
		this.ordList = ordList;
	}

	public String getFlagMedOrd() {
		return flagMedOrd;
	}

	public void setFlagMedOrd(String flagMedOrd) {
		this.flagMedOrd = flagMedOrd;
	}

	public String getOrdsnParent() {
		return ordsnParent;
	}

	public void setOrdsnParent(String ordsnParent) {
		this.ordsnParent = ordsnParent;
	}

}
