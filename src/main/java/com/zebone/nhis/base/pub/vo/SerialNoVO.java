package com.zebone.nhis.base.pub.vo;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

@Table(value = "BD_SERIALNO")
public class SerialNoVO {

    @PK
    @Field(value = "PK_SERIALNO", id = Field.KeyId.UUID)
    private String pkSerialno;
    @Field(value = "PK_ORG")
    private String pkOrg;
    @Field(value = "NAME_TB")
    private String nameTb;
    @Field(value = "NAME_FD")
    private String nameFd;
    @Field(value = "VAL_INIT")
    private String valInit;
    @Field(value = "VAL")
    private String val;
    @Field(userfield = "pkEmp", userfieldscop = Field.FieldType.INSERT)
    private String creator;
    @Field(value = "create_time", date = Field.FieldType.INSERT)
    private Date createTime;
    @Field(userfield = "pkEmp", userfieldscop = Field.FieldType.ALL)
    private String modifier;

    @Field(value = "MODITY_TIME")
    private Date modityTime;

    @Field(value = "DEL_FLAG")
    private String delFlag = "0";

    @Field(value = "TS")
    private Date ts;

    @Field(value = "LENGTH")
    private String length;
    
    @Field(value = "PREFIX")
    private String prefix;

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getPkSerialno() {
        return pkSerialno;
    }

    public void setPkSerialno(String pkSerialno) {
        this.pkSerialno = pkSerialno;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public String getNameTb() {
        return nameTb;
    }

    public void setNameTb(String nameTb) {
        this.nameTb = nameTb;
    }

    public String getNameFd() {
        return nameFd;
    }

    public void setNameFd(String nameFd) {
        this.nameFd = nameFd;
    }

    public String getValInit() {
        return valInit;
    }

    public void setValInit(String valInit) {
        this.valInit = valInit;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
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

    public Date getModityTime() {
        return modityTime;
    }

    public void setModityTime(Date modityTime) {
        this.modityTime = modityTime;
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

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
