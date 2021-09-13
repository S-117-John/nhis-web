package com.zebone.nhis.ma.pub.platform.zsba.vo;

public class Employee {

    private String user_id;
    private String user_name;
    private String pk_emp;


    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id == null ? null : user_id.trim();
    }

    public String getUser_name() {
        return user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name == null ? null : user_name.trim();
    }

    public String getPk_emp() {return pk_emp;}
    public void setPk_emp(String pk_emp) {this.pk_emp = pk_emp;}
}