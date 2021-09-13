package com.zebone.nhis.common.module.ma.sms;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import java.util.Date;
//短信模板
@Table(value="SMS_TEMP")
public class SmsTemp extends BaseModule {


    @PK
    @Field(value="PK_SMSTEMP",id= Field.KeyId.UUID)
    private String pkSmstemp;

    @Field(value="PK_ORG")
    private String pkOrg;

    @Field(value="CODE_TEMP")
    private String codeTemp;

    @Field(value="NAME_TEMP")
    private String nameTemp;

    @Field(value="EU_TYPE")
    private String euType;

    @Field(value="SORTNO")
    private Integer sortno;

    @Field(value="CONTENT")
    private String content;

    @Field(value="MOBILE")
    private String mobile;

    @Field(value="EU_CHKTYPE")
    private String  euChktype;

    @Field(value="TIME_SEND")
    private String timeSend;


    @Field(value="ECNAME")
    private String ecname;

    @Field(value="REQID")
    private String reqid;

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

    @Override
    public String getCreator() {
        return creator;
    }

    @Override
    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getPkSmstemp() {
        return pkSmstemp;
    }

    public void setPkSmstemp(String pkSmstemp) {
        this.pkSmstemp = pkSmstemp;
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

    public String getEuType() {
        return euType;
    }

    public void setEuType(String euType) {
        this.euType = euType;
    }

    public Integer getSortno() {
        return sortno;
    }

    public void setSortno(Integer sortno) {
        this.sortno = sortno;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEuChktype() {
        return euChktype;
    }

    public void setEuChktype(String euChktype) {
        this.euChktype = euChktype;
    }

    public String getTimeSend() {
        return timeSend;
    }

    public void setTimeSend(String timeSend) {
        this.timeSend = timeSend;
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
