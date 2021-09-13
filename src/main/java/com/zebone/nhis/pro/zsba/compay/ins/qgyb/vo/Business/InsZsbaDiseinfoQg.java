package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: ins_pv_qg
 *
 * @since 2020-10-26 10:42:10
 */
@Table(value="INS_DISEINFO_QG")
public class InsZsbaDiseinfoQg extends BaseModule{

	@PK
	@Field(value="PK_INSDISEINFOQG",id=KeyId.UUID)
    private String pkInsdiseinfoqg;
	
	@Field(value="PK_INSPVQg")
    private String pkInspvqg;
	
	@Field(value="MDTRT_ID")
    private String mdtrtId;
	
	@Field(value="PSN_NO")
    private String psnNo;
	
	@Field(value="DIAG_TYPE")
    private String diagType;
	
	@Field(value="MAINDIAG_FLAG")
    private String maindiagFlag;
	
	@Field(value="DIAG_SRT_NO")
    private String diagSrtNo;
	
	@Field(value="INOUT_DIAG_TYPE")
    private String inoutDiagType;
	
	@Field(value="DIAG_CODE")
    private String diagCode;
	
	@Field(value="DIAG_NAME")
    private String diagName;
	
	@Field(value="ADM_COND")
    private String admCond;
	
	@Field(value="DIAG_DEPT")
    private String diagDept;
	
	@Field(value="DISE_DOR_NO")
    private String diseDorNo;
	
	@Field(value="DISE_DOR_NAME")
    private String diseDorName;
	
	@Field(value="DIAG_TIME")
    private String diagTime;

	public String getPkInsdiseinfoqg() {
		return pkInsdiseinfoqg;
	}

	public void setPkInsdiseinfoqg(String pkInsdiseinfoqg) {
		this.pkInsdiseinfoqg = pkInsdiseinfoqg;
	}

	public String getPkInspvqg() {
		return pkInspvqg;
	}

	public void setPkInspvqg(String pkInspvqg) {
		this.pkInspvqg = pkInspvqg;
	}

	public String getMdtrtId() {
		return mdtrtId;
	}

	public void setMdtrtId(String mdtrtId) {
		this.mdtrtId = mdtrtId;
	}

	public String getPsnNo() {
		return psnNo;
	}

	public void setPsnNo(String psnNo) {
		this.psnNo = psnNo;
	}

	public String getDiagType() {
		return diagType;
	}

	public void setDiagType(String diagType) {
		this.diagType = diagType;
	}

	public String getMaindiagFlag() {
		return maindiagFlag;
	}

	public void setMaindiagFlag(String maindiagFlag) {
		this.maindiagFlag = maindiagFlag;
	}

	public String getDiagSrtNo() {
		return diagSrtNo;
	}

	public void setDiagSrtNo(String diagSrtNo) {
		this.diagSrtNo = diagSrtNo;
	}

	public String getInoutDiagType() {
		return inoutDiagType;
	}

	public void setInoutDiagType(String inoutDiagType) {
		this.inoutDiagType = inoutDiagType;
	}

	public String getDiagCode() {
		return diagCode;
	}

	public void setDiagCode(String diagCode) {
		this.diagCode = diagCode;
	}

	public String getDiagName() {
		return diagName;
	}

	public void setDiagName(String diagName) {
		this.diagName = diagName;
	}

	public String getAdmCond() {
		return admCond;
	}

	public void setAdmCond(String admCond) {
		this.admCond = admCond;
	}

	public String getDiagDept() {
		return diagDept;
	}

	public void setDiagDept(String diagDept) {
		this.diagDept = diagDept;
	}

	public String getDiseDorNo() {
		return diseDorNo;
	}

	public void setDiseDorNo(String diseDorNo) {
		this.diseDorNo = diseDorNo;
	}

	public String getDiseDorName() {
		return diseDorName;
	}

	public void setDiseDorName(String diseDorName) {
		this.diseDorName = diseDorName;
	}

	public String getDiagTime() {
		return diagTime;
	}

	public void setDiagTime(String diagTime) {
		this.diagTime = diagTime;
	}
	
	
}
