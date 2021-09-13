package com.zebone.nhis.common.module.base.bd.price;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_PAYER  - bd_payer 
 *
 * @since 2016-09-20 11:15:25
 */
@Table(value="BD_PAYER")
public class BdPayer extends BaseModule  {

	@PK
	@Field(value="PK_PAYER",id=KeyId.UUID)
    private String pkPayer;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

    /** EU_TYPE - 0 本人，1本机构，2第三方机构 */
	@Field(value="EU_TYPE")
    private String euType;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;


    public String getPkPayer(){
        return this.pkPayer;
    }
    public void setPkPayer(String pkPayer){
        this.pkPayer = pkPayer;
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

    public String getEuType(){
        return this.euType;
    }
    public void setEuType(String euType){
        this.euType = euType;
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