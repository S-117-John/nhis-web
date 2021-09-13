package com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo;

import java.util.List;

/**
 * 保存医保结算数据参数
 * @author zrj
 *
 */
public class YbSettlementData {

	private String status;//医保状态
	private InsZsBaYbSt insSt;  //外部医保-中山医保住院结算
	private List<InsZsBaYbStItemcate> insStItemcateList; //外部医保-中山医保结算返回项目分类
	private String jsywh;//结算业务号，重新结算时需要传旧的结算业务号
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public InsZsBaYbSt getInsSt() {
		return insSt;
	}
	public void setInsSt(InsZsBaYbSt insSt) {
		this.insSt = insSt;
	}
	public List<InsZsBaYbStItemcate> getInsStItemcateList() {
		return insStItemcateList;
	}
	public void setInsStItemcateList(List<InsZsBaYbStItemcate> insStItemcateList) {
		this.insStItemcateList = insStItemcateList;
	}
	public String getJsywh() {
		return jsywh;
	}
	public void setJsywh(String jsywh) {
		this.jsywh = jsywh;
	}
}
