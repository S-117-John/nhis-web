package com.zebone.nhis.common.module.ex.nis.ns;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EX_ASSIST_OCC_DT 
 *
 * @since 2018-12-21 04:36:27
 */
@Table(value="EX_ASSIST_OCC_DT")
public class ExAssistOccDt extends BaseModule  {

	@PK
	@Field(value="PK_ASSOCCDT",id=KeyId.UUID)
    private String pkAssoccdt;

	@Field(value="PK_ASSOCC")
    private String pkAssocc;
	
	@Field(value="FLAG_MAJ")
	private String flagMaj;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="PK_EXOCC")
    private String pkExocc;
	
	@Field(value="PK_ORD")
	private String pkOrd;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

    public String getPkOrd() {
		return pkOrd;
	}
	public void setPkOrd(String pkOrd) {
		this.pkOrd = pkOrd;
	}
	public String getFlagMaj() {
		return flagMaj;
	}
	public void setFlagMaj(String flagMaj) {
		this.flagMaj = flagMaj;
	}
	public String getPkAssoccdt(){
        return this.pkAssoccdt;
    }
    public void setPkAssoccdt(String pkAssoccdt){
        this.pkAssoccdt = pkAssoccdt;
    }

    public String getPkAssocc(){
        return this.pkAssocc;
    }
    public void setPkAssocc(String pkAssocc){
        this.pkAssocc = pkAssocc;
    }

    public String getPkCnord(){
        return this.pkCnord;
    }
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
    }

    public String getPkExocc(){
        return this.pkExocc;
    }
    public void setPkExocc(String pkExocc){
        this.pkExocc = pkExocc;
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
}