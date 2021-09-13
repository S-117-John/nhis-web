package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.pv.PvEncounter;

public class BlIpCgVo {
	
	private List<ItemPriceVo> items = new ArrayList<>();
	
	private String codeCg;
	
	private int cgSort = 1;
	
	private PvEncounter pv;

	public List<ItemPriceVo> getItems() {
		return items;
	}

	public void setItems(List<ItemPriceVo> items) {
		this.items = items;
	}

	public String getCodeCg() {
		return codeCg;
	}

	public void setCodeCg(String codeCg) {
		this.codeCg = codeCg;
	}

	public int getCgSort() {
		return cgSort;
	}

	public void setCgSort(int cgSort) {
		this.cgSort = cgSort;
	}

	public PvEncounter getPv() {
		return pv;
	}

	public void setPv(PvEncounter pv) {
		this.pv = pv;
	}
	
	
	
}
