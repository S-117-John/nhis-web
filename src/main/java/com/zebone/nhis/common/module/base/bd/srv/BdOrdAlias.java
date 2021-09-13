package com.zebone.nhis.common.module.base.bd.srv;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_ORD_ALIAS  - bd_ord_alias 
 *
 * @since 2016-09-08 01:43:45
 */
@Table(value="BD_ORD_ALIAS")
public class BdOrdAlias extends BaseModule  {

	@PK
	@Field(value="PK_ORDALIA",id=KeyId.UUID)
    private String pkOrdalia;

	@Field(value="PK_ORD")
    private String pkOrd;

	@Field(value="ALIAS")
    private String alias;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;


    public String getPkOrdalia(){
        return this.pkOrdalia;
    }
    public void setPkOrdalia(String pkOrdalia){
        this.pkOrdalia = pkOrdalia;
    }

    public String getPkOrd(){
        return this.pkOrd;
    }
    public void setPkOrd(String pkOrd){
        this.pkOrd = pkOrd;
    }

    public String getAlias(){
        return this.alias;
    }
    public void setAlias(String alias){
        this.alias = alias;
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

}