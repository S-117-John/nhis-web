package com.zebone.nhis.common.module.base.bd.price;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_HP_CGDIV 
 *
 * @since 2018-07-15 11:56:40
 */
@Table(value="BD_HP_CGDIV")
public class BdHpCgdiv extends BaseModule  {

	@PK
	@Field(value="PK_HPCGDIV",id=KeyId.UUID)
    private String pkHpcgdiv;

	@Field(value="CODE_DIV")
    private String codeDiv;

	@Field(value="NAME_DIV")
    private String nameDiv;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	@Field(value="DT_HPDICTTYPE")
	private String dtHpdicttype;

	public String getDtHpdicttype() {
		return dtHpdicttype;
	}
	public void setDtHpdicttype(String dtHpdicttype) {
		this.dtHpdicttype = dtHpdicttype;
	}
	public String getPkHpcgdiv(){
        return this.pkHpcgdiv;
    }
    public void setPkHpcgdiv(String pkHpcgdiv){
        this.pkHpcgdiv = pkHpcgdiv;
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
    
}