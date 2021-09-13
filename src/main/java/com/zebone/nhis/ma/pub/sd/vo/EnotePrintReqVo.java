package com.zebone.nhis.ma.pub.sd.vo;

import java.util.List;

/**
 * 住院电子票据开立请求类
 * @author Administrator
 *
 */
public class EnotePrintReqVo {

	/**
	 * 业务流水号
	 * true
	 */
	private String busNo;

	/**
	 * 业务标识
	 * true
	 */
	private String busType;

	/**
	 * 患者姓名
	 * true
	 */
	private String payer;

	/**
	 * 业务发生时间
	 * true
	 * yyyyMMddHHmmssSSS
	 */
	private String busDateTime;

	/**
	 * 开票点编码
	 * true
	 */
	private String placeCode;

	/**
	 * 收费员
	 * true
	 */
	private String payee;

	/**
	 * 票据编制人
	 * true
	 */
	private String author;

	/**
	 * 开票总金额
	 * true
	 */
	private Double totalAmt;

	/**
	 * 备注
	 * false
	 */
	private String remark;

	/**
	 * 患者支付宝账户
	 * false
	 */
	private String alipayCode;

	/**
	 * 微信支付订单号
	 * false
	 */
	private String weChatOrderNo;

	/**
	 * 微信医保支付订单号
	 * false
	 */
	private String weChatMedTransNo;

	/**
	 * 微信公众号或小程序用户ID
	 * false
	 */
	private String openID;

	/**
	 * 患者手机号码
	 * false
	 */
	private String tel;

	/**
	 * 患者邮箱地址
	 * false
	 */
	private String email;

	/**
	 * 交款人类型
	 * true
	 * 交款人类型：1 个人 2 单位
	 */
	private String payerType;

	/**
	 * 统一社会信用代码
	 * false
	 * 个人患者可填身份证号码
	 */
	private String idCardNo;

	/**
	 * 卡类型
	 * true
	 */
	private String cardType;

	/**
	 * 卡号
	 * true
	 */
	private String cardNo;

	/**
	 * 医疗机构类型
	 * false
	 */
	private String medicalInstitution;

	/**
	 * 医保机构编码
	 * false
	 */
	private String medCareInstitution;

	/**
	 * 医保类型编码
	 * false
	 */
	private String medCareTypeCode;

	/**
	 * 医保类型名称
	 * false
	 */
	private String medicalCareType;

	/**
	 * 患者医保编号
	 * false
	 */
	private String medicalInsuranceID;

	/**
	 * 入院科室名称
	 * false
	 */
	private String category;

	/**
	 * 就诊科室编码
	 * false
	 * 门诊挂号使用
	 */
	private String patientCategory;

	/**
	 * 就诊科室编码
	 * false
	 * 门诊结算使用
	 */
	private String patientCategoryCode;

	/**
	 * 入院科室编码
	 * false
	 */
	private String categoryCode;

	/**
	 * 出院科室名称
	 * false
	 */
	private String leaveCategory;

	/**
	 * 出院科室编码
	 * false
	 */
	private String leaveCategoryCode;

	/**
	 * 患者住院号
	 * true
	 */
	private String hospitalNo;

	/**
	 * 住院就诊编号
	 * true
	 */
	private String visitNo;

	/**
	 * 就诊日期
	 * false
	 * yyyy-MM-dd
	 */
	private String consultationDate;

	/**
	 * 患者唯一ID
	 * false
	 */
	private String patientId;

	/**
	 * 患者就诊编号
	 * false
	 */
	private String patientNo;

	/**
	 * 性别
	 * true
	 */
	private String sex;

	/**
	 * 年龄
	 * true
	 */
	private String age;

	/**
	 * 病区
	 * false
	 */
	private String hospitalArea;

	/**
	 * 床号
	 * false
	 */
	private String bedNo;

	/**
	 * 病历号
	 * false
	 */
	private String caseNumber;

	/**
	 * 特种病名称
	 * false
	 * 门诊结算使用
	 */
	private String specialDiseasesName;

	/**
	 * 住院日期
	 * false
	 * yyyy-MM-dd
	 */
	private String inHospitalDate;

	/**
	 * 出院日期
	 * false
	 * yyyy-MM-dd
	 */
	private String outHospitalDate;

