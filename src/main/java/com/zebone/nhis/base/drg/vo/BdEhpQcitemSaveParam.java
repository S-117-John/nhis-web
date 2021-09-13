package com.zebone.nhis.base.drg.vo;

import com.zebone.nhis.common.module.base.bd.drg.BdTermCchiDept;
import com.zebone.nhis.common.module.base.bd.mk.BdEhpQcitem;
import com.zebone.nhis.common.module.base.bd.mk.BdEhpQcitemRule;

import java.util.List;

public class BdEhpQcitemSaveParam {
	public BdEhpQcitem bdEhpQcitem;
	
	public List<BdEhpQcitemRule> addBdEhpQcitemRuleList;
	
	public List<BdEhpQcitemRule> delBdEhpQcitemRuleList;

	public BdEhpQcitem getBdEhpQcitem() {
		return bdEhpQcitem;
	}

	public void setBdEhpQcitem(BdEhpQcitem bdEhpQcitem) {
		this.bdEhpQcitem = bdEhpQcitem;
	}

	public List<BdEhpQcitemRule> getAddBdEhpQcitemRuleList() {
		return addBdEhpQcitemRuleList;
	}

	public void setAddBdEhpQcitemRuleList(List<BdEhpQcitemRule> addBdEhpQcitemRuleList) {
		this.addBdEhpQcitemRuleList = addBdEhpQcitemRuleList;
	}

	public List<BdEhpQcitemRule> getDelBdEhpQcitemRuleList() {
		return delBdEhpQcitemRuleList;
	}

	public void setDelBdEhpQcitemRuleList(List<BdEhpQcitemRule> delBdEhpQcitemRuleList) {
		this.delBdEhpQcitemRuleList = delBdEhpQcitemRuleList;
	}
}
