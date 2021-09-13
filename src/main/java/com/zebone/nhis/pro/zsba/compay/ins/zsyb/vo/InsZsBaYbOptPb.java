package com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo;

import java.util.Date;
import java.math.BigDecimal;

import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ins_opt_pb - 外部医保-计生手术资料维护[2147] 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="INS_OPT_PB")
public class InsZsBaYbOptPb extends BaseModule  {

	@PK
	@Field(value="PK_OPTPB",id=KeyId.UUID)
    private String pkOptpb;

	@Field(value="PK_INSPV")
    private String pkInspv;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="PK_PV")
    private String pkPv;

    /** EU_PVTYPE - 1 门诊，2 急诊，3 住院，4 体检 */
	@Field(value="EU_PVTYPE")
    private String euPvtype;

	@Field(value="JZJLH")
    private String jzjlh;

	@Field(value="SSRQ")
    private Date ssrq;

    /** JHSYSS1 - 医保字典0007 */
	@Field(value="JHSYSS1")
    private String jhsyss1;

    /** JHSYSS2 - 医保字典0007 */
	@Field(value="JHSYSS2")
    private String jhsyss2;

    /** JHSYSS3 - 医保字典0007 */
	@Field(value="JHSYSS3")
    private String jhsyss3;

	@Field(value="YHZH")
    private String yhzh;

	@Field(value="YHBH")
    private String yhbh;

	@Field(value="YHHM")
    private String yhhm;

	@Field(value="LXFS")
    private String lxfs;

	@Field(value="JBR")
    private String jbr;

	@Field(value="SHR")
    private String shr;

    /** HYZS - 如手术类型为引产术则必填，用于业务判断 */
	@Field(value="HYZS")
    private BigDecimal hyzs;

	@Field(value="SFECJS")
    private String sfecjs;

    /** FHZ - 1:执行成功<0:失败 */
	@Field(value="FHZ")
    private String fhz;

	@Field(value="MSG")
    private String msg;

	@Field(value="SYZFXE")
    private BigDecimal syzfxe;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;
	
	
    public String getPkOptpb(){
        return this.pkOptpb;
    }
    public void setPkOptpb(String pkOptpb){
        this.pkOptpb = pkOptpb;
    }

    public String getPkInspv(){
        return this.pkInspv;
    }
    public void setPkInspv(String pkInspv){
        this.pkInspv = pkInspv;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getEuPvtype(){
        return this.euPvtype;
    }
    public void setEuPvtype(String euPvtype){
        this.euPvtype = euPvtype;
    }

    public String getJzjlh(){
        return this.jzjlh;
    }
    public void setJzjlh(String jzjlh){
        this.jzjlh = jzjlh;
    }

    public Date getSsrq(){
        return this.ssrq;
    }
    public void setSsrq(Date ssrq){
        this.ssrq = ssrq;
    }

    public String getJhsyss1(){
        return this.jhsyss1;
    }
    public void setJhsyss1(String jhsyss1){
        this.jhsyss1 = jhsyss1;
    }

    public String getJhsyss2(){
        return this.jhsyss2;
    }
    public void setJhsyss2(String jhsyss2){
        this.jhsyss2 = jhsyss2;
    }

    public String getJhsyss3(){
        return this.jhsyss3;
    }
    public void setJhsyss3(String jhsyss3){
        this.jhsyss3 = jhsyss3;
    }

    public String getYhzh(){
        return this.yhzh;
    }
    public void setYhzh(String yhzh){
        this.yhzh = yhzh;
    }

    public String getYhbh(){
        return this.yhbh;
    }
    public void setYhbh(String yhbh){
        this.yhbh = yhbh;
    }

    public String getYhhm(){
        return this.yhhm;
    }
    public void setYhhm(String yhhm){
        this.yhhm = yhhm;
    }

    public String getLxfs(){
        return this.lxfs;
    }
    public void setLxfs(String lxfs){
        this.lxfs = lxfs;
    }

    public String getJbr(){
        return this.jbr;
    }
    public void setJbr(String jbr){
        this.jbr = jbr;
    }

    public String getShr(){
        return this.shr;
    }
    public void setShr(String shr){
        this.shr = shr;
    }

    public BigDecimal getHyzs(){
        return this.hyzs;
    }
    public void setHyzs(BigDecimal hyzs){
        this.hyzs = hyzs;
    }

    public String getSfecjs(){
        return this.sfecjs;
    }
    public void setSfecjs(String sfecjs){
        this.sfecjs = sfecjs;
    }

    public String getFhz(){
        return this.fhz;
    }
    public void setFhz(String fhz){
        this.fhz = fhz;
    }

    public String getMsg(){
        return this.msg;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }

    public BigDecimal getSyzfxe(){
        return this.syzfxe;
    }
    public void setSyzfxe(BigDecimal syzfxe){
        this.syzfxe = syzfxe;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}