package com.zebone.nhis.compay.pub.syx.vo;

import java.util.List;

public class InsSyncVo {

	private List<ViewItemOut> addViewItemListOut;
	private List<ViewMediOut> addViewMediListOut;
	private List<ViewMatchOut> addViewMatchListOut;
	private List<ViewItem> addViewItemList;
	private List<ViewMedi> addViewMediList;
	private List<ViewMatch> addViewMatchList;
	private String euHpDictType;
	private String datasourcename;
	public List<ViewItemOut> getAddViewItemListOut() {
		return addViewItemListOut;
	}
	public void setAddViewItemListOut(List<ViewItemOut> addViewItemListOut) {
		this.addViewItemListOut = addViewItemListOut;
	}
	public List<ViewMediOut> getAddViewMediListOut() {
		return addViewMediListOut;
	}
	public void setAddViewMediListOut(List<ViewMediOut> addViewMediListOut) {
		this.addViewMediListOut = addViewMediListOut;
	}
	public List<ViewMatchOut> getAddViewMatchListOut() {
		return addViewMatchListOut;
	}
	public void setAddViewMatchListOut(List<ViewMatchOut> addViewMatchListOut) {
		this.addViewMatchListOut = addViewMatchListOut;
	}
	public List<ViewItem> getAddViewItemList() {
		return addViewItemList;
	}
	public void setAddViewItemList(List<ViewItem> addViewItemList) {
		this.addViewItemList = addViewItemList;
	}
	public List<ViewMedi> getAddViewMediList() {
		return addViewMediList;
	}
	public void setAddViewMediList(List<ViewMedi> addViewMediList) {
		this.addViewMediList = addViewMediList;
	}
	public List<ViewMatch> getAddViewMatchList() {
		return addViewMatchList;
	}
	public void setAddViewMatchList(List<ViewMatch> addViewMatchList) {
		this.addViewMatchList = addViewMatchList;
	}
	public String getEuHpDictType() {
		return euHpDictType;
	}
	public void setEuHpDictType(String euHpDictType) {
		this.euHpDictType = euHpDictType;
	}
	public String getDatasourcename() {
		return datasourcename;
	}
	public void setDatasourcename(String datasourcename) {
		this.datasourcename = datasourcename;
	}
	
}
