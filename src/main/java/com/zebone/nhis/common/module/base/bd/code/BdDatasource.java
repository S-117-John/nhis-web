package com.zebone.nhis.common.module.base.bd.code;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_DATASOURCE  - bd_datasource 
 *
 * @since 2016-08-30 01:09:11
 */
@Table(value="BD_DATASOURCE")
public class BdDatasource extends BaseModule  {

	@PK
	@Field(value="PK_DATASOURCE",id=KeyId.UUID)
    private String pkDatasource;

	@Field(value="NAME")
    private String name;

	@Field(value="EU_SOURCE_TYPE")
    private String euSourceType;

	@Field(value="SOURCE")
    private String source;

	@Field(value="CONFIG")
    private String config;

	@Field(value="DISPLAY_MEMBER")
    private String displayMember;

	@Field(value="VALUE_MEMBER")
    private String valueMember;

	@Field(value="FETCH_ONCE")
    private String fetchOnce;

	@Field(value="DESCR")
    private String descr;

	@Field(value="EU_MNEMONIC_CODE")
    private String euMnemonicCode;


    public String getPkDatasource(){
        return this.pkDatasource;
    }
    public void setPkDatasource(String pkDatasource){
        this.pkDatasource = pkDatasource;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getEuSourceType(){
        return this.euSourceType;
    }
    public void setEuSourceType(String euSourceType){
        this.euSourceType = euSourceType;
    }

    public String getSource(){
        return this.source;
    }
    public void setSource(String source){
        this.source = source;
    }

    public String getConfig(){
        return this.config;
    }
    public void setConfig(String config){
        this.config = config;
    }

    public String getDisplayMember(){
        return this.displayMember;
    }
    public void setDisplayMember(String displayMember){
        this.displayMember = displayMember;
    }

    public String getValueMember(){
        return this.valueMember;
    }
    public void setValueMember(String valueMember){
        this.valueMember = valueMember;
    }

    public String getFetchOnce(){
        return this.fetchOnce;
    }
    public void setFetchOnce(String fetchOnce){
        this.fetchOnce = fetchOnce;
    }

    public String getDescr(){
        return this.descr;
    }
    public void setDescr(String descr){
        this.descr = descr;
    }

    public String getEuMnemonicCode(){
        return this.euMnemonicCode;
    }
    public void setEuMnemonicCode(String euMnemonicCode){
        this.euMnemonicCode = euMnemonicCode;
    }
}