package com.zebone.nhis.common.module.ex.nis.ns;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;


/**
 * Table: EX_RIS_OCC - ex_ris_occ 
 *  医疗结果-检查项目
 * @since 2016-10-28 10:52:35
 */
@Table(value="EX_RIS_OCC")
public class ExRisOcc extends BaseModule  {

	@PK
	@Field(value="PK_RISOCC",id=KeyId.UUID)
    private String pkRisocc;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="CODE_APPLY")
    private String codeApply;

	@Field(value="PK_ORG_OCC")
    private String pkOrgOcc;

	@Field(value="PK_DEPT_OCC")
    private String pkDeptOcc;

	@Field(value="PK_EMP_OCC")
    private String pkEmpOcc;

	@Field(value="NAME_EMP_OCC")
    private String nameEmpOcc;

	@Field(value="DATE_OCC")
    private Date dateOcc;

	@Field(value="DATE_RPT")
    private Date dateRpt;

	@Field(value="FLAG_CHK")
    private String flagChk;

	@Field(value="DATE_CHK")
    private Date dateChk;

	@Field(value="PK_EMP_CHK")
    private String pkEmpChk;

	@Field(value="NAME_EMP_CHK")
    private String nameEmpChk;

	@Field(value="PK_ORD")
    private String pkOrd;

	@Field(value="RESULT_OBJ")
    private String resultObj;

	@Field(value="RESULT_SUB")
    private String resultSub;

	@Field(value="EU_RESULT")
    private String euResult;

	@Field(value="PK_EMR")
    private String pkEmr;

	@Field(value="ADDR_IMG")
    private String addrImg;

	@Field(value="CODE_RPT")
    private String codeRpt;
	
	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	@Field(value="NAME_ORD")
	private String nameOrd;
	
	@Field(value="NOTE")
	private String note;
	
	@Field(value="examway")
	private String examway;

    @Field(value="PK_MSP")
	private String pkMsp;

    public String getNameOrd() {
		return nameOrd;
	}
	public void setNameOrd(String nameOrd) {
		this.nameOrd = nameOrd;
	}
	public String getPkRisocc(){
        return this.pkRisocc;
    }
    public void setPkRisocc(String pkRisocc){
        this.pkRisocc = pkRisocc;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkCnord(){
        return this.pkCnord;
    }
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
    }

    public String getCodeApply(){
        return this.codeApply;
    }
    public void setCodeApply(String codeApply){
        this.codeApply = codeApply;
    }

    public String getPkOrgOcc(){
        return this.pkOrgOcc;
    }
    public void setPkOrgOcc(String pkOrgOcc){
        this.pkOrgOcc = pkOrgOcc;
    }

    public String getPkDeptOcc(){
        return this.pkDeptOcc;
    }
    public void setPkDeptOcc(String pkDeptOcc){
        this.pkDeptOcc = pkDeptOcc;
    }

    public String getPkEmpOcc(){
        return this.pkEmpOcc;
    }
    public void setPkEmpOcc(String pkEmpOcc){
        this.pkEmpOcc = pkEmpOcc;
    }

    public String getNameEmpOcc(){
        return this.nameEmpOcc;
    }
    public void setNameEmpOcc(String nameEmpOcc){
        this.nameEmpOcc = nameEmpOcc;
    }

    public Date getDateOcc(){
        return this.dateOcc;
    }
    public void setDateOcc(Date dateOcc){
        this.dateOcc = dateOcc;
    }

    public Date getDateRpt(){
        return this.dateRpt;
    }
    public void setDateRpt(Date dateRpt){
        this.dateRpt = dateRpt;
    }

    public String getFlagChk(){
        return this.flagChk;
    }
    public void setFlagChk(String flagChk){
        this.flagChk = flagChk;
    }

    public Date getDateChk(){
        return this.dateChk;
    }
    public void setDateChk(Date dateChk){
        this.dateChk = dateChk;
    }

    public String getPkEmpChk(){
        return this.pkEmpChk;
    }
    public void setPkEmpChk(String pkEmpChk){
        this.pkEmpChk = pkEmpChk;
    }

    public String getNameEmpChk(){
        return this.nameEmpChk;
    }
    public void setNameEmpChk(String nameEmpChk){
        this.nameEmpChk = nameEmpChk;
    }

    public String getPkOrd(){
        return this.pkOrd;
    }
    public void setPkOrd(String pkOrd){
        this.pkOrd = pkOrd;
    }

    public String getResultObj(){
        return this.resultObj;
    }
    public void setResultObj(String resultObj){
        this.resultObj = resultObj;
    }

    public String getResultSub(){
        return this.resultSub;
    }
    public void setResultSub(String resultSub){
        this.resultSub = resultSub;
    }

    public String getEuResult() {
		return euResult;
	}
	public void setEuResult(String euResult) {
		this.euResult = euResult;
	}
	public String getPkEmr(){
        return this.pkEmr;
    }
    public void setPkEmr(String pkEmr){
        this.pkEmr = pkEmr;
    }

    public String getAddrImg(){
        return this.addrImg;
    }
    public void setAddrImg(String addrImg){
        this.addrImg = addrImg;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getExamway() {
		return examway;
	}
	public void setExamway(String examway) {
		this.examway = examway;
	}
	public String getCodeRpt() {
		return codeRpt;
	}
	public void setCodeRpt(String codeRpt) {
		this.codeRpt = codeRpt;
	}
	public String getPkMsp() {
		return pkMsp;
	}
	public void setPkMsp(String pkMsp) {
		this.pkMsp = pkMsp;
	}
	
}