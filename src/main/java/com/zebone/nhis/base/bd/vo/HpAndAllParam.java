package com.zebone.nhis.base.bd.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.base.bd.code.BdDictattr;
import com.zebone.nhis.common.module.base.bd.price.*;

public class HpAndAllParam {

	/**
	 * 医保计划
	 */
	private BdHp hpInfo = new BdHp();
	
	/**
	 * 总额分摊定义
	 */
	private BdHpTotaldiv totaldivInfo = new BdHpTotaldiv();
	
	/**
	 * 服务分类分摊定义
	 */
	List<BdHpItemcatediv> itemcatedivList = new ArrayList<BdHpItemcatediv>();
	
	/**
	 * 项目分摊定义
	 */
	List<BdHpItemdiv> itemdivList = new ArrayList<BdHpItemdiv>();
	
	/**
	 * 支付段分摊
	 */
	List<BdHpSecdiv> secdivList = new ArrayList<BdHpSecdiv>();
	
	/**
	 * 单病种限价定义
	 */
	List<BdHpDiagdiv> diagdivList = new ArrayList<BdHpDiagdiv>();
	
	/**
	 * 使用机构
	 */
	List<BdHpOrg> HpOrgList = new ArrayList<BdHpOrg>();
	/**
	 * 医保计划下的策略配置
	 */
	List<BdHpDivconfig> hpDivConfigList = new ArrayList<BdHpDivconfig>();
	/**
	 * 医保计划下的策略配置--删除用
	 */
	List<BdHpDivconfig> delHpDivConfigList = new ArrayList<BdHpDivconfig>();
	
	/**
	 * 使用科室
	 */
	List<BdHpDept> HpDeptList = new ArrayList<BdHpDept>();
	

	public List<BdHpDivconfig> getHpDivConfigList() {
		return hpDivConfigList;
	}

	public void setHpDivConfigList(List<BdHpDivconfig> hpDivConfigList) {
		this.hpDivConfigList = hpDivConfigList;
	}

	public List<BdHpDivconfig> getDelHpDivConfigList() {
		return delHpDivConfigList;
	}

	public void setDelHpDivConfigList(List<BdHpDivconfig> delHpDivConfigList) {
		this.delHpDivConfigList = delHpDivConfigList;
	}

	public List<BdHpOrg> getHpOrgList() {
		return HpOrgList;
	}

	public void setHpOrgList(List<BdHpOrg> hpOrgList) {
		HpOrgList = hpOrgList;
	}

	public List<BdDictattr> getHpDictAttrList() {
		return HpDictAttrList;
	}

	public void setHpDictAttrList(List<BdDictattr> hpDictAttrList) {
		HpDictAttrList = hpDictAttrList;
	}

	/**
	 * 扩展属性
	 */
	List<BdDictattr> HpDictAttrList = new ArrayList<BdDictattr>();
	

	public BdHp getHpInfo() {
		return hpInfo;
	}

	public void setHpInfo(BdHp hpInfo) {
		this.hpInfo = hpInfo;
	}

	public BdHpTotaldiv getTotaldivInfo() {
		return totaldivInfo;
	}

	public void setTotaldivInfo(BdHpTotaldiv totaldivInfo) {
		this.totaldivInfo = totaldivInfo;
	}

	public List<BdHpItemcatediv> getItemcatedivList() {
		return itemcatedivList;
	}

	public void setItemcatedivList(List<BdHpItemcatediv> itemcatedivList) {
		this.itemcatedivList = itemcatedivList;
	}

	public List<BdHpItemdiv> getItemdivList() {
		return itemdivList;
	}

	public void setItemdivList(List<BdHpItemdiv> itemdivList) {
		this.itemdivList = itemdivList;
	}

	public List<BdHpSecdiv> getSecdivList() {
		return secdivList;
	}

	public void setSecdivList(List<BdHpSecdiv> secdivList) {
		this.secdivList = secdivList;
	}

	public List<BdHpDiagdiv> getDiagdivList() {
		return diagdivList;
	}

	public void setDiagdivList(List<BdHpDiagdiv> diagdivList) {
		this.diagdivList = diagdivList;
	}

	public List<BdHpDept> getHpDeptList() {
		return HpDeptList;
	}

	public void setHpDeptList(List<BdHpDept> hpDeptList) {
		HpDeptList = hpDeptList;
	}
	
	
}
