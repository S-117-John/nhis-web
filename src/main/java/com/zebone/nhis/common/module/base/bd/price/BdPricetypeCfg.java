package com.zebone.nhis.common.module.base.bd.price;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_PRICETYPE_CFG  - bd_pricetype_cfg 
 *
 * @since 2016-10-09 10:22:04
 */
@Table(value="BD_PRICETYPE_CFG")
public class BdPricetypeCfg extends BaseModule  {

	@PK
	@Field(value="PK_PRICETYPECFG",id=KeyId.UUID)
    private String pkPricetypecfg;

	@Field(value="PK_HP")
    private String pkHp;

    /** EU_PRICETYPE - 1 省级 2 市级 3 县级 9 其他 */
	@Field(value="EU_PRICETYPE")
    private String euPricetype;


    public String getPkPricetypecfg(){
        return this.pkPricetypecfg;
    }
    public void setPkPricetypecfg(String pkPricetypecfg){
        this.pkPricetypecfg = pkPricetypecfg;
    }

    public String getPkHp(){
        return this.pkHp;
    }
    public void setPkHp(String pkHp){
        this.pkHp = pkHp;
    }

    public String getEuPricetype(){
        return this.euPricetype;
    }
    public void setEuPricetype(String euPricetype){
        this.euPricetype = euPricetype;
    }

}