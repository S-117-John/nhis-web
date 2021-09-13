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
 * Table: ins_st_itemcate - 外部医保-中山医保结算返回项目分类：（中山医保返回18项、住院医保费用结算[2044]） 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="INS_ST_ITEMCATE")
public class InsZsybStItemcate extends BaseModule  {

	@PK
	@Field(value="PK_INSITEMCATE",id=KeyId.UUID)
    private String pkInsitemcate;

	@Field(value="PK_INSST")
    private String pkInsst;
	
	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="PK_PV")
    private String pkPv;

    /** EU_PVTYPE - 1 门诊，2 急诊，3 住院，4 体检 */
	@Field(value="EU_PVTYPE")
    private String euPvtype;

	@Field(value="PK_SETTLE")
    private String pkSettle;

	@Field(value="JSXM")
    private String jsxm;

    /** JSXMMC - 结算项目名称 */
	@Field(value="JSXMMC")
    private String jsxmmc;

	@Field(value="FYXJ")
    private BigDecimal fyxj;

	@Field(value="YBFY")
    private BigDecimal ybfy;

	@Field(value="ZFJE")
    private BigDecimal zfje;

	@Field(value="JZJLH")
    private String jzjlh;

	@Field(value="JSYWH")
    private String jsywh;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;


    public String getPkInsitemcate() {
		return pkInsitemcate;
	}
	public void setPkInsitemcate(String pkInsitemcate) {
		this.pkInsitemcate = pkInsitemcate;
	}
	
	public String getPkInsst() {
		return pkInsst;
	}
	public void setPkInsst(String pkInsst) {
		this.pkInsst = pkInsst;
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

    public String getPkSettle(){
        return this.pkSettle;
    }
    public void setPkSettle(String pkSettle){
        this.pkSettle = pkSettle;
    }

    public String getJsxm(){
        return this.jsxm;
    }
    public void setJsxm(String jsxm){
        this.jsxm = jsxm;
    }

    public String getJsxmmc(){
        return this.jsxmmc;
    }
    public void setJsxmmc(String jsxmmc){
        this.jsxmmc = jsxmmc;
    }

    public BigDecimal getFyxj(){
        return this.fyxj;
    }
    public void setFyxj(BigDecimal fyxj){
        this.fyxj = fyxj;
    }

    public BigDecimal getYbfy(){
        return this.ybfy;
    }
    public void setYbfy(BigDecimal ybfy){
        this.ybfy = ybfy;
    }

    public BigDecimal getZfje(){
        return this.zfje;
    }
    public void setZfje(BigDecimal zfje){
        this.zfje = zfje;
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

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}