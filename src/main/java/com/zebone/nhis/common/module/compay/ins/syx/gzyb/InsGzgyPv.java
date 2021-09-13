package com.zebone.nhis.common.module.compay.ins.syx.gzyb;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_GZGY_PV 
 *
 * @since 2018-09-21 05:33:00
 */
@Table(value="INS_GZGY_PV")
public class InsGzgyPv   {

	@PK
	@Field(value="PK_GZGYPV",id=KeyId.UUID)
    private String pkGzgypv;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_HP")
    private String pkHp;

	@Field(value="EU_PVTYPE")
    private String euPvtype;

	@Field(value="MCNO")
    private String mcno;

	@Field(value="DICT_PSNLEVEL")
    private String dictPsnlevel;

	@Field(value="EU_PVMODE_HP")
    private String euPvmodeHp;

	@Field(value="DRUGQUOTA")
    private Double drugquota;

	@Field(value="DICT_SPECUNIT")
    private String dictSpecunit;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkGzgypv(){
        return this.pkGzgypv;
    }
    public void setPkGzgypv(String pkGzgypv){
        this.pkGzgypv = pkGzgypv;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkHp(){
        return this.pkHp;
    }
    public void setPkHp(String pkHp){
        this.pkHp = pkHp;
    }

    public String getEuPvtype(){
        return this.euPvtype;
    }
    public void setEuPvtype(String euPvtype){
        this.euPvtype = euPvtype;
    }

    public String getMcno(){
        return this.mcno;
    }
    public void setMcno(String mcno){
        this.mcno = mcno;
    }

    public String getDictPsnlevel(){
        return this.dictPsnlevel;
    }
    public void setDictPsnlevel(String dictPsnlevel){
        this.dictPsnlevel = dictPsnlevel;
    }

    public String getEuPvmodeHp(){
        return this.euPvmodeHp;
    }
    public void setEuPvmodeHp(String euPvmodeHp){
        this.euPvmodeHp = euPvmodeHp;
    }

    public Double getDrugquota(){
        return this.drugquota;
    }
    public void setDrugquota(Double drugquota){
        this.drugquota = drugquota;
    }

    public String getDictSpecunit(){
        return this.dictSpecunit;
    }
    public void setDictSpecunit(String dictSpecunit){
        this.dictSpecunit = dictSpecunit;
    }

    public Date getCreateTime(){
        return this.createTime;
    }
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    public String getCreator(){
        return this.creator;
    }
    public void setCreator(String creator){
        this.creator = creator;
    }

    public String getDelFlag(){
        return this.delFlag;
    }
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }
}