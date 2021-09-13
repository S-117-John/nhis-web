package com.zebone.nhis.ex.pivas.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.ex.pivas.conf.BdPivasCate;
import com.zebone.nhis.common.module.ex.pivas.conf.BdPivasPd;

public class PivasCateAndPdsParam {

	/**
	 * 静配分类
	 */
	private BdPivasCate bdPivasCate;
	/**
	 * 静配分类下的药品目录
	 */
	private List<BdPivasPd> bdPivasPdList = new ArrayList<BdPivasPd>();

	private List<Object[]> delelteList;//删除的明细主键
	
	public List<Object[]> getDelelteList() {
		return delelteList;
	}

	public void setDelelteList(List<Object[]> delelteList) {
		this.delelteList = delelteList;
	}

	public BdPivasCate getBdPivasCate() {
		return bdPivasCate;
	}

	public void setBdPivasCate(BdPivasCate bdPivasCate) {
		this.bdPivasCate = bdPivasCate;
	}

	public List<BdPivasPd> getBdPivasPdList() {
		return bdPivasPdList;
	}

	public void setBdPivasPdList(List<BdPivasPd> bdPivasPdList) {
		this.bdPivasPdList = bdPivasPdList;
	}
}
