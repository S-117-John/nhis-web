package com.zebone.nhis.base.bd.vo;

import com.zebone.nhis.common.module.base.bd.code.BdDictattr;
import com.zebone.nhis.common.module.base.bd.srv.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BdOrdAndAllParam {

	/**
	 * 登录用户为机构时打开的菜单是否为全局（机构用户用户集团用户权限）
	 */
	private String isAll;
	/**
	 * 医嘱项目
	 */
	private BdOrd bdOrd = new BdOrd();
	
	/**
	 * 别名
	 */
	private List<BdOrdAlias> bdOrdAliases = new ArrayList<BdOrdAlias>();
	
	/**
	 * 使用机构
	 */
	private List<BdOrdOrg> bdOrdOrgs = new ArrayList<BdOrdOrg>();
	
	/**
	 * 收费项目
	 */
	private List<BdOrdItem> bdOrdItems = new ArrayList<BdOrdItem>();
	
	/**
	 * 检验属性
	 */
	private BdOrdLab bdOrdLab = new BdOrdLab();
	
	/**
	 * 检查属性
	 */
	private BdOrdRis bdOrdRis = new BdOrdRis();
	
	/**
	 * 执行科室
	 */
	private List<BdOrdDept> bdOrdDepts = new ArrayList<BdOrdDept>();
	
	/**
	 * 医疗记录
	 */
	private List<BdOrdEmr> bdOrdEmrs = new ArrayList<BdOrdEmr>();
	
	/**
	 * 附加属性
	 */
	private List<Map<String,Object>>  bdOrdAttrList= new ArrayList<Map<String,Object>>();
	private List<BdDictattr>  bdOrdAttrListForSave= new ArrayList<BdDictattr>();
	
	private List<BdOrdLabCol> labColList = new ArrayList<BdOrdLabCol>();

	/**
	 * 附加收费项目含code为了界面展示
	 * */
	private  List<BdOrdItemExt> bdOrdItemExts=new ArrayList<>();
	public List<BdOrdItemExt> getBdOrdItemExts() {
		return bdOrdItemExts;
	}

	public void setBdOrdItemExts(List<BdOrdItemExt> bdOrdItemExts) {
		this.bdOrdItemExts = bdOrdItemExts;
	}








	public List<BdOrdLabCol> getLabColList() {
		return labColList;
	}

	public void setLabColList(List<BdOrdLabCol> labColList) {
		this.labColList = labColList;
	}

	public List<Map<String, Object>> getBdOrdAttrList() {
		return bdOrdAttrList;
	}

	public void setBdOrdAttrList(List<Map<String, Object>> bdOrdAttrList) {
		this.bdOrdAttrList = bdOrdAttrList;
	}
	
	


	public String getIsAll() {
		return isAll;
	}

	public void setIsAll(String isAll) {
		this.isAll = isAll;
	}

	public List<BdDictattr> getBdOrdAttrListForSave() {
		return bdOrdAttrListForSave;
	}

	public void setBdOrdAttrListForSave(List<BdDictattr> bdOrdAttrListForSave) {
		this.bdOrdAttrListForSave = bdOrdAttrListForSave;
	}

	public BdOrd getBdOrd() {
		return bdOrd;
	}

	public void setBdOrd(BdOrd bdOrd) {
		this.bdOrd = bdOrd;
	}

	public List<BdOrdAlias> getBdOrdAliases() {
		return bdOrdAliases;
	}

	public void setBdOrdAliases(List<BdOrdAlias> bdOrdAliases) {
		this.bdOrdAliases = bdOrdAliases;
	}

	public List<BdOrdOrg> getBdOrdOrgs() {
		return bdOrdOrgs;
	}

	public void setBdOrdOrgs(List<BdOrdOrg> bdOrdOrgs) {
		this.bdOrdOrgs = bdOrdOrgs;
	}

	public List<BdOrdItem> getBdOrdItems() {
		return bdOrdItems;
	}

	public void setBdOrdItems(List<BdOrdItem> bdOrdItems) {
		this.bdOrdItems = bdOrdItems;
	}

	public BdOrdLab getBdOrdLab() {
		return bdOrdLab;
	}

	public void setBdOrdLab(BdOrdLab bdOrdLab) {
		this.bdOrdLab = bdOrdLab;
	}

	public BdOrdRis getBdOrdRis() {
		return bdOrdRis;
	}

	public void setBdOrdRis(BdOrdRis bdOrdRis) {
		this.bdOrdRis = bdOrdRis;
	}

	public List<BdOrdDept> getBdOrdDepts() {
		return bdOrdDepts;
	}

	public void setBdOrdDepts(List<BdOrdDept> bdOrdDepts) {
		this.bdOrdDepts = bdOrdDepts;
	}

	public List<BdOrdEmr> getBdOrdEmrs() {
		return bdOrdEmrs;
	}

	public void setBdOrdEmrs(List<BdOrdEmr> bdOrdEmrs) {
		this.bdOrdEmrs = bdOrdEmrs;
	}
	
	
}
