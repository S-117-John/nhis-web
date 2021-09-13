package com.zebone.nhis.webservice.zhongshan.vo;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @Classname OutpatientOpChargeVo
 * @Description 三方记费接口实体类
 * @Date 2021-03-04 19:30
 * @Created by wuqiang
 */
public class OutpatientOpChargeVo {
    /**
     * 门诊号
     */
    @NotBlank(message = "门诊号不能为空")
    private String code_op;
    /**
     * 就诊流水号
     */

    private String codePv;
    /**
     * 收费项目编码
     */
    @NotBlank(message = "收费项目编码不能为空")
    private String code_item;
    /**
     * 记费数量
     */
    @NotBlank(message = "记费数量不能为空")
    private String quan;
    /**
     * 开立机构编码
     */
    @NotBlank(message = "开立机构编码不能为空")
    private String code_org_app;
    /**
     * 开立科室编码
     */
    @NotBlank(message = "开立科室编码不能为空")
    private String code_dept_app;
    /**
     * 开立医生工号
     */
    @NotBlank(message = "开立医生工号不能为空")
    private String code_emp_app;
    /**
     * 开立医生考勤科室编码
     */
    @NotBlank(message = "开立医生考勤科室编码不能为空")
    private String code_dept_job;
    /**
     * 执行机构编码
     */
    @NotBlank(message = "执行机构编码不能为空")
    private String code_org_ex;
    /**
     * 执行科室编码
     */
    @NotBlank(message = "执行科室编码不能为空")
    private String code_dept_ex;
    /**
     * 执行医生编码
     */
    @NotBlank(message = "执行医生编码编码不能为空")
    private String code_emp_ex;
    /**
     * 费用发生日期
     */
    @NotBlank(message = "费用发生日期编码不能为空")
    private String date_hap;
    /**
     * 记费部门编码
     */
    @NotBlank(message = "记费部门编码编码不能为空")
    private String code_dept_cg;
    /**
     * 记费人员工号
     */
    @NotBlank(message = "记费人员工号编码不能为空")
    private String code_emp_cg;

    /**
     * 医保计划
     */
    private String code_hp;
    public String getCode_op() {
        return code_op;
    }

    public void setCode_op(String code_op) {
        this.code_op = code_op;
    }

    public String getCode_item() {
        return code_item;
    }

    public void setCode_item(String code_item) {
        this.code_item = code_item;
    }

    public String getQuan() {
        return quan;
    }

    public void setQuan(String quan) {
        this.quan = quan;
    }

    public String getCode_org_app() {
        return code_org_app;
    }

    public void setCode_org_app(String code_org_app) {
        this.code_org_app = code_org_app;
    }

    public String getCode_dept_app() {
        return code_dept_app;
    }

    public void setCode_dept_app(String code_dept_app) {
        this.code_dept_app = code_dept_app;
    }

    public String getCode_emp_app() {
        return code_emp_app;
    }

    public void setCode_emp_app(String code_emp_app) {
        this.code_emp_app = code_emp_app;
    }

    public String getCode_dept_job() {
        return code_dept_job;
    }

    public void setCode_dept_job(String code_dept_job) {
        this.code_dept_job = code_dept_job;
    }

    public String getCode_org_ex() {
        return code_org_ex;
    }

    public void setCode_org_ex(String code_org_ex) {
        this.code_org_ex = code_org_ex;
    }

    public String getCode_dept_ex() {
        return code_dept_ex;
    }

    public void setCode_dept_ex(String code_dept_ex) {
        this.code_dept_ex = code_dept_ex;
    }

    public String getCode_emp_ex() {
        return code_emp_ex;
    }

    public void setCode_emp_ex(String code_emp_ex) {
        this.code_emp_ex = code_emp_ex;
    }

    public String getDate_hap() {
        return date_hap;
    }

    public void setDate_hap(String date_hap) {
        this.date_hap = date_hap;
    }

    public String getCode_dept_cg() {
        return code_dept_cg;
    }

    public void setCode_dept_cg(String code_dept_cg) {
        this.code_dept_cg = code_dept_cg;
    }

    public String getCode_emp_cg() {
        return code_emp_cg;
    }

    public void setCode_emp_cg(String code_emp_cg) {
        this.code_emp_cg = code_emp_cg;
    }

    public String getCodePv() {
        return codePv;
    }

    public void setCodePv(String codePv) {
        this.codePv = codePv;
    }

    @Override
    public String toString() {
        return "OutpatientOpChargeVo{" +
                "code_op='" + code_op + '\'' +
                ", codePv='" + codePv + '\'' +
                ", code_item='" + code_item + '\'' +
                ", quan='" + quan + '\'' +
                ", code_org_app='" + code_org_app + '\'' +
                ", code_dept_app='" + code_dept_app + '\'' +
                ", code_emp_app='" + code_emp_app + '\'' +
                ", code_dept_job='" + code_dept_job + '\'' +
                ", code_org_ex='" + code_org_ex + '\'' +
                ", code_dept_ex='" + code_dept_ex + '\'' +
                ", code_emp_ex='" + code_emp_ex + '\'' +
                ", date_hap='" + date_hap + '\'' +
                ", code_dept_cg='" + code_dept_cg + '\'' +
                ", code_emp_cg='" + code_emp_cg + '\'' +
                '}';
    }

    public String getCode_hp() {
        return code_hp;
    }

    public void setCode_hp(String code_hp) {
        this.code_hp = code_hp;
    }
}
