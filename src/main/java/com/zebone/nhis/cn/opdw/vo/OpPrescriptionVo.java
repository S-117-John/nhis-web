package com.zebone.nhis.cn.opdw.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.bl.opcg.vo.OpCgCnOrder;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnPrescription;


public class OpPrescriptionVo {
	private List<CnPrescription> cnPres = new ArrayList<CnPrescription>();
	private List<OpCgCnOrder> cnOrder = new ArrayList<OpCgCnOrder>();
	private List<BlOpDt> cnOrderCharge = new ArrayList<BlOpDt>();
	private String euType;
	public void setCnOrderCharge(List<BlOpDt> cnOrderCharge) {
		this.cnOrderCharge = cnOrderCharge;
	}
	public List<BlOpDt> getCnOrderCharge() {
		return cnOrderCharge;
	}
	public List<CnPrescription> getCnPres() {
		return cnPres;
	}
	public void setCnPres(List<CnPrescription> cnPres) {
		this.cnPres = cnPres;
	}
	public List<OpCgCnOrder> getCnOrder() {
		return cnOrder;
	}
	public void setCnOrder(List<OpCgCnOrder> cnOrder) {
		this.cnOrder = cnOrder;
	}
	public String getEuType() {
		return euType;
	}
	public void setEuType(String euType) {
		this.euType = euType;
	}
}
