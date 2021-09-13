package com.zebone.nhis.common.module.base.bd.res;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * 计算机工作站定义
 * Table: BD_RES_PC  - bd_res_pc 
 *
 * @since 2016-08-23 10:38:03
 */
@Table(value="BD_RES_PC")
public class BdResPc extends BaseModule  {

	@PK
	@Field(value="PK_PC",id=KeyId.UUID)
    private String pkPc;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

    /** EU_ADDRTYPE - 0 计算机名称，1 IP 地址，2 MAC 地址 */
	@Field(value="EU_ADDRTYPE")
    private String euAddrtype;

	@Field(value="ADDR")
    private String addr;

	@Field(value="NAME_PLACE")
    private String namePlace;
	
	@Field(value="PK_DEPTUNIT")
    private String pkDeptunit;
    /** FLAG_ACTIVE - 1:启用
0:停用 */
	@Field(value="FLAG_ACTIVE")
    private String flagActive;

    @Field(value = "mac")
    private String mac;

    @Field(value="MODIFIER")
    private String modifier;

    @Field(value = "MODITY_TIME")
    private Date modityTime;

    public String getPkPc(){
        return this.pkPc;
    }
    public void setPkPc(String pkPc){
        this.pkPc = pkPc;
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

    public String getEuAddrtype(){
        return this.euAddrtype;
    }
    public void setEuAddrtype(String euAddrtype){
        this.euAddrtype = euAddrtype;
    }

    public String getAddr(){
        return this.addr;
    }
    public void setAddr(String addr){
        this.addr = addr;
    }

    public String getNamePlace(){
        return this.namePlace;
    }
    public void setNamePlace(String namePlace){
        this.namePlace = namePlace;
    }

    public String getPkDeptunit() {
		return pkDeptunit;
	}
	public void setPkDeptunit(String pkDeptunit) {
		this.pkDeptunit = pkDeptunit;
	}
	public String getFlagActive(){
        return this.flagActive;
    }
    public void setFlagActive(String flagActive){
        this.flagActive = flagActive;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    @Override
    public String getModifier() {  return modifier; }

    @Override
    public void setModifier(String modifier) { this.modifier = modifier; }

    public Date getModityTime(){return modityTime;}
    public void setModityTime(Date modityTime){this.modityTime=modityTime;}
}