package com.zebone.nhis.compay.ins.changzhou.vo;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 医保结算信息
 */
@Table(value = "INS_CZYB_SETTLEINFO")
public class YBSettleInfo extends BaseModule {
    /**
     * 主键
     */
    @PK
    @Field(value = "ID",id = Field.KeyId.UUID)
    private String id;

    /**
     * 发送方交易流水号
     */
    @Field(value = "FSFJYLSH")
    private String fsfjylsh;

    /**
     * 中心交易流水号
     */
    @Field(value = "zxjylsh")
    private String zxjylsh;

    /**
     * 流水号
     */
    @Field(value = "LSH")
    private String lsh;
    /**
     * 出院诊断疾病编码
     */
    @Field(value = "CYZDJBBM")
    private String cyzdjbbm;
    /**
     * 出院诊断疾病名称
     */
    @Field(value = "CYZDJBMC")
    private String cyzdjbmc;
    /**
     * 医疗类别
     */
    @Field(value = "YLLB")
    private String yllb;
    /**
     * 结算日期
     */
    @Field(value = "JSRQ")
    private Date jsrq;
    /**
     * 医疗费总费用
     */
    @Field(value = "YLFZFY")
    private BigDecimal ylfzfy;
    /**
     * 自费费用
     */
    @Field(value = "ZFFY")
    private BigDecimal zffy;
    /**
     * 乙类自理费用
     */
    @Field(value = "YLZLFY")
    private BigDecimal ylzlfy;
    /**
     * 特检自理费用
     */
    @Field(value = "TJZLFY")
    private BigDecimal tjzlfy;
    /**
     * 特治自理费用
     */
    @Field(value = "TZZLFY")
    private BigDecimal tzzlfy;
    /**
     * 账户余额
     */
    @Field(value = "ZHYE")
    private BigDecimal zhye;
    /**
     * 符合基本医疗费用
     */
    @Field(value = "FHJBYLFY")
    private BigDecimal fhjbylfy;
    /**
     * 起付标准自付
     */
    @Field(value = "QFBZZF")
    private BigDecimal qfbzzf;
    /**
     * 进入统筹费用
     */
    @Field(value = "JRTCFY")
    private BigDecimal jrtcfy;
    /**
     * 统筹分段自付
     */
    @Field(value = "TCFDZF")
    private BigDecimal tcfdzf;
    /**
     * 进入救助金费用
     */
    @Field(value = "JRJZJFY")
    private BigDecimal jrjzjfy;
    /**
     * 救助金自付
     */
    @Field(value = "JZJZF")
    private BigDecimal jzjzf;
    /**
     * 转诊先自付
     */
    @Field(value = "ZZXZF")
    private BigDecimal zzxzf;
    /**
     * 本次账户支付
     */
    @Field(value = "BCZHZF")
    private BigDecimal bczhzf;
    /**
     * 统筹支出
     */
    @Field(value = "TCZC")
    private BigDecimal tczc;
    /**
     * 公务员门诊补助
     */
    @Field(value = "GWYMZBZ")
    private BigDecimal gwymzbz;
    /**
     * 救助金支付金额
     */
    @Field(value = "JZJZFJE")
    private BigDecimal jzjzfje;
    /**
     * 公务员住院补助
     */
    @Field(value = "GWYZYBZ")
    private BigDecimal gwyzybz;
    /**
     * 个人现金支付
     */
    @Field(value = "GRXJZF")
    private BigDecimal grxjzf;
    /**
     * 单据号
     */
    @Field(value = "DJH")
    private String djh;
    /**
     * 住院次数
     */
    @Field(value = "ZYCS")
    private String zycs;
    /**
     * 荣军补助
     */
    @Field(value = "RJBZ")
    private BigDecimal rjbz;
    /**
     * 超过救助金封顶线个人自付金额
     */
    @Field(value = "CGJZJFDXGRZFJE")
    private BigDecimal cgjzjfdxgrzfje;
    /**
     * 精神疾病补助
     */
    @Field(value = "JSJBBZ")
    private BigDecimal jsjbbz;
    /**
     * 社会化管理费用
     */
    @Field(value = "SHHGLFY")
    private BigDecimal shhglfy;
    /**
     * 工伤基金支出
     */
    @Field(value = "GSJJZC")
    private BigDecimal gsjjzc;
    /**
     * 门诊特定病种补助统筹支出
     */
    @Field(value = "MZTDBZBZTCZC")
    private BigDecimal mztdbzbztczc;
    /**
     * 门诊特定病种补助救助金支付
     */
    @Field(value = "MZTDBZBZJZJZF")
    private BigDecimal mztdbzbzjzjzf;
    /**
     * 特检特治统筹支付
     */
    @Field(value = "TJTZTCZF")
    private BigDecimal tjtztczf;
    /**
     * 特检特治救助金支付
     */
    @Field(value = "TJTZJZJZF")
    private BigDecimal tjtzjzjzf;
    /**
     * 门诊特殊诊疗统筹支付
     */
    @Field(value = "MZTSZLTCZF")
    private BigDecimal mztszltczf;
    /**
     * 门诊特殊诊疗救助金支付
     */
    @Field(value = "MZTSZLJZJZF")
    private BigDecimal mztszljzjzf;
    /**
     * 门诊特定病种统筹余额
     */
    @Field(value = "MZTDBZTCYE")
    private BigDecimal mztdbztcye;
    /**
     * 公务员门诊补助余额
     */
    @Field(value = "GWYMZBZYE")
    private BigDecimal gwymzbzye;
    /**
     * 低保救助类别
     */
    @Field(value = "DBJZLB")
    private String dbjzlb;
    /**
     * 困难救助金支付
     */
    @Field(value = "KNJZJZHIFU")
    private BigDecimal knjzjzhifu;
    /**
     * 普通门诊统筹报销金额
     */
    @Field(value = "PTMZTCBXJE")
    private BigDecimal ptmztcbxje;
    /**
     * 普通门诊统筹起付线
     */
    @Field(value = "PTMZTCQFX")
    private BigDecimal ptmztcqfx;
    /**
     * 进入门诊统筹累计
     */
    @Field(value = "JRMZTCLJ")
    private BigDecimal jrmztclj;
    /**
     * 门诊大病基本项目起付线
     */
    @Field(value = "MZDBJBXMQFX")
    private BigDecimal mzdbjbxmqfx;
    /**
     * 门诊大病起付
     */
    @Field(value = "MZDBQF")
    private BigDecimal mzdbqf;
    /**
     * 门诊大病统筹支付金额
     */
    @Field(value = "MZDBTCZFJE")
    private BigDecimal mzdbtczfje;
    /**
     * 门诊大病基本项目进入累计
     */
    @Field(value = "MZDBJBXMJRLJ")
    private BigDecimal mzdbjbxmjrlj;
    /**
     * 超限价金额
     */
    @Field(value = "CXJJE")
    private BigDecimal cxjje;
    /**
     * 困难救助金自付
     */
    @Field(value = "KNJZJZIFU")
    private BigDecimal knjzjzifu;
    /**
     * 门诊特殊诊疗自付
     */
    @Field(value = "MZTSZLZF")
    private BigDecimal mztszlzf;
    /**
     * 门诊特定病种自付
     */
    @Field(value = "MZTDBZZF")
    private BigDecimal mztdbzzf;
    /**
     * 门诊大病救助金支付
     */
    @Field(value = "MZDBJZJZF")
    private BigDecimal mzdbjzjzf;
    /**
     * 门诊大病自付
     */
    @Field(value = "MZDBZF")
    private BigDecimal mzdbzf;
    /**
     * 普通门诊统筹救助金支付
     */
    @Field(value = "PTMZTCJZJZF")
    private BigDecimal ptmztcjzjzf;
    /**
     * 普通门诊统筹自付
     */
    @Field(value = "PTMZTCZF")
    private BigDecimal ptmztczf;
    /**
     * 费用结算ID
     */
    @Field(value = "FYJSID")
    private String fyjsid;
    /**
     * 大病支付
     */
    @Field(value = "DBZF")
    private BigDecimal dbzf;
    /**
     * 门诊慢性病支付
     */
    @Field(value = "MZMXBZF")
    private BigDecimal mzmxbzf;
    /**
     * 门诊慢性病救助金支付
     */
    @Field(value = "MZMXBJZJZF")
    private BigDecimal mzmxbjzjzf;
    /**
     * 医院承担金额
     */
    @Field(value = "YYCDJE")
    private BigDecimal yycdje;
    /**
     * 特殊账户支出
     */
    @Field(value = "TSZHZC")
    private BigDecimal tszhzc;
    /**
     * 公务员门诊起付线累计
     */
    @Field(value = "GWYMZQFXLJ")
    private BigDecimal gwymzqfxlj;
    /**
     * 进入大病保险累计
     */
    @Field(value = "JRDBBXLJ")
    private BigDecimal jrdbbxlj;
    /**
     * 门诊大额支付
     */
    @Field(value = "MZDEZF")
    private BigDecimal mzdezf;
    /**
     * 门诊大额累计
     */
    @Field(value = "MZDELJ")
    private BigDecimal mzdelj;
    /**
     * 特殊账户余额
     */
    @Field(value = "TSZHYE")
    private BigDecimal tszhye;
    /**
     * 公务员可报销金额
     */
    @Field(value = "GWYKBXJE")
    private BigDecimal gwykbxje;
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

