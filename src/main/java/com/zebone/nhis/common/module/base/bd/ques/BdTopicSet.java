package com.zebone.nhis.common.module.base.bd.ques;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_TOPIC_SET - bd_topic_set 
 *
 * @since 2016-11-09 01:53:55
 */
@Table(value="BD_TOPIC_SET")
public class BdTopicSet extends BaseModule  {

	@Field(value="PK_TOPICSET")
    private String pkTopicset;

	@Field(value="CODE_SCHM")
    private String codeSchm;

	@Field(value="NAME_SCHM")
    private String nameSchm;

	@Field(value="DT_SCHMTYPE")
    private String dtSchmtype;

	@Field(value="FLAG_PAGE")
    private String flagPage;

	@Field(value="TPCNT")
    private Integer tpcnt;

	@Field(value="FLAG_PUB")
    private String flagPub;

	@Field(value="HEADER_PAGE")
    private String headerPage;

	@Field(value="FOOTER_PAGE")
    private String footerPage;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkTopicset(){
        return this.pkTopicset;
    }
    public void setPkTopicset(String pkTopicset){
        this.pkTopicset = pkTopicset;
    }

    public String getCodeSchm(){
        return this.codeSchm;
    }
    public void setCodeSchm(String codeSchm){
        this.codeSchm = codeSchm;
    }

    public String getNameSchm(){
        return this.nameSchm;
    }
    public void setNameSchm(String nameSchm){
        this.nameSchm = nameSchm;
    }

    public String getDtSchmtype(){
        return this.dtSchmtype;
    }
    public void setDtSchmtype(String dtSchmtype){
        this.dtSchmtype = dtSchmtype;
    }

    public String getFlagPage(){
        return this.flagPage;
    }
    public void setFlagPage(String flagPage){
        this.flagPage = flagPage;
    }

    public Integer getTpcnt(){
        return this.tpcnt;
    }
    public void setTpcnt(Integer tpcnt){
        this.tpcnt = tpcnt;
    }

    public String getFlagPub(){
        return this.flagPub;
    }
    public void setFlagPub(String flagPub){
        this.flagPub = flagPub;
    }

    public String getHeaderPage(){
        return this.headerPage;
    }
    public void setHeaderPage(String headerPage){
        this.headerPage = headerPage;
    }

    public String getFooterPage(){
        return this.footerPage;
    }
    public void setFooterPage(String footerPage){
        this.footerPage = footerPage;
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