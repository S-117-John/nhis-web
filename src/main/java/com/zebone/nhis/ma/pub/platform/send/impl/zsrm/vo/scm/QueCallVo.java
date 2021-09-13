package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.scm;

public class QueCallVo {

    /**患者登记流水 */
    private String queueid;
    /** 患者编号*/
    private String patientinfoid;
    /** 病人姓名*/
    private String patientinfoname;
    /** 队列类型编号*/
    private String queuetypeid;
    /** 排队序号*/
    private String registerid;
    /** 队列编号*/
    private String queuenum;
    /** 队列级别*/
    private String subqueueorder;
    /** 队列级别名称*/
    private String subqueuetype;
    /** 午别*/
    private String timeinterval;
    /** 医生编号*/
    private String doctorloginid;
    /**0 上屏  |  1 下屏 */
    private String isdeleted;
    /**抓药完成时间 */
    private String reservetime;
    /** 扩展字段*/
    private String content;
    /** 身份证号码*/
    private String idcard;
    /** 医院就诊卡*/
    private String hospitalcard;
    /** 医保卡号*/
    private String socialsecuritycard;

    public String getQueueid() {
        return queueid;
    }

    public void setQueueid(String queueid) {
        this.queueid = queueid;
    }

    public String getPatientinfoid() {
        return patientinfoid;
    }

    public void setPatientinfoid(String patientinfoid) {
        this.patientinfoid = patientinfoid;
    }

    public String getPatientinfoname() {
        return patientinfoname;
    }

    public void setPatientinfoname(String patientinfoname) {
        this.patientinfoname = patientinfoname;
    }

    public String getQueuetypeid() {
        return queuetypeid;
    }

    public void setQueuetypeid(String queuetypeid) {
        this.queuetypeid = queuetypeid;
    }

    public String getRegisterid() {
        return registerid;
    }

    public void setRegisterid(String registerid) {
        this.registerid = registerid;
    }

    public String getQueuenum() {
        return queuenum;
    }

    public void setQueuenum(String queuenum) {
        this.queuenum = queuenum;
    }

    public String getSubqueueorder() {
        return subqueueorder;
    }

    public void setSubqueueorder(String subqueueorder) {
        this.subqueueorder = subqueueorder;
    }

    public String getSubqueuetype() {
        return subqueuetype;
    }

    public void setSubqueuetype(String subqueuetype) {
        this.subqueuetype = subqueuetype;
    }

    public String getTimeinterval() {
        return timeinterval;
    }

    public void setTimeinterval(String timeinterval) {
        this.timeinterval = timeinterval;
    }

    public String getDoctorloginid() {
        return doctorloginid;
    }

    public void setDoctorloginid(String doctorloginid) {
        this.doctorloginid = doctorloginid;
    }

    public String getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(String isdeleted) {
        this.isdeleted = isdeleted;
    }

    public String getReservetime() {
        return reservetime;
    }

    public void setReservetime(String reservetime) {
        this.reservetime = reservetime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getHospitalcard() {
        return hospitalcard;
    }

    public void setHospitalcard(String hospitalcard) {
        this.hospitalcard = hospitalcard;
    }

    public String getSocialsecuritycard() {
        return socialsecuritycard;
    }

    public void setSocialsecuritycard(String socialsecuritycard) {
        this.socialsecuritycard = socialsecuritycard;
    }
}
