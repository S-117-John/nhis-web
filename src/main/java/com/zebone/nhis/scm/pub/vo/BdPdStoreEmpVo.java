package com.zebone.nhis.scm.pub.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.scm.pub.BdPdStore;
import com.zebone.nhis.common.module.scm.pub.BdPdStoreEmp;

public class BdPdStoreEmpVo {
	
private BdPdStore pdStore = new BdPdStore();
private List<BdPdStoreEmp> pdStoreEmpList = new ArrayList<BdPdStoreEmp>() ;
private List<BdPdStoreEmp> pdStoreEmpDel = new ArrayList<BdPdStoreEmp>() ;
public BdPdStore getPdStore() {
	return pdStore;
}
public void setPdStore(BdPdStore pdStore) {
	this.pdStore = pdStore;
}
public List<BdPdStoreEmp> getPdStoreEmpList() {
	return pdStoreEmpList;
}
public void setPdStoreEmpList(List<BdPdStoreEmp> pdStoreEmpList) {
	this.pdStoreEmpList = pdStoreEmpList;
}
public List<BdPdStoreEmp> getPdStoreEmpDel() {
	return pdStoreEmpDel;
}
public void setPdStoreEmpDel(List<BdPdStoreEmp> pdStoreEmpDel) {
	this.pdStoreEmpDel = pdStoreEmpDel;
}

}