	/**
	 * 住院天数
	 * false
	 */
	private Double hospitalDays;

	/**
	 * 预交金凭证消费扣款列表
	 * false
	 */
	private List<PayMentVoucherVo> payMentVoucher;

	/**
	 * 个人账户支付
	 * true
	 */
	private Double accountPay;

	/**
	 * 医保统筹基金支付
	 * true
	 */
	private Double fundPay;

	/**
	 * 其它医保支付
	 * true
	 */
	private Double otherfundPay;

	/**
	 * 个人自费金额
	 * true
	 */
	private Double ownPay;

	/**
	 * 个人自负
	 * true
	 */
	private Double selfConceitedAmt;

	/**
	 * 个人自付
	 * true
	 */
	private Double selfPayAmt;

	/**
	 * 个人现金支付
	 * true
	 */
	private Double selfCashPay;

	/**
	 * 现金预交款金额
	 * false
	 */
	private Double cashPay;

	/**
	 * 支票预交款金额
	 * false
	 */
	private Double chequePay;

	/**
	 * 转账预交款金额
	 * false
	 */
	private Double transferAccountPay;

	/**
	 * 补交金额(现金)
	 * false
	 */
	private Double cashRecharge;

	/**
	 * 补交金额(支票)
	 * false
	 */
	private Double chequeRecharge;

	/**
	 * 补交金额（转账）
	 * false
	 */
	private Double transferRecharge;

	/**
	 * 退还金额(现金)
	 * false
	 */
	private Double cashRefund;

	/**
	 * 退交金额(支票)
	 * false
	 */
	private Double chequeRefund;

	/**
	 * 退交金额(转账)
	 * false
	 */
	private Double transferRefund;

	/**
	 * 个人账户余额
	 * false
	 */
	private Double ownAcBalance;

	/**
	 * 报销总金额
	 * false
	 */
	private Double reimbursementAmt;

	/**
	 * 结算号
	 * false
	 */
	private String balancedNumber;

	/**
	 * 其它扩展信息列表
	 * false
	 */
	private List<OtherInfoVo > otherInfo;

	/**
	 * 其它医保信息列表
	 * false
	 */
	private OtherMedicalVo otherMedicalList;

	/**
	 * 交费渠道列表
	 * true
	 */
	private List<PayChannelDetailVo> payChannelDetail;

	/**
	 * 业务票据关联号
	 * false
	 */
	private String eBillRelateNo;

	/**
	 * 是否可流通
	 * true
	 */
	private String isArrears;

	/**
	 * 不可流通原因
	 * false
	 */
	private String arrearsReason;

	/**
	 * 收费项目明细
	 * true
	 */
	private List<ChargeDetailVo> chargeDetail;

	/**
	 * 清单项目明细
	 * true
	 */
	private List<ListDetailVo> listDetail;

	/**
	 * 体检号码
	 */
	private String checkUpNo;


	public String getPatientCategory() {
		return patientCategory;
	}

	public void setPatientCategory(String patientCategory) {
		this.patientCategory = patientCategory;
	}

	public String getBusNo() {
		return busNo;
	}

	public void setBusNo(String busNo) {
		this.busNo = busNo;
	}

	public String getBusType() {
		return busType;
	}

	public void setBusType(String busType) {
		this.busType = busType;
	}

	public String getPayer() {
		return payer;
	}

	public void setPayer(String payer) {
		this.payer = payer;
	}

	public String getBusDateTime() {
		return busDateTime;
	}

	public void setBusDateTime(String busDateTime) {
		this.busDateTime = busDateTime;
	}

	public String getPlaceCode() {
		return placeCode;
	}

