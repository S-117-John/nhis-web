package com.zebone.nhis.scm.material.vo;

import java.util.List;

import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.common.module.scm.pub.BdPdAs;
import com.zebone.nhis.common.module.scm.pub.BdPdAtt;
import com.zebone.nhis.common.module.scm.pub.BdPdConvert;
import com.zebone.nhis.common.module.scm.pub.BdPdStore;

public class MtlSavePdInfo {

	/*物品基本信息*/
	private BdPd bdPd;
	
	/*物品别名信息*/
	private List<BdPdAs> pdAsList;
	
	/*物品附加属性*/
	private List<BdPdAtt> pdAttList;
	
	/*物品仓库信息*/
	private List<BdPdStore> pdStoreList;
	
	/*物品包装信息*/
	private List<BdPdConvert> pdConvertList;

	public BdPd getBdPd() {
		return bdPd;
	}

	public void setBdPd(BdPd bdPd) {
		this.bdPd = bdPd;
	}

	public List<BdPdAs> getPdAsList() {
		return pdAsList;
	}

	public void setPdAsList(List<BdPdAs> pdAsList) {
		this.pdAsList = pdAsList;
	}

	public List<BdPdAtt> getPdAttList() {
		return pdAttList;
	}

	public void setPdAttList(List<BdPdAtt> pdAttList) {
		this.pdAttList = pdAttList;
	}

	public List<BdPdStore> getPdStoreList() {
		return pdStoreList;
	}

	public void setPdStoreList(List<BdPdStore> pdStoreList) {
		this.pdStoreList = pdStoreList;
	}

	public List<BdPdConvert> getPdConvertList() {
		return pdConvertList;
	}

	public void setPdConvertList(List<BdPdConvert> pdConvertList) {
		this.pdConvertList = pdConvertList;
	}
}
