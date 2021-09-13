package com.zebone.nhis.common.module.scm.pub;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_PD_IND 
 *
 * @since 2017-11-09 06:46:18
 */
@Table(value="BD_PD_IND")
public class BdPdInd extends BaseModule  {

	@PK
	@Field(value="PK_PDIND",id=KeyId.UUID)
    private String pkPdind;

	@Field(value="PK_HP")
	private String pkHp;
	
	@Field(value="CODE_IND")
    private String codeInd;

	@Field(value="NAME_IND")
    private String nameInd;

	@Field(value="RATIO_BASE")
    private BigDecimal ratioBase;

	@Field(value="RATIO_SPEC")
    private BigDecimal ratioSpec;

	@Field(value="RATIO_COMM")
    private BigDecimal ratioComm;

	@Field(value="RATIO_RET")
    private BigDecimal ratioRet;

	@Field(value="RATIO_BEAR")
    private BigDecimal ratioBear;

	@Field(value="RATIO_INJ")
    private BigDecimal ratioInj;

	@Field(value="RATIO_REC")
    private BigDecimal ratioRec;

	@Field(value="DESC_IND")
    private String descInd;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	@Field(value="CODE_INDTYPE")
	private String codeIndtype;

    public String getPkPdind(){
        return this.pkPdind;
    }
    public void setPkPdind(String pkPdind){
        this.pkPdind = pkPdind;
    }

    public String getPkHp() {
		return pkHp;
	}
    
	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
	}
	public String getCodeInd(){
        return this.codeInd;
    }
    public void setCodeInd(String codeInd){
        this.codeInd = codeInd;
    }

    public String getNameInd(){
        return this.nameInd;
    }
    public void setNameInd(String nameInd){
        this.nameInd = nameInd;
    }

    public BigDecimal getRatioBase(){
        return this.ratioBase;
    }
    public void setRatioBase(BigDecimal ratioBase){
        this.ratioBase = ratioBase;
    }

    public BigDecimal getRatioSpec(){
        return this.ratioSpec;
    }
    public void setRatioSpec(BigDecimal ratioSpec){
        this.ratioSpec = ratioSpec;
    }

    public BigDecimal getRatioComm(){
        return this.ratioComm;
    }
    public void setRatioComm(BigDecimal ratioComm){
        this.ratioComm = ratioComm;
    }

    public BigDecimal getRatioRet(){
        return this.ratioRet;
    }
    public void setRatioRet(BigDecimal ratioRet){
        this.ratioRet = ratioRet;
    }

    public BigDecimal getRatioBear(){
        return this.ratioBear;
    }
    public void setRatioBear(BigDecimal ratioBear){
        this.ratioBear = ratioBear;
    }

    public BigDecimal getRatioInj(){
        return this.ratioInj;
    }
    public void setRatioInj(BigDecimal ratioInj){
        this.ratioInj = ratioInj;
    }

    public BigDecimal getRatioRec(){
        return this.ratioRec;
    }
    public void setRatioRec(BigDecimal ratioRec){
        this.ratioRec = ratioRec;
    }

    public String getDescInd(){
        return this.descInd;
    }
    public void setDescInd(String descInd){
        this.descInd = descInd;
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
	public String getCodeIndtype() {
		return codeIndtype;
	}
	public void setCodeIndtype(String codeIndtype) {
		this.codeIndtype = codeIndtype;
	}
    
    
}