    public String getLsh() {
        return lsh;
    }

    public void setLsh(String lsh) {
        this.lsh = lsh;
    }

    public String getCyzdjbbm() {
        return cyzdjbbm;
    }

    public void setCyzdjbbm(String cyzdjbbm) {
        this.cyzdjbbm = cyzdjbbm;
    }

    public String getCyzdjbmc() {
        return cyzdjbmc;
    }

    public void setCyzdjbmc(String cyzdjbmc) {
        this.cyzdjbmc = cyzdjbmc;
    }

    public String getYllb() {
        return yllb;
    }

    public void setYllb(String yllb) {
        this.yllb = yllb;
    }

    public Date getJsrq() {
        return jsrq;
    }

    public void setJsrq(Date jsrq) {
        this.jsrq = jsrq;
    }

    public BigDecimal getYlfzfy() {
        return ylfzfy;
    }

    public void setYlfzfy(BigDecimal ylfzfy) {
        this.ylfzfy = ylfzfy;
    }

    public BigDecimal getZffy() {
        return zffy;
    }

    public void setZffy(BigDecimal zffy) {
        this.zffy = zffy;
    }

    public BigDecimal getYlzlfy() {
        return ylzlfy;
    }

    public void setYlzlfy(BigDecimal ylzlfy) {
        this.ylzlfy = ylzlfy;
    }

