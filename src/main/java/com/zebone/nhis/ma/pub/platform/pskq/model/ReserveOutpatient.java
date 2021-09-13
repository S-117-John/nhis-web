package com.zebone.nhis.ma.pub.platform.pskq.model;


import com.zebone.nhis.ma.pub.platform.pskq.annotation.MetadataDescribe;

/**
 * 门诊预约信息
 */
public class ReserveOutpatient {

    @MetadataDescribe(id= "LHDE0043001",name = "患者主索引号码",eName = "EMPI_ID")
    private String empiId;

    @MetadataDescribe(id= "LHDE0043002",name = "患者主键",eName = "PK_PATIENT")
    private String pkPatient;

    @MetadataDescribe(id= "LHDE0043003",name = "机构/院部代码",eName = "ORG_CODE")
    private String orgCode;

    @MetadataDescribe(id= "LHDE0043004",name = "机构/院部名称",eName = "ORG_NAME")
    private String orgName;

    @MetadataDescribe(id= "LHDE0043005",name = "病人姓名",eName = "PATIENT_NAME")
    private String patientName;

    @MetadataDescribe(id= "LHDE0043006",name = "性别代码",eName = "GENDER_CODE")
    private String genderCode;

    @MetadataDescribe(id= "LHDE0043007",name = "性别名称",eName = "GENDER_NAME")
    private String genderName;

    @MetadataDescribe(id= "LHDE0043008",name = "出生日期",eName = "DATE_OF_BIRTH")
    private String dateOfBirth;

    @MetadataDescribe(id= "LHDE0043009",name = "证件类型代码",eName = "ID_TYPE_CODE")
    private String idTypeCode;

    @MetadataDescribe(id= "LHDE0043010",name = "证件类型名称",eName = "ID_TYPE_NAME")
    private String idTypeName;

    @MetadataDescribe(id= "LHDE0043011",name = "证件号",eName = "ID_NO")
    private String idNo;

    @MetadataDescribe(id= "LHDE0043012",name = "联系电话",eName = "PHONE_NO")
    private String phoneNo;

    @MetadataDescribe(id= "LHDE0043013",name = "医保卡号",eName = "MED_INSURANCE_NO")
    private String medInsuranceNo;

    @MetadataDescribe(id= "LHDE0043014",name = "是否医保",eName = "IS_INSURANCE")
    private String isInsurance;

    @MetadataDescribe(id= "LHDE0043015",name = "预约序号",eName = "RESERVE_ID")
    private String reserveId;

    @MetadataDescribe(id= "LHDE0043016",name = "取号密码",eName = "HIS_TAKE_NO")
    private String hisTakeNo;

    @MetadataDescribe(id= "LHDE0043017",name = "预约渠道代码",eName = "RESERVE_CAHANNEL_CODE")
    private String reserveCahannelCode;

    @MetadataDescribe(id= "LHDE0043018",name = "预约渠道名称",eName = "RESERVE_CAHANNEL_NAME")
    private String reserveCahannelName;

    @MetadataDescribe(id= "LHDE0043019",name = "预约时间",eName = "RESERVE_DATE_TIME")
    private String reserveDateTime;

    @MetadataDescribe(id= "LHDE0043020",name = "排班表ID",eName = "SCHEDULE_ID")
    private String scheduleId;

    @MetadataDescribe(id= "LHDE0043021",name = "号源ID",eName = "RESERVE_SOURCE_ID")
    private String reserveSourceId;

    @MetadataDescribe(id= "LHDE0043022",name = "预约状态",eName = "RESERVE_STATE")
    private String reserveState;

    @MetadataDescribe(id= "LHDE0043023",name = "支付状态",eName = "PAY_STATE")
    private String payState;

    @MetadataDescribe(id= "LHDE0043024",name = "支付方式代码",eName = "PAY_METHOD_CODE")
    private String payMethodCode;

    @MetadataDescribe(id= "LHDE0043025",name = "支付方式名称",eName = "PAY_METHOD_NAME")
    private String payMethodName;

    @MetadataDescribe(id= "LHDE0043026",name = "支付时间",eName = "PAY_DATE_TIME")
    private String payDateTime;

    @MetadataDescribe(id= "LHDE0043027",name = "支付总费用",eName = "Total_Payment")
    private String totalPayment;

    @MetadataDescribe(id= "LHDE0043028",name = "医保费用",eName = "Medical_Insurance_Expenses")
    private String medicalInsuranceExpenses;

    @MetadataDescribe(id= "LHDE0043029",name = "自费费用",eName = "Personal_Expenses")
    private String personalExpenses;

    @MetadataDescribe(id= "LHDE0043030",name = "预约订单流水号",eName = "RESERVE_Order_No")
    private String reserveOrderNo;

    @MetadataDescribe(id= "LHDE0043031",name = "预约支付订单号",eName = "Payment_Order_No")
    private String paymentOrderNo;

