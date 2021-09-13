package com.zebone.nhis.pro.lb.arch.vo;

import java.util.List;

import com.zebone.nhis.common.module.arch.ArchDoc;
import com.zebone.nhis.common.module.arch.ArchPrint;

public class ArchDocVo extends ArchDoc{

	private static final long serialVersionUID = 1L;
	
	private String dtMeddocname;
	
	public String getDtMeddocname() {
		return dtMeddocname;
	}

	public void setDtMeddocname(String dtMeddocname) {
		this.dtMeddocname = dtMeddocname;
	}

	private List<ArchPrint> listPrint;

	public List<ArchPrint> getListPrint() {
		return listPrint;
	}

	public void setListPrint(List<ArchPrint> listPrint) {
		this.listPrint = listPrint;
	}
	
	
}
