package com.zebone.nhis.ma.pub.zsba.vo;

import java.util.Date;

/**
 * @Classname PackPdMedVo
 * 药袋执行数据
 * @Description TODO
 * @Date 2020-04-10 9:04
 * @Created by wuqiang
 */
public class PackPdMedVo extends PackPdVo{

    /**
     * 执行时间
     * */
    private Date datePlan;


    /**
     * 医嘱主键
     * */
    private  String pkCnord;


    /**
     *用作计算包药数据的时候使用
     */
    private  int statue=0;
    /**
     *患者就诊号
     */
    private String  pkPv;
    /**
     *发药单号
     */
    private String  codeDe;
    /**
     *患者信息主键
     */
    private String pkPi;


    /**
     *药袋编码
     */
    private String codeBag;


    /**
     *请领明细主键
     */
    private String pkPdapdt;

    /**
     *药品发放主键
     */
    private String  pkPdde;


    /**
     *计费主键
     */
    private String  pkCgip;

    /**
     *住院号
     */
    private String  codeIp;

    /**
     * 执行单主键
      */
    private String pkExocc;
    /**
     *药品附加属性0504值，1：发送电子包药机，0人工摆药
     * */
    private  String val;
    /**
     *医嘱属性 0长期，1 临时
     * */
    private  String euAlways;
    /**
     *处方主键
     * */
    private  String pkPres;
    /**
     *发放分类编码
     * */
    private  String codeDecate;
    /**
     * 药品主键
     * */
    private  String pKPD;
    /**
     * 毒麻分类
     * */
    private  String  dtPois;
    public String getPkExocc() {
        return pkExocc;
    }

    public void setPkExocc(String pkExocc) {
        this.pkExocc = pkExocc;
    }


    public String getPkCgip() {
        return pkCgip;
    }

    public void setPkCgip(String pkCgip) {
        this.pkCgip = pkCgip;
    }

    public String getCodeIp() {
        return codeIp;
    }

    public void setCodeIp(String codeIp) {
        this.codeIp = codeIp;
    }



    public String getCodeBag() {
        return codeBag;
    }

    public void setCodeBag(String codeBag) {
        this.codeBag = codeBag;
    }

    public Date getDatePlan() {
        return datePlan;
    }

    public void setDatePlan(Date datePlan) {
        this.datePlan = datePlan;
    }

    public String getPkCnord() {
        return pkCnord;
    }

    public void setPkCnord(String pkCnord) {
        this.pkCnord = pkCnord;
    }

    public int getStatue() {
        return statue;
    }

    public void setStatue(int statue) {
        this.statue = statue;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getCodeDe() {
        return codeDe;
    }

    public void setCodeDe(String codeDe) {
        this.codeDe = codeDe;
    }

    public String getPkPi() {
        return pkPi;
    }

    public void setPkPi(String pkPi) {
        this.pkPi = pkPi;
    }



    public String getPkPdde() {
        return pkPdde;
    }

    public void setPkPdde(String pkPdde) {
        this.pkPdde = pkPdde;
    }
    public String getPkPdapdt() {
        return pkPdapdt;
    }

    public void setPkPdapdt(String pkPdapdt) {
        this.pkPdapdt = pkPdapdt;
    }


    public String getEuAlways() {
        return euAlways;
    }

    public void setEuAlways(String euAlways) {
        this.euAlways = euAlways;
    }

    public String getPkPres() {
        return pkPres;
    }

    public void setPkPres(String pkPres) {
        this.pkPres = pkPres;
    }

    public String getCodeDecate() {
        return codeDecate;
    }

    public void setCodeDecate(String codeDecate) {
        this.codeDecate = codeDecate;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getpKPD() {
        return pKPD;
    }

    public void setpKPD(String pKPD) {
        this.pKPD = pKPD;
    }

    public String getDtPois() {
        return dtPois;
    }

    public void setDtPois(String dtPois) {
        this.dtPois = dtPois;
    }
}
