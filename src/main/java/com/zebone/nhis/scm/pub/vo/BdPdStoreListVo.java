package com.zebone.nhis.scm.pub.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.scm.pub.BdPdStore;

public class BdPdStoreListVo {
	
	private List<BdPdStore> pdList = new ArrayList<>();
	
	private String isDrug;	//判断由物资还是供应链打开仓库物品界面，1为供应链打开，0为物资打开

	public List<BdPdStore> getPdList() {
		return pdList;
	}

	public void setPdList(List<BdPdStore> pdList) {
		this.pdList = pdList;
	}

	public String getIsDrug() {
		return isDrug;
	}

	public void setIsDrug(String isDrug) {
		this.isDrug = isDrug;
	}
}