    @MetadataDescribe(id= "LHDE0043032",name = "预约支付交易流水号",eName = "Transaction_Serial_No")
    private String transactionSerialNo;

    @MetadataDescribe(id= "LHDE0043033",name = "支付平台终端号",eName = "Pay_Terminal_No")
    private String payTerminalNo;

    @MetadataDescribe(id= "LHDE0043034",name = "自助机终端编号",eName = "Machine_No")
    private String machineNo;

    @MetadataDescribe(id= "LHDE0043035",name = "电子健康卡号",eName = "EHEALTH_CARD_NO")
    private String ehealthCardNo;

    @MetadataDescribe(id= "LHDE0043036",name = "HIS产生预约记录的唯一ID",eName = "RESERVE_OUTPATIENT_ID")
    private String reserveOutpatientId;

    @MetadataDescribe(id= "LHDE0043037",name = "挂号发票明细ID",eName = "REGISTERED_INVOICE_ID")
    private String registeredInvoiceId;

    @MetadataDescribe(id= "LHDE0043038",name = "挂号电子发票PDF路径",eName = "REGISTERED_INVOICE_PDF_URL")
    private String registeredInvoicePdfUrl;

    @MetadataDescribe(id= "LHDE0043039",name = "原始挂号发票明细ID",eName = "OLD_REGISTERED_INVOICE_ID")
    private String oldRegisteredInvoiceId;

    @MetadataDescribe(id= "LHDE0043040",name = "挂号电子发票状态",eName = "REGISTERED_INVOICE_PDF_STATUS")
    private String registeredInvoicePdfStatus;

    @MetadataDescribe(id= "LHDE0043041",name = "医保类型编码",eName = "MEDICARE_TYPE_ID")
    private String medicareTypeId;

    @MetadataDescribe(id= "LHDE0043042",name = "医保类型名称",eName = "MEDICARE_TYPE_NAME")
    private String medicareTypeName;

    @MetadataDescribe(id= "LHDE0043043",name = "就诊科室代码",eName = "VISIT_DEPT_ID")
    private String visitDeptId;

    @MetadataDescribe(id= "LHDE0043044",name = "就诊科室名称",eName = "VISIT_DEPT_NAME")
    private String visitDeptName;

    @MetadataDescribe(id= "LHDE0043044",name = "就诊医师ID",eName = "DOCTOR_ID")
    private String doctorId;

    @MetadataDescribe(id= "LHDE0043045",name = "就诊医师姓名",eName = "DOCTOR_NAME")
    private String doctorName;

    @MetadataDescribe(id= "LHDE0043046",name = "就诊开始时间",eName = "VISIT_START_DATE_TIME")
    private String visitStartDateTime;

    @MetadataDescribe(id= "LHDE0043047",name = "门诊号",eName = "OUTPATIENT_NO")
    private String outpatientNo;

    public String getVisitDeptId() {
        return visitDeptId;
    }

    public void setVisitDeptId(String visitDeptId) {
        this.visitDeptId = visitDeptId;
    }

    public String getVisitDeptName() {
        return visitDeptName;
    }

