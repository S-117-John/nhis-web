package com.zebone.nhis.common.module.base.bd.srv;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_ORDTYPE  - bd_ordtype 
 *
 * @since 2016-09-09 09:01:24
 */
@Table(value="BD_ORDTYPE")
public class BdOrdtype extends BaseModule  {

    /** PK_ORDTYPE - 见注释 */
	@PK
	@Field(value="PK_ORDTYPE",id=KeyId.UUID)
    private String pkOrdtype;

	@Field(value="PK_ORG")
    private String pkOrg;
	
	@Field(value="CODE")
	private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="PK_PARENT")
    private String pkParent;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="FLAG_PRT")
    private String flagPrt;
	
	@Field(value="EU_CPOETYPE")
	private String euCpoetype;
	
    public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	public String getPkOrdtype(){
        return this.pkOrdtype;
    }
    public void setPkOrdtype(String pkOrdtype){
        this.pkOrdtype = pkOrdtype;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getPkParent(){
        return this.pkParent;
    }
    public void setPkParent(String pkParent){
        this.pkParent = pkParent;
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

    public String getFlagPrt(){
        return this.flagPrt;
    }
    public void setFlagPrt(String flagPrt){
        this.flagPrt = flagPrt;
    }
	public String getEuCpoetype() {
		return euCpoetype;
	}
	public void setEuCpoetype(String euCpoetype) {
		this.euCpoetype = euCpoetype;
	}
    

}