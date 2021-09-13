package com.zebone.nhis.common.module.base.bd.mk;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_CNDIAG_COMP 
 *
 * @since 2018-12-25 10:10:24
 */
@Table(value="BD_CNDIAG_COMP")
public class BdCndiagComp extends BaseModule  {

	@PK
	@Field(value="PK_CNDIAGCOMP",id=KeyId.UUID)
    private String pkCndiagcomp;

	@Field(value="PK_CNDIAG")
    private String pkCndiag;

	@Field(value="PK_CNDIAG_COMP")
    private String pkCndiagComp;

	@Field(value="CODE_ICD")
    private String codeIcd;

	@Field(value="NAME_COMP")
    private String nameComp;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	private String nameCd;

    public String getPkCndiagcomp(){
        return this.pkCndiagcomp;
    }
    public void setPkCndiagcomp(String pkCndiagcomp){
        this.pkCndiagcomp = pkCndiagcomp;
    }

    public String getPkCndiag(){
        return this.pkCndiag;
    }
    public void setPkCndiag(String pkCndiag){
        this.pkCndiag = pkCndiag;
    }

    public String getPkCndiagComp(){
        return this.pkCndiagComp;
    }
    public void setPkCndiagComp(String pkCndiagComp){
        this.pkCndiagComp = pkCndiagComp;
    }

    public String getCodeIcd(){
        return this.codeIcd;
    }
    public void setCodeIcd(String codeIcd){
        this.codeIcd = codeIcd;
    }

    public String getNameComp(){
        return this.nameComp;
    }
    public void setNameComp(String nameComp){
        this.nameComp = nameComp;
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
	public String getNameCd() {
		return nameCd;
	}
	public void setNameCd(String nameCd) {
		this.nameCd = nameCd;
	}
    
}