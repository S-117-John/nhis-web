package com.zebone.nhis.cn.opdw.vo;

import java.util.List;

import com.zebone.nhis.cn.ipdw.vo.BloodApplyVO;
import com.zebone.nhis.cn.ipdw.vo.CnConsultApplyVO;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnLabApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnPrescription;
import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;

/**
 * 医嘱与门诊收费明细
 * 
 * @author yuanxinan
 *
 */
public class CnOrderBlOpDtVo extends CnOrder {

	private List<BlOpDt> blOpDt;
	
	private CnRisApply cnRisApply;
	
	private CnLabApply cnLabApply;

	private CnPrescription cnPrescription;
	
	private CnConsultApplyVO cnConsultApply;
	
	private BloodApplyVO bloodApply;
	

	public List<BlOpDt> getBlOpDt() {
		return blOpDt;
	}

	public void setBlOpDt(List<BlOpDt> blOpDt) {
		this.blOpDt = blOpDt;
	}

	public CnRisApply getCnRisApply() {
		return cnRisApply;
	}

	public void setCnRisApply(CnRisApply cnRisApply) {
		this.cnRisApply = cnRisApply;
	}

	public CnLabApply getCnLabApply() {
		return cnLabApply;
	}

	public void setCnLabApply(CnLabApply cnLabApply) {
		this.cnLabApply = cnLabApply;
	}

	public CnPrescription getCnPrescription() {
		return cnPrescription;
	}

	public void setCnPrescription(CnPrescription cnPrescription) {
		this.cnPrescription = cnPrescription;
	}

	public CnConsultApplyVO getCnConsultApply() {
		return cnConsultApply;
	}

	public void setCnConsultApply(CnConsultApplyVO cnConsultApply) {
		this.cnConsultApply = cnConsultApply;
	}

	public BloodApplyVO getBloodApply() {
		return bloodApply;
	}

	public void setBloodApply(BloodApplyVO bloodApply) {
		this.bloodApply = bloodApply;
	}

}
