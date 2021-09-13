package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: CN_DIAG_DT - cn_diag_dt 
 *
 * @since 2016-11-08 10:40:54
 */
@Table(value="CN_DIAG_DT")
public class CnDiagDt extends BaseModule  {

	@PK
	@Field(value="PK_CNDIAGDT",id=KeyId.UUID)
    private String pkCndiagdt;

	@Field(value="PK_CNDIAG")
    private String pkCndiag;

	@Field(value="SORT_NO")
    private Integer sortNo;

    /** DT_DIAGTYPE - 0X 门诊过程：00 门诊诊断。
1X 住院过程：10  入院诊断，11 手术诊断，12 出院诊断。
2X 医技辅助：20 放射诊断，21 病理诊断 */
	@Field(value="DT_DIAGTYPE")
    private String dtDiagtype;

	@Field(value="PK_DIAG")
    private String pkDiag;

	@Field(value="DESC_DIAG")
    private String descDiag;

	@Field(value="FLAG_MAJ")
    private String flagMaj;

	@Field(value="FLAG_SUSP")
    private String flagSusp;

	@Field(value="FLAG_INFECT")
    private String flagInfect;

	@Field(value="FLAG_FINALLY")
    private String flagFinally;

	@Field(value="FLAG_CURE")
    private String flagCure;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	@Field(value="DT_TREATWAY")
    private String dtTreatway;

    public String getDtTreatway() {
		return dtTreatway;
	}
	public void setDtTreatway(String dtTreatway) {
		this.dtTreatway = dtTreatway;
	}
	public String getPkCndiagdt(){
        return this.pkCndiagdt;
    }
    public void setPkCndiagdt(String pkCndiagdt){
        this.pkCndiagdt = pkCndiagdt;
    }

    public String getPkCndiag(){
        return this.pkCndiag;
    }
    public void setPkCndiag(String pkCndiag){
        this.pkCndiag = pkCndiag;
    }

    public Integer getSortNo(){
        return this.sortNo;
    }
    public void setSortNo(Integer sortNo){
        this.sortNo = sortNo;
    }

    public String getDtDiagtype(){
        return this.dtDiagtype;
    }
    public void setDtDiagtype(String dtDiagtype){
        this.dtDiagtype = dtDiagtype;
    }

    public String getPkDiag(){
        return this.pkDiag;
    }
    public void setPkDiag(String pkDiag){
        this.pkDiag = pkDiag;
    }

    public String getDescDiag(){
        return this.descDiag;
    }
    public void setDescDiag(String descDiag){
        this.descDiag = descDiag;
    }

    public String getFlagMaj(){
        return this.flagMaj;
    }
    public void setFlagMaj(String flagMaj){
        this.flagMaj = flagMaj;
    }

    public String getFlagSusp(){
        return this.flagSusp;
    }
    public void setFlagSusp(String flagSusp){
        this.flagSusp = flagSusp;
    }

    public String getFlagInfect(){
        return this.flagInfect;
    }
    public void setFlagInfect(String flagInfect){
        this.flagInfect = flagInfect;
    }

    public String getFlagFinally(){
        return this.flagFinally;
    }
    public void setFlagFinally(String flagFinally){
        this.flagFinally = flagFinally;
    }

    public String getFlagCure(){
        return this.flagCure;
    }
    public void setFlagCure(String flagCure){
        this.flagCure = flagCure;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}