package com.zebone.nhis.bl.pub.syx.vo;

public class BlBedItemStyVo extends BlItemPriceStyVo {
	//人员级别
	private String dictPsnlevel;
	
	//床位费限价
	private Double bedquota;
	
	public String getDictPsnlevel() {
		return dictPsnlevel;
	}

	public void setDictPsnlevel(String dictPsnlevel) {
		this.dictPsnlevel = dictPsnlevel;
	}

	public Double getBedquota() {
		return bedquota;
	}

	public void setBedquota(Double bedquota) {
		this.bedquota = bedquota;
	}

	
}
