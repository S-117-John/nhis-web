package com.zebone.nhis.common.module.compay.ins.shenzhen;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_SZYB_ORDER - ins_szyb_order 
 *
 * @since 2020-03-24 10:18:57
 */
@Table(value="INS_SZYB_ORDER")
public class InsSzybOrder extends BaseModule  {

	@PK
	@Field(value="PK_INSORD",id=KeyId.UUID)
    private String pkInsord;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="ORDSN")
    private Integer ordsn;

    /** PK_ORD - 对应医嘱服务字典的主键，如果是药品则对应pk_pd */
	@Field(value="PK_ORD")
    private String pkOrd;

	@Field(value="CODE_ORD")
    private String codeOrd;

	@Field(value="NAME_ORD")
    private String nameOrd;

	@Field(value="CODE_HP")
    private String codeHp;

	@Field(value="FLAG_FIT")
    private String flagFit;

	@Field(value="DESC_FIT")
    private String descFit;

    /** EU_HP - 01=基本记帐，02=地补记帐，03=重疾记账，99=自费 */
	@Field(value="EU_HP")
    private String euHp;

	@Field(value="FLAG_VALID")
    private String flagValid;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkInsord(){
        return this.pkInsord;
    }
    public void setPkInsord(String pkInsord){
        this.pkInsord = pkInsord;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkCnord(){
        return this.pkCnord;
    }
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
    }

    public Integer getOrdsn(){
        return this.ordsn;
    }
    public void setOrdsn(Integer ordsn){
        this.ordsn = ordsn;
    }

    public String getPkOrd(){
        return this.pkOrd;
    }
    public void setPkOrd(String pkOrd){
        this.pkOrd = pkOrd;
    }

    public String getCodeOrd(){
        return this.codeOrd;
    }
    public void setCodeOrd(String codeOrd){
        this.codeOrd = codeOrd;
    }

    public String getNameOrd(){
        return this.nameOrd;
    }
    public void setNameOrd(String nameOrd){
        this.nameOrd = nameOrd;
    }

    public String getCodeHp(){
        return this.codeHp;
    }
    public void setCodeHp(String codeHp){
        this.codeHp = codeHp;
    }

    public String getFlagFit(){
        return this.flagFit;
    }
    public void setFlagFit(String flagFit){
        this.flagFit = flagFit;
    }

    public String getDescFit(){
        return this.descFit;
    }
    public void setDescFit(String descFit){
        this.descFit = descFit;
    }

    public String getEuHp(){
        return this.euHp;
    }
    public void setEuHp(String euHp){
        this.euHp = euHp;
    }

    public String getFlagValid(){
        return this.flagValid;
    }
    public void setFlagValid(String flagValid){
        this.flagValid = flagValid;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}