    public BigDecimal getTjzlfy() {
        return tjzlfy;
    }

    public void setTjzlfy(BigDecimal tjzlfy) {
        this.tjzlfy = tjzlfy;
    }

    public BigDecimal getTzzlfy() {
        return tzzlfy;
    }

    public void setTzzlfy(BigDecimal tzzlfy) {
        this.tzzlfy = tzzlfy;
    }

    public BigDecimal getZhye() {
        return zhye;
    }

    public void setZhye(BigDecimal zhye) {
        this.zhye = zhye;
    }

    public BigDecimal getFhjbylfy() {
        return fhjbylfy;
    }

    public void setFhjbylfy(BigDecimal fhjbylfy) {
        this.fhjbylfy = fhjbylfy;
    }

    public BigDecimal getQfbzzf() {
        return qfbzzf;
    }

    public void setQfbzzf(BigDecimal qfbzzf) {
        this.qfbzzf = qfbzzf;
    }

    public BigDecimal getJrtcfy() {
        return jrtcfy;
    }

    public void setJrtcfy(BigDecimal jrtcfy) {
        this.jrtcfy = jrtcfy;
    }

    public BigDecimal getTcfdzf() {
        return tcfdzf;
    }

    public void setTcfdzf(BigDecimal tcfdzf) {
        this.tcfdzf = tcfdzf;
    }

    public BigDecimal getJrjzjfy() {
        return jrjzjfy;
    }

    public void setJrjzjfy(BigDecimal jrjzjfy) {
        this.jrjzjfy = jrjzjfy;
    }

    public BigDecimal getJzjzf() {
        return jzjzf;
    }

    public void setJzjzf(BigDecimal jzjzf) {
        this.jzjzf = jzjzf;
    }

    public BigDecimal getZzxzf() {
        return zzxzf;
    }

    public void setZzxzf(BigDecimal zzxzf) {
        this.zzxzf = zzxzf;
    }

    public BigDecimal getBczhzf() {
        return bczhzf;
    }

    public void setBczhzf(BigDecimal bczhzf) {
        this.bczhzf = bczhzf;
    }

    public BigDecimal getTczc() {
        return tczc;
    }

    public void setTczc(BigDecimal tczc) {
        this.tczc = tczc;
    }

    public BigDecimal getGwymzbz() {
        return gwymzbz;
    }

    public void setGwymzbz(BigDecimal gwymzbz) {
        this.gwymzbz = gwymzbz;
    }

    public BigDecimal getJzjzfje() {
        return jzjzfje;
    }

    public void setJzjzfje(BigDecimal jzjzfje) {
        this.jzjzfje = jzjzfje;
    }

    public BigDecimal getGwyzybz() {
        return gwyzybz;
    }

    public void setGwyzybz(BigDecimal gwyzybz) {
        this.gwyzybz = gwyzybz;
    }

    public BigDecimal getGrxjzf() {
        return grxjzf;
    }

