package com.zebone.nhis.cn.opdw.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.cn.opdw.PiOrdTemp;

public class OpFavoritesVo {
	private List<PiOrdTemp>  favList = new ArrayList<PiOrdTemp>();
    private List<PiOrdTemp> favListDel = new ArrayList<PiOrdTemp>();
	public List<PiOrdTemp> getFavList() {
		return favList;
	}
	public void setFavList(List<PiOrdTemp> favList) {
		this.favList = favList;
	}
	public List<PiOrdTemp> getFavListDel() {
		return favListDel;
	}
	public void setFavListDel(List<PiOrdTemp> favListDel) {
		this.favListDel = favListDel;
	}

}
