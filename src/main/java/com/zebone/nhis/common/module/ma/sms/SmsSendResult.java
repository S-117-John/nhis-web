package com.zebone.nhis.common.module.ma.sms;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

//短信发送结果
@Table(value="SMS_SEND_RESULT")
public class SmsSendResult extends BaseModule {
    @PK
    @Field(value="PK_SENDRESULT",id= Field.KeyId.UUID)
    private String pkSendResult;

    @Field(value="PK_ORG")
    private String pkOrg;

    @Field(value="PK_SMSSEND")
    private String pkSmssend;

    @Field(value="MOBILE")
    private String mobile;

    @Field(value="DATE_SEND")
    private Date dateSend;

    @Field(value="FLAG_SUCCESS")
    private String flagSuccess;

    @Field(value="DESC_RETURN")
    private String descReturn;

    @Field(value="NOTE")
    private String note;

    @Field(value="CREATOR")
    private String creator;

    @Field(value="CREATE_TIME")
    private Date createTime;

    @Field(value="MODIFIER")
    private String modifier;

    @Field(value="DEL_FLAG")
    private String delFlag;

    @Field(value="TS")
    private Date ts;

    @Field(value="MSG_GROUP")
    private String msgGroup;

    public String getMsgGroup() {
        return msgGroup;
    }

    public void setMsgGroup(String msgGroup) {
        this.msgGroup = msgGroup;
    }

    public String getPkSendResult() {
        return pkSendResult;
    }

    public void setPkSendResult(String pkSendResult) {
        this.pkSendResult = pkSendResult;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public String getPkSmssend() {
        return pkSmssend;
    }

    public void setPkSmssend(String pkSmssend) {
        this.pkSmssend = pkSmssend;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getDateSend() {
        return dateSend;
    }

    public void setDateSend(Date dateSend) {
        this.dateSend = dateSend;
    }

    public String getFlagSuccess() {
        return flagSuccess;
    }

    public void setFlagSuccess(String flagSuccess) {
        this.flagSuccess = flagSuccess;
    }

    public String getDescReturn() {
        return descReturn;
    }

    public void setDescReturn(String descReturn) {
        this.descReturn = descReturn;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