    public void setGrxjzf(BigDecimal grxjzf) {
        this.grxjzf = grxjzf;
    }

    public String getDjh() {
        return djh;
    }

    public void setDjh(String djh) {
        this.djh = djh;
    }

    public String getZycs() {
        return zycs;
    }

    public void setZycs(String zycs) {
        this.zycs = zycs;
    }

    public BigDecimal getRjbz() {
        return rjbz;
    }

    public void setRjbz(BigDecimal rjbz) {
        this.rjbz = rjbz;
    }

    public BigDecimal getCgjzjfdxgrzfje() {
        return cgjzjfdxgrzfje;
    }

    public void setCgjzjfdxgrzfje(BigDecimal cgjzjfdxgrzfje) {
        this.cgjzjfdxgrzfje = cgjzjfdxgrzfje;
    }

    public BigDecimal getJsjbbz() {
        return jsjbbz;
    }

    public void setJsjbbz(BigDecimal jsjbbz) {
        this.jsjbbz = jsjbbz;
    }

    public BigDecimal getShhglfy() {
        return shhglfy;
    }

    public void setShhglfy(BigDecimal shhglfy) {
        this.shhglfy = shhglfy;
    }

    public BigDecimal getGsjjzc() {
        return gsjjzc;
    }

    public void setGsjjzc(BigDecimal gsjjzc) {
        this.gsjjzc = gsjjzc;
    }

    public BigDecimal getMztdbzbztczc() {
        return mztdbzbztczc;
    }

    public void setMztdbzbztczc(BigDecimal mztdbzbztczc) {
        this.mztdbzbztczc = mztdbzbztczc;
    }

    public BigDecimal getMztdbzbzjzjzf() {
        return mztdbzbzjzjzf;
    }

    public void setMztdbzbzjzjzf(BigDecimal mztdbzbzjzjzf) {
        this.mztdbzbzjzjzf = mztdbzbzjzjzf;
    }

    public BigDecimal getTjtztczf() {
        return tjtztczf;
    }

    public void setTjtztczf(BigDecimal tjtztczf) {
        this.tjtztczf = tjtztczf;
    }

    public BigDecimal getTjtzjzjzf() {
        return tjtzjzjzf;
    }

    public void setTjtzjzjzf(BigDecimal tjtzjzjzf) {
        this.tjtzjzjzf = tjtzjzjzf;
    }

    public BigDecimal getMztszltczf() {
        return mztszltczf;
    }

    public void setMztszltczf(BigDecimal mztszltczf) {
        this.mztszltczf = mztszltczf;
    }

    public BigDecimal getMztszljzjzf() {
        return mztszljzjzf;
    }

    public void setMztszljzjzf(BigDecimal mztszljzjzf) {
        this.mztszljzjzf = mztszljzjzf;
    }

    public BigDecimal getMztdbztcye() {
        return mztdbztcye;
    }

    public void setMztdbztcye(BigDecimal mztdbztcye) {
        this.mztdbztcye = mztdbztcye;
    }

    public BigDecimal getGwymzbzye() {
        return gwymzbzye;
    }

    public void setGwymzbzye(BigDecimal gwymzbzye) {
        this.gwymzbzye = gwymzbzye;
    }

    public String getDbjzlb() {
        return dbjzlb;
    }

    public void setDbjzlb(String dbjzlb) {
        this.dbjzlb = dbjzlb;
    }

    public BigDecimal getKnjzjzhifu() {
        return knjzjzhifu;
    }

    public void setKnjzjzhifu(BigDecimal knjzjzhifu) {
        this.knjzjzhifu = knjzjzhifu;
    }

    public BigDecimal getPtmztcbxje() {
        return ptmztcbxje;
    }

    public void setPtmztcbxje(BigDecimal ptmztcbxje) {
        this.ptmztcbxje = ptmztcbxje;
    }

    public BigDecimal getPtmztcqfx() {
        return ptmztcqfx;
    }

    public void setPtmztcqfx(BigDecimal ptmztcqfx) {
        this.ptmztcqfx = ptmztcqfx;
    }

    public BigDecimal getJrmztclj() {
        return jrmztclj;
    }

    public void setJrmztclj(BigDecimal jrmztclj) {
        this.jrmztclj = jrmztclj;
    }

    public BigDecimal getMzdbjbxmqfx() {
        return mzdbjbxmqfx;
    }

    public void setMzdbjbxmqfx(BigDecimal mzdbjbxmqfx) {
        this.mzdbjbxmqfx = mzdbjbxmqfx;
    }

    public BigDecimal getMzdbqf() {
        return mzdbqf;
    }

