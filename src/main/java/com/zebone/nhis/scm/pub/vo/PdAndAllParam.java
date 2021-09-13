package com.zebone.nhis.scm.pub.vo;

import com.zebone.nhis.base.pub.vo.CgdivItemVo;
import com.zebone.nhis.common.module.base.bd.code.InsGzgyDisease;
import com.zebone.nhis.common.module.base.bd.code.InsGzgyDiseaseOrd;
import com.zebone.nhis.common.module.base.bd.srv.BdItemHp;
import com.zebone.nhis.common.module.scm.pub.*;

import java.util.ArrayList;
import java.util.List;

public class PdAndAllParam {

	/**
	 * 药品
	 */
	private BdPd pd = new BdPd();
	/**
	 * 包装
	 */
	private List<BdPdConvert> pdConvertList = new ArrayList<BdPdConvert>();
	/**
	 * 别名
	 */
	private List<BdPdAs> pdAsList = new ArrayList<BdPdAs>();
	/**
	 * 附加属性
	 */
	private List<BdPdAtt> pdAttList = new ArrayList<BdPdAtt>();
	/**
	 * 关联的溶媒(新增、修改)
	 */
	private List<BdPdMens> pdMensList = new ArrayList<BdPdMens>();
	/**
	 * 关联的溶媒(删除)
	 */
	private List<BdPdMens> delPdMensList = new ArrayList<BdPdMens>();
	/**
	 * 要取消默认标志的溶媒列表
	 */
	private List<BdPdMens> cancelDefPdMensList = new ArrayList<BdPdMens>();
	
	/**
	 * 分配
	 */
	private List<BdPdStore> pdStoreList = new ArrayList<BdPdStore>();
	/**医保类型*/
	private List<BdItemHp> pdItemHpList=new ArrayList<BdItemHp>();
	
	//门慢和门特
	private List<InsGzgyDiseaseOrd> insGzgyDiseaseOrdList = new ArrayList<InsGzgyDiseaseOrd>();
	
	//门慢和门特
	private List<InsGzgyDisease> insGzgyDiseaseList = new ArrayList<>();

	/**
	 * 平台编码集合（中山人医使用）
	 */
	private List<BdPdOutcode> outCodeList;



	/**
	 * 药品集合
	 */
	private List<BdPd> bdPdList = new ArrayList<>();

		
	public List<InsGzgyDisease> getInsGzgyDiseaseList() {
		return insGzgyDiseaseList;
	}

	public void setInsGzgyDiseaseList(List<InsGzgyDisease> insGzgyDiseaseList) {
		this.insGzgyDiseaseList = insGzgyDiseaseList;
	}

	private List<CgdivItemVo> ItemCgDivs = new ArrayList<CgdivItemVo>();
	
	
	public List<InsGzgyDiseaseOrd> getInsGzgyDiseaseOrdList() {
		return insGzgyDiseaseOrdList;
	}

	public void setInsGzgyDiseaseOrdList(
			List<InsGzgyDiseaseOrd> insGzgyDiseaseOrdList) {
		this.insGzgyDiseaseOrdList = insGzgyDiseaseOrdList;
	}

	public List<CgdivItemVo> getItemCgDivs() {
		return ItemCgDivs;
	}

	public void setPdCgdivList(List<CgdivItemVo> ItemCgDivs) {
		this.ItemCgDivs = ItemCgDivs;
	}

	public List<BdPdMens> getDelPdMensList() {
		return delPdMensList;
	}

	public void setDelPdMensList(List<BdPdMens> delPdMensList) {
		this.delPdMensList = delPdMensList;
	}

	
	
	
	public List<BdPdMens> getCancelDefPdMensList() {
		return cancelDefPdMensList;
	}

	public void setCancelDefPdMensList(List<BdPdMens> cancelDefPdMensList) {
		this.cancelDefPdMensList = cancelDefPdMensList;
	}

	public List<BdPdMens> getPdMensList() {
		return pdMensList;
	}
	
	public void setPdMensList(List<BdPdMens> pdMensList) {
		this.pdMensList = pdMensList;
	}
	
	
	public BdPd getPd() {
		return pd;
	}
	public void setPd(BdPd pd) {
		this.pd = pd;
	}
	public List<BdPdConvert> getPdConvertList() {
		return pdConvertList;
	}
	public void setPdConvertList(List<BdPdConvert> pdConvertList) {
		this.pdConvertList = pdConvertList;
	}
	public List<BdPdAs> getPdAsList() {
		return pdAsList;
	}
	public void setPdAsList(List<BdPdAs> pdAsList) {
		this.pdAsList = pdAsList;
	}
	public List<BdPdAtt> getPdAttList() {
		return pdAttList;
	}
	public void setPdAttList(List<BdPdAtt> pdAttList) {
		this.pdAttList = pdAttList;
	}
	public List<BdPdStore> getPdStoreList() {
		return pdStoreList;
	}
	public void setPdStoreList(List<BdPdStore> pdStoreList) {
		this.pdStoreList = pdStoreList;
	}

	public List<BdItemHp> getPdItemHpList() {
		return pdItemHpList;
	}

	public void setPdItemHpList(List<BdItemHp> pdItemHpList) {
		this.pdItemHpList = pdItemHpList;
	}

	public List<BdPd> getBdPdList() {
		return bdPdList;
	}

	public void setBdPdList(List<BdPd> bdPdList) {
		this.bdPdList = bdPdList;
	}

	public List<BdPdOutcode> getOutCodeList() {
		return outCodeList;
	}

	public void setOutCodeList(List<BdPdOutcode> outCodeList) {
		this.outCodeList = outCodeList;
	}
}
