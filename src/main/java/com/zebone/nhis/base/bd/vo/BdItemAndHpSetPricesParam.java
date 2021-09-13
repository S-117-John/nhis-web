package com.zebone.nhis.base.bd.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.base.pub.vo.CgdivItemVo;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.base.bd.srv.BdItemHp;
import com.zebone.nhis.common.module.base.bd.srv.BdItemPrice;
import com.zebone.nhis.common.module.base.bd.srv.BdItemSet;
import com.zebone.nhis.common.module.base.bd.srv.BdItemSp;

public class BdItemAndHpSetPricesParam {

	private String isAll = "";
	/**
	 * 收费项目
	 */
	private BdItem item = new BdItem();
	
	/**
	 * 价格定义
	 */
	private List<BdItemPrice> itemPrices = new ArrayList<BdItemPrice>();
	/**
	 * 价格定义--要删除的主键
	 */
	private List<BdItemPrice> itemPricesDelPk = new ArrayList<BdItemPrice>();
	
	/**
	 * 医保类型
	 */
	private List<BdItemHp> itemHps = new ArrayList<BdItemHp>();
	
	/**
	 * 组套
	 */
	private List<BdItemSet> itemSets = new ArrayList<BdItemSet>();
	
	/**
	 * 扩展属性
	 */
	private List<BdItemAttrVo> itemAttr = new ArrayList<BdItemAttrVo>();

	/**
	 * 特殊定价
	 */
	private List<BdItemSp> itemSps = new ArrayList<BdItemSp>();
	
	/**
	 * 记费策略
	 */
	private List<CgdivItemVo> itemCgDivs = new ArrayList<CgdivItemVo>();
	
	
	
	
	public List<BdItemPrice> getItemPricesDelPk() {
		return itemPricesDelPk;
	}

	public void setItemPricesDelPk(List<BdItemPrice> itemPricesDelPk) {
		this.itemPricesDelPk = itemPricesDelPk;
	}

	public List<BdItemSp> getItemSps() {
		return itemSps;
	}

	public void setItemSps(List<BdItemSp> itemSps) {
		this.itemSps = itemSps;
	}



	public List<CgdivItemVo> getItemCgDivs() {
		return itemCgDivs;
	}

	public void setItemCgDivs(List<CgdivItemVo> itemCgDivs) {
		this.itemCgDivs = itemCgDivs;
	}

	public List<BdItemAttrVo> getItemAttr() {
		return itemAttr;
	}

	public void setItemAttr(List<BdItemAttrVo> itemAttr) {
		this.itemAttr = itemAttr;
	}

	public String getIsAll() {
		return isAll;
	}

	public void setIsAll(String isAll) {
		this.isAll = isAll;
	}

	public BdItem getItem() {
		return item;
	}

	public void setItem(BdItem item) {
		this.item = item;
	}

	public List<BdItemPrice> getItemPrices() {
		return itemPrices;
	}

	public void setItemPrices(List<BdItemPrice> itemPrices) {
		this.itemPrices = itemPrices;
	}

	public List<BdItemHp> getItemHps() {
		return itemHps;
	}

	public void setItemHps(List<BdItemHp> itemHps) {
		this.itemHps = itemHps;
	}

	public List<BdItemSet> getItemSets() {
		return itemSets;
	}

	public void setItemSets(List<BdItemSet> itemSets) {
		this.itemSets = itemSets;
	}
}
