package com.zebone.nhis.compay.pub.vo;

/**
 * 通用医保表数据,分页查询入参
 */
public class InsCommonMedical extends InsCommonTableParam{

	private String pageSize; //"每页行数",
	private String pageIndex; //"当前页码",
	private String appendWhere; //"查询where条件"
	
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public String getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(String pageIndex) {
		this.pageIndex = pageIndex;
	}
	public String getAppendWhere() {
		return appendWhere;
	}
	public void setAppendWhere(String appendWhere) {
		this.appendWhere = appendWhere;
	}
}
