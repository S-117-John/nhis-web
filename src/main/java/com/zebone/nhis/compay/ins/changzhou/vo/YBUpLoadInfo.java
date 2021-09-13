package com.zebone.nhis.compay.ins.changzhou.vo;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.time.DateTimeException;
import java.util.Date;


/**
 * 处方明细上传信息
 */
@Table(value = "INS_CZYB_CFMX")
public class YBUpLoadInfo {
    /**
     * 主键
     */
    @PK
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
     * 超出限价金额
     */
    @Field(value = "CCXJJE")
    private String ccxjje;
    /**
     * 金额
     */
    @Field(value = "JE")
    private String je;
    /**
     * 自理金额
     */
    @Field(value = "ZLJE")
    private String zlje;
    /**
     * 自费金额
     */
    @Field(value = "ZFJE")
    private String zfje;
    /**
     * 收费项目等级
     */
    @Field(value = "SFXMDJ")
    private String sfxmdj;
    /**
     * 全额自费标志
     */
    @Field(value = "QEZFBZ")
    private String qezfbz;
    /**
     * 处方明细ID
     */
    @Field(value = "CFMXID")
    private String cfmxid;
    /**
     * HIS处方号
     */
    @Field(value = "CFH")
    private String cfh;
    /**
     * 流水号
     */
    @Field(value = "LSH")
    private String lsh;
    /**
     * 自费原因
     */
    @Field(value = "ZFYY")
    private String zfyy;

    @Field(value = "YL1")
    private String yl1;

    @Field(value = "YL2")
    private String yl2;

    @Field(value = "YL3")
    private String yl3;
    /**
     * 医院收费项目内码
     */
    @Field(value = "YYSFXMNM")
    private String yysfxmnm;
    /**
     * 医院收费项目名称
     */
    @Field(value = "YYSFXMMC")
    private String yysfxmmc;
    /**
     * 收费项目中心编码
     */
    @Field(value = "SFXMZXBM")
    private String sfxmzxbm;

    /**
     * 创建时间
     */
    @Field(value = "CREATE_TIME")
    private Date createTime;
    /**
     * 创建人
     */
    @Field(value = "CREATOR")
    private String creator;
    /**
     * 机构
     */
    @Field(value = "PK_ORG")
    private String pkOrg;
    /**
     * 修改人
     */
    @Field(value = "MODIFIER")
    private String modifier;
    /**
     * 修改时间
     */
    @Field(value = "MODIFY_TIME")
    private Date modifyTime;
    /**
     * 删除标志
     */
    @Field(value = "DEL_FLAG")
    private String delFlag;
    /**
     * 时间戳
     */
    @Field(value = "TS")
    private Date ts;
    /**
     * 单价
     */
    @Field(value = "DJ")
    private Double dj;
    /**
     * 数量
     */
    @Field(value = "SL")
    private Double sl;
    /**
     * 处方日期
     */
    @Field(value = "CFRQ")
    private Date cfrq;
    /**
     * 医生姓名
     */
    @Field(value = "YSXM")
    private String ysxm;
    /**
     * 处方医师编码
     */
    @Field(value = "CFYSBM")
    private String cfysbm;
    /**
     * 科室编号
     */
    @Field(value = "KSBH")
    private String ksbh;
    /**
     * 科室名称
     */
    @Field(value = "KSMC")
    private String ksmc;
    /**
     * HIS费用明细ID
     */
    @Field(value = "PK_CGOP")
    private String pkCgOp;

    /**
     * HIS新的费用明细
     */
    @Field(value = "PK_CGNEW")
    private String pkCgNew;

    public String getPkCgNew() {
        return pkCgNew;
    }

    public void setPkCgNew(String pkCgNew) {
        this.pkCgNew = pkCgNew;
    }

    public String getPkCgOp() {
        return pkCgOp;
    }

    public void setPkCgOp(String pkCgOp) {
        this.pkCgOp = pkCgOp;
    }

    public Double getDj() {
        return dj;
    }

    public void setDj(Double dj) {
        this.dj = dj;
    }

    public Double getSl() {
        return sl;
    }

    public void setSl(Double sl) {
        this.sl = sl;
    }

    public Date getCfrq() {
        return cfrq;
    }

    public void setCfrq(Date cfrq) {
        this.cfrq = cfrq;
    }

    public String getYsxm() {
        return ysxm;
    }

    public void setYsxm(String ysxm) {
        this.ysxm = ysxm;
    }

    public String getCfysbm() {
        return cfysbm;
    }

    public void setCfysbm(String cfysbm) {
        this.cfysbm = cfysbm;
    }

    public String getKsbh() {
        return ksbh;
    }

    public void setKsbh(String ksbh) {
        this.ksbh = ksbh;
    }

    public String getKsmc() {
        return ksmc;
    }

    public void setKsmc(String ksmc) {
        this.ksmc = ksmc;
    }

    public String getCcxjje() {
        return ccxjje;
    }

    public void setCcxjje(String ccxjje) {
        this.ccxjje = ccxjje;
    }

    public String getJe() {
        return je;
    }

    public void setJe(String je) {
        this.je = je;
    }

    public String getZlje() {
        return zlje;
    }

    public void setZlje(String zlje) {
        this.zlje = zlje;
    }

    public String getZfje() {
        return zfje;
    }

    public void setZfje(String zfje) {
        this.zfje = zfje;
    }

    public String getSfxmdj() {
        return sfxmdj;
    }

    public void setSfxmdj(String sfxmdj) {
        this.sfxmdj = sfxmdj;
    }

    public String getCfmxid() {
        return cfmxid;
    }

    public void setCfmxid(String cfmxid) {
        this.cfmxid = cfmxid;
    }

    public String getZfyy() {
        return zfyy;
    }

    public void setZfyy(String zfyy) {
        this.zfyy = zfyy;
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

    public String getYl3() {
        return yl3;
    }

    public void setYl3(String yl3) {
        this.yl3 = yl3;
    }

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

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getCfh() {
        return cfh;
    }

    public void setCfh(String cfh) {
        this.cfh = cfh;
    }

    public String getLsh() {
        return lsh;
    }

    public void setLsh(String lsh) {
        this.lsh = lsh;
    }

    public String getQezfbz() {
        return qezfbz;
    }

    public void setQezfbz(String qezfbz) {
        this.qezfbz = qezfbz;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
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

    public String getYysfxmnm() {
        return yysfxmnm;
    }

    public void setYysfxmnm(String yysfxmnm) {
        this.yysfxmnm = yysfxmnm;
    }

    public String getYysfxmmc() {
        return yysfxmmc;
    }

    public void setYysfxmmc(String yysfxmmc) {
        this.yysfxmmc = yysfxmmc;
    }

    public String getSfxmzxbm() {
        return sfxmzxbm;
    }

    public void setSfxmzxbm(String sfxmzxbm) {
        this.sfxmzxbm = sfxmzxbm;
    }
}
