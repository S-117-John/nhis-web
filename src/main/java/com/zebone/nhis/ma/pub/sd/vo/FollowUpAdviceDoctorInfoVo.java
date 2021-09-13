package com.zebone.nhis.ma.pub.sd.vo;

public class FollowUpAdviceDoctorInfoVo {

    /**
     * 医生ID
     */
    private String adviceDoctorId;

    /**
     *医生姓名
     */
    private String doctorName;

    /**
     * 性别
     */
    private String gender;

    /**
     *医生手机号
     */
    private String telephone;
    /**
     * 工作电话
     */
    private String deptTel;

    /**
     *证件号码
     */
    private String idNo;

    /**
     *科室名称
     */
    private String deptName;

    /**
     *来源信息：
     * 1:来源his
     * 2：来源emr
     * 20：emr和his共享
     */
    private String share;

    /**
     *数据来源：
     * 1：HIS住院医嘱单
     * 2：HIS门诊医嘱单
     * 3: EMR出院记录
     */
    private String source;

    public String getDeptTel() {
        return deptTel;
    }

    public void setDeptTel(String deptTel) {
        this.deptTel = deptTel;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAdviceDoctorId() {
        return adviceDoctorId;
    }

    public void setAdviceDoctorId(String adviceDoctorId) {
        this.adviceDoctorId = adviceDoctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
