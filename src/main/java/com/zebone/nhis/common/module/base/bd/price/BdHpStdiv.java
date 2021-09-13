package com.zebone.nhis.common.module.base.bd.price;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_HP_STDIV 
 *
 * @since 2018-07-21 11:55:16
 */
@Table(value="BD_HP_STDIV")
public class BdHpStdiv extends BaseModule  {

	@PK
	@Field(value="PK_HPSTDIV",id=KeyId.UUID)
    private String pkHpstdiv;

	@Field(value="CODE_DIV")
    private String codeDiv;

	@Field(value="NAME_DIV")
    private String nameDiv;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="EU_DIVTYPE")
    private String euDivtype;

	@Field(value="NOTE")
    private String note;
	
	@Field(value="DESC_DEFDIV")
	private String descDefdiv; 


    public String getDescDefdiv() {
		return descDefdiv;
	}
	public void setDescDefdiv(String descDefdiv) {
		this.descDefdiv = descDefdiv;
	}
	public String getPkHpstdiv(){
        return this.pkHpstdiv;
    }
    public void setPkHpstdiv(String pkHpstdiv){
        this.pkHpstdiv = pkHpstdiv;
    }

    public String getCodeDiv(){
        return this.codeDiv;
    }
    public void setCodeDiv(String codeDiv){
        this.codeDiv = codeDiv;
    }

    public String getNameDiv(){
        return this.nameDiv;
    }
    public void setNameDiv(String nameDiv){
        this.nameDiv = nameDiv;
    }

    public String getSpcode(){
        return this.spcode;
    }
    public void setSpcode(String spcode){
        this.spcode = spcode;
    }

    public String getdCode(){
        return this.dCode;
    }
    public void setdCode(String dCode){
        this.dCode = dCode;
    }

    public String getEuDivtype(){
        return this.euDivtype;
    }
    public void setEuDivtype(String euDivtype){
        this.euDivtype = euDivtype;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

}