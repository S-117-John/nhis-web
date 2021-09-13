package com.zebone.nhis.common.module.base.bd.mk;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * 用法分类
 * Table: BD_SUPPLY_CLASS  -bd_supply_class
 *
 * @since 2016-08-31 10:47:37
 */
@Table(value="BD_SUPPLY_CLASS")
public class BdSupplyClass extends BaseModule {

	private static final long serialVersionUID = 1L;

	/** PK_SUPPLYCATE - 用法分类主键 */
	@PK
	@Field(value="PK_SUPPLYCATE",id=KeyId.UUID)
    private String pkSupplycate;

    /** CODE - 分类编码 */
	@Field(value="CODE")
    private String code;

    /** NAME - 分类名称 */
	@Field(value="NAME")
    private String name;

    /** SPCODE - 拼音码 */
	@Field(value="SPCODE")
    private String spcode;

    /** D_CODE - 自定义码 */
	@Field(value="D_CODE")
    private String dCode;

    /** EU_USECATE - 用法分类 */
    @Field(value="EU_USECATE")
	private String euUsecate;

    public String getPkSupplycate(){
        return this.pkSupplycate;
    }
    public void setPkSupplycate(String pkSupplycate){
        this.pkSupplycate = pkSupplycate;
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

    public String getEuUsecate() {return euUsecate;}
    public void setEuUsecate(String euUsecate) {this.euUsecate = euUsecate;}
}