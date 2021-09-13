package com.zebone.nhis.common.module.cn.cp;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: CP_TEMP_REASON  - cp_temp_reason 
 *
 * @since 2016-12-12 02:43:26
 */
@Table(value="CP_TEMP_REASON")
public class CpTempReason extends BaseModule  {

	@PK
	@Field(value="PK_CPREASON",id=KeyId.UUID)
    private String pkCpreason;

	@Field(value="PK_CPTEMP")
    private String pkCptemp;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="DESC_REASON")
    private String descReason;

    /** EU_REASON - 1启用，2拒绝，9退出 */
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

    public String getPkCptemp(){
        return this.pkCptemp;
    }
    public void setPkCptemp(String pkCptemp){
        this.pkCptemp = pkCptemp;
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

    public String getDescReason(){
        return this.descReason;
    }
    public void setDescReason(String descReason){
        this.descReason = descReason;
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
	/**
	 * 数据更新状态
	 */
	private String rowStatus;

	public String getRowStatus() {
		return rowStatus;
	}
	public void setRowStatus(String rowStatus) {
		this.rowStatus = rowStatus;
	}
}