package com.zebone.nhis.cn.pub.vo;

import java.util.List;

public class PdStrockVo {

    /**公共类**/
    private String pkPv;//就诊主键
    private String pkPi;//患者主键
    private List<String> pkPds; //物品主键
    private String pkPd;//药品主键
    /****/

    /**药品库存查询参数**/
    private String pkDept; //科室
    private String euPvtyep;//就诊类型
    private String euPvtype;//就诊类型
    private List<String> pkDepts;//执行科室
    /****/

    /**单个药品库存查询参数**/
    //private String pkDept; //科室，药房
    private String pkUnit;//单位
    /****/

    /**医嘱计费查询参数**/
    private List<String> pkCnords; //医嘱
    /****/

    /**物品附加属性查询**/
    private String codeAttr;//附加属性编码
    /****/

    /**药品适应症信息查询**/
    private String pkHp;//医保类型
    private String descInd;//适应症
    private String pdType;//药品类型 "1"：代表西药 ； "2"：代码草药
    /****/

    /**药物咨询功能参数**/
    private String dtPharm;//药品主键
    /****/

    /**药物咨询功能参数**/
    private String code;//药品分类编码

    private String name;//药品分类名称

    private String fCode;//父编码
    /****/


    public String getPkDept() {
        return pkDept;
    }

    public void setPkDept(String pkDept) {
        this.pkDept = pkDept;
    }

    public List<String> getPkPds() {
        return pkPds;
    }

    public void setPkPds(List<String> pkPds) {
        this.pkPds = pkPds;
    }

    public String getEuPvtyep() {
        return euPvtyep;
    }

    public void setEuPvtyep(String euPvtyep) {
        this.euPvtyep = euPvtyep;
    }

    public List<String> getPkCnords() {
        return pkCnords;
    }

    public void setPkCnords(List<String> pkCnords) {
        this.pkCnords = pkCnords;
    }

    public String getCodeAttr() {
        return codeAttr;
    }

    public void setCodeAttr(String codeAttr) {
        this.codeAttr = codeAttr;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getPkPi() {
        return pkPi;
    }

    public void setPkPi(String pkPi) {
        this.pkPi = pkPi;
    }

    public String getPkPd() {
        return pkPd;
    }

    public void setPkPd(String pkPd) {
        this.pkPd = pkPd;
    }

    public String getPkUnit() {
        return pkUnit;
    }

    public void setPkUnit(String pkUnit) {
        this.pkUnit = pkUnit;
    }

    public String getEuPvtype() {
        return euPvtype;
    }

    public void setEuPvtype(String euPvtype) {
        this.euPvtype = euPvtype;
    }

    public String getDescInd() {
        return descInd;
    }

    public void setDescInd(String descInd) {
        this.descInd = descInd;
    }

    public String getPkHp() {
        return pkHp;
    }

    public void setPkHp(String pkHp) {
        this.pkHp = pkHp;
    }

    public String getDtPharm() {
        return dtPharm;
    }

    public void setDtPharm(String dtPharm) {
        this.dtPharm = dtPharm;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getfCode() {
        return fCode;
    }

    public void setfCode(String fCode) {
        this.fCode = fCode;
    }

    public String getPdType() {
        return pdType;
    }

    public void setPdType(String pdType) {
        this.pdType = pdType;
    }

    public List<String> getPkDepts() {
        return pkDepts;
    }

    public void setPkDepts(List<String> pkDepts) {
        this.pkDepts = pkDepts;
    }
}
