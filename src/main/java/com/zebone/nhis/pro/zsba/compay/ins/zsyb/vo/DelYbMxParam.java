package com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo;

import java.util.List;

/**
 * 删除月结明细参数
 * @author zrj
 *
 */
public class DelYbMxParam {

	private String tjlb; //统计类别
	private List<String> jjywhList; //交接业务号List
	public String getTjlb() {
		return tjlb;
	}
	public void setTjlb(String tjlb) {
		this.tjlb = tjlb;
	}
	public List<String> getJjywhList() {
		return jjywhList;
	}
	public void setJjywhList(List<String> jjywhList) {
		this.jjywhList = jjywhList;
	}


}
