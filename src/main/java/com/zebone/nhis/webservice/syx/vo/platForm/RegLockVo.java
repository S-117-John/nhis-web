package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RegLockVo {
    /**
     * 号源锁定ID
     */
    @XmlElement(name = "lockId")
    private String lockId;
    /**
     * 科室代码
     */
    @XmlElement(name = "deptId")
    private String deptId;
    /**
     * 外部预约系统的订单号
     */
    @XmlElement(name = "orderId")
    private String orderId;
    /**
     * 医生代码
     */
    @XmlElement(name = "doctorId")
    private String doctorId;
    /**
     * 出诊日期，格式：YYYY-MM-DD
     */
    @XmlElement(name = "regDate")
    private String regDate;
    /**
     * 时段ID
     */
    @XmlElement(name = "timeId")
    private String timeID;
    
    @XmlElement(name = "timeRegInfo")
    private TimeRegInfo timeRegInfo;
    
    /**
     * 分时开始时间，格式：HH:MI
     */
    @XmlElement(name = "startTime")
    private String startTime;
    /**
     * 分时结束时间，格式：HH:MI
     */
    @XmlElement(name = "endTime")
    private String endTime;
    /**
     * 预约来源
     * 1-健康之路
     * 2-EMEM
     * 3-电话（114）
     * 7-农行网点
     * 8-医享网微信
     * 9-医享网支付宝
     * 11-平安挂号
     * 13-翼健康
     * 40-医程通
     * 58-华医App
     * 107-银联（建自助）
     * 108-微信（建自助）
     * 109-支付宝（建自助）
     */
    @XmlElement(name = "orderType")
    private String orderType;
    /**
     * 挂号费(单位“分”)
     */
    @XmlElement(name = "regFee")
    private String regFee;
    /**
     * 诊疗费(单位“分”)
     */
    @XmlElement(name = "treatFee")
    private String treatFee;
    /**
     * 处理结果代码：0-锁定号源成功
     */
    @XmlElement(name = "resultCode")
    private String resultCode;
    /**
     * 处理结果描述
     */
    @XmlElement(name = "resultDesc")
    private String resultDesc;
    /**
     * 用户对应HIS系统患者ID
     */
    @XmlElement(name = "userHisPatientId")
    private String userHisPatientId;
    /**
     * 患者身份证件号码
     */
    @XmlElement(name = "userIdCard")
    private String userIdCard;
    /**
     * 患者诊疗卡号码
     */
    @XmlElement(name = "userJKK")
    private String userJkk;
    /**
     * 患者市民卡号码
     */
    @XmlElement(name = "userSMK")
    private String userSmk;
    /**
     * 患者医保卡号码
     */
    @XmlElement(name = "userYBK")
    private String userYbk;
    /**
     * 患者监护人身份证件号码
     */
    @XmlElement(name = "userParentIdCard")
    private String userParentIdCard;
    /**
     * 患者农行卡号
     */
    @XmlElement(name = "userNHCard")
    private String userNhCard;
    /**
     * 患者姓名
     */
    @XmlElement(name = "userName")
    private String userName;
    /**
     * 患者性别：M-男性 F-女性
     */
    @XmlElement(name = "userGender")
    private String userGender;
    /**
     * 患者电话
     */
    @XmlElement(name = "userMobile")
    private String userMobile;
    /**
     * 患者出生日期：YYYY-MM-DD
     */
    @XmlElement(name = "userBirthday")
    private String userBirthday;
    /**
     * 座席工号
     */
    @XmlElement(name = "agentId")
    private String agentId;
    /**
     * 下订单时间，格式：
     * YYYY-MM-DD HH24:MI:SS
     */
    @XmlElement(name = "orderTime")
    private String orderTime;
    /**
     * 挂号费
     */
    @XmlElement(name = "fee")
    private String fee;
    /**
     * 移动加号标识（默认为0）
     * 0-非加号
     * 1-加号
     */
    @XmlElement(name = "addFlag")
    private String addFlag;
    /**
     * 当resultCode=0时，不可为空，存放HIS系统生成的订单号。
     */
    @XmlElement(name = "orderIdHis")
    private String orderIdHis;
    /**
     * 患者标志（HIS需要根据订单中的患者信息如身份证件号码判断是否在HIS里存在的标志，在取号环节，如果该字段是0，则预约系统提示患者去挂号窗口登记注册）
     * 0-初次就诊患者
     * 1-复诊患者
     */
    @XmlElement(name = "userFlag")
    private String userFlag;
    /**
     * HIS系统生成的候诊时间。
     */
    @XmlElement(name = "waitTime")
    private String waitTime;
    /**
     * HIS系统生成的就诊诊室名称
     */
    @XmlElement(name = "diagnoseRoomName")
    private String diagnoseRoomName;
    /**
     * 取消时间，格式：
     * YYYY-MM-DD HH24:MI:SS
     */
    @XmlElement(name = "cancelTime")
    private String cancelTime;
    /**
     * 取消原因
     */
    @XmlElement(name = "cancelReason")
    private String cancelReason;
    /**
     * 就诊顺序号或者时间
     */
    @XmlElement(name = "infoSeq")
    private String infoSeq;
    /**
     * 就诊顺序号或者时间
     */
    @XmlElement(name = "successType")
    private String successType;
   
    public TimeRegInfo getTimeRegInfo() {
		return timeRegInfo;
	}

	public void setTimeRegInfo(TimeRegInfo timeRegInfo) {
		this.timeRegInfo = timeRegInfo;
	}

	public String getSuccessType() {
        return successType;
    }

    public void setSuccessType(String successType) {
        this.successType = successType;
    }

    public String getInfoSeq() {
        return infoSeq;
    }

    public void setInfoSeq(String infoSeq) {
        this.infoSeq = infoSeq;
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getAddFlag() {
        return addFlag;
    }

    public void setAddFlag(String addFlag) {
        this.addFlag = addFlag;
    }

    public String getOrderIdHis() {
        return orderIdHis;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setOrderIdHis(String orderIdHis) {
        this.orderIdHis = orderIdHis;
    }

    public String getUserFlag() {
        return userFlag;
    }

    public void setUserFlag(String userFlag) {
        this.userFlag = userFlag;
    }

    public String getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(String waitTime) {
        this.waitTime = waitTime;
    }

    public String getDiagnoseRoomName() {
        return diagnoseRoomName;
    }

    public void setDiagnoseRoomName(String diagnoseRoomName) {
        this.diagnoseRoomName = diagnoseRoomName;
    }

    public String getUserHisPatientId() {
        return userHisPatientId;
    }

    public void setUserHisPatientId(String userHisPatientId) {
        this.userHisPatientId = userHisPatientId;
    }

    public String getUserIdCard() {
        return userIdCard;
    }

    public void setUserIdCard(String userIdCard) {
        this.userIdCard = userIdCard;
    }

    public String getUserJkk() {
        return userJkk;
    }

    public void setUserJkk(String userJkk) {
        this.userJkk = userJkk;
    }

    public String getUserSmk() {
        return userSmk;
    }

    public void setUserSmk(String userSmk) {
        this.userSmk = userSmk;
    }

    public String getUserYbk() {
        return userYbk;
    }

    public void setUserYbk(String userYbk) {
        this.userYbk = userYbk;
    }

    public String getUserParentIdCard() {
        return userParentIdCard;
    }

    public void setUserParentIdCard(String userParentIdCard) {
        this.userParentIdCard = userParentIdCard;
    }

    public String getUserNhCard() {
        return userNhCard;
    }

    public void setUserNhCard(String userNhCard) {
        this.userNhCard = userNhCard;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(String userBirthday) {
        this.userBirthday = userBirthday;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }
    
	public String getTimeID() {
		return timeID;
	}

	public void setTimeID(String timeID) {
		this.timeID = timeID;
	}

	public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getRegFee() {
        return regFee;
    }

    public void setRegFee(String regFee) {
        this.regFee = regFee;
    }

    public String getTreatFee() {
        return treatFee;
    }

    public void setTreatFee(String treatFee) {
        this.treatFee = treatFee;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }
}
