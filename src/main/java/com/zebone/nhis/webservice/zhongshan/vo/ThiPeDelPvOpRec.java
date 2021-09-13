package com.zebone.nhis.webservice.zhongshan.vo;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @Classname ThiPeDelPvOpRec
 * @Description 15号接口
 * @Date 2021-06-15 10:05
 * @Created by wuqiang
 */
public class ThiPeDelPvOpRec {

    @NotBlank(message = "就诊主键不能为空")
    private String pkPv;

    @NotBlank(message = "取消人主键不能为空")
    private String pkEmpCancel;

    @NotBlank(message = "取消日期不能为空")
    private String dateCancel;


    public String getDateCancel() {
        return dateCancel;
    }

    public void setDateCancel(String dateCancel) {
        this.dateCancel = dateCancel;
    }

    public String getPkEmpCancel() {
        return pkEmpCancel;
    }

    public void setPkEmpCancel(String pkEmpCancel) {
        this.pkEmpCancel = pkEmpCancel;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    @Override
    public String toString() {
        return "ThiPeDelPvOpRec{" +
                "pkPv='" + pkPv + '\'' +
                ", pkEmpCancel='" + pkEmpCancel + '\'' +
                ", dateCancel='" + dateCancel + '\'' +
                '}';
    }
}
