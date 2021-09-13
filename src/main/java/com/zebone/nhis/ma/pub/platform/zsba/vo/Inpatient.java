package com.zebone.nhis.ma.pub.platform.zsba.vo;

public class Inpatient {

    private String patient_id;
    private Integer visit_id;
    private String inp_no;
    private String inpatient_no;
    private String ward_code;
    private String ward_name;
    private String dept_code;
    private String dept_name;
    private String bed_no;
    private String bed_label;
    private String name;
    private String sex;
    private String pk_pv;//患者本次就诊主键
    private String pk_org;//患者所属机构
    private String pk_dept;//患者本次就诊科室
    private String pk_dept_ns;//患者本次就诊病区
    private  String ip_times;//患者本次住院次数
    private  String code_pv;//患者本次住院就诊编码

    public String getPatient_id() {
        return patient_id;
    }
    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id == null ? null : patient_id.trim();
    }

    public Integer getVisit_id() {
        return visit_id;
    }
    public void setVisit_id(Integer visit_id) {
        this.visit_id = visit_id;
    }

    public String getInp_no() {
        return inp_no;
    }
    public void setInp_no(String inp_no) {
        this.inp_no = inp_no == null ? null : inp_no.trim();
    }

    public String getInpatient_no() {
        return inpatient_no;
    }
    public void setInpatient_no(String inpatient_no) {this.inpatient_no = inpatient_no == null ? null : inpatient_no.trim();}

    public String getWard_code() {
        return ward_code;
    }
    public void setWard_code(String ward_code) {
        this.ward_code = ward_code == null ? null : ward_code.trim();
    }

    public String getWard_name() {
        return ward_name;
    }
    public void setWard_name(String ward_name) {
        this.ward_name = ward_name == null ? null : ward_name.trim();
    }

    public String getDept_code() {
        return dept_code;
    }
    public void setDept_code(String dept_code) {
        this.dept_code = dept_code == null ? null : dept_code.trim();
    }

    public String getDept_name() {
        return dept_name;
    }
    public void setDept_name(String dept_name) {
        this.dept_name = dept_name == null ? null : dept_name.trim();
    }

    public String getBed_no() {return bed_no;}
    public void setBed_no(String bed_no) {
        this.bed_no = bed_no == null ? null : bed_no.trim();
    }

    public String getBed_label() {
        return bed_label;
    }
    public void setBed_label(String bed_label) {
        this.bed_label = bed_label == null ? null : bed_label.trim();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getPk_pv() {return pk_pv;}
    public void setPk_pv(String pk_pv) {this.pk_pv = pk_pv;}

    public String getPk_org() {return pk_org;}
    public void setPk_org(String pk_org) {this.pk_org = pk_org;}

    public String getPk_dept() {return pk_dept;}
    public void setPk_dept(String pk_dept) {this.pk_dept = pk_dept;}

    public String getPk_dept_ns() {return pk_dept_ns;}
    public void setPk_dept_ns(String pk_dept_ns) {this.pk_dept_ns = pk_dept_ns;}

    public String getIp_times() {
        return ip_times;
    }

    public void setIp_times(String ip_times) {
        this.ip_times = ip_times;
    }

    public String getCode_pv() {
        return code_pv;
    }

    public void setCode_pv(String code_pv) {
        this.code_pv = code_pv;
    }
}