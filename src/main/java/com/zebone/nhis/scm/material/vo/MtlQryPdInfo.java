package com.zebone.nhis.scm.material.vo;

import java.util.List;

import com.zebone.nhis.common.module.scm.pub.BdPdAs;

/**
 * 物品相关全部信息
 * @author wj
 *
 */
public class MtlQryPdInfo {
	
	/*物品基本信息*/
	private BdPdBaseVo bdPd;
	
	/*物品别名信息*/
	private List<BdPdAs> pdAsList;
	
	/*物品附加属性*/
	private List<BdPdAttVo> pdAttList;
	
	/*物品仓库信息*/
	private List<BdPdStoreInfo> pdStoreList;

	public BdPdBaseVo getBdPd() {
		return bdPd;
	}

	public void setBdPd(BdPdBaseVo bdPd) {
		this.bdPd = bdPd;
	}

	public List<BdPdAs> getPdAsList() {
		return pdAsList;
	}

	public void setPdAsList(List<BdPdAs> pdAsList) {
		this.pdAsList = pdAsList;
	}

	public List<BdPdAttVo> getPdAttList() {
		return pdAttList;
	}

	public void setPdAttList(List<BdPdAttVo> pdAttList) {
		this.pdAttList = pdAttList;
	}

	public List<BdPdStoreInfo> getPdStoreList() {
		return pdStoreList;
	}

	public void setPdStoreList(List<BdPdStoreInfo> pdStoreList) {
		this.pdStoreList = pdStoreList;
	}
}
