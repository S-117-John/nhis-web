package com.zebone.nhis.common.module.ex.pivas.conf;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_PIVAS_PD - bd_pivas_pd 
 *
 * @since 2016-12-06 01:53:39
 */
@Table(value="BD_PIVAS_PD")
public class BdPivasPd extends BaseModule  {

	@PK
	@Field(value="PK_PIVASPD",id=KeyId.UUID)
    private String pkPivaspd;

	@Field(value="PK_PIVASCATE")
    private String pkPivascate;

	@Field(value="PK_PD")
    private String pkPd;


    public String getPkPivaspd(){
        return this.pkPivaspd;
    }
    public void setPkPivaspd(String pkPivaspd){
        this.pkPivaspd = pkPivaspd;
    }

    public String getPkPivascate(){
        return this.pkPivascate;
    }
    public void setPkPivascate(String pkPivascate){
        this.pkPivascate = pkPivascate;
    }

    public String getPkPd(){
        return this.pkPd;
    }
    public void setPkPd(String pkPd){
        this.pkPd = pkPd;
    }
}