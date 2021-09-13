package com.zebone.nhis.ma.pub.zsba.vo;


import java.util.Date;

public class PvInfantLab{

    // 住院号
    private String codeIp;
    // 姓名
    private String namePi;
    // 性别
    private String dtSex;
    // 年龄
    private String age;
    //就诊开始时间
    private Date dateBegin;
    //就诊开始时间
    private Date dateEnd;
    // 住院天数
    private String days;
    // 病情
    private String levelDiseHint;
    // 护理等级
    private String levelNsHint;
    // 入科时间
    private Date dateAdmit;
    // 就诊科室
    private String pkDept;
    private String nameDept;
    // 就诊病区
    private String pkDeptNs;
    private String nameDeptNs;
    // 诊断
    private String descDiag;
    // 过敏史
    private String nameAl;
    // 医保计划
    private String nameHp;
    //就诊主键
    private String pkPv;
    //患者主键
    private String pkPi;
   /*
   * 就诊状态，pv表状态
   * */
    private String euStatus;

    public String getCodeIp() {return codeIp;}
    public void setCodeIp(String codeIp) {this.codeIp = codeIp;}

    public String getNamePi() {return namePi;}
    public void setNamePi(String namePi) {this.namePi = namePi;}

    public String getDtSex() {return dtSex;}
    public void setDtSex(String dtSex) {this.dtSex = dtSex;}

    public String getAge() {return age;}
    public void setAge(String age) {this.age = age;}

    public String getDays() {return days;}
    public void setDays(String days) {this.days = days;}

    public String getLevelDiseHint() {return levelDiseHint;}
    public void setLevelDiseHint(String levelDiseHint) {this.levelDiseHint = levelDiseHint;}

    public String getLevelNsHint() {return levelNsHint;}
    public void setLevelNsHint(String levelNsHint) {this.levelNsHint = levelNsHint;}

    public Date getDateAdmit() {return dateAdmit;}
    public void setDateAdmit(Date dateAdmit) {this.dateAdmit = dateAdmit;}

    public String getNameDept() {return nameDept;}
    public void setNameDept(String nameDept) {this.nameDept = nameDept;}

    public String getNameDeptNs() {return nameDeptNs;}
    public void setNameDeptNs(String nameDeptNs) {this.nameDeptNs = nameDeptNs;}

    public String getDescDiag() {return descDiag;}
    public void setDescDiag(String descDiag) {this.descDiag = descDiag;}

    public String getNameAl() {return nameAl;}
    public void setNameAl(String nameAl) {this.nameAl = nameAl;}

    public String getPkDept() {return pkDept;}
    public void setPkDept(String pkDept) {this.pkDept = pkDept;}

    public String getPkDeptNs() {return pkDeptNs;}
    public void setPkDeptNs(String pkDeptNs) {this.pkDeptNs = pkDeptNs;}

    public Date getDateBegin() {return dateBegin;}
    public void setDateBegin(Date dateBegin) {this.dateBegin = dateBegin;}

    public Date getDateEnd() {return dateEnd;}
    public void setDateEnd(Date dateEnd) {this.dateEnd = dateEnd;}

    public String getPkPv() {return pkPv;}
    public void setPkPv(String pkPv) {this.pkPv = pkPv;}

    public String getPkPi() {return pkPi;}
    public void setPkPi(String pkPi) {this.pkPi = pkPi;}

    public String getNameHp() {return nameHp;}
    public void setNameHp(String nameHp) {this.nameHp = nameHp;}

    public String getEuStatus() {
        return euStatus;
    }

    public void setEuStatus(String euStatus) {
        this.euStatus = euStatus;
    }
}
