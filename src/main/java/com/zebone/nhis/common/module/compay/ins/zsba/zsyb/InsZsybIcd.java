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
 * Table: ins_zsyb_icd - 中山医保icd编码修改：（ICD编码修改[7001]、ICD编码汇总[7002]、取消ICD编码汇总[7003]、ICD 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="INS_ZSYB_ICD")
public class InsZsybIcd extends BaseModule  {

	@PK
	@Field(value="PK_INSZSYBICD",id=KeyId.UUID)
    private String pkInszsybicd;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_SETTLE")
    private String pkSettle;

    /** EU_PVTYPE - 1 门诊，2 急诊，3 住院，4 体检 */
	@Field(value="EU_PVTYPE")
    private String euPvtype;

	@Field(value="JZJLH")
    private String jzjlh;

	@Field(value="JSYWH")
    private String jsywh;

	@Field(value="XGHCYZD")
    private String xghcyzd;

	@Field(value="XGHICDBM")
    private String xghicdbm;

	@Field(value="XGHZLFF")
    private String xghzlff;

	@Field(value="YYBH")
    private String yybh;

	@Field(value="JBR")
    private String jbr;

	@Field(value="XGHFZ")
    private BigDecimal xghfz;

	@Field(value="KSRQ")
    private Date ksrq;

	@Field(value="ZZRQ")
    private Date zzrq;

	@Field(value="HZYWH")
    private String hzywh;

	@Field(value="FHBZ")
    private String fhbz;

	@Field(value="FHRQ")
    private String fhrq;

	@Field(value="FHZ")
    private Integer fhz;

	@Field(value="MSG")
    private String msg;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;


    public String getPkInszsybicd() {
		return pkInszsybicd;
	}
	public void setPkInszsybicd(String pkInszsybicd) {
		this.pkInszsybicd = pkInszsybicd;
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

    public String getPkSettle(){
        return this.pkSettle;
    }
    public void setPkSettle(String pkSettle){
        this.pkSettle = pkSettle;
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

    public String getJsywh(){
        return this.jsywh;
    }
    public void setJsywh(String jsywh){
        this.jsywh = jsywh;
    }

    public String getXghcyzd(){
        return this.xghcyzd;
    }
    public void setXghcyzd(String xghcyzd){
        this.xghcyzd = xghcyzd;
    }

    public String getXghicdbm(){
        return this.xghicdbm;
    }
    public void setXghicdbm(String xghicdbm){
        this.xghicdbm = xghicdbm;
    }

    public String getXghzlff(){
        return this.xghzlff;
    }
    public void setXghzlff(String xghzlff){
        this.xghzlff = xghzlff;
    }

    public String getYybh(){
        return this.yybh;
    }
    public void setYybh(String yybh){
        this.yybh = yybh;
    }

    public String getJbr(){
        return this.jbr;
    }
    public void setJbr(String jbr){
        this.jbr = jbr;
    }

    public BigDecimal getXghfz(){
        return this.xghfz;
    }
    public void setXghfz(BigDecimal xghfz){
        this.xghfz = xghfz;
    }

    public Date getKsrq(){
        return this.ksrq;
    }
    public void setKsrq(Date ksrq){
        this.ksrq = ksrq;
    }

    public Date getZzrq(){
        return this.zzrq;
    }
    public void setZzrq(Date zzrq){
        this.zzrq = zzrq;
    }

    public String getHzywh(){
        return this.hzywh;
    }
    public void setHzywh(String hzywh){
        this.hzywh = hzywh;
    }

    public String getFhbz(){
        return this.fhbz;
    }
    public void setFhbz(String fhbz){
        this.fhbz = fhbz;
    }

    public String getFhrq(){
        return this.fhrq;
    }
    public void setFhrq(String fhrq){
        this.fhrq = fhrq;
    }

    public Integer getFhz(){
        return this.fhz;
    }
    public void setFhz(Integer fhz){
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