package com.zebone.nhis.common.module.ma.dp;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: DP_INV - dp_inv 
 *
 * @since 2016-11-09 01:35:27
 */
@Table(value="DP_INV")
public class DpInv extends BaseModule  {

	@Field(value="PK_DPINV",id=KeyId.UUID)
    private String pkDpinv;

	@Field(value="CODE_INV")
    private String codeInv;

	@Field(value="NAME_INV")
    private String nameInv;

	@Field(value="DATE_BEGIN")
    private Date dateBegin;

	@Field(value="DATE_END")
    private Date dateEnd;

    /** DT_INVTYPE - 00满意度调查，01患者随访，02员工调查 */
	@Field(value="DT_INVTYPE")
    private String dtInvtype;

    /** EU_TATYPE - 0患者，1员工 */
	@Field(value="EU_TATYPE")
    private String euTatype;

	@Field(value="QUAN_MAX")
    private Double quanMax;

	@Field(value="FLAG_ANON")
    private String flagAnon;

    /** EU_STATUS - 0新建，1发布，2结束 */
	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="PK_TOPICSET")
    private String pkTopicset;

	@Field(value="FLAG_RANGE")
    private String flagRange;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkDpinv(){
        return this.pkDpinv;
    }
    public void setPkDpinv(String pkDpinv){
        this.pkDpinv = pkDpinv;
    }

    public String getCodeInv(){
        return this.codeInv;
    }
    public void setCodeInv(String codeInv){
        this.codeInv = codeInv;
    }

    public String getNameInv(){
        return this.nameInv;
    }
    public void setNameInv(String nameInv){
        this.nameInv = nameInv;
    }

    public Date getDateBegin(){
        return this.dateBegin;
    }
    public void setDateBegin(Date dateBegin){
        this.dateBegin = dateBegin;
    }

    public Date getDateEnd(){
        return this.dateEnd;
    }
    public void setDateEnd(Date dateEnd){
        this.dateEnd = dateEnd;
    }

    public String getDtInvtype(){
        return this.dtInvtype;
    }
    public void setDtInvtype(String dtInvtype){
        this.dtInvtype = dtInvtype;
    }

    public String getEuTatype(){
        return this.euTatype;
    }
    public void setEuTatype(String euTatype){
        this.euTatype = euTatype;
    }

    public Double getQuanMax(){
        return this.quanMax;
    }
    public void setQuanMax(Double quanMax){
        this.quanMax = quanMax;
    }

    public String getFlagAnon(){
        return this.flagAnon;
    }
    public void setFlagAnon(String flagAnon){
        this.flagAnon = flagAnon;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getPkTopicset(){
        return this.pkTopicset;
    }
    public void setPkTopicset(String pkTopicset){
        this.pkTopicset = pkTopicset;
    }

    public String getFlagRange(){
        return this.flagRange;
    }
    public void setFlagRange(String flagRange){
        this.flagRange = flagRange;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}