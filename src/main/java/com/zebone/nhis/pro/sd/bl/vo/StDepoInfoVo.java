package com.zebone.nhis.pro.sd.bl.vo;

import java.util.List;

import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.common.module.bl.BlDeposit;

/**
 * 结算收款信息、收费信息
 * @author Administrator
 *
 */
public class StDepoInfoVo {
	
	private List<ItemPriceVo> stOpList;
	
	private List<ItemPriceVo> priceList;
	
	private List<BlDeposit> stDepoList;

	public List<ItemPriceVo> getStOpList() {
		return stOpList;
	}

	public void setStOpList(List<ItemPriceVo> stOpList) {
		this.stOpList = stOpList;
	}

	public List<ItemPriceVo> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<ItemPriceVo> priceList) {
		this.priceList = priceList;
	}

	public List<BlDeposit> getStDepoList() {
		return stDepoList;
	}

	public void setStDepoList(List<BlDeposit> stDepoList) {
		this.stDepoList = stDepoList;
	}
	
	
	
}
