package com.zebone.nhis.common.module.compay.ins.lb.szyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_SZYB_MZDJ 
 *
 * @since 2018-05-04 02:20:43
 */
@Table(value="INS_SZYB_MZDJ")
public class InsSzybMzdj extends BaseModule  {

    /** ID - 主键 */
	@PK
	@Field(value="ID",id=KeyId.UUID)
    private String id;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="YWLX")
    private String ywlx;

    /** LSH - LSH-门诊_住院流水号 */
	@Field(value="LSH")
    private String lsh;

    /** YLLB - YLLB-医疗类别 */
	@Field(value="YLLB")
    private String yllb;

    /** GHRQ - GHRQ-挂号日期 */
	@Field(value="GHRQ")
    private String ghrq;

    /** ZDJBBM - ZDJBBM-诊断疾病编码 */
	@Field(value="ZDJBBM")
    private String zdjbbm;

    /** ZDJBMC - ZDJBMC-诊断疾病名称 */
	@Field(value="ZDJBMC")
    private String zdjbmc;

    /** BLXX - BLXX-病历信息 */
	@Field(value="BLXX")
    private String blxx;

    /** KSMC - KSMC-科室名称 */
	@Field(value="KSMC")
    private String ksmc;

    /** CWH - CWH-床位号 */
	@Field(value="CWH")
    private String cwh;

    /** YSDM - YSDM-医生代码 */
	@Field(value="YSDM")
    private String ysdm;

    /** YSXM - YSXM-医生姓名 */
	@Field(value="YSXM")
    private String ysxm;

    /** GHF - GHF-挂号费 */
	@Field(value="GHF")
    private String ghf;

    /** JCF - JCF-检查费 */
	@Field(value="JCF")
    private String jcf;

    /** JBR - JBR-经办人 */
	@Field(value="JBR")
    private String jbr;

    /** QCZYH - QCZYH-前次住院号 */
	@Field(value="QCZYH")
    private String qczyh;

    /** JSBYMQFXBZ - JSBYMQFXBZ-精神病院免起付线标志 */
	@Field(value="JSBYMQFXBZ")
    private String jsbymqfxbz;

    /** FYBM - FYBM-分院编码 */
	@Field(value="FYBM")
    private String fybm;

    /** RYDYFZDBM - RYDYFZDBM-入院第一副诊断编码 */
	@Field(value="RYDYFZDBM")
    private String rydyfzdbm;

    /** RYDYFZDMC - RYDYFZDMC-入院第一副诊断名称 */
	@Field(value="RYDYFZDMC")
    private String rydyfzdmc;

    /** RYDEFZDBM - RYDEFZDBM-入院第二副诊断编码 */
	@Field(value="RYDEFZDBM")
    private String rydefzdbm;

    /** RYDEFZDMC - RYDEFZDMC-入院第二副诊断名称 */
	@Field(value="RYDEFZDMC")
    private String rydefzdmc;

    /** DAH - DAH-档案号 */
	@Field(value="DAH")
    private String dah;

    /** SFZH - SFZH-身份证号 */
	@Field(value="SFZH")
    private String sfzh;

    /** LXDH - LXDH-联系电话 */
	@Field(value="LXDH")
    private String lxdh;

    /** BZ - BZ-备注 */
	@Field(value="BZ")
    private String bz;

    /** YYDJ - YYDJ-医院等级 */
	@Field(value="YYDJ")
    private String yydj;

    /** RYDSFZDBM3 - RYDSFZDBM-入院第三副诊断编码 */
	@Field(value="RYDSFZDBM3")
    private String rydsfzdbm3;

    /** RYDSFZDBM4 - RYDSFZDBM-入院第四副诊断编码 */
	@Field(value="RYDSFZDBM4")
    private String rydsfzdbm4;

    /** RYDWFZDBM5 - RYDWFZDBM-入院第五副诊断编码 */
	@Field(value="RYDWFZDBM5")
    private String rydwfzdbm5;

    /** MODIFIER - 最后操作人 */
	@Field(value="MODIFIER")
    private String modifier;

    /** MODIFY_TIME - 最后操作时间 */
	@Field(value="MODIFY_TIME")
    private Date modifyTime;

    /** SFCX - 是否撤销 */
	@Field(value="SFCX")
    private String sfcx;
	
	/** fsflsh - 发送方流水号 */
	@Field(value="FSFLSH")
	private String fsflsh;
	
	/** 审批类别 */
	@Field(value="SPLB")
	private String splb;
	
	/** 读卡方式 */
	@Field(value="DIS_TYPE")
	private String disType;

    public String getSplb() {
		return splb;
	}
	public void setSplb(String splb) {
		this.splb = splb;
	}
	public String getFsflsh() {
		return fsflsh;
	}
	public void setFsflsh(String fsflsh) {
		this.fsflsh = fsflsh;
	}
	public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getYwlx(){
        return this.ywlx;
    }
    public void setYwlx(String ywlx){
        this.ywlx = ywlx;
    }

    public String getLsh(){
        return this.lsh;
    }
    public void setLsh(String lsh){
        this.lsh = lsh;
    }

    public String getYllb(){
        return this.yllb;
    }
    public void setYllb(String yllb){
        this.yllb = yllb;
    }

    public String getGhrq(){
        return this.ghrq;
    }
    public void setGhrq(String ghrq){
        this.ghrq = ghrq;
    }

    public String getZdjbbm(){
        return this.zdjbbm;
    }
    public void setZdjbbm(String zdjbbm){
        this.zdjbbm = zdjbbm;
    }

    public String getZdjbmc(){
        return this.zdjbmc;
    }
    public void setZdjbmc(String zdjbmc){
        this.zdjbmc = zdjbmc;
    }

    public String getBlxx(){
        return this.blxx;
    }
    public void setBlxx(String blxx){
        this.blxx = blxx;
    }

    public String getKsmc(){
        return this.ksmc;
    }
    public void setKsmc(String ksmc){
        this.ksmc = ksmc;
    }

    public String getCwh(){
        return this.cwh;
    }
    public void setCwh(String cwh){
        this.cwh = cwh;
    }

    public String getYsdm(){
        return this.ysdm;
    }
    public void setYsdm(String ysdm){
        this.ysdm = ysdm;
    }

    public String getYsxm(){
        return this.ysxm;
    }
    public void setYsxm(String ysxm){
        this.ysxm = ysxm;
    }

    public String getGhf(){
        return this.ghf;
    }
    public void setGhf(String ghf){
        this.ghf = ghf;
    }

    public String getJcf(){
        return this.jcf;
    }
    public void setJcf(String jcf){
        this.jcf = jcf;
    }

    public String getJbr(){
        return this.jbr;
    }
    public void setJbr(String jbr){
        this.jbr = jbr;
    }

    public String getQczyh(){
        return this.qczyh;
    }
    public void setQczyh(String qczyh){
        this.qczyh = qczyh;
    }

    public String getJsbymqfxbz(){
        return this.jsbymqfxbz;
    }
    public void setJsbymqfxbz(String jsbymqfxbz){
        this.jsbymqfxbz = jsbymqfxbz;
    }

    public String getFybm(){
        return this.fybm;
    }
    public void setFybm(String fybm){
        this.fybm = fybm;
    }

    public String getRydyfzdbm(){
        return this.rydyfzdbm;
    }
    public void setRydyfzdbm(String rydyfzdbm){
        this.rydyfzdbm = rydyfzdbm;
    }

    public String getRydyfzdmc(){
        return this.rydyfzdmc;
    }
    public void setRydyfzdmc(String rydyfzdmc){
        this.rydyfzdmc = rydyfzdmc;
    }

    public String getRydefzdbm(){
        return this.rydefzdbm;
    }
    public void setRydefzdbm(String rydefzdbm){
        this.rydefzdbm = rydefzdbm;
    }

    public String getRydefzdmc(){
        return this.rydefzdmc;
    }
    public void setRydefzdmc(String rydefzdmc){
        this.rydefzdmc = rydefzdmc;
    }

    public String getDah(){
        return this.dah;
    }
    public void setDah(String dah){
        this.dah = dah;
    }

    public String getSfzh(){
        return this.sfzh;
    }
    public void setSfzh(String sfzh){
        this.sfzh = sfzh;
    }

    public String getLxdh(){
        return this.lxdh;
    }
    public void setLxdh(String lxdh){
        this.lxdh = lxdh;
    }

    public String getBz(){
        return this.bz;
    }
    public void setBz(String bz){
        this.bz = bz;
    }

    public String getYydj(){
        return this.yydj;
    }
    public void setYydj(String yydj){
        this.yydj = yydj;
    }

    public String getRydsfzdbm3(){
        return this.rydsfzdbm3;
    }
    public void setRydsfzdbm3(String rydsfzdbm3){
        this.rydsfzdbm3 = rydsfzdbm3;
    }

    public String getRydsfzdbm4(){
        return this.rydsfzdbm4;
    }
    public void setRydsfzdbm4(String rydsfzdbm4){
        this.rydsfzdbm4 = rydsfzdbm4;
    }

    public String getRydwfzdbm5(){
        return this.rydwfzdbm5;
    }
    public void setRydwfzdbm5(String rydwfzdbm5){
        this.rydwfzdbm5 = rydwfzdbm5;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }

    public String getSfcx(){
        return this.sfcx;
    }
    public void setSfcx(String sfcx){
        this.sfcx = sfcx;
    }
	public String getDisType() {
		return disType;
	}
	public void setDisType(String disType) {
		this.disType = disType;
	}
    
}