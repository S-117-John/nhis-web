package com.zebone.nhis.common.module.compay.ins.zsba.zsyb;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ins_info - 外部医保-中山医保个人资料查询：（统筹余额查询[2078]、起付累计查询[2079]、特定病种查询[2091]） 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="INS_INFO")
public class InsZsybInfo extends BaseModule  {

	@PK
	@Field(value="PK_INSINFO",id=KeyId.UUID)
    private String pkInsinfo;

	@Field(value="PK_INSPV")
    private String pkInspv;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="PK_PV")
    private String pkPv;

    /** EU_PVTYPE - 1 门诊，2 急诊，3 住院，4 体检 */
	@Field(value="EU_PVTYPE")
    private String euPvtype;

	@Field(value="GRSXH")
    private String grsxh;

	@Field(value="JZRQ")
    private Date jzrq;

    /** JYLB - 1 门诊;2 住院（含特定门诊）;4 生育 */
	@Field(value="JYLB")
    private String jylb;

	@Field(value="FHZ")
    private String fhz;

	@Field(value="MSG")
    private String msg;

	@Field(value="GMSFHM")
    private String gmsfhm;

	@Field(value="XM")
    private String xm;

	@Field(value="XB")
    private String xb;

	@Field(value="CSNY")
    private String csny;

    /** RYLB - 1在职,2退休,3离休 */
	@Field(value="RYLB")
    private String rylb;

    /** JBYLXSBZ - 1 能享受;0 不能享受 */
	@Field(value="JBYLXSBZ")
    private String jbylxsbz;

	@Field(value="JBTCKFJE")
    private BigDecimal jbtckfje;

    /** BCYLBZ - 1 能享受;0 不能享受 */
	@Field(value="BCYLBZ")
    private String bcylbz;

	@Field(value="BCTCXE")
    private BigDecimal bctcxe;

	@Field(value="TMTCXE")
    private BigDecimal tmtcxe;

    /** GWYLB - 1 能享受;0 不能享受 */
	@Field(value="GWYLB")
    private String gwylb;

	@Field(value="GWYTCXE")
    private BigDecimal gwytcxe;

	@Field(value="GWYMZTCXE")
    private BigDecimal gwymztcxe;

    /** GMMZBZ - 1 能享受;0 不能享受 */
	@Field(value="GMMZBZ")
    private String gmmzbz;

	@Field(value="GMTCJE")
    private BigDecimal gmtcje;

	@Field(value="SQBH")
    private String sqbh;

	@Field(value="SQMC")
    private String sqmc;

	@Field(value="BZ")
    private String bz;

	@Field(value="XQBZYE")
    private BigDecimal xqbzye;

	@Field(value="DBTCYE")
    private BigDecimal dbtcye;

	@Field(value="MZDBTCYE")
    private BigDecimal mzdbtcye;

    /** SYDYXSBZ - 1 能享受;0 不能享受 */
	@Field(value="SYDYXSBZ")
    private String sydyxsbz;

	@Field(value="SYLJJFYS")
    private BigDecimal syljjfys;

	@Field(value="JSBZYE")
    private BigDecimal jsbzye;

    /** TDBZXSBZ - 1 能享受;0 不能享受 */
	@Field(value="TDBZXSBZ")
    private String tdbzxsbz;

	@Field(value="TDBZQFLJ")
    private BigDecimal tdbzqflj;

    /** TSBZXSBZ - 1 能享受;0 不能享受 */
	@Field(value="TSBZXSBZ")
    private String tsbzxsbz;

	@Field(value="TSBZQFLJ")
    private BigDecimal tsbzqflj;

	@Field(value="BCZYQFLJ")
    private BigDecimal bczyqflj;

	@Field(value="GWYMZQFJE")
    private BigDecimal gwymzqfje;

	@Field(value="GWYZYQFLJ")
    private BigDecimal gwyzyqflj;

    /** XQBZXSBZ - 1 能享受;0 不能享受 */
	@Field(value="XQBZXSBZ")
    private String xqbzxsbz;

	@Field(value="XQBZQFLJ")
    private BigDecimal xqbzqflj;

	@Field(value="JBDBQFLJ")
    private BigDecimal jbdbqflj;

	@Field(value="BCDBQFLJ")
    private BigDecimal bcdbqflj;

	@Field(value="DJBZ")
    private String djbz;

	@Field(value="DJYY1")
    private String djyy1;

	@Field(value="DYXSBZ")
    private String dyxsbz;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;


    public String getPkInsinfo(){
        return this.pkInsinfo;
    }
    public void setPkInsinfo(String pkInsinfo){
        this.pkInsinfo = pkInsinfo;
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

    public String getGrsxh(){
        return this.grsxh;
    }
    public void setGrsxh(String grsxh){
        this.grsxh = grsxh;
    }

    public Date getJzrq(){
        return this.jzrq;
    }
    public void setJzrq(Date jzrq){
        this.jzrq = jzrq;
    }

    public String getJylb(){
        return this.jylb;
    }
    public void setJylb(String jylb){
        this.jylb = jylb;
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

    public String getGmsfhm(){
        return this.gmsfhm;
    }
    public void setGmsfhm(String gmsfhm){
        this.gmsfhm = gmsfhm;
    }

    public String getXm(){
        return this.xm;
    }
    public void setXm(String xm){
        this.xm = xm;
    }

    public String getXb(){
        return this.xb;
    }
    public void setXb(String xb){
        this.xb = xb;
    }

    public String getCsny(){
        return this.csny;
    }
    public void setCsny(String csny){
        this.csny = csny;
    }

    public String getRylb(){
        return this.rylb;
    }
    public void setRylb(String rylb){
        this.rylb = rylb;
    }

    public String getJbylxsbz(){
        return this.jbylxsbz;
    }
    public void setJbylxsbz(String jbylxsbz){
        this.jbylxsbz = jbylxsbz;
    }

    public BigDecimal getJbtckfje(){
        return this.jbtckfje;
    }
    public void setJbtckfje(BigDecimal jbtckfje){
        this.jbtckfje = jbtckfje;
    }

    public String getBcylbz(){
        return this.bcylbz;
    }
    public void setBcylbz(String bcylbz){
        this.bcylbz = bcylbz;
    }

    public BigDecimal getBctcxe(){
        return this.bctcxe;
    }
    public void setBctcxe(BigDecimal bctcxe){
        this.bctcxe = bctcxe;
    }

    public BigDecimal getTmtcxe(){
        return this.tmtcxe;
    }
    public void setTmtcxe(BigDecimal tmtcxe){
        this.tmtcxe = tmtcxe;
    }

    public String getGwylb(){
        return this.gwylb;
    }
    public void setGwylb(String gwylb){
        this.gwylb = gwylb;
    }

    public BigDecimal getGwytcxe(){
        return this.gwytcxe;
    }
    public void setGwytcxe(BigDecimal gwytcxe){
        this.gwytcxe = gwytcxe;
    }

    public BigDecimal getGwymztcxe(){
        return this.gwymztcxe;
    }
    public void setGwymztcxe(BigDecimal gwymztcxe){
        this.gwymztcxe = gwymztcxe;
    }

    public String getGmmzbz(){
        return this.gmmzbz;
    }
    public void setGmmzbz(String gmmzbz){
        this.gmmzbz = gmmzbz;
    }

    public BigDecimal getGmtcje(){
        return this.gmtcje;
    }
    public void setGmtcje(BigDecimal gmtcje){
        this.gmtcje = gmtcje;
    }

    public String getSqbh(){
        return this.sqbh;
    }
    public void setSqbh(String sqbh){
        this.sqbh = sqbh;
    }

    public String getSqmc(){
        return this.sqmc;
    }
    public void setSqmc(String sqmc){
        this.sqmc = sqmc;
    }

    public String getBz(){
        return this.bz;
    }
    public void setBz(String bz){
        this.bz = bz;
    }

    public BigDecimal getXqbzye(){
        return this.xqbzye;
    }
    public void setXqbzye(BigDecimal xqbzye){
        this.xqbzye = xqbzye;
    }

    public BigDecimal getDbtcye(){
        return this.dbtcye;
    }
    public void setDbtcye(BigDecimal dbtcye){
        this.dbtcye = dbtcye;
    }

    public BigDecimal getMzdbtcye(){
        return this.mzdbtcye;
    }
    public void setMzdbtcye(BigDecimal mzdbtcye){
        this.mzdbtcye = mzdbtcye;
    }

    public String getSydyxsbz(){
        return this.sydyxsbz;
    }
    public void setSydyxsbz(String sydyxsbz){
        this.sydyxsbz = sydyxsbz;
    }

    public BigDecimal getSyljjfys(){
        return this.syljjfys;
    }
    public void setSyljjfys(BigDecimal syljjfys){
        this.syljjfys = syljjfys;
    }

    public BigDecimal getJsbzye(){
        return this.jsbzye;
    }
    public void setJsbzye(BigDecimal jsbzye){
        this.jsbzye = jsbzye;
    }

    public String getTdbzxsbz(){
        return this.tdbzxsbz;
    }
    public void setTdbzxsbz(String tdbzxsbz){
        this.tdbzxsbz = tdbzxsbz;
    }

    public BigDecimal getTdbzqflj(){
        return this.tdbzqflj;
    }
    public void setTdbzqflj(BigDecimal tdbzqflj){
        this.tdbzqflj = tdbzqflj;
    }

    public String getTsbzxsbz(){
        return this.tsbzxsbz;
    }
    public void setTsbzxsbz(String tsbzxsbz){
        this.tsbzxsbz = tsbzxsbz;
    }

    public BigDecimal getTsbzqflj(){
        return this.tsbzqflj;
    }
    public void setTsbzqflj(BigDecimal tsbzqflj){
        this.tsbzqflj = tsbzqflj;
    }

    public BigDecimal getBczyqflj(){
        return this.bczyqflj;
    }
    public void setBczyqflj(BigDecimal bczyqflj){
        this.bczyqflj = bczyqflj;
    }

    public BigDecimal getGwymzqfje(){
        return this.gwymzqfje;
    }
    public void setGwymzqfje(BigDecimal gwymzqfje){
        this.gwymzqfje = gwymzqfje;
    }

    public BigDecimal getGwyzyqflj(){
        return this.gwyzyqflj;
    }
    public void setGwyzyqflj(BigDecimal gwyzyqflj){
        this.gwyzyqflj = gwyzyqflj;
    }

    public String getXqbzxsbz(){
        return this.xqbzxsbz;
    }
    public void setXqbzxsbz(String xqbzxsbz){
        this.xqbzxsbz = xqbzxsbz;
    }

    public BigDecimal getXqbzqflj(){
        return this.xqbzqflj;
    }
    public void setXqbzqflj(BigDecimal xqbzqflj){
        this.xqbzqflj = xqbzqflj;
    }

    public BigDecimal getJbdbqflj(){
        return this.jbdbqflj;
    }
    public void setJbdbqflj(BigDecimal jbdbqflj){
        this.jbdbqflj = jbdbqflj;
    }

    public BigDecimal getBcdbqflj(){
        return this.bcdbqflj;
    }
    public void setBcdbqflj(BigDecimal bcdbqflj){
        this.bcdbqflj = bcdbqflj;
    }

    public String getDjbz(){
        return this.djbz;
    }
    public void setDjbz(String djbz){
        this.djbz = djbz;
    }

    public String getDjyy1(){
        return this.djyy1;
    }
    public void setDjyy1(String djyy1){
        this.djyy1 = djyy1;
    }

    public String getDyxsbz(){
        return this.dyxsbz;
    }
    public void setDyxsbz(String dyxsbz){
        this.dyxsbz = dyxsbz;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}