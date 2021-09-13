package com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo;

import java.util.List;


/**
 * 获取日间手术初始数据的返回值
 * @author zrj
 *
 */
public class InsOptDayData extends InsZsBaYbOptDay{

	private List<String> ssrqList; //手术日期

	public List<String> getSsrqList() {
		return ssrqList;
	}

	public void setSsrqList(List<String> ssrqList) {
		this.ssrqList = ssrqList;
	}
}
