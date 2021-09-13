package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: INS_CLEARING_DETAILED_QG
 *
 * @since 2020-10-26 10:42:10
 */
@Table(value="INS_CLEARBRANCH_DETAILED_QG")
public class InsZsbaClearbranchDetailedQg extends BaseModule{

	@PK
	@Field(value="PK_INSCLEDETQG",id=KeyId.UUID)
    private String pkInscledetqg;
	
	@Field(value="PK_INSCLEARBRANCHQG")
    private String pkInsclearbranchqg;
	
	@Field(value="SEQNO")
    private String seqno;
	
	@Field(value="MDTRTAREA")
    private String mdtrtarea;
	
	@Field(value="MEDINS_NO")
    private String medinsNo;
	
	@Field(value="CERTNO")
    private String certno;
	
	@Field(value="MDTRT_ID")
    private String mdtrtId;
	
	@Field(value="MDTRT_SETL_TIME")
    private String mdtrtSetlTime;
	
	@Field(value="SETL_SN")
    private String setlSn;
	
	@Field(value="FULAMT_ADVPAY_FLAG")
    private String fulamtAdvpayFlag;
	
	@Field(value="MEDFEE_SUMAMT")
    private String medfeeSumamt;
	
	@Field(value="OPTINS_PAY_SUMAMT")
    private String optinsPaySumamt;

	public String getPkInscledetqg() {
		return pkInscledetqg;
	}

	public void setPkInscledetqg(String pkInscledetqg) {
		this.pkInscledetqg = pkInscledetqg;
	}

	public String getPkInsclearbranchqg() {
		return pkInsclearbranchqg;
	}

	public void setPkInsclearbranchqg(String pkInsclearbranchqg) {
		this.pkInsclearbranchqg = pkInsclearbranchqg;
	}

	public String getSeqno() {
		return seqno;
	}

	public void setSeqno(String seqno) {
		this.seqno = seqno;
	}

	public String getMdtrtarea() {
		return mdtrtarea;
	}

	public void setMdtrtarea(String mdtrtarea) {
		this.mdtrtarea = mdtrtarea;
	}

	public String getMedinsNo() {
		return medinsNo;
	}

	public void setMedinsNo(String medinsNo) {
		this.medinsNo = medinsNo;
	}

	public String getCertno() {
		return certno;
	}

	public void setCertno(String certno) {
		this.certno = certno;
	}

	public String getMdtrtId() {
		return mdtrtId;
	}

	public void setMdtrtId(String mdtrtId) {
		this.mdtrtId = mdtrtId;
	}

	public String getMdtrtSetlTime() {
		return mdtrtSetlTime;
	}

	public void setMdtrtSetlTime(String mdtrtSetlTime) {
		this.mdtrtSetlTime = mdtrtSetlTime;
	}

	public String getSetlSn() {
		return setlSn;
	}

	public void setSetlSn(String setlSn) {
		this.setlSn = setlSn;
	}

	public String getFulamtAdvpayFlag() {
		return fulamtAdvpayFlag;
	}

	public void setFulamtAdvpayFlag(String fulamtAdvpayFlag) {
		this.fulamtAdvpayFlag = fulamtAdvpayFlag;
	}

	public String getMedfeeSumamt() {
		return medfeeSumamt;
	}

	public void setMedfeeSumamt(String medfeeSumamt) {
		this.medfeeSumamt = medfeeSumamt;
	}

	public String getOptinsPaySumamt() {
		return optinsPaySumamt;
	}

	public void setOptinsPaySumamt(String optinsPaySumamt) {
		this.optinsPaySumamt = optinsPaySumamt;
	}
	
	
	
	
}
