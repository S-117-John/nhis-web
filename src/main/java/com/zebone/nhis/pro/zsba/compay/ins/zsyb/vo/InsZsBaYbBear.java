package com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ins_bear - 外部医保-生育资料维[2047] 
 *
 * @since 2017-09-06 10:42:09
 */
@Table(value="INS_BEAR")
public class InsZsBaYbBear extends BaseModule  {

	@PK
	@Field(value="PK_INSBEAR",id=KeyId.UUID)
    private String pkInsbear;

	@Field(value="PK_INSPV")
    private String pkInspv;

	@Field(value="PK_PI")
    private String pkPi;

    /** EU_PVTYPE - 1 门诊，2 急诊，3 住院，4 体检 */
	@Field(value="EU_PVTYPE")
    private String euPvtype;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="JZJLH")
    private String jzjlh;

    /** PTSJ - 排胎或新生儿出生时间 */
	@Field(value="PTSJ")
    private Date ptsj;

	@Field(value="TESL")
    private BigDecimal tesl;

	@Field(value="HYZS")
    private BigDecimal hyzs;

    /** SFNC - 当生育类别为顺产、多胞胎时，不能为空。当生育类别为剖腹产时，系统默认为是。 0为否，1为是。 */
	@Field(value="SFNC")
    private String sfnc;

    /** SYLB - 生育类别只能传入1、2、3、4、5、12 */
	@Field(value="SYLB")
    private String sylb;

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

    /** HBJSSS - 1放置宫内节育器；2取出宫内节育器；3皮下埋植术；4流产术（怀孕不满16周）；5流产术（怀孕满16周或以上）； 6引产术（怀孕28周以下）；7输精管结扎术；8输卵管结扎术；9输精管复通术；10输卵管复 */
	@Field(value="HBJSSS")
    private String hbjsss;

	@Field(value="SYZFXE")
    private BigDecimal syzfxe;

    /** FHZ - 1:执行成功 <0:失败 */
	@Field(value="FHZ")
    private String fhz;

	@Field(value="MSG")
    private String msg;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;


    public String getPkInsbear(){
        return this.pkInsbear;
    }
    public void setPkInsbear(String pkInsbear){
        this.pkInsbear = pkInsbear;
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

    public String getEuPvtype(){
        return this.euPvtype;
    }
    public void setEuPvtype(String euPvtype){
        this.euPvtype = euPvtype;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getJzjlh(){
        return this.jzjlh;
    }
    public void setJzjlh(String jzjlh){
        this.jzjlh = jzjlh;
    }

    public Date getPtsj(){
        return this.ptsj;
    }
    public void setPtsj(Date ptsj){
        this.ptsj = ptsj;
    }

    public BigDecimal getTesl(){
        return this.tesl;
    }
    public void setTesl(BigDecimal tesl){
        this.tesl = tesl;
    }

    public BigDecimal getHyzs(){
        return this.hyzs;
    }
    public void setHyzs(BigDecimal hyzs){
        this.hyzs = hyzs;
    }

    public String getSfnc(){
        return this.sfnc;
    }
    public void setSfnc(String sfnc){
        this.sfnc = sfnc;
    }

    public String getSylb(){
        return this.sylb;
    }
    public void setSylb(String sylb){
        this.sylb = sylb;
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

    public String getHbjsss(){
        return this.hbjsss;
    }
    public void setHbjsss(String hbjsss){
        this.hbjsss = hbjsss;
    }

    public BigDecimal getSyzfxe(){
        return this.syzfxe;
    }
    public void setSyzfxe(BigDecimal syzfxe){
        this.syzfxe = syzfxe;
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

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}