package com.zebone.nhis.base.bd.vo;

import java.util.List;

import com.zebone.nhis.common.module.base.bd.price.BdHpStdiv;
import com.zebone.nhis.common.module.base.bd.price.BdHpStdivDt;

public class BdHpStdivVo extends BdHpStdiv{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String tlnameDiv;

	private BdHpStdiv bdHpStdiv;
	
	private List<BdHpStdivDt> bdHpStdivDtList ;
	
	private List<BdHpStdivDt> delBdHpStdivDtList;
	
	public String getTlnameDiv() {
		return tlnameDiv;
	}

	public void setTlnameDiv(String tlnameDiv) {
		this.tlnameDiv = tlnameDiv;
	}

	public BdHpStdiv getBdHpStdiv() {
		return bdHpStdiv;
	}

	public void setBdHpStdiv(BdHpStdiv bdHpStdiv) {
		this.bdHpStdiv = bdHpStdiv;
	}

	public List<BdHpStdivDt> getBdHpStdivDtList() {
		return bdHpStdivDtList;
	}

	public void setBdHpStdivDtList(List<BdHpStdivDt> bdHpStdivDtList) {
		this.bdHpStdivDtList = bdHpStdivDtList;
	}

	public List<BdHpStdivDt> getDelBdHpStdivDtList() {
		return delBdHpStdivDtList;
	}

	public void setDelBdHpStdivDtList(List<BdHpStdivDt> delBdHpStdivDtList) {
		this.delBdHpStdivDtList = delBdHpStdivDtList;
	}

}
