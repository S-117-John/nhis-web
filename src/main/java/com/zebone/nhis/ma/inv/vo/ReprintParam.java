package com.zebone.nhis.ma.inv.vo;

import java.util.ArrayList;
import java.util.List;

public class ReprintParam {

	/**
	 * 机器名
	 */
	String nameMachine;
	
	/**
	 * 票据类型: 0 门诊发票，1 住院发票
	 */
	String euType;
	
	/**
	 * 作废号码组
	 */
	List<String> invalidNoList = new ArrayList<String>();

	public String getNameMachine() {
		return nameMachine;
	}

	public void setNameMachine(String nameMachine) {
		this.nameMachine = nameMachine;
	}

	public String getEuType() {
		return euType;
	}

	public void setEuType(String euType) {
		this.euType = euType;
	}

	public List<String> getInvalidNoList() {
		return invalidNoList;
	}

	public void setInvalidNoList(List<String> invalidNoList) {
		this.invalidNoList = invalidNoList;
	}
	
}
