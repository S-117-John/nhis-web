package com.zebone.nhis.ma.self.vo;

import java.util.List;

import com.zebone.nhis.common.module.ma.self.BdOuSssOper;

public class MachineOperVo {
private String pkMachine;
private String pkOrg;
private String ipAddress;
private String MachineName;
private List<BdOuSssOper> Opers;
public String getPkMachine() {
	return pkMachine;
}
public void setPkMachine(String pkMachine) {
	this.pkMachine = pkMachine;
}
public String getPkOrg() {
	return pkOrg;
}
public void setPkOrg(String pkOrg) {
	this.pkOrg = pkOrg;
}
public String getIpAddress() {
	return ipAddress;
}
public void setIpAddress(String ipAddress) {
	this.ipAddress = ipAddress;
}
public String getMachineName() {
	return MachineName;
}
public void setMachineName(String machineName) {
	MachineName = machineName;
}
public List<BdOuSssOper> getOpers() {
	return Opers;
}
public void setOpers(List<BdOuSssOper> opers) {
	Opers = opers;
}
}
