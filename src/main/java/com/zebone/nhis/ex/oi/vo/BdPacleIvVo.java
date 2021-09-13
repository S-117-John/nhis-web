package com.zebone.nhis.ex.oi.vo;

import com.zebone.nhis.common.module.ex.oi.BdPlaceIv;

public class BdPacleIvVo extends BdPlaceIv {
	private String sexName;
	private String deptName;
	private String itemName;
	private String euStatusName;
	private String flagActive;
	
	public String getEuStatusName() {
		return euStatusName;
	}
	public void setEuStatusName(String euStatusName) {
		this.euStatusName = euStatusName;
	}
	public String getSexName() {
		return sexName;
	}
	public void setSexName(String sexName) {
		this.sexName = sexName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getFlagActive() {
		return flagActive;
	}
	public void setFlagActive(String flagActive) {
		this.flagActive = flagActive;
	}
	

}
