package com.zebone.nhis.webservice.sd.vo;

import java.util.List;

import com.zebone.nhis.common.module.cn.ipdw.CnLabApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOrdHerb;
import com.zebone.nhis.common.module.cn.ipdw.CnPrescription;
import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;

public class OrderInfo {

	private List<BlOpDtVo> blOpDtList;
	private List<CnOrderVo> cnOrderList;
	private List<CnRisApply> cnRisApplyList;
	private List<CnLabApply> cnLabApplyList;
	private List<CnPrescription> cnPrescriptionList;
	private List<CnOrdHerb> cnOrdHerbList;
	private List<PvDiagVo> pvDiag;

	public List<BlOpDtVo> getBlOpDtList() {
		return blOpDtList;
	}

	public void setBlOpDtList(List<BlOpDtVo> blOpDtList) {
		this.blOpDtList = blOpDtList;
	}

	public List<CnOrderVo> getCnOrderList() {
		return cnOrderList;
	}

	public void setCnOrderList(List<CnOrderVo> cnOrderList) {
		this.cnOrderList = cnOrderList;
	}

	public List<CnRisApply> getCnRisApplyList() {
		return cnRisApplyList;
	}

	public void setCnRisApplyList(List<CnRisApply> cnRisApplyList) {
		this.cnRisApplyList = cnRisApplyList;
	}

	public List<CnLabApply> getCnLabApplyList() {
		return cnLabApplyList;
	}

	public void setCnLabApplyList(List<CnLabApply> cnLabApplyList) {
		this.cnLabApplyList = cnLabApplyList;
	}

	public List<CnPrescription> getCnPrescriptionList() {
		return cnPrescriptionList;
	}

	public void setCnPrescriptionList(List<CnPrescription> cnPrescriptionList) {
		this.cnPrescriptionList = cnPrescriptionList;
	}

	public List<CnOrdHerb> getCnOrdHerbList() {
		return cnOrdHerbList;
	}

	public void setCnOrdHerbList(List<CnOrdHerb> cnOrdHerbList) {
		this.cnOrdHerbList = cnOrdHerbList;
	}

	public List<PvDiagVo> getPvDiag() {
		return pvDiag;
	}

	public void setPvDiag(List<PvDiagVo> pvDiag) {
		this.pvDiag = pvDiag;
	}

}
