package com.zebone.nhis.common.module.scm.pub;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: bd_pd_store_pack 
 *
 * @since 2017-09-23 04:13:13
 */
@Table(value="bd_pd_store_pack")
public class BdPdStorePack extends BaseModule  {

	@PK
	@Field(value="pk_pdstorepack",id=KeyId.UUID)
    private String pkPdstorepack;

	@Field(value="pk_pdstore")
    private String pkPdstore;

	@Field(value="pk_pdconvert")
    private String pkPdconvert;

	@Field(value="flag_def")
    private String flagDef;

	@Field(value="modifier")
    private String modifier;

	@Field(value="modity_time")
    private Date modityTime;
	
	@Field(value="PK_UNIT")
	private String pkUnit;			//包装单位
	
	@Field(value="PACK_SIZE")	
	private Long packSize;			//包装数量

    @Field(value="FLAG_OP")
    private String flagOp;

    @Field(value="FLAG_IP")
    private String flagIp;

    public String getPkUnit() {
		return pkUnit;
	}
	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}
	public Long getPackSize() {
		return packSize;
	}
	public void setPackSize(Long packSize) {
		this.packSize = packSize;
	}
	public String getPkPdstorepack(){
        return this.pkPdstorepack;
    }
    public void setPkPdstorepack(String pkPdstorepack){
        this.pkPdstorepack = pkPdstorepack;
    }

    public String getPkPdstore(){
        return this.pkPdstore;
    }
    public void setPkPdstore(String pkPdstore){
        this.pkPdstore = pkPdstore;
    }

    public String getPkPdconvert(){
        return this.pkPdconvert;
    }
    public void setPkPdconvert(String pkPdconvert){
        this.pkPdconvert = pkPdconvert;
    }

    public String getFlagDef(){
        return this.flagDef;
    }
    public void setFlagDef(String flagDef){
        this.flagDef = flagDef;
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

    public String getFlagOp() {
        return flagOp;
    }

    public void setFlagOp(String flagOp) {
        this.flagOp = flagOp;
    }

    public String getFlagIp() {
        return flagIp;
    }

    public void setFlagIp(String flagIp) {
        this.flagIp = flagIp;
    }
}