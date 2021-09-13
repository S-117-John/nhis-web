package com.zebone.nhis.webservice.zhongshan.vo;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @Classname ThiCgItem
 * @Description 06 号接口子项
 * @Date 2021-04-08 10:38
 * @Created by wuqiang
 */
public class ThiCgItem {

    /**
     *就诊编码
     */
    @NotBlank(message = "就诊主键不能为空")
    private String pkPv;
    /**
     *费用主键
     */
    @NotBlank(message = "收费项目主键不能为空")
    private String pkItem;
    /**
     *数量
     */
    @NotBlank(message = "数量不能为空")
    private String quan;
    /**
     *开立科室编码
     */
    @NotBlank(message = "开立科室不能为空")
    private String pkDeptApp;
    /**
     *开立医生编码
     */
    @NotBlank(message = "开立医生不能为空")
    private String pkEmpApp;
    /**
     *执行科室编码
     */
    @NotBlank(message = "执行科室不能为空")
    private String pkDeptEx;
    /**
     *记费类型
     */
    @NotBlank(message = "记费类型不能为空")
    private String euBlType;
    /**
     *医嘱主键
     */
    @NotBlank(message = "医嘱主键不能为空")
    private String pkCnord;
    /**
     *医嘱序号
     */
    private String ordsn;
    /**
     *备注信息
     */
    private String noteCg;

    @Override
    public String toString() {
        return "ThiCgItem{" +
                "pkPv='" + pkPv + '\'' +
                ", pkItem='" + pkItem + '\'' +
                ", quan='" + quan + '\'' +
                ", pkDeptApp='" + pkDeptApp + '\'' +
                ", pkEmpApp='" + pkEmpApp + '\'' +
                ", pkDeptEx='" + pkDeptEx + '\'' +
                ", euBlType='" + euBlType + '\'' +
                ", pkCnord='" + pkCnord + '\'' +
                ", ordsn='" + ordsn + '\'' +
                ", noteCg='" + noteCg + '\'' +
                '}';
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getPkItem() {
        return pkItem;
    }

    public void setPkItem(String pkItem) {
        this.pkItem = pkItem;
    }

    public String getQuan() {
        return quan;
    }

    public void setQuan(String quan) {
        this.quan = quan;
    }

    public String getPkDeptApp() {
        return pkDeptApp;
    }

    public void setPkDeptApp(String pkDeptApp) {
        this.pkDeptApp = pkDeptApp;
    }

    public String getPkEmpApp() {
        return pkEmpApp;
    }

    public void setPkEmpApp(String pkEmpApp) {
        this.pkEmpApp = pkEmpApp;
    }

    public String getPkDeptEx() {
        return pkDeptEx;
    }

    public void setPkDeptEx(String pkDeptEx) {
        this.pkDeptEx = pkDeptEx;
    }

    public String getEuBlType() {
        return euBlType;
    }

    public void setEuBlType(String euBlType) {
        this.euBlType = euBlType;
    }

    public String getOrdsn() {
        return ordsn;
    }

    public void setOrdsn(String ordsn) {
        this.ordsn = ordsn;
    }

    public String getNoteCg() {
        return noteCg;
    }

    public void setNoteCg(String noteCg) {
        this.noteCg = noteCg;
    }

	public String getPkCnord() {
		return pkCnord;
	}

	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}
    
}
