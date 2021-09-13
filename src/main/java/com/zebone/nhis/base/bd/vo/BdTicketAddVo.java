package com.zebone.nhis.base.bd.vo;

import java.util.List;

import com.zebone.nhis.common.module.base.bd.price.BdTicketaddCs;
import com.zebone.nhis.common.module.base.bd.price.BdTicketaddCsdt;

public class BdTicketAddVo {
	
	private BdTicketaddCs btacs;
	
	private List<BdTicketaddCsdt> btacsdts;
	
	private List<String> delPkCsdtList;

	public List<String> getDelPkCsdtList() {
		return delPkCsdtList;
	}

	public void setDelPkCsdtList(List<String> delPkCsdtList) {
		this.delPkCsdtList = delPkCsdtList;
	}

	public BdTicketaddCs getBtacs() {
		return btacs;
	}

	public void setBtacs(BdTicketaddCs btacs) {
		this.btacs = btacs;
	}

	public List<BdTicketaddCsdt> getBtacsdts() {
		return btacsdts;
	}

	public void setBtacsdts(List<BdTicketaddCsdt> btacsdts) {
		this.btacsdts = btacsdts;
	}

	
	
}
