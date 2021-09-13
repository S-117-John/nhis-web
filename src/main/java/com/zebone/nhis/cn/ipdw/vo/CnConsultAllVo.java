package com.zebone.nhis.cn.ipdw.vo;

import java.util.List;

import com.zebone.nhis.common.module.cn.ipdw.CnConsultApply;
import com.zebone.nhis.common.module.cn.ipdw.CnConsultResponse;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;

public class CnConsultAllVo {
	private CnOrder cnOrder;
	
	private CnConsultApply cnConsultApply;
	
	private List<CnConsultResponse> consultResList;
	private List<CnConsultResponse> consultResListForDel;

	
	
	public List<CnConsultResponse> getConsultResListForDel() {
		return consultResListForDel;
	}

	public void setConsultResListForDel(List<CnConsultResponse> consultResListForDel) {
		this.consultResListForDel = consultResListForDel;
	}

	public CnOrder getCnOrder() {
		return cnOrder;
	}

	public void setCnOrder(CnOrder cnOrder) {
		this.cnOrder = cnOrder;
	}

	public CnConsultApply getCnConsultApply() {
		return cnConsultApply;
	}

	public void setCnConsultApply(CnConsultApply cnConsultApply) {
		this.cnConsultApply = cnConsultApply;
	}

	public List<CnConsultResponse> getConsultResList() {
		return consultResList;
	}

	public void setConsultResList(List<CnConsultResponse> consultResList) {
		this.consultResList = consultResList;
	}

	
	
}
