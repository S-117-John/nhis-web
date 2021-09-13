package com.zebone.nhis.scm.pub.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.scm.pub.BdPdSupplyer;

public class PdSuppyersParam {

	/**
	 * 原供应商协议号
	 */
	private String oldCodeAgree;
	/**
	 * 原供应商主键
	 */
	private String oldPkSupplyer;
	
	List<BdPdSupplyer> pdSuppList = new ArrayList<BdPdSupplyer>();

	public String getOldCodeAgree() {
		return oldCodeAgree;
	}

	public void setOldCodeAgree(String oldCodeAgree) {
		this.oldCodeAgree = oldCodeAgree;
	}

	public String getOldPkSupplyer() {
		return oldPkSupplyer;
	}

	public void setOldPkSupplyer(String oldPkSupplyer) {
		this.oldPkSupplyer = oldPkSupplyer;
	}

	public List<BdPdSupplyer> getPdSuppList() {
		return pdSuppList;
	}

	public void setPdSuppList(List<BdPdSupplyer> pdSuppList) {
		this.pdSuppList = pdSuppList;
	}
}
