package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_CP_REASON 
 *
 * @since 2019-05-13 10:15:27
 */
@Table(value="BD_CP_REASON")
public class BdCpReason extends BaseModule  {

	@PK
	@Field(value="PK_CPREASON",id=KeyId.UUID)
    private String pkCpreason;

	@Field(value="SORTNO")
    private Integer sortno;

	@Field(value="NAME_REASON")
    private String nameReason;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="EU_REASON")
    private String euReason;

	@Field(value="FLAG_NEC")
    private String flagNec;

	@Field(value="FLAG_MON")
    private String flagMon;

	@Field(value="INDEX_MON")
    private String indexMon;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkCpreason(){
        return this.pkCpreason;
    }
    public void setPkCpreason(String pkCpreason){
        this.pkCpreason = pkCpreason;
    }

    public Integer getSortno(){
        return this.sortno;
    }
    public void setSortno(Integer sortno){
        this.sortno = sortno;
    }

    public String getNameReason(){
        return this.nameReason;
    }
    public void setNameReason(String nameReason){
        this.nameReason = nameReason;
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

    public String getEuReason(){
        return this.euReason;
    }
    public void setEuReason(String euReason){
        this.euReason = euReason;
    }

    public String getFlagNec(){
        return this.flagNec;
    }
    public void setFlagNec(String flagNec){
        this.flagNec = flagNec;
    }

    public String getFlagMon(){
        return this.flagMon;
    }
    public void setFlagMon(String flagMon){
        this.flagMon = flagMon;
    }

    public String getIndexMon(){
        return this.indexMon;
    }
    public void setIndexMon(String indexMon){
        this.indexMon = indexMon;
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
}