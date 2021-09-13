package com.zebone.nhis.common.module.scm.pub;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_PD_INDHP 
 *
 * @since 2018-12-14 02:52:09
 */
@Table(value="BD_PD_INDHP")
public class BdPdIndhp extends BaseModule  {

	@PK
	@Field(value="PK_PDINDHP",id=KeyId.UUID)
    private String pkPdindhp;

	@Field(value="PK_PDIND")
    private String pkPdind;

	@Field(value="PK_HP")
    private String pkHp;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	@Field(value="PK_INDTYPE")
	private String pkIndtype;
	
    public String getPkPdindhp(){
        return this.pkPdindhp;
    }
    public void setPkPdindhp(String pkPdindhp){
        this.pkPdindhp = pkPdindhp;
    }

    public String getPkPdind(){
        return this.pkPdind;
    }
    public void setPkPdind(String pkPdind){
        this.pkPdind = pkPdind;
    }

    public String getPkHp(){
        return this.pkHp;
    }
    public void setPkHp(String pkHp){
        this.pkHp = pkHp;
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
	public String getPkIndtype() {
		return pkIndtype;
	}
	public void setPkIndtype(String pkIndtype) {
		this.pkIndtype = pkIndtype;
	}
    
}