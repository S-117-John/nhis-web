package com.zebone.nhis.pro.zsba.cn.opdw.vo;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class SchOrPv {
    /**输出**/
    private List<Map<String,Object>> pvInf;  /**患者预约信息**/
    private List<Map<String,Object>> pvSch; /**患者就诊信息**/

    /**输入**/
    private String pkOrg; /**机构**/
    private String pkDeptEx; /**科室**/
    private String dateAppt; /**就诊治疗日期**/
    private String pkDateslot; /**午别**/
    private String pkEmp; /**医生**/
    private String pkPi; /**患者主键**/

    private String pkSchres; /**资源主键**/
    private String pkSchsrv; /**服务主键**/
    private String pkPv; /**患者就诊**/
    private String euPvtype; /**就诊类型**/

    private String pkDept; /**接诊科室**/
    private String pkEmpPhy; /**接诊医生**/
    private Date  dateHap; /**计费日期**/

    private String time;/**48小时就诊判断**/

    /**诊查费使用**/
    private String name;/**诊查费名称**/
    private String codeOrdtype;/**诊查医嘱编码**/
    private String pkOrd;/**诊查医嘱项目主键**/
    private List<String> pkOrds;/**诊查医嘱项目主键**/
    /** 是否记费*/
    private String flagCg;
    private String pkItemSpec;/**门诊特需费用**/

    private String pkItem;



    /**当前科室医生接诊资源查询**/
    private String schResourceSpecial; /**判断是否能选择特诊出诊资源，0否，1是**/

    public List<Map<String, Object>> getPvInf() {
        return pvInf;
    }

    public void setPvInf(List<Map<String, Object>> pvInf) {
        this.pvInf = pvInf;
    }

    public List<Map<String, Object>> getPvSch() {
        return pvSch;
    }

    public void setPvSch(List<Map<String, Object>> pvSch) {
        this.pvSch = pvSch;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public String getPkDeptEx() {
        return pkDeptEx;
    }

    public void setPkDeptEx(String pkDeptEx) {
        this.pkDeptEx = pkDeptEx;
    }

    public String getDateAppt() {
        return dateAppt;
    }

    public void setDateAppt(String dateAppt) {
        this.dateAppt = dateAppt;
    }

    public String getPkDateslot() {
        return pkDateslot;
    }

    public void setPkDateslot(String pkDateslot) {
        this.pkDateslot = pkDateslot;
    }

    public String getPkEmp() {
        return pkEmp;
    }

    public void setPkEmp(String pkEmp) {
        this.pkEmp = pkEmp;
    }

    public String getPkPi() {
        return pkPi;
    }

    public void setPkPi(String pkPi) {
        this.pkPi = pkPi;
    }

    public String getPkSchres() {
        return pkSchres;
    }

    public void setPkSchres(String pkSchres) {
        this.pkSchres = pkSchres;
    }

    public String getPkSchsrv() {
        return pkSchsrv;
    }

    public void setPkSchsrv(String pkSchsrv) {
        this.pkSchsrv = pkSchsrv;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getEuPvtype() {
        return euPvtype;
    }

    public void setEuPvtype(String euPvtype) {
        this.euPvtype = euPvtype;
    }

    public String getPkDept() {
        return pkDept;
    }

    public void setPkDept(String pkDept) {
        this.pkDept = pkDept;
    }

    public String getPkEmpPhy() {
        return pkEmpPhy;
    }

    public void setPkEmpPhy(String pkEmpPhy) {
        this.pkEmpPhy = pkEmpPhy;
    }

    public Date getDateHap() {
        return dateHap;
    }

    public void setDateHap(Date dateHap) {
        this.dateHap = dateHap;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodeOrdtype() {
        return codeOrdtype;
    }

    public void setCodeOrdtype(String codeOrdtype) {
        this.codeOrdtype = codeOrdtype;
    }

    public String getPkOrd() {
        return pkOrd;
    }

    public void setPkOrd(String pkOrd) {
        this.pkOrd = pkOrd;
    }

    public String getPkItemSpec() {
        return pkItemSpec;
    }

    public void setPkItemSpec(String pkItemSpec) {
        this.pkItemSpec = pkItemSpec;
    }

    public String getSchResourceSpecial() {
        return schResourceSpecial;
    }

    public void setSchResourceSpecial(String schResourceSpecial) {
        this.schResourceSpecial = schResourceSpecial;
    }

    public List<String> getPkOrds() {
        return pkOrds;
    }

    public void setPkOrds(List<String> pkOrds) {
        this.pkOrds = pkOrds;
    }

    public String getFlagCg() {
        return flagCg;
    }

    public void setFlagCg(String flagCg) {
        this.flagCg = flagCg;
    }

    public String getPkItem() {
        return pkItem;
    }

    public void setPkItem(String pkItem) {
        this.pkItem = pkItem;
    }
}
