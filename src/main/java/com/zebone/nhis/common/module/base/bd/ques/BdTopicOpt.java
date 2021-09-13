package com.zebone.nhis.common.module.base.bd.ques;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_TOPIC_OPT - BD_TOPIC_OPT 
 *
 * @since 2016-11-09 01:53:31
 */
@Table(value="BD_TOPIC_OPT")
public class BdTopicOpt extends BaseModule  {

	@Field(value="PK_TOPICOPT")
    private String pkTopicopt;

	@Field(value="PK_TOPIC")
    private String pkTopic;

	@Field(value="PK_TPBANK")
    private String pkTpbank;

	@Field(value="CODE_OPT")
    private String codeOpt;

	@Field(value="DESC_OPT")
    private String descOpt;

	@Field(value="FLAG_EDIT")
    private String flagEdit;

	@Field(value="VAL")
    private Double val;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkTopicopt(){
        return this.pkTopicopt;
    }
    public void setPkTopicopt(String pkTopicopt){
        this.pkTopicopt = pkTopicopt;
    }

    public String getPkTopic(){
        return this.pkTopic;
    }
    public void setPkTopic(String pkTopic){
        this.pkTopic = pkTopic;
    }

    public String getPkTpbank(){
        return this.pkTpbank;
    }
    public void setPkTpbank(String pkTpbank){
        this.pkTpbank = pkTpbank;
    }

    public String getCodeOpt(){
        return this.codeOpt;
    }
    public void setCodeOpt(String codeOpt){
        this.codeOpt = codeOpt;
    }

    public String getDescOpt(){
        return this.descOpt;
    }
    public void setDescOpt(String descOpt){
        this.descOpt = descOpt;
    }

    public String getFlagEdit(){
        return this.flagEdit;
    }
    public void setFlagEdit(String flagEdit){
        this.flagEdit = flagEdit;
    }

    public Double getVal(){
        return this.val;
    }
    public void setVal(Double val){
        this.val = val;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}