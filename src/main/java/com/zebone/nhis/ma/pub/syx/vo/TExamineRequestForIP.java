package com.zebone.nhis.ma.pub.syx.vo;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: TEXAMINEREQUESTFORIP 
 *
 * @since 2018-12-14 03:02:46
 */
@Table(value="tExamineRequestForIP")
public class TExamineRequestForIP   {

	/** 主键自增字段 */
	@PK
//	@Field(value="ExamineRequestForIPID",id=KeyId.UUID)
    private Integer examineRequestForIPID;

	/** HIS的检验申请单流水号（每张检验申请单的唯一标志） */
	@Field(value="ExamineRequestID")
    private Long examineRequestID;

	/** 患者ID */
	@Field(value="PatientID")
    private String patientID;

	/** 患者姓名 */
	@Field(value="PatientName")
    private String patientName;

	/**  患者性别1-男；2-女 */
	@Field(value="PatientSexFlag")
    private String patientSexFlag;

	/**  患者出生日期 */
	@Field(value="PatientBirthDay")
    private Date patientBirthDay;

	/**  例如：20Y-20岁；20M-20月；20D-20天；20H-20小时。 */
	@Field(value="AGE")
    private String age;

	/**  电话号码 */
	@Field(value="PHONE")
    private String phone;

	/**  联系地址 */
	@Field(value="ADDRESS")
    private String address;

	/**  患者类型为I时：住院号; 患者类型为T时：体检登记号 */
	@Field(value="IPSeqNoText")
    private String iPSeqNoText;

	/**  患者类型为I时：住院次数; 患者类型为T时：空串 */
	@Field(value="IPTIMES")
    private int iptimes;

	/**  患者类型为I时：床号; 患者类型为T时：空串 */
	@Field(value="SickBedNo")
    private String sickBedNo;

	/**  申请医生ID */
	@Field(value="RequestDoctorEmployeeID")
    private String requestDoctorEmployeeID;

	/**  申请医生工号 */
	@Field(value="RequestDoctorEmployeeNo")
    private String requestDoctorEmployeeNo;

	/**  申请医生姓名 */
	@Field(value="RequestDoctorEmployeeName")
    private String requestDoctorEmployeeName;

	/**  申请科室ID */
	@Field(value="RequestDepartmentID")
    private String requestDepartmentID;

	/**  申请科室编码 */
	@Field(value="RequestDepartmentNo")
    private String requestDepartmentNo;

	/**  申请科室名称 */
	@Field(value="RequestDepartmentName")
    private String requestDepartmentName;

	/**  检验科室ID */
	@Field(value="ExamineDepartmentID")
    private String examineDepartmentID;

	/**  检验科室编码 */
	@Field(value="ExamineDepartmentNo")
    private String examineDepartmentNo;

	/**  检验科室名称 */
	@Field(value="ExamineDepartmentName")
    private String examineDepartmentName;

	/**  申请执行时间 */
	@Field(value="RequestExecutiveDateTime")
    private Date requestExecutiveDateTime;

	/**  患者类型为I时：入院处填写的门诊诊断的诊断名称(或门诊诊断的补充描述); 患者类型为T时：健康体检 */
	@Field(value="DiseaseName")
    private String diseaseName;

	/**  患者类型为I时：入院处填写的门诊诊断ICD码; 患者类型为T时：AAB0033 */
	@Field(value="ICDCode")
    private String iCDCode;

	/**  急诊标志， 0-非急诊； 1-? 急诊 患者类型为T时：0 */
	@Field(value="EmergencyFlag")
    private String emergencyFlag;

	/**  状态标志 0-HIS系统已送检； 1-检验系统已打印标签（由检验系统回写）; 2-检验有结果（由检验系统回写） */
	@Field(value="FLAG")
    private String flag;

	/**  插入时间 */
	@Field(value="InsertDateTime")
    private Date insertDateTime;

	/**  接收时间: 由检验系统回写 */
	@Field(value="ReceiveDateTime")
    private Date receiveDateTime;

	/**  结果时间: 由检验系统回写 */
	@Field(value="ResultDateTime")
    private Date resultDateTime;

