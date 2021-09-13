package com.zebone.nhis.common.module.cn.ipdw;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: QC_EHP - 病案首页质控 
 *
 * @since 2020-06-16 09:43:15
 */
@Table(value="QC_EHP")
public class QcEhp extends BaseModule  {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** PK_QCEHP - 主键 */
	@PK
	@Field(value="PK_QCEHP",id=KeyId.UUID)
    private String pkQcehp;

    /** PK_PV - 患者就诊 */
	@Field(value="PK_PV")
    private String pkPv;

    /** EU_STATUS - 质控状态 */
	@Field(value="EU_STATUS")
    private String euStatus;

    /** EU_GRADE - 质控分级 */
	@Field(value="EU_GRADE")
    private String euGrade;

    /** SCORE - 质控得分 */
	@Field(value="SCORE")
    private Double score;

    /** EU_QC_DEPT - 科室质控标志 */
	@Field(value="EU_QC_DEPT")
    private String euQcDept;

    /** EU_QC_MM - 医务质控标志 */
	@Field(value="EU_QC_MM")
    private String euQcMm;

    /** EU_QC_INS - 医保质控标志 */
	@Field(value="EU_QC_INS")
    private String euQcIns;

    /** EU_QC_PCE - 物价质控标志 */
	@Field(value="EU_QC_PCE")
    private String euQcPce;

    /** NOTE - 备注 */
	@Field(value="NOTE")
    private String note;


    public String getPkQcehp(){
        return this.pkQcehp;
    }
    public void setPkQcehp(String pkQcehp){
        this.pkQcehp = pkQcehp;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getEuGrade(){
        return this.euGrade;
    }
    public void setEuGrade(String euGrade){
        this.euGrade = euGrade;
    }

    public Double getScore(){
        return this.score;
    }
    public void setScore(Double score){
        this.score = score;
    }

    public String getEuQcDept(){
        return this.euQcDept;
    }
    public void setEuQcDept(String euQcDept){
        this.euQcDept = euQcDept;
    }

    public String getEuQcMm(){
        return this.euQcMm;
    }
    public void setEuQcMm(String euQcMm){
        this.euQcMm = euQcMm;
    }

    public String getEuQcIns(){
        return this.euQcIns;
    }
    public void setEuQcIns(String euQcIns){
        this.euQcIns = euQcIns;
    }

    public String getEuQcPce(){
        return this.euQcPce;
    }
    public void setEuQcPce(String euQcPce){
        this.euQcPce = euQcPce;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }
}