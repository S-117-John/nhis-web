package com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo;

import java.util.List;


/**
 * 计生手术初始数据返回值
 * @author zrj
 *
 */
public class InsOptPbData extends InsZsBaYbOptPb{

	private List<String> ssrqList; //手术日期

	public List<String> getSsrqList() {
		return ssrqList;
	}

	public void setSsrqList(List<String> ssrqList) {
		this.ssrqList = ssrqList;
	}
}