	/**  检验标本号(与V_EMR_Get_Lis_Report.TEST_NO)相对；由检验系统回写 */
	@Field(value="LisLableNo")
    private String lisLableNo;

	/**  LIS系统回写的错误信息  */
	@Field(value="LisCreateErrMsg")
    private String lisCreateErrMsg;

	/**  LIS系统错误后重新生成的次数,默认为0 */
	@Field(value="LisCreateCount")
    private int lisCreateCount;

	/**  读取标志（由检验系统回写） 0-检验系统未读取 1-检验系统已读取 */
	@Field(value="LabRecvStatus")
    private String labRecvStatus;

	@Field(value="LisFlag")
    private String lisFlag;

	@Field(value="LisCreateDateTime")
    private Date lisCreateDateTime;

	/**  患者类型： T 体检病人，I 住院病人，默认值为I。 */
	@Field(value="PatType")
    private String patType;

	/**  患者类型为I时：传空串; 患者类型为T时：体检单位编码; 默认值为空串。 */
	@Field(value="OganID")
    private String oganID;

	/**  患者类型为I时：传空串; 患者类型为T时：体检单位名称; 默认值为空串。 */
	@Field(value="OganName")
    private String oganName;

	@Field(value="ReceiveBy")
    private String receiveBy;

	@Field(value="VSeqNo")
    private String vSeqNo;

	@Field(value="PayFlag")
    private String payFlag;

	@Field(value="PayFlagOperationDateTime")
    private Date payFlagOperationDateTime;

	@Field(value="CancleTime")
    private Date cancleTime;

	@Field(value="CancleEmployeeID")
    private Long cancleEmployeeID;

	@Field(value="CancleFlag")
    private String cancleFlag;

	@Field(value="LisCancelFlag")
    private String lisCancelFlag;

	@Field(value="LisCancelTime")
    private Date lisCancelTime;

	@Field(value="LisCancelBy")
    private String lisCancelBy;

	@Field(value="LisCancelMsg")
    private String lisCancelMsg;

	@Field(value="RegisterSeqNo")
    private String registerSeqNo;

	@Field(value="StatusFlag")
    private Long statusFlag;

	@Field(value="LabColDateTime")
    private Date labColDateTime;

	@Field(value="LabColBy")
    private String labColBy;

	@Field(value="LabColAt")
    private String labColAt;

	@Field(value="LabColFlag")
    private String labColFlag;

	@Field(value="HospID")
    private String hospID;

	@Field(value="HospName")
    private String hospName;

	@Field(value="LISHaveOverFlag")
    private String lISHaveOverFlag;

	@Field(value="ResultEmployeeID")
    private String resultEmployeeID;

	@Field(value="ConfirmTime")
    private Date confirmTime;

	@Field(value="ConfirmEmployeeID")
    private String confirmEmployeeID;

	@Field(value="PrintTime")
    private Date printTime;

	@Field(value="PrintleEmployeeID")
    private String printleEmployeeID;

	@Field(value="HaveOverTime")
    private Date haveOverTime;

	@Field(value="HaveOverEmployeeID")
    private String haveOverEmployeeID;

	@Field(value="HaveOverFlag")
    private String haveOverFlag;

	@Field(value="InfectedFlag")
    private String infectedFlag;

	@Field(value="LIS_REG_FLAG")
    private String lisRegFlag;

	@Field(value="LIS_REG_MSG")
    private String lisRegMsg;

	@Field(value="LIS_CNL_FLAG")
    private String lisCnlFlag;

	@Field(value="LIS_CNL_MSG")
    private String lisCnlMsg;

	@Field(value="LIS_COL_FLAG")
    private String lisColFlag;

	@Field(value="LIS_COL_MSG")
    private String lisColMsg;

	@Field(value="LIS_SEND_FLAG")
    private String lisSendFlag;

	@Field(value="LIS_SEND_MSG")
    private String lisSendMsg;

	@Field(value="PLATFORMFLAG")
    private String platformflag;

	public Integer getExamineRequestForIPID() {
		return examineRequestForIPID;
	}

	public void setExamineRequestForIPID(Integer examineRequestForIPID) {
		this.examineRequestForIPID = examineRequestForIPID;
	}

	public Long getExamineRequestID() {
		return examineRequestID;
	}

