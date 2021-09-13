package com.zebone.nhis.compay.ins.lb.vo.lxyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_SZLX_MZDJ 
 *
 * @since 2018-06-21 10:40:01
 */
@Table(value="INS_SZLX_MZDJ")
public class InsSzlxMzdj extends BaseModule  {

    /** ID - 主键 */
	@PK
	@Field(value="ID",id=KeyId.UUID)
    private String id;

    /** ICKH - ICKH-IC卡号 */
	@Field(value="ICKH")
    private String ickh;

    /** FYLB - FYLB-费用类别 */
	@Field(value="FYLB")
    private String fylb;

    /** YLFWJGBM - YLFWJGBM-医疗服务机构编码 */
	@Field(value="YLFWJGBM")
    private String ylfwjgbm;

    /** YWDM - YWDM-业务代码 */
	@Field(value="YWDM")
    private String ywdm;

    /** YLFS - YLFS-医疗方式 */
	@Field(value="YLFS")
    private String ylfs;

    /** YLLB - YLLB-医疗类别 */
	@Field(value="YLLB")
    private String yllb;

    /** CFYS - CFYS-处方医师 */
	@Field(value="CFYS")
    private String cfys;

    /** CZY - CZY-操作员 */
	@Field(value="CZY")
    private String czy;

    /** CZRQ - CZRQ-操作日期 */
	@Field(value="CZRQ")
    private String czrq;

    /** ICDBMGZ - ICDBMGZ-ICD编码规则 */
	@Field(value="ICDBMGZ")
    private String icdbmgz;

    /** ICDJBBM - ICDJBBM-ICD疾病编码 */
	@Field(value="ICDJBBM")
    private String icdjbbm;

    /** ICDJBMC - ICDJBMC-ICD疾病名称 */
	@Field(value="ICDJBMC")
    private String icdjbmc;

    /** DJYY - DJYY-登记原因 */
	@Field(value="DJYY")
    private String djyy;

    /** KSMC - KSMC-科室名称 */
	@Field(value="KSMC")
    private String ksmc;

    /** DJBQ - DJBQ-登记病区 */
	@Field(value="DJBQ")
    private String djbq;

    /** DJCW - DJCW-登记床位 */
	@Field(value="DJCW")
    private String djcw;

	@Field(value="DJDM")
    private String djdm;

    /** PK_PV - 就诊主键 */
	@Field(value="PK_PV")
    private String pkPv;

    /** YWLX - 业务类型 1 门诊 2住院 */
	@Field(value="YWLX")
    private String ywlx;

    /** MODIFY_TIME - 最后操作时间 */
	@Field(value="MODIFY_TIME")
    private Date modifyTime;


    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getIckh(){
        return this.ickh;
    }
    public void setIckh(String ickh){
        this.ickh = ickh;
    }

    public String getFylb(){
        return this.fylb;
    }
    public void setFylb(String fylb){
        this.fylb = fylb;
    }

    public String getYlfwjgbm(){
        return this.ylfwjgbm;
    }
    public void setYlfwjgbm(String ylfwjgbm){
        this.ylfwjgbm = ylfwjgbm;
    }

    public String getYwdm(){
        return this.ywdm;
    }
    public void setYwdm(String ywdm){
        this.ywdm = ywdm;
    }

    public String getYlfs(){
        return this.ylfs;
    }
    public void setYlfs(String ylfs){
        this.ylfs = ylfs;
    }

    public String getYllb(){
        return this.yllb;
    }
    public void setYllb(String yllb){
        this.yllb = yllb;
    }

    public String getCfys(){
        return this.cfys;
    }
    public void setCfys(String cfys){
        this.cfys = cfys;
    }

    public String getCzy(){
        return this.czy;
    }
    public void setCzy(String czy){
        this.czy = czy;
    }

    public String getCzrq(){
        return this.czrq;
    }
    public void setCzrq(String czrq){
        this.czrq = czrq;
    }

    public String getIcdbmgz(){
        return this.icdbmgz;
    }
    public void setIcdbmgz(String icdbmgz){
        this.icdbmgz = icdbmgz;
    }

    public String getIcdjbbm(){
        return this.icdjbbm;
    }
    public void setIcdjbbm(String icdjbbm){
        this.icdjbbm = icdjbbm;
    }

    public String getIcdjbmc(){
        return this.icdjbmc;
    }
    public void setIcdjbmc(String icdjbmc){
        this.icdjbmc = icdjbmc;
    }

    public String getDjyy(){
        return this.djyy;
    }
    public void setDjyy(String djyy){
        this.djyy = djyy;
    }

    public String getKsmc(){
        return this.ksmc;
    }
    public void setKsmc(String ksmc){
        this.ksmc = ksmc;
    }

    public String getDjbq(){
        return this.djbq;
    }
    public void setDjbq(String djbq){
        this.djbq = djbq;
    }

    public String getDjcw(){
        return this.djcw;
    }
    public void setDjcw(String djcw){
        this.djcw = djcw;
    }

    public String getDjdm(){
        return this.djdm;
    }
    public void setDjdm(String djdm){
        this.djdm = djdm;
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

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }
}