	public void setPlaceCode(String placeCode) {
		this.placeCode = placeCode;
	}

	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Double getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(Double totalAmt) {
		this.totalAmt = totalAmt;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAlipayCode() {
		return alipayCode;
	}

	public void setAlipayCode(String alipayCode) {
		this.alipayCode = alipayCode;
	}

	public String getWeChatOrderNo() {
		return weChatOrderNo;
	}

	public void setWeChatOrderNo(String weChatOrderNo) {
		this.weChatOrderNo = weChatOrderNo;
	}

	public String getWeChatMedTransNo() {
		return weChatMedTransNo;
	}

	public void setWeChatMedTransNo(String weChatMedTransNo) {
		this.weChatMedTransNo = weChatMedTransNo;
	}

	public String getOpenID() {
		return openID;
	}

	public void setOpenID(String openID) {
		this.openID = openID;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPayerType() {
		return payerType;
	}

	public void setPayerType(String payerType) {
		this.payerType = payerType;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getMedicalInstitution() {
		return medicalInstitution;
	}

	public void setMedicalInstitution(String medicalInstitution) {
		this.medicalInstitution = medicalInstitution;
	}

	public String getMedCareInstitution() {
		return medCareInstitution;
	}

	public void setMedCareInstitution(String medCareInstitution) {
		this.medCareInstitution = medCareInstitution;
	}

	public String getMedCareTypeCode() {
		return medCareTypeCode;
	}

	public void setMedCareTypeCode(String medCareTypeCode) {
		this.medCareTypeCode = medCareTypeCode;
	}

	public String getMedicalCareType() {
		return medicalCareType;
	}

	public void setMedicalCareType(String medicalCareType) {
		this.medicalCareType = medicalCareType;
	}

	public String getMedicalInsuranceID() {
		return medicalInsuranceID;
	}

	public void setMedicalInsuranceID(String medicalInsuranceID) {
		this.medicalInsuranceID = medicalInsuranceID;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getLeaveCategory() {
		return leaveCategory;
	}

	public void setLeaveCategory(String leaveCategory) {
		this.leaveCategory = leaveCategory;
	}

	public String getLeaveCategoryCode() {
		return leaveCategoryCode;
	}

	public void setLeaveCategoryCode(String leaveCategoryCode) {
		this.leaveCategoryCode = leaveCategoryCode;
	}

	public String getHospitalNo() {
		return hospitalNo;
	}

	public void setHospitalNo(String hospitalNo) {
		this.hospitalNo = hospitalNo;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}

	public String getConsultationDate() {
		return consultationDate;
	}

	public void setConsultationDate(String consultationDate) {
		this.consultationDate = consultationDate;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getPatientNo() {
		return patientNo;
	}

	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getHospitalArea() {
		return hospitalArea;
	}

	public void setHospitalArea(String hospitalArea) {
		this.hospitalArea = hospitalArea;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	public String getInHospitalDate() {
		return inHospitalDate;
	}

	public void setInHospitalDate(String inHospitalDate) {
		this.inHospitalDate = inHospitalDate;
	}

	public String getOutHospitalDate() {
		return outHospitalDate;
	}

	public void setOutHospitalDate(String outHospitalDate) {
		this.outHospitalDate = outHospitalDate;
	}

	public Double getHospitalDays() {
		return hospitalDays;
	}

	public void setHospitalDays(Double hospitalDays) {
		this.hospitalDays = hospitalDays;
	}

	public List<PayMentVoucherVo> getPayMentVoucher() {
		return payMentVoucher;
	}

	public void setPayMentVoucher(List<PayMentVoucherVo> payMentVoucher) {
		this.payMentVoucher = payMentVoucher;
	}

	public Double getAccountPay() {
		return accountPay;
	}

	public void setAccountPay(Double accountPay) {
		this.accountPay = accountPay;
	}

	public Double getFundPay() {
		return fundPay;
	}

	public void setFundPay(Double fundPay) {
		this.fundPay = fundPay;
	}

	public Double getOtherfundPay() {
		return otherfundPay;
	}

	public void setOtherfundPay(Double otherfundPay) {
		this.otherfundPay = otherfundPay;
	}

	public Double getOwnPay() {
		return ownPay;
	}

	public void setOwnPay(Double ownPay) {
		this.ownPay = ownPay;
	}

	public Double getSelfConceitedAmt() {
		return selfConceitedAmt;
	}

	public void setSelfConceitedAmt(Double selfConceitedAmt) {
		this.selfConceitedAmt = selfConceitedAmt;
	}

	public Double getSelfPayAmt() {
		return selfPayAmt;
	}

	public void setSelfPayAmt(Double selfPayAmt) {
		this.selfPayAmt = selfPayAmt;
	}

	public Double getSelfCashPay() {
		return selfCashPay;
	}

	public void setSelfCashPay(Double selfCashPay) {
		this.selfCashPay = selfCashPay;
	}

	public Double getCashPay() {
		return cashPay;
	}

	public void setCashPay(Double cashPay) {
		this.cashPay = cashPay;
	}

	public Double getChequePay() {
		return chequePay;
	}

	public void setChequePay(Double chequePay) {
		this.chequePay = chequePay;
	}

	public Double getTransferAccountPay() {
		return transferAccountPay;
	}

	public void setTransferAccountPay(Double transferAccountPay) {
		this.transferAccountPay = transferAccountPay;
	}

	public Double getCashRecharge() {
		return cashRecharge;
	}

	public void setCashRecharge(Double cashRecharge) {
		this.cashRecharge = cashRecharge;
	}

	public Double getChequeRecharge() {
		return chequeRecharge;
	}

	public void setChequeRecharge(Double chequeRecharge) {
		this.chequeRecharge = chequeRecharge;
	}

	public Double getTransferRecharge() {
		return transferRecharge;
	}

	public void setTransferRecharge(Double transferRecharge) {
		this.transferRecharge = transferRecharge;
	}

	public Double getCashRefund() {
		return cashRefund;
	}

	public void setCashRefund(Double cashRefund) {
		this.cashRefund = cashRefund;
	}

	public Double getChequeRefund() {
		return chequeRefund;
	}

	public void setChequeRefund(Double chequeRefund) {
		this.chequeRefund = chequeRefund;
	}

	public Double getTransferRefund() {
		return transferRefund;
	}

	public void setTransferRefund(Double transferRefund) {
		this.transferRefund = transferRefund;
	}

	public Double getOwnAcBalance() {
		return ownAcBalance;
	}

	public void setOwnAcBalance(Double ownAcBalance) {
		this.ownAcBalance = ownAcBalance;
	}

	public Double getReimbursementAmt() {
		return reimbursementAmt;
	}

	public void setReimbursementAmt(Double reimbursementAmt) {
		this.reimbursementAmt = reimbursementAmt;
	}

	public String getBalancedNumber() {
		return balancedNumber;
	}

	public void setBalancedNumber(String balancedNumber) {
		this.balancedNumber = balancedNumber;
	}


	public OtherMedicalVo getOtherMedicalList() {
		return otherMedicalList;
	}

	public void setOtherMedicalList(OtherMedicalVo otherMedicalList) {
		this.otherMedicalList = otherMedicalList;
	}

	public List<PayChannelDetailVo> getPayChannelDetail() {
		return payChannelDetail;
	}

	public void setPayChannelDetail(List<PayChannelDetailVo> payChannelDetail) {
		this.payChannelDetail = payChannelDetail;
	}

	public String geteBillRelateNo() {
		return eBillRelateNo;
	}

	public void seteBillRelateNo(String eBillRelateNo) {
		this.eBillRelateNo = eBillRelateNo;
	}

	public String getIsArrears() {
		return isArrears;
	}

	public void setIsArrears(String isArrears) {
		this.isArrears = isArrears;
	}

	public String getArrearsReason() {
		return arrearsReason;
	}

	public void setArrearsReason(String arrearsReason) {
		this.arrearsReason = arrearsReason;
	}

	public List<ChargeDetailVo> getChargeDetail() {
		return chargeDetail;
	}

	public void setChargeDetail(List<ChargeDetailVo> chargeDetail) {
		this.chargeDetail = chargeDetail;
	}


	public List<ListDetailVo> getListDetail() {
		return listDetail;
	}

	public void setListDetail(List<ListDetailVo> listDetail) {
		this.listDetail = listDetail;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getPatientCategoryCode() {
		return patientCategoryCode;
	}

	public void setPatientCategoryCode(String patientCategoryCode) {
		this.patientCategoryCode = patientCategoryCode;
	}

	public String getSpecialDiseasesName() {
		return specialDiseasesName;
	}

	public void setSpecialDiseasesName(String specialDiseasesName) {
		this.specialDiseasesName = specialDiseasesName;
	}


	public List<OtherInfoVo> getOtherInfo() {
		return otherInfo;
	}

	public void setOtherInfo(List<OtherInfoVo> otherInfo) {
		this.otherInfo = otherInfo;
	}
}
