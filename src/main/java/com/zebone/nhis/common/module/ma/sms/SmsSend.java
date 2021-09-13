package com.zebone.nhis.common.module.ma.sms;


import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;
//短信发送
@Table(value="SMS_SEND")
public class SmsSend {


    @PK
    @Field(value="PK_SMSSEND",id= Field.KeyId.UUID)
    private String pkSmssend;

    @Field(value="PK_ORG")
    private String pkOrg;

    @Field(value="CODE_TEMP")
    private String codeTemp;

    @Field(value="NAME_TEMP")
    private String nameTemp;

    @Field(value="EU_STATUS_CHK")
    private String euStatusChk;

    @Field(value="DATE_SEND")
    private Date dateSend;

    @Field(value="CNT_SEND")
    private Integer cntSend;

    @Field(value="CNT_FAILURE")
    private Integer cntFailure;

    @Field(value="MOBILE")
    private String mobile;

    @Field(value="CONTENT")
    private String content;

    @Field(value="FLAG_FINISH")
    private String flagFinish;

    @Field(value="ECNAME")
    private String ecname;

    @Field(value="REQID")
    private String reqid;

    @Field(value="ADDSERIAL")
    private String addserial;

    @Field(value="MAC")
    private String mac;

    @Field(value="CREATOR")
    private String creator ;

    @Field(value="CREATE_TIME")
    private Date createTime;

    @Field(value="MODIFIER")
    private String modifier;

    @Field(value="DEL_FLAG")
    private String delFlag;

    @Field(value="TS")
    private Date ts;


    public String getPkSmssend() {
        return pkSmssend;
    }

    public void setPkSmssend(String pkSmssend) {
        this.pkSmssend = pkSmssend;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public String getCodeTemp() {
        return codeTemp;
    }

    public void setCodeTemp(String codeTemp) {
        this.codeTemp = codeTemp;
    }

    public String getNameTemp() {
        return nameTemp;
    }

    public void setNameTemp(String nameTemp) {
        this.nameTemp = nameTemp;
    }

    public String getEuStatusChk() {
        return euStatusChk;
    }

    public void setEuStatusChk(String euStatusChk) {
        this.euStatusChk = euStatusChk;
    }

    public Date getDateSend() {
        return dateSend;
    }

    public void setDateSend(Date dateSend) {
        this.dateSend = dateSend;
    }

    public Integer getCntSend() {
        return cntSend;
    }

    public void setCntSend(Integer cntSend) {
        this.cntSend = cntSend;
    }

    public Integer getCntFailure() {
        return cntFailure;
    }

    public void setCntFailure(Integer cntFailure) {
        this.cntFailure = cntFailure;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFlagFinish() {
        return flagFinish;
    }

    public void setFlagFinish(String flagFinish) {
        this.flagFinish = flagFinish;
    }

    public String getEcname() {
        return ecname;
    }

    public void setEcname(String ecname) {
        this.ecname = ecname;
    }

    public String getReqid() {
        return reqid;
    }

    public void setReqid(String reqid) {
        this.reqid = reqid;
    }

    public String getAddserial() {
        return addserial;
    }

    public void setAddserial(String addserial) {
        this.addserial = addserial;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }
}
