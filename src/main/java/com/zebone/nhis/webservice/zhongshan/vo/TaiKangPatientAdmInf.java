package com.zebone.nhis.webservice.zhongshan.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 12370
 * @Classname TaiKangPatientAdmInf
 * @Description 患者入院信息返回实体类
 * @Date 2020-11-23 10:51
 * @Created by wuqiang
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "MedicalInfo")
public class TaiKangPatientAdmInf {
    /*
     * 业务流水号_入院日期（拼接）
     * */

    @XmlElement(name = "TreatSerialNo")
    private String codeIpDateAdmit;

    /*
     * 就诊号-业务流水号
     * */

    @XmlElement(name = "PatientNumber")
    private String codeIp;

    /*
     * 入科时间-入院时间
     * */

    @XmlElement(name = "InHosDate")
    private String dateAdmit;
    /*
     * 出生日期
     * */

    @XmlElement(name = "BirthDate")
    private String BirthDate;

    /*
     * 证件号码-证件号码
     * */

    @XmlElement(name = "CardId")
    private String idNo;
    /*
     * 姓名-用户名
     * */

    @XmlElement(name = "Name")
    private String name;

    /*
     * 性别-性别
     * */

    @XmlElement(name = "Sex")
    private String Sex;

    /*
     * 科室-科室
     * */

    @XmlElement(name = "HospitalDepartmentName")
    private String pkDeptName;
    /*
     * 病区-病区
     * */

    @XmlElement(name = "AreaName")

    private String pkDeptNsName;

    /*
     * 房间号-病房号
     * */

    @XmlElement(name = "WardNumber")
    private String houseno;

    /*
     * 床位号
     * */

    @XmlElement(name = "BedNumber")
    private String BedNo;
    /*
     * 初始诊断名称
     * */

    @XmlElement(name = "InitialDiagnoses")
    private String InitialDiagnoses;
    /*
     * 手机号
     * */



    public String getcodeIp() {
        return codeIp;
    }

    public void setcodeIp(String codeIp) {
        this.codeIp = codeIp;
    }



    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getPkDeptName() {
        return pkDeptName;
    }

    public void setPkDeptName(String pkDeptName) {
        this.pkDeptName = pkDeptName;
    }

    public String getPkDeptNsName() {
        return pkDeptNsName;
    }

    public void setPkDeptNsName(String pkDeptNsName) {
        this.pkDeptNsName = pkDeptNsName;
    }

    public String getHouseno() {
        return houseno;
    }

    public void setHouseno(String houseno) {
        this.houseno = houseno;
    }

    public String getBedNo() {
        return BedNo;
    }

    public void setBedNo(String bedNo) {
        BedNo = bedNo;
    }

    public String getInitialDiagnoses() {
        return InitialDiagnoses;
    }

    public void setInitialDiagnoses(String initialDiagnoses) {
        InitialDiagnoses = initialDiagnoses;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    @XmlElement(name = "Phone")

    private String MOBILE;

    public String getCodeIpDateAdmit() {
        return codeIpDateAdmit;
    }

    public void setCodeIpDateAdmit(String codeIpDateAdmit) {
        this.codeIpDateAdmit = codeIpDateAdmit;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    public String getDateAdmit() {
        return dateAdmit;
    }

    public void setDateAdmit(String dateAdmit) {
        this.dateAdmit = dateAdmit;
    }
}
