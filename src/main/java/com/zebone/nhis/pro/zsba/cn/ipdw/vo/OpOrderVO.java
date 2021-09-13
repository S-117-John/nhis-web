package com.zebone.nhis.pro.zsba.cn.ipdw.vo;

import java.util.Date;

public class OpOrderVO {
    private String pkDeptNsCur;//当前就诊病区
    private String bedNo;//床号
    private String namePi;//患者姓名
    private String valAtt;//危重
    private String nameUnitDosage;//剂量单位名称
    private String nameUnit;//用量单位名称
    private String nameUsage;//用法名称
    private String nameUnitCg;//计费单位名称
    private String nameFreq;//频次名称
    private String newbornName;//新生儿姓名
    private String nameDeptExec;//执行科室名称
    private String sign;
    private Integer cnt;//周期执行次数
    private String euCycle;//周期类型0按天1按周2按小时
    private String nameSupplyItem;//用法附加费名称
    private String nameTubeItem;//检验容器费用名称
    private String nameOrdtype;//医嘱类型名称
    private String nameHp;//医保名称
    private String euBoil;//代煎方式
    private String flagSpec;//特诊标志
    private String birthDate;//出生日期
    private Integer countOcc;//执行单数量
    private String pkSupplycate;//用法分类
    private String labName;//检验标本名称
    private String codeIp;//患者住院号
    private String dtOplevel;//手术级别
    private String dtAnae;//麻醉方式
    private Date dateApply;//申请日期
    private String euOptype;//手术类型
    private String descOp;//手术描述
    private String nameOrd;//手术名称
    private String nameEmpChk;//核对人d
    private Date dateChk;//核对时间
    private String noteOrd;//医嘱备注
    private String nameEmpOrd;//申请人
    private String deptName;//申请科室
    private String euStatusOrd;//医嘱状态
    private Date ts;//数据库记录时间
    private String pkCnord;//医嘱主键
    /**
     * 注射药类型
     */
    private String dtInjtype;

