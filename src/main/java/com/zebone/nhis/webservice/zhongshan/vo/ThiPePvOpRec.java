package com.zebone.nhis.webservice.zhongshan.vo;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @Classname ThiPePvOpRec
 * @Description 14号接口入参
 * @Date 2021-06-10 17:25
 * @Created by wuqiang
 */
public class ThiPePvOpRec {
    /**
     *门诊号
     */
    @NotBlank(message = "门诊号不能为空")
    private String codeOp;
    /**
     *科室主键
     */
    @NotBlank(message = "科室主键不能为空")
    private String pkDept;
    /**
     *登记人主键
     */
    @NotBlank(message = "登记人主键不能为空")
    private String pkEmpReg;
    /**
     *登记日期
     */
    @NotBlank(message = "登记日期不能为空")
    private String dateReg;
    /**
     *结算方式主键
     */
    @NotBlank(message = "结算方式主键不能为空")
    private String pkHp;
    /**
     *就诊类型
     */
    @NotBlank(message = "就诊类型不能为空")
    private String pvType;
   //返回参数
    /**
     *患者就诊主键
     */
    private String pkPv;
    /**
     *患者门诊次数
     */
    private String pvTimes;

    public String getCodeOp() {
        return codeOp;
    }

    public void setCodeOp(String codeOp) {
        this.codeOp = codeOp;
    }

    public String getPkDept() {
        return pkDept;
    }

    public void setPkDept(String pkDept) {
        this.pkDept = pkDept;
    }

    public String getPkEmpReg() {
        return pkEmpReg;
    }

    public void setPkEmpReg(String pkEmpReg) {
        this.pkEmpReg = pkEmpReg;
    }

    public String getDateReg() {
        return dateReg;
    }

    public void setDateReg(String dateReg) {
        this.dateReg = dateReg;
    }

    public String getPkHp() {
        return pkHp;
    }

    public void setPkHp(String pkHp) {
        this.pkHp = pkHp;
    }

    public String getPvType() {
        return pvType;
    }

    public void setPvType(String pvType) {
        this.pvType = pvType;
    }

    @Override
    public String toString() {
        return "ThiPePvOpRec{" +
                "codeOp='" + codeOp + '\'' +
                ", pkDept='" + pkDept + '\'' +
                ", pkEmpReg='" + pkEmpReg + '\'' +
                ", dateReg='" + dateReg + '\'' +
                ", pkHp='" + pkHp + '\'' +
                ", pvType='" + pvType + '\'' +
                '}';
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getPvTimes() {
        return pvTimes;
    }

    public void setPvTimes(String pvTimes) {
        this.pvTimes = pvTimes;
    }
}
