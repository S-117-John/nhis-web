package com.zebone.nhis.cn.ipdw.vo;

import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.bl.BlIpDt;

public class CnBlIpDtVo extends BlIpDt {
	private String code;

	private String unitCode;

	private String euHptype;
	
	private Integer chargeId;
	
	private Integer orderNo;
	
	private String codePv;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getEuHptype() {
		return euHptype;
	}

	public void setEuHptype(String euHptype) {
		this.euHptype = euHptype;
	}

	public Integer getChargeId() {
		return chargeId;
	}

	public void setChargeId(Integer chargeId) {
		this.chargeId = chargeId;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}
	
}
