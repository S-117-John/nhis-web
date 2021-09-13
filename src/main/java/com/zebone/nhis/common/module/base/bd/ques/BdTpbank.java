package com.zebone.nhis.common.module.base.bd.ques;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_TPBANK - bd_tpbank 
 *
 * @since 2016-11-09 01:55:05
 */
@Table(value="BD_TPBANK")
public class BdTpbank extends BaseModule  {

	@Field(value="PK_TPBANK")
    private String pkTpbank;

	@Field(value="CODE_TP")
    private String codeTp;

	@Field(value="DESC_TP")
    private String descTp;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="PK_TPCATE")
    private String pkTpcate;

    /** EU_TPTYPE - 0 单选，1 多选，2 数字，3 文本 */
	@Field(value="EU_TPTYPE")
    private String euTptype;

	@Field(value="VAL_MAX")
    private Integer valMax;

	@Field(value="VAL_MIN")
    private Integer valMin;

	@Field(value="PRECISION")
    private Integer precision;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkTpbank(){
        return this.pkTpbank;
    }
    public void setPkTpbank(String pkTpbank){
        this.pkTpbank = pkTpbank;
    }

    public String getCodeTp(){
        return this.codeTp;
    }
    public void setCodeTp(String codeTp){
        this.codeTp = codeTp;
    }

    public String getDescTp(){
        return this.descTp;
    }
    public void setDescTp(String descTp){
        this.descTp = descTp;
    }

    public String getSpcode(){
        return this.spcode;
    }
    public void setSpcode(String spcode){
        this.spcode = spcode;
    }

    public String getPkTpcate(){
        return this.pkTpcate;
    }
    public void setPkTpcate(String pkTpcate){
        this.pkTpcate = pkTpcate;
    }

    public String getEuTptype(){
        return this.euTptype;
    }
    public void setEuTptype(String euTptype){
        this.euTptype = euTptype;
    }

    public Integer getValMax(){
        return this.valMax;
    }
    public void setValMax(Integer valMax){
        this.valMax = valMax;
    }

    public Integer getValMin(){
        return this.valMin;
    }
    public void setValMin(Integer valMin){
        this.valMin = valMin;
    }

    public Integer getPrecision(){
        return this.precision;
    }
    public void setPrecision(Integer precision){
        this.precision = precision;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}