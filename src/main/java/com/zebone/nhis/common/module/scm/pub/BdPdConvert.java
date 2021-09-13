package com.zebone.nhis.common.module.scm.pub;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_PD_CONVERT - bd_pd_convert 
 *
 * @since 2016-10-29 09:38:57
 */
@Table(value="BD_PD_CONVERT")
public class BdPdConvert extends BaseModule  {

	@PK
	@Field(value="PK_PDCONVERT",id=KeyId.UUID)
    private String pkPdconvert;

	@Field(value="PK_PD")
    private String pkPd;

	@Field(value="SPEC")
    private String spec;

    /** PACK_SIZE - 针对基本单位的包装数量 */
	@Field(value="PACK_SIZE")
    private Integer packSize;

	@Field(value="PK_UNIT")
    private String pkUnit;

	@Field(value="FLAG_OP")
    private String flagOp;

	@Field(value="FLAG_IP")
    private String flagIp;


    public String getPkPdconvert(){
        return this.pkPdconvert;
    }
    public void setPkPdconvert(String pkPdconvert){
        this.pkPdconvert = pkPdconvert;
    }

    public String getPkPd(){
        return this.pkPd;
    }
    public void setPkPd(String pkPd){
        this.pkPd = pkPd;
    }

    public String getSpec(){
        return this.spec;
    }
    public void setSpec(String spec){
        this.spec = spec;
    }

    public Integer getPackSize(){
        return this.packSize;
    }
    public void setPackSize(Integer packSize){
        this.packSize = packSize;
    }

    public String getPkUnit(){
        return this.pkUnit;
    }
    public void setPkUnit(String pkUnit){
        this.pkUnit = pkUnit;
    }

    public String getFlagOp(){
        return this.flagOp;
    }
    public void setFlagOp(String flagOp){
        this.flagOp = flagOp;
    }

    public String getFlagIp(){
        return this.flagIp;
    }
    public void setFlagIp(String flagIp){
        this.flagIp = flagIp;
    }
}