	public void setExamineRequestID(Long examineRequestID) {
		this.examineRequestID = examineRequestID;
	}

	public String getPatientID() {
		return patientID;
	}

	public void setPatientID(String patientID) {
		this.patientID = patientID;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientSexFlag() {
		return patientSexFlag;
	}

	public void setPatientSexFlag(String patientSexFlag) {
		this.patientSexFlag = patientSexFlag;
	}

	public Date getPatientBirthDay() {
		return patientBirthDay;
	}

	public void setPatientBirthDay(Date patientBirthDay) {
		this.patientBirthDay = patientBirthDay;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getiPSeqNoText() {
		return iPSeqNoText;
	}

	public void setiPSeqNoText(String iPSeqNoText) {
		this.iPSeqNoText = iPSeqNoText;
	}

	public int getIptimes() {
		return iptimes;
	}

	public void setIptimes(int iptimes) {
		this.iptimes = iptimes;
	}

	public String getSickBedNo() {
		return sickBedNo;
	}

	public void setSickBedNo(String sickBedNo) {
		this.sickBedNo = sickBedNo;
	}

	public String getRequestDoctorEmployeeID() {
		return requestDoctorEmployeeID;
	}

	public void setRequestDoctorEmployeeID(String requestDoctorEmployeeID) {
		this.requestDoctorEmployeeID = requestDoctorEmployeeID;
	}

	public String getRequestDoctorEmployeeNo() {
		return requestDoctorEmployeeNo;
	}

	public void setRequestDoctorEmployeeNo(String requestDoctorEmployeeNo) {
		this.requestDoctorEmployeeNo = requestDoctorEmployeeNo;
	}

	public String getRequestDoctorEmployeeName() {
		return requestDoctorEmployeeName;
	}

	public void setRequestDoctorEmployeeName(String requestDoctorEmployeeName) {
		this.requestDoctorEmployeeName = requestDoctorEmployeeName;
	}

	public String getRequestDepartmentID() {
		return requestDepartmentID;
	}

	public void setRequestDepartmentID(String requestDepartmentID) {
		this.requestDepartmentID = requestDepartmentID;
	}

	public String getRequestDepartmentNo() {
		return requestDepartmentNo;
	}

	public void setRequestDepartmentNo(String requestDepartmentNo) {
		this.requestDepartmentNo = requestDepartmentNo;
	}

	public String getRequestDepartmentName() {
		return requestDepartmentName;
	}

	public void setRequestDepartmentName(String requestDepartmentName) {
		this.requestDepartmentName = requestDepartmentName;
	}

	public String getExamineDepartmentID() {
		return examineDepartmentID;
	}

	public void setExamineDepartmentID(String examineDepartmentID) {
		this.examineDepartmentID = examineDepartmentID;
	}

	public String getExamineDepartmentNo() {
		return examineDepartmentNo;
	}

	public void setExamineDepartmentNo(String examineDepartmentNo) {
		this.examineDepartmentNo = examineDepartmentNo;
	}

	public String getExamineDepartmentName() {
		return examineDepartmentName;
	}

	public void setExamineDepartmentName(String examineDepartmentName) {
		this.examineDepartmentName = examineDepartmentName;
	}

	public Date getRequestExecutiveDateTime() {
		return requestExecutiveDateTime;
	}

	public void setRequestExecutiveDateTime(Date requestExecutiveDateTime) {
		this.requestExecutiveDateTime = requestExecutiveDateTime;
	}

	public String getDiseaseName() {
		return diseaseName;
	}

	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}

	public String getiCDCode() {
		return iCDCode;
	}

	public void setiCDCode(String iCDCode) {
		this.iCDCode = iCDCode;
	}

	public String getEmergencyFlag() {
		return emergencyFlag;
	}

	public void setEmergencyFlag(String emergencyFlag) {
		this.emergencyFlag = emergencyFlag;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Date getInsertDateTime() {
		return insertDateTime;
	}

	public void setInsertDateTime(Date insertDateTime) {
		this.insertDateTime = insertDateTime;
	}

	public Date getReceiveDateTime() {
		return receiveDateTime;
	}

	public void setReceiveDateTime(Date receiveDateTime) {
		this.receiveDateTime = receiveDateTime;
	}

	public Date getResultDateTime() {
		return resultDateTime;
	}

	public void setResultDateTime(Date resultDateTime) {
		this.resultDateTime = resultDateTime;
	}

	public String getLisLableNo() {
		return lisLableNo;
	}

	public void setLisLableNo(String lisLableNo) {
		this.lisLableNo = lisLableNo;
	}

	public String getLisCreateErrMsg() {
		return lisCreateErrMsg;
	}

	public void setLisCreateErrMsg(String lisCreateErrMsg) {
		this.lisCreateErrMsg = lisCreateErrMsg;
	}

	public int getLisCreateCount() {
		return lisCreateCount;
	}

	public void setLisCreateCount(int lisCreateCount) {
		this.lisCreateCount = lisCreateCount;
	}

	public String getLabRecvStatus() {
		return labRecvStatus;
	}

	public void setLabRecvStatus(String labRecvStatus) {
		this.labRecvStatus = labRecvStatus;
	}

	public String getLisFlag() {
		return lisFlag;
	}

	public void setLisFlag(String lisFlag) {
		this.lisFlag = lisFlag;
	}

	public Date getLisCreateDateTime() {
		return lisCreateDateTime;
	}

	public void setLisCreateDateTime(Date lisCreateDateTime) {
		this.lisCreateDateTime = lisCreateDateTime;
	}

	public String getPatType() {
		return patType;
	}

	public void setPatType(String patType) {
		this.patType = patType;
	}

	public String getOganID() {
		return oganID;
	}

	public void setOganID(String oganID) {
		this.oganID = oganID;
	}

	public String getOganName() {
		return oganName;
	}

	public void setOganName(String oganName) {
		this.oganName = oganName;
	}

	public String getReceiveBy() {
		return receiveBy;
	}

	public void setReceiveBy(String receiveBy) {
		this.receiveBy = receiveBy;
	}

	public String getvSeqNo() {
		return vSeqNo;
	}

	public void setvSeqNo(String vSeqNo) {
		this.vSeqNo = vSeqNo;
	}

	public String getPayFlag() {
		return payFlag;
	}

	public void setPayFlag(String payFlag) {
		this.payFlag = payFlag;
	}

	public Date getPayFlagOperationDateTime() {
		return payFlagOperationDateTime;
	}

	public void setPayFlagOperationDateTime(Date payFlagOperationDateTime) {
		this.payFlagOperationDateTime = payFlagOperationDateTime;
	}

	public Date getCancleTime() {
		return cancleTime;
	}

	public void setCancleTime(Date cancleTime) {
		this.cancleTime = cancleTime;
	}

	public Long getCancleEmployeeID() {
		return cancleEmployeeID;
	}

	public void setCancleEmployeeID(Long cancleEmployeeID) {
		this.cancleEmployeeID = cancleEmployeeID;
	}

	public String getCancleFlag() {
		return cancleFlag;
	}

	public void setCancleFlag(String cancleFlag) {
		this.cancleFlag = cancleFlag;
	}

	public String getLisCancelFlag() {
		return lisCancelFlag;
	}

	public void setLisCancelFlag(String lisCancelFlag) {
		this.lisCancelFlag = lisCancelFlag;
	}

	public Date getLisCancelTime() {
		return lisCancelTime;
	}

	public void setLisCancelTime(Date lisCancelTime) {
		this.lisCancelTime = lisCancelTime;
	}

	public String getLisCancelBy() {
		return lisCancelBy;
	}

	public void setLisCancelBy(String lisCancelBy) {
		this.lisCancelBy = lisCancelBy;
	}

	public String getLisCancelMsg() {
		return lisCancelMsg;
	}

	public void setLisCancelMsg(String lisCancelMsg) {
		this.lisCancelMsg = lisCancelMsg;
	}

	public String getRegisterSeqNo() {
		return registerSeqNo;
	}

	public void setRegisterSeqNo(String registerSeqNo) {
		this.registerSeqNo = registerSeqNo;
	}

	public Long getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(Long statusFlag) {
		this.statusFlag = statusFlag;
	}

	public Date getLabColDateTime() {
		return labColDateTime;
	}

	public void setLabColDateTime(Date labColDateTime) {
		this.labColDateTime = labColDateTime;
	}

	public String getLabColBy() {
		return labColBy;
	}

	public void setLabColBy(String labColBy) {
		this.labColBy = labColBy;
	}

	public String getLabColAt() {
		return labColAt;
	}

	public void setLabColAt(String labColAt) {
		this.labColAt = labColAt;
	}

	public String getLabColFlag() {
		return labColFlag;
	}

	public void setLabColFlag(String labColFlag) {
		this.labColFlag = labColFlag;
	}

	public String getHospID() {
		return hospID;
	}

	public void setHospID(String hospID) {
		this.hospID = hospID;
	}

	public String getHospName() {
		return hospName;
	}

	public void setHospName(String hospName) {
		this.hospName = hospName;
	}

	public String getlISHaveOverFlag() {
		return lISHaveOverFlag;
	}

	public void setlISHaveOverFlag(String lISHaveOverFlag) {
		this.lISHaveOverFlag = lISHaveOverFlag;
	}

	public String getResultEmployeeID() {
		return resultEmployeeID;
	}

	public void setResultEmployeeID(String resultEmployeeID) {
		this.resultEmployeeID = resultEmployeeID;
	}

	public Date getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public String getConfirmEmployeeID() {
		return confirmEmployeeID;
	}

	public void setConfirmEmployeeID(String confirmEmployeeID) {
		this.confirmEmployeeID = confirmEmployeeID;
	}

	public Date getPrintTime() {
		return printTime;
	}

	public void setPrintTime(Date printTime) {
		this.printTime = printTime;
	}

	public String getPrintleEmployeeID() {
		return printleEmployeeID;
	}

	public void setPrintleEmployeeID(String printleEmployeeID) {
		this.printleEmployeeID = printleEmployeeID;
	}

	public Date getHaveOverTime() {
		return haveOverTime;
	}

	public void setHaveOverTime(Date haveOverTime) {
		this.haveOverTime = haveOverTime;
	}

	public String getHaveOverEmployeeID() {
		return haveOverEmployeeID;
	}

	public void setHaveOverEmployeeID(String haveOverEmployeeID) {
		this.haveOverEmployeeID = haveOverEmployeeID;
	}

	public String getHaveOverFlag() {
		return haveOverFlag;
	}

	public void setHaveOverFlag(String haveOverFlag) {
		this.haveOverFlag = haveOverFlag;
	}

	public String getInfectedFlag() {
		return infectedFlag;
	}

	public void setInfectedFlag(String infectedFlag) {
		this.infectedFlag = infectedFlag;
	}

	public String getLisRegFlag() {
		return lisRegFlag;
	}

	public void setLisRegFlag(String lisRegFlag) {
		this.lisRegFlag = lisRegFlag;
	}

	public String getLisRegMsg() {
		return lisRegMsg;
	}

	public void setLisRegMsg(String lisRegMsg) {
		this.lisRegMsg = lisRegMsg;
	}

	public String getLisCnlFlag() {
		return lisCnlFlag;
	}

	public void setLisCnlFlag(String lisCnlFlag) {
		this.lisCnlFlag = lisCnlFlag;
	}

	public String getLisCnlMsg() {
		return lisCnlMsg;
	}

	public void setLisCnlMsg(String lisCnlMsg) {
		this.lisCnlMsg = lisCnlMsg;
	}

	public String getLisColFlag() {
		return lisColFlag;
	}

	public void setLisColFlag(String lisColFlag) {
		this.lisColFlag = lisColFlag;
	}

	public String getLisColMsg() {
		return lisColMsg;
	}

	public void setLisColMsg(String lisColMsg) {
		this.lisColMsg = lisColMsg;
	}

	public String getLisSendFlag() {
		return lisSendFlag;
	}

	public void setLisSendFlag(String lisSendFlag) {
		this.lisSendFlag = lisSendFlag;
	}

	public String getLisSendMsg() {
		return lisSendMsg;
	}

	public void setLisSendMsg(String lisSendMsg) {
		this.lisSendMsg = lisSendMsg;
	}

	public String getPlatformflag() {
		return platformflag;
	}

	public void setPlatformflag(String platformflag) {
		this.platformflag = platformflag;
	}

}