    public void setMzdbqf(BigDecimal mzdbqf) {
        this.mzdbqf = mzdbqf;
    }

    public BigDecimal getMzdbtczfje() {
        return mzdbtczfje;
    }

    public void setMzdbtczfje(BigDecimal mzdbtczfje) {
        this.mzdbtczfje = mzdbtczfje;
    }

    public BigDecimal getMzdbjbxmjrlj() {
        return mzdbjbxmjrlj;
    }

    public void setMzdbjbxmjrlj(BigDecimal mzdbjbxmjrlj) {
        this.mzdbjbxmjrlj = mzdbjbxmjrlj;
    }

    public BigDecimal getCxjje() {
        return cxjje;
    }

    public void setCxjje(BigDecimal cxjje) {
        this.cxjje = cxjje;
    }

    public BigDecimal getKnjzjzifu() {
        return knjzjzifu;
    }

    public void setKnjzjzifu(BigDecimal knjzjzifu) {
        this.knjzjzifu = knjzjzifu;
    }

    public BigDecimal getMztszlzf() {
        return mztszlzf;
    }

    public void setMztszlzf(BigDecimal mztszlzf) {
        this.mztszlzf = mztszlzf;
    }

    public BigDecimal getMztdbzzf() {
        return mztdbzzf;
    }

    public void setMztdbzzf(BigDecimal mztdbzzf) {
        this.mztdbzzf = mztdbzzf;
    }

    public BigDecimal getMzdbjzjzf() {
        return mzdbjzjzf;
    }

    public void setMzdbjzjzf(BigDecimal mzdbjzjzf) {
        this.mzdbjzjzf = mzdbjzjzf;
    }

    public BigDecimal getMzdbzf() {
        return mzdbzf;
    }

    public void setMzdbzf(BigDecimal mzdbzf) {
        this.mzdbzf = mzdbzf;
    }

    public BigDecimal getPtmztcjzjzf() {
        return ptmztcjzjzf;
    }

    public void setPtmztcjzjzf(BigDecimal ptmztcjzjzf) {
        this.ptmztcjzjzf = ptmztcjzjzf;
    }

    public BigDecimal getPtmztczf() {
        return ptmztczf;
    }

    public void setPtmztczf(BigDecimal ptmztczf) {
        this.ptmztczf = ptmztczf;
    }

    public String getFyjsid() {
        return fyjsid;
    }

    public void setFyjsid(String fyjsid) {
        this.fyjsid = fyjsid;
    }

    public BigDecimal getDbzf() {
        return dbzf;
    }

    public void setDbzf(BigDecimal dbzf) {
        this.dbzf = dbzf;
    }

    public BigDecimal getMzmxbzf() {
        return mzmxbzf;
    }

    public void setMzmxbzf(BigDecimal mzmxbzf) {
        this.mzmxbzf = mzmxbzf;
    }

    public BigDecimal getMzmxbjzjzf() {
        return mzmxbjzjzf;
    }

    public void setMzmxbjzjzf(BigDecimal mzmxbjzjzf) {
        this.mzmxbjzjzf = mzmxbjzjzf;
    }

    public BigDecimal getYycdje() {
        return yycdje;
    }

    public void setYycdje(BigDecimal yycdje) {
        this.yycdje = yycdje;
    }

    public BigDecimal getTszhzc() {
        return tszhzc;
    }

    public void setTszhzc(BigDecimal tszhzc) {
        this.tszhzc = tszhzc;
    }

    public BigDecimal getGwymzqfxlj() {
        return gwymzqfxlj;
    }

    public void setGwymzqfxlj(BigDecimal gwymzqfxlj) {
        this.gwymzqfxlj = gwymzqfxlj;
    }

    public BigDecimal getJrdbbxlj() {
        return jrdbbxlj;
    }

    public void setJrdbbxlj(BigDecimal jrdbbxlj) {
        this.jrdbbxlj = jrdbbxlj;
    }

    public BigDecimal getMzdezf() {
        return mzdezf;
    }

    public void setMzdezf(BigDecimal mzdezf) {
        this.mzdezf = mzdezf;
    }

    public BigDecimal getMzdelj() {
        return mzdelj;
    }

    public void setMzdelj(BigDecimal mzdelj) {
        this.mzdelj = mzdelj;
    }

    public BigDecimal getTszhye() {
        return tszhye;
    }

    public void setTszhye(BigDecimal tszhye) {
        this.tszhye = tszhye;
    }

    public BigDecimal getGwykbxje() {
        return gwykbxje;
    }

    public void setGwykbxje(BigDecimal gwykbxje) {
        this.gwykbxje = gwykbxje;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
