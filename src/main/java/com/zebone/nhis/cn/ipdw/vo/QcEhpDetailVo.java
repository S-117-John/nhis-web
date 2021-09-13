package com.zebone.nhis.cn.ipdw.vo;

import com.zebone.nhis.common.module.cn.ipdw.QcEhpDetail;

public class QcEhpDetailVo extends QcEhpDetail{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2407567025182527301L;
	//项目类别-关联
	public String euItemcate;
	//项目数-关联
	public String itemCount;
	//评分项-关联
	public String scoreItem;
	public String getEuItemcate() {
		return euItemcate;
	}
	public void setEuItemcate(String euItemcate) {
		this.euItemcate = euItemcate;
	}
	public String getItemCount() {
		return itemCount;
	}
	public void setItemCount(String itemCount) {
		this.itemCount = itemCount;
	}
	public String getScoreItem() {
		return scoreItem;
	}
	public void setScoreItem(String scoreItem) {
		this.scoreItem = scoreItem;
	}
	
	
}
