package com.zebone.nhis.bl.pub.vo;

import java.util.List;

import com.zebone.nhis.common.module.bl.BlCgset;

public class BlCgSetVo extends BlCgset {
	private List<BlCgSetDtVo> dtlist;

	public List<BlCgSetDtVo> getDtlist() {
		return dtlist;
	}

	public void setDtlist(List<BlCgSetDtVo> dtlist) {
		this.dtlist = dtlist;
	}

}
