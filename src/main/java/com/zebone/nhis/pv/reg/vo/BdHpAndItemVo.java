package com.zebone.nhis.pv.reg.vo;

import java.util.List;

import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;

/**
 * 医保计划列表以及一次性加收费用（就诊类型为门诊）
 * 
 * @author wangpeng
 * @date 2016年9月13日
 *
 */
public class BdHpAndItemVo {
	
	/** 医保计划 */
	private List<BdHp> bdHpList;
	
	/** 就诊收费项目 */
	private List<BdItem> itemList;

	public List<BdHp> getBdHpList() {
		return bdHpList;
	}

	public void setBdHpList(List<BdHp> bdHpList) {
		this.bdHpList = bdHpList;
	}

	public List<BdItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<BdItem> itemList) {
		this.itemList = itemList;
	}

}