    public String getNameTubeItem() {
        return nameTubeItem;
    }
    public void setNameTubeItem(String nameTubeItem) {
        this.nameTubeItem = nameTubeItem;
    }
    public String getNameSupplyItem() {
        return nameSupplyItem;
    }
    public void setNameSupplyItem(String nameSupplyItem) {
        this.nameSupplyItem = nameSupplyItem;
    }
    public String getEuCycle() {
        return euCycle;
    }
    public void setEuCycle(String euCycle) {
        this.euCycle = euCycle;
    }
    public Integer getCnt() {
        return cnt;
    }
    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }
    public String getSign() {
        return sign;
    }
    public void setSign(String sign) {
        this.sign = sign;
    }
    public String getPkDeptNsCur() {
        return pkDeptNsCur;
    }
    public void setPkDeptNsCur(String pkDeptNsCur) {
        this.pkDeptNsCur = pkDeptNsCur;
    }
    public String getBedNo() {
        return bedNo;
    }
    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }
    public String getNamePi() {
        return namePi;
    }
    public void setNamePi(String namePi) {
        this.namePi = namePi;
    }
    public String getNameUnitDosage() {
        return nameUnitDosage;
    }
    public void setNameUnitDosage(String nameUnitDosage) {
        this.nameUnitDosage = nameUnitDosage;
    }
    public String getNameUnit() {
        return nameUnit;
    }
    public void setNameUnit(String nameUnit) {
        this.nameUnit = nameUnit;
    }
    public String getNameUsage() {
        return nameUsage;
    }
    public void setNameUsage(String nameUsage) {
        this.nameUsage = nameUsage;
    }
    public String getNameUnitCg() {
        return nameUnitCg;
    }
    public void setNameUnitCg(String nameUnitCg) {
        this.nameUnitCg = nameUnitCg;
    }
    public String getNameFreq() {
        return nameFreq;
    }
    public void setNameFreq(String nameFreq) {
        this.nameFreq = nameFreq;
    }
    public String getNewbornName() {
        return newbornName;
    }
    public void setNewbornName(String newbornName) {
        this.newbornName = newbornName;
    }
    public String getNameDeptExec() {
        return nameDeptExec;
    }
    public void setNameDeptExec(String nameDeptExec) {
        this.nameDeptExec = nameDeptExec;
    }
    public String getNameOrdtype() {
        return nameOrdtype;
    }
    public void setNameOrdtype(String nameOrdtype) {
        this.nameOrdtype = nameOrdtype;
    }
    public String getNameHp() {
        return nameHp;
    }
    public void setNameHp(String nameHp) {
        this.nameHp = nameHp;
    }
    public String getEuBoil() {
        return euBoil;
    }
    public void setEuBoil(String euBoil) {
        this.euBoil = euBoil;
    }
    public String getFlagSpec() {
        return flagSpec;
    }
    public void setFlagSpec(String flagSpec) {
        this.flagSpec = flagSpec;
    }
    public String getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
    public String getDtInjtype() {
        return dtInjtype;
    }
    public void setDtInjtype(String dtInjtype) {
        this.dtInjtype = dtInjtype;
    }
    public String getValAtt() {
        return valAtt;
    }
    public void setValAtt(String valAtt) {
        this.valAtt = valAtt;
    }
    public Integer getCountOcc() {
        return countOcc;
    }
    public void setCountOcc(Integer countOcc) {
        this.countOcc = countOcc;
    }
    public String getPkSupplycate() {return pkSupplycate;}
    public void setPkSupplycate(String pkSupplycate) {this.pkSupplycate = pkSupplycate;}
    public String getLabName() { return labName; }
    public void setLabName(String labName) { this.labName = labName; }

    public String getCodeIp() {
        return codeIp;
    }

    public void setCodeIp(String codeIp) {
        this.codeIp = codeIp;
    }

    public String getDtOplevel() {
        return dtOplevel;
    }

    public void setDtOplevel(String dtOplevel) {
        this.dtOplevel = dtOplevel;
    }

    public String getDtAnae() {
        return dtAnae;
    }

    public void setDtAnae(String dtAnae) {
        this.dtAnae = dtAnae;
    }

    public Date getDateApply() {
        return dateApply;
    }

    public void setDateApply(Date dateApply) {
        this.dateApply = dateApply;
    }

    public String getEuOptype() {
        return euOptype;
    }

    public void setEuOptype(String euOptype) {
        this.euOptype = euOptype;
    }

    public String getDescOp() {
        return descOp;
    }

    public void setDescOp(String descOp) {
        this.descOp = descOp;
    }

    public String getNameOrd() {
        return nameOrd;
    }

    public void setNameOrd(String nameOrd) {
        this.nameOrd = nameOrd;
    }

    public String getNameEmpChk() {
        return nameEmpChk;
    }

    public void setNameEmpChk(String nameEmpChk) {
        this.nameEmpChk = nameEmpChk;
    }

    public Date getDateChk() {
        return dateChk;
    }

    public void setDateChk(Date dateChk) {
        this.dateChk = dateChk;
    }

    public String getNoteOrd() {
        return noteOrd;
    }

    public void setNoteOrd(String noteOrd) {
        this.noteOrd = noteOrd;
    }

    public String getNameEmpOrd() {
        return nameEmpOrd;
    }

    public void setNameEmpOrd(String nameEmpOrd) {
        this.nameEmpOrd = nameEmpOrd;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getEuStatusOrd() {
        return euStatusOrd;
    }

    public void setEuStatusOrd(String euStatusOrd) {
        this.euStatusOrd = euStatusOrd;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public String getPkCnord() {
        return pkCnord;
    }

    public void setPkCnord(String pkCnord) {
        this.pkCnord = pkCnord;
    }
}
