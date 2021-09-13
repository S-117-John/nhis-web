package com.zebone.nhis.common.module.compay.ins.lb.szyb;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.io.Serializable;
import java.util.Date;

@Table(value="INS_SZYB_DRGS")
public class InsSzybDrgs{

    @PK
    @Field(value="PK_INSSZYBDRGS",id= Field.KeyId.UUID)
    public String pkInsszybdrgs;

    @Field(value="PK_PV")
    private String pkPv;

    @Field(value="YLLB")
    public String  yllb;

    @Field(value="JBFZ")
    public String  jbfz;

    @Field(value="ZLFS")
    public String  zlfs;

    public String getPkInsszybdrgs() {
        return pkInsszybdrgs;
    }

    public void setPkInsszybdrgs(String pkInsszybdrgs) {
        this.pkInsszybdrgs = pkInsszybdrgs;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getYllb() {
        return yllb;
    }

    public void setYllb(String yllb) {
        this.yllb = yllb;
    }

    public String getJbfz() {
        return jbfz;
    }

    public void setJbfz(String jbfz) {
        this.jbfz = jbfz;
    }

    public String getZlfs() {
        return zlfs;
    }

    public void setZlfs(String zlfs) {
        this.zlfs = zlfs;
    }

    /**
     * 创建人
     */
    @Field(value="CREATOR")
    public String creator;

    /**
     * 修改人
     */
    @Field(value="MODIFIER")
    private String modifier;

    /**
     * 创建时间
     */
    @Field(value="CREATE_TIME",date= Field.FieldType.INSERT)
    public Date createTime;


    /**
     * 时间戳
     */
    @Field(value="TS",date= Field.FieldType.ALL)
    public Date ts;

    /**
     *删除标志
     */
    @Field(value="DEL_FLAG")
    public String delFlag = "0";  // 0未删除  1：删除

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

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
}
