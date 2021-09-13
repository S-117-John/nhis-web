package com.zebone.nhis.scm.pub.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.scm.pub.BdPdStore;
import com.zebone.nhis.common.module.scm.pub.BdPdStorePack;

public class BdPdStoreVo {
	private BdPdStore pdStoreInfo = new BdPdStore();
	
	private List<BdPdStorePack> pdStorePackList = new ArrayList<BdPdStorePack>();
	
	public BdPdStore getPdStoreInfo() {
		return pdStoreInfo;
	}
	public void setPdStoreInfo(BdPdStore pdStoreInfo) {
		this.pdStoreInfo = pdStoreInfo;
	}
	public List<BdPdStorePack> getPdStorePackList() {
		return pdStorePackList;
	}
	public void setPdStorePackList(List<BdPdStorePack> pdStorePackList) {
		this.pdStorePackList = pdStorePackList;
	}
	
	   
}
