package com.zebone.nhis.pv.arr.vo;

import java.util.List;
import com.zebone.nhis.common.module.base.bd.srv.BdQcScreen;
import com.zebone.nhis.common.module.base.bd.srv.BdQcScreenDu;

public class BdQcScreenVO {

	private BdQcScreen bdQcScreen;
	
	private List<BdQcScreenDu> list;

	public BdQcScreen getBdQcScreen() {
		return bdQcScreen;
	}

	public void setBdQcScreen(BdQcScreen bdQcScreen) {
		this.bdQcScreen = bdQcScreen;
	}

	public List<BdQcScreenDu> getList() {
		return list;
	}

	public void setList(List<BdQcScreenDu> list) {
		this.list = list;
	}
}
