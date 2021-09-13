package com.zebone.nhis.compay.ins.changzhou.vo;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

/**
 * 医保登记信息
 */
@Table(value = "INS_CZYB_REGISTERRECORD")
public class YBRegisterInfo extends BaseModule {
    /**
     * 主键
     */
    @Field(value = "ID", id = Field.KeyId.UUID)
    private String id;

    /**
     * 发送方交易流水号
     */
    @Field(value = "FSFJYLSH")
    private String fsfjylsh;

    /**
     * 中心交易流水号
     */
    @Field(value = "ZXJYLSH")
    private String zxjylsh;

    /**
     * 单据来源：1 门诊 2 住院
     */
    @Field(value = "DJLY")
    private String djly;

    /**
     * 流水号
     */
    @Field(value = "LSH")
    private String lsh;

    /**
     * 医疗类别
     */
    @Field(value = "YLLB")
    private String yllb;

    /**
     * 账户支付
     */
    @Field(value = "ZHZF")
    private String zhzf;

    /**
     * 现金支付
     */
    @Field(value = "XJZF")
    private String xjzf;

    /**
     * 就诊id
     */
    @Field(value = "JZID")
    private String jzid;

    /**
     * 医疗待遇类别
     */
    @Field(value = "YLDYLB")
    private String yldylb;

    /**
     * 统筹区号
     */
    @Field(value = "TCQH")
    private String tcqh;

    /**
     * 诊断编码
     */
    @Field(value = "ZDBM")
    private String zdbm;

    /**
     * 诊断名称
     */
    @Field(value = "ZDMC")
    private String zdmc;

    /**
     * 收治医生编码
     */
    @Field(value = "SZYSBM")
    private String szysbm;

    /**
     * 收治医生姓名
     */
    @Field(value = "SZYSMC")
    private String szysmc;

    /**
     * 预留字段
     */
    @Field(value = "YL1")
    private String yl1;
    @Field(value = "YL2")
    private String yl2;

    /**
     * 最后操作时间
     */
    @Field(value = "MODIFY_TIME")
    private Date modifyTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFsfjylsh() {
        return fsfjylsh;
    }

    public void setFsfjylsh(String fsfjylsh) {
        this.fsfjylsh = fsfjylsh;
    }

    public String getZxjylsh() {
        return zxjylsh;
    }

    public void setZxjylsh(String zxjylsh) {
        this.zxjylsh = zxjylsh;
    }

    public String getDjly() {
        return djly;
    }

    public void setDjly(String djly) {
        this.djly = djly;
    }

    public String getLsh() {
        return lsh;
    }

    public void setLsh(String lsh) {
        this.lsh = lsh;
    }

    public String getYllb() {
        return yllb;
    }

    public void setYllb(String yllb) {
        this.yllb = yllb;
    }

    public String getZhzf() {
        return zhzf;
    }

    public void setZhzf(String zhzf) {
        this.zhzf = zhzf;
    }

    public String getXjzf() {
        return xjzf;
    }

    public void setXjzf(String xjzf) {
        this.xjzf = xjzf;
    }

    public String getJzid() {
        return jzid;
    }

    public void setJzid(String jzid) {
        this.jzid = jzid;
    }

    public String getYldylb() {
        return yldylb;
    }

    public void setYldylb(String yldylb) {
        this.yldylb = yldylb;
    }

    public String getTcqh() {
        return tcqh;
    }

    public void setTcqh(String tcqh) {
        this.tcqh = tcqh;
    }

    public String getZdbm() {
        return zdbm;
    }

    public void setZdbm(String zdbm) {
        this.zdbm = zdbm;
    }

    public String getZdmc() {
        return zdmc;
    }

    public void setZdmc(String zdmc) {
        this.zdmc = zdmc;
    }

    public String getSzysbm() {
        return szysbm;
    }

    public void setSzysbm(String szysbm) {
        this.szysbm = szysbm;
    }

    public String getSzysmc() {
        return szysmc;
    }

    public void setSzysmc(String szysmc) {
        this.szysmc = szysmc;
    }

    public String getYl1() {
        return yl1;
    }

    public void setYl1(String yl1) {
        this.yl1 = yl1;
    }

    public String getYl2() {
        return yl2;
    }

    public void setYl2(String yl2) {
        this.yl2 = yl2;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

}
