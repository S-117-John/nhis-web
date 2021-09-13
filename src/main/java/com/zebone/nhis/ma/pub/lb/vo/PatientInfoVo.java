package com.zebone.nhis.ma.pub.lb.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class PatientInfoVo {

    /**
     * 就诊机构编码
     */
    private String orgcode;
    /**
     * 就诊机构名称
     */
    private String orgname;
    /**
     * 就诊流水号
     */
    private String Visitno;
    /**
     * 身份证号
     */
    private String idno;
    /**
     * 患者姓名
     */
    private String name;
    /**
     * 年龄
     */
    private int age;
    /**
     * 性别
     */
    private int sex;
    /**
     * 医保补偿类别
     */
    private String RedeemType;

    /**
     * 科室名称
     */
    private String depName;
    /**
     * 床位号码
     */
    private String bedNo;
    /** 就诊日期 */
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date inofficeDate;
    /**
     * 入院日期
     */
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date officeDate;
    /**
     * 出院日期
     */
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date leaveDate;
    /**
     * 诊断编码
     */
    @JSONField(name = "Picd01")
    private String picd01;
    /**
     * 诊断名称
     */
    @JSONField(name = "Picd02")
    private String picd02;
    /**
     * 医生编码
     */
    private String DoctorCode;
    /**
     * 医生姓名
     */
    private String DoctorName;
    /**
     * 总费用
     */
    private BigDecimal totalCosts;
    /**
     * 参保类型
     */
    private String protecttype;
    /**
     * 调用类型
     */
    private String type;
    /**
     * 治疗方式编码
     */
    private String treatCode;
    /**
     * 医嘱明细参数
     */
    private List<MedicalAdviceVo> inputdata;


    public String getOrgcode() {
        return orgcode;
    }

    public void setOrgcode(String orgcode) {
        this.orgcode = orgcode;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }
    @JSONField(name = "Visitno")
    public String getVisitno() {
        return Visitno;
    }

    public void setVisitno(String visitno) {
        Visitno = visitno;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
    @JSONField(name = "Redeemtype")
    public String getRedeemType() {
        return RedeemType;
    }

    public void setRedeemType(String redeemType) {
        RedeemType = redeemType;
    }

    public Date getInofficeDate() {
        return inofficeDate;
    }

    public void setInofficeDate(Date inofficeDate) {
        this.inofficeDate = inofficeDate;
    }

    public Date getOfficeDate() {
        return officeDate;
    }

    public void setOfficeDate(Date officeDate) {
        this.officeDate = officeDate;
    }

    public Date getLeaveDate() {
//        if (null == this.leaveDate){
//            return  new Date();
//        }else {
//            return  this.leaveDate;
//        }
        return leaveDate;
    }

    public void setLeaveDate(Date leaveDate) {
        this.leaveDate = leaveDate;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getBedNo() {
        return bedNo;
    }

    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }

    public String getPicd01() {
        return picd01;
    }

    public void setPicd01(String picd01) {
        this.picd01 = picd01;
    }

    public String getPicd02() {
        return picd02;
    }

    public void setPicd02(String picd02) {
        this.picd02 = picd02;
    }

    @JSONField(name = "DoctorCode")
    public String getDoctorCode() {
        return DoctorCode;
    }

    public void setDoctorCode(String doctorCode) {
        DoctorCode = doctorCode;
    }
    @JSONField(name = "DoctorName")
    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }

    public BigDecimal getTotalCosts() {
        return totalCosts;
    }

    public void setTotalCosts(BigDecimal totalCosts) {
        this.totalCosts = totalCosts;
    }

    public String getProtecttype() {
        return protecttype;
    }

    public void setProtecttype(String protecttype) {
        this.protecttype = protecttype;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTreatCode() {
        return treatCode;
    }

    public void setTreatCode(String treatCode) {
        this.treatCode = treatCode;
    }

    public List<MedicalAdviceVo> getInputdata() {
        return inputdata;
    }

    public void setInputdata(List<MedicalAdviceVo> inputdata) {
        this.inputdata = inputdata;
    }

    @Override
    public String toString() {
        return "PatientInfoVo{" +
                "orgcode='" + orgcode + '\'' +
                ", orgname='" + orgname + '\'' +
                ", Visitno='" + Visitno + '\'' +
                '}';
    }
}