    public void setVisitDeptName(String visitDeptName) {
        this.visitDeptName = visitDeptName;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getVisitStartDateTime() {
        return visitStartDateTime;
    }

    public void setVisitStartDateTime(String visitStartDateTime) {
        this.visitStartDateTime = visitStartDateTime;
    }

    public String getOutpatientNo() {
        return outpatientNo;
    }

    public void setOutpatientNo(String outpatientNo) {
        this.outpatientNo = outpatientNo;
    }

    public String getRegisteredInvoiceId() {
        return registeredInvoiceId;
    }

    public void setRegisteredInvoiceId(String registeredInvoiceId) {
        this.registeredInvoiceId = registeredInvoiceId;
    }

    public String getRegisteredInvoicePdfUrl() {
        return registeredInvoicePdfUrl;
    }

    public void setRegisteredInvoicePdfUrl(String registeredInvoicePdfUrl) {
        this.registeredInvoicePdfUrl = registeredInvoicePdfUrl;
    }

    public String getOldRegisteredInvoiceId() {
        return oldRegisteredInvoiceId;
    }

    public void setOldRegisteredInvoiceId(String oldRegisteredInvoiceId) {
        this.oldRegisteredInvoiceId = oldRegisteredInvoiceId;
    }

    public String getRegisteredInvoicePdfStatus() {
        return registeredInvoicePdfStatus;
    }

    public void setRegisteredInvoicePdfStatus(String registeredInvoicePdfStatus) {
        this.registeredInvoicePdfStatus = registeredInvoicePdfStatus;
    }

    public String getMedicareTypeId() {
        return medicareTypeId;
    }

    public void setMedicareTypeId(String medicareTypeId) {
        this.medicareTypeId = medicareTypeId;
    }

    public String getMedicareTypeName() {
        return medicareTypeName;
    }

    public void setMedicareTypeName(String medicareTypeName) {
        this.medicareTypeName = medicareTypeName;
    }

    public String getEmpiId() {
        return empiId;
    }

    public void setEmpiId(String empiId) {
        this.empiId = empiId;
    }

    public String getPkPatient() {
        return pkPatient;
    }

    public void setPkPatient(String pkPatient) {
        this.pkPatient = pkPatient;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(String genderCode) {
        switch(genderCode){
            case "02":
                this.genderCode = "1";
                break;
            case "03":
                this.genderCode = "2";
                break;   
            default:
                this.genderCode = "9";
                break;
        }
    }
    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
    	String name = "未说明的性别";
        switch(genderName){
            case "男":
                name = "男性";
                break;
            case "女":
                name = "女性";
                break;
  
        }
        this.genderName = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getIdTypeCode() {
        return idTypeCode;
    }

    public void setIdTypeCode(String idTypeCode) {
        this.idTypeCode = idTypeCode;
    }

    public String getIdTypeName() {
        return idTypeName;
    }

    public void setIdTypeName(String idTypeName) {
        this.idTypeName = idTypeName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getMedInsuranceNo() {
        return medInsuranceNo;
    }

    public void setMedInsuranceNo(String medInsuranceNo) {
        this.medInsuranceNo = medInsuranceNo;
    }

    public String getIsInsurance() {
        return isInsurance;
    }

    public void setIsInsurance(String isInsurance) {
        this.isInsurance = isInsurance;
    }

    public String getReserveId() {
        return reserveId;
    }

    public void setReserveId(String reserveId) {
        this.reserveId = reserveId;
    }

    public String getHisTakeNo() {
        return hisTakeNo;
    }

    public void setHisTakeNo(String hisTakeNo) {
        this.hisTakeNo = hisTakeNo;
    }

    public String getReserveCahannelCode() {
        return reserveCahannelCode;
    }

    public void setReserveCahannelCode(String reserveCahannelCode) {
        this.reserveCahannelCode = reserveCahannelCode;
    }

    public String getReserveCahannelName() {
        return reserveCahannelName;
    }

    public void setReserveCahannelName(String reserveCahannelName) {
        this.reserveCahannelName = reserveCahannelName;
    }

    public String getReserveDateTime() {
        return reserveDateTime;
    }

    public void setReserveDateTime(String reserveDateTime) {
        this.reserveDateTime = reserveDateTime;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getReserveSourceId() {
        return reserveSourceId;
    }

    public void setReserveSourceId(String reserveSourceId) {
        this.reserveSourceId = reserveSourceId;
    }

    public String getReserveState() {
        return reserveState;
    }

    public void setReserveState(String reserveState) {
        this.reserveState = reserveState;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public String getPayMethodCode() {
        return payMethodCode;
    }

    public void setPayMethodCode(String payMethodCode) {
        this.payMethodCode = payMethodCode;
    }

    public String getPayMethodName() {
        return payMethodName;
    }

    public void setPayMethodName(String payMethodName) {
        this.payMethodName = payMethodName;
    }

    public String getPayDateTime() {
        return payDateTime;
    }

    public void setPayDateTime(String payDateTime) {
        this.payDateTime = payDateTime;
    }

    public String getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(String totalPayment) {
        this.totalPayment = totalPayment;
    }

    public String getMedicalInsuranceExpenses() {
        return medicalInsuranceExpenses;
    }

    public void setMedicalInsuranceExpenses(String medicalInsuranceExpenses) {
        this.medicalInsuranceExpenses = medicalInsuranceExpenses;
    }

    public String getPersonalExpenses() {
        return personalExpenses;
    }

    public void setPersonalExpenses(String personalExpenses) {
        this.personalExpenses = personalExpenses;
    }

    public String getReserveOrderNo() {
        return reserveOrderNo;
    }

    public void setReserveOrderNo(String reserveOrderNo) {
        this.reserveOrderNo = reserveOrderNo;
    }

    public String getPaymentOrderNo() {
        return paymentOrderNo;
    }

    public void setPaymentOrderNo(String paymentOrderNo) {
        this.paymentOrderNo = paymentOrderNo;
    }

    public String getTransactionSerialNo() {
        return transactionSerialNo;
    }

    public void setTransactionSerialNo(String transactionSerialNo) {
        this.transactionSerialNo = transactionSerialNo;
    }

    public String getPayTerminalNo() {
        return payTerminalNo;
    }

    public void setPayTerminalNo(String payTerminalNo) {
        this.payTerminalNo = payTerminalNo;
    }

    public String getMachineNo() {
        return machineNo;
    }

    public void setMachineNo(String machineNo) {
        this.machineNo = machineNo;
    }

    public String getEhealthCardNo() {
        return ehealthCardNo;
    }

    public void setEhealthCardNo(String ehealthCardNo) {
        this.ehealthCardNo = ehealthCardNo;
    }

	public String getReserveOutpatientId() {
		return reserveOutpatientId;
	}

	public void setReserveOutpatientId(String reserveOutpatientId) {
		this.reserveOutpatientId = reserveOutpatientId;
	}
}
