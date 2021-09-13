package com.zebone.nhis.common.module.ma.dp;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: DP_INV_RESULT_DT - DP_INV_RESULT_DT 
 *
 * @since 2016-11-09 01:39:38
 */
@Table(value="DP_INV_RESULT_DT")
public class DpInvResultDt extends BaseModule  {

	@Field(value="PK_INVRESULTDT",id=KeyId.UUID)
    private String pkInvresultdt;

	@Field(value="PK_INVRESULT")
    private String pkInvresult;

	@Field(value="PK_TOPIC")
    private String pkTopic;

	@Field(value="PK_TOPICOPT")
    private String pkTopicopt;

	@Field(value="VAL_NUM")
    private Double valNum;

	@Field(value="VAL_STR")
    private String valStr;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkInvresultdt(){
        return this.pkInvresultdt;
    }
    public void setPkInvresultdt(String pkInvresultdt){
        this.pkInvresultdt = pkInvresultdt;
    }

    public String getPkInvresult(){
        return this.pkInvresult;
    }
    public void setPkInvresult(String pkInvresult){
        this.pkInvresult = pkInvresult;
    }

    public String getPkTopic(){
        return this.pkTopic;
    }
    public void setPkTopic(String pkTopic){
        this.pkTopic = pkTopic;
    }

    public String getPkTopicopt(){
        return this.pkTopicopt;
    }
    public void setPkTopicopt(String pkTopicopt){
        this.pkTopicopt = pkTopicopt;
    }

    public Double getValNum(){
        return this.valNum;
    }
    public void setValNum(Double valNum){
        this.valNum = valNum;
    }

    public String getValStr(){
        return this.valStr;
    }
    public void setValStr(String valStr){
        this.valStr = valStr;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}