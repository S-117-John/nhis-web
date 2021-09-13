package com.zebone.nhis.common.module.base.bd.mk;

import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_CNDIAG_COMT 
 *
 * @since 2018-12-25 10:11:54
 */
@Table(value="BD_CNDIAG_COMT")
public class BdCndiagComt extends BaseModule  {

	@PK
	@Field(value="PK_CNDIAGCOMT",id=KeyId.UUID)
    private String pkCndiagcomt;

	@Field(value="PK_CNDIAG")
    private String pkCndiag;

	@Field(value="EU_COMTYPE")
    private String euComtype;

	@Field(value="SORTNO")
    private Integer sortno;

	@Field(value="EU_DTTYPE")
    private String euDttype;

	@Field(value="CODE_DT")
    private String codeDt;

	@Field(value="NAME_MENT")
    private String nameMent;

	@Field(value="FLAG_MAND")
    private String flagMand;

	@Field(value="FLAG_HIDDEN")
    private String flagHidden;

	@Field(value="FLAG_RB")
    private String flagRb;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	private String nameCd;
	
	private List<BdCndiagComtDt> bdCndiagComtDts;
    public String getPkCndiagcomt(){
        return this.pkCndiagcomt;
    }
    public void setPkCndiagcomt(String pkCndiagcomt){
        this.pkCndiagcomt = pkCndiagcomt;
    }

    public String getPkCndiag(){
        return this.pkCndiag;
    }
    public void setPkCndiag(String pkCndiag){
        this.pkCndiag = pkCndiag;
    }

    public String getEuComtype(){
        return this.euComtype;
    }
    public void setEuComtype(String euComtype){
        this.euComtype = euComtype;
    }

    public Integer getSortno(){
        return this.sortno;
    }
    public void setSortno(Integer sortno){
        this.sortno = sortno;
    }

    public String getEuDttype(){
        return this.euDttype;
    }
    public void setEuDttype(String euDttype){
        this.euDttype = euDttype;
    }

    public String getCodeDt(){
        return this.codeDt;
    }
    public void setCodeDt(String codeDt){
        this.codeDt = codeDt;
    }

    public String getNameMent(){
        return this.nameMent;
    }
    public void setNameMent(String nameMent){
        this.nameMent = nameMent;
    }

    public String getFlagMand(){
        return this.flagMand;
    }
    public void setFlagMand(String flagMand){
        this.flagMand = flagMand;
    }

    public String getFlagHidden(){
        return this.flagHidden;
    }
    public void setFlagHidden(String flagHidden){
        this.flagHidden = flagHidden;
    }

    public String getFlagRb(){
        return this.flagRb;
    }
    public void setFlagRb(String flagRb){
        this.flagRb = flagRb;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
	public List<BdCndiagComtDt> getBdCndiagComtDts() {
		return bdCndiagComtDts;
	}
	public void setBdCndiagComtDts(List<BdCndiagComtDt> bdCndiagComtDts) {
		this.bdCndiagComtDts = bdCndiagComtDts;
	}
	public String getNameCd() {
		return nameCd;
	}
	public void setNameCd(String nameCd) {
		this.nameCd = nameCd;
	}
    
}