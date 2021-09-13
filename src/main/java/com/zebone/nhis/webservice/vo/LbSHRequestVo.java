package com.zebone.nhis.webservice.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "request")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbSHRequestVo {
	
       /**
        * 院区编码
        */
       @XmlElement(name="areaId")
       private String codeOrg;
	    /**
	     * 科室首字母
	     */
	    @XmlElement(name="initials")
	    private String pyCode;
	    /**
	     * 自助设备编码
	     */
	    @XmlElement(name="deviceId")
	    private String deviceid;
	    /**
	     * 医生编码
	     */
	    @XmlElement(name="doctCode")
	    private String doctCode;
	    /**
	     * 预约日期
	     */
	    @XmlElement(name="regDate")
	    private String regDate;
	    /**
         *患者ID
         */
	    @XmlElement(name="patientId")
	    private String patientId;
	    /**
         *患者编码
         */
	    @XmlElement(name="codePi")
	    private String codePi; 
        /**
         * 卡类型
         */
	    @XmlElement(name="cardType")
	    private String cardType;
	    /**
         * 卡号
         */
	    @XmlElement(name="cardNo")
	    private String cardNo;
	    /**
         *收取制卡费模式
         */
	    @XmlElement(name="repCard")
	    private String repCard;
	    /**
         * 姓名
         */
	    @XmlElement(name="name")
	    private String name;
	    /**
         * 性别
         */
	    @XmlElement(name="sex")
	    private String sex;
	    /**
         * 出生日期
         */
	    @XmlElement(name="birthday")
	    private String birthday;
	    /**
         *证件类型
         */
	    @XmlElement(name="idCardType")
	    private String idcardtype;
	    /**
         *办卡证件号码
         */
	    @XmlElement(name="idCardNo")
	    private String idcardno;
	    /**
         *名族
         */
	    @XmlElement(name="nation")
	    private String nation;
	    /**
         *地址
         */
	    @XmlElement(name="address")
	    private String address;
	    /**
         *电话
         */
	    @XmlElement(name="phoneNo")
	    private String phoneno;
	    /**
         *交易支付模式
         */
	    @XmlElement(name="payType")
	    private String payType;
	    /**
         *银行交易节点
         */
	    @XmlElement(name="bankInfo")
	    private List<LbBankinfoVo> bankinfoVo;
	    /**
         *扫码支付交易节点
         */
	    @XmlElement(name="qrCodeInfo")
	    private List<LbQrcodeinfoVo> qrCodeInfoVo;
	    /**
         *医保交易节点
         */
	    @XmlElement(name="insuranceInfo")
	    private List<LbInsuranceinfoVo> insuranceInfo;
	    /**
         *现金交易节点
         */
	    @XmlElement(name="cashInfo")
	    private List<LbCashinfoVo> cashInfo; 
	    /**
	     *分页
	     */
	    @XmlElement(name="pageInfo")
	    private List<LbPageInfo> pageInfo;
	    /**
         *院内卡类型
         */
	    @XmlElement(name="dtCardType")
	    private String dtCardType;
	    /**
         *现金交易节点
         */
	    @XmlElement(name="dtCardNo")
	    private String dtCardNo;
	    
	    //院区编号
	    @XmlElement(name="areaId")
		private String areaId;
	    //预约类型
	    @XmlElement(name="bookType")
	    private String bookType;
	    //号类编号
	    @XmlElement(name="typeCode")
	    private String typeCode;
	    //科室编码
	    @XmlElement(name="deptCode")
	    private String deptCode;
		//医生级别编码
	    @XmlElement(name="rankCode")
	    private String rankCode;
		//时段编码
	    @XmlElement(name="phaseCod")
	    private String phaseCod;
	    //号源编码
	    @XmlElement(name="regId")
	    private String regId;
		//候诊编号(号源)
	    @XmlElement(name="waitNo")
		private String waitNo;
		//医院订单号
	    @XmlElement(name="orderNo")
		private String orderNo;
		//总挂号费
	    @XmlElement(name="totalFee")
	    private String totalFee;
	    //挂号费
	    @XmlElement(name="regFee")
	    private String regFee;
	    //检查费
	    @XmlElement(name="treatFee")
	    private String treatFee;
	    //服务费
	    @XmlElement(name="serviceFee")
	    private String serviceFee;
	    //其它费用
	    @XmlElement(name="otherFee")
	    private String otherFee;
		//流水号
	    @XmlElement(name="flowNo")
	    private String flowNo;
	    //支付费用
	    @XmlElement(name="payAmt")
	    private String payAmt;
	    //医保支付费用
	    @XmlElement(name="insurePayment")
	    private Double insurePayment;
	    //个人自费费用
	    @XmlElement(name="selfPayment")
	    private Double selfPayment;
	    //支付时间
	    @XmlElement(name="payTime")
	    private String payTime;
	    //支付方账号
	    @XmlElement(name="accountNo")
	    private String accountNo;
	    //商户号
	    @XmlElement(name="merchantId")
	    private String merchantId;
		//第三方支付方式
	    @XmlElement(name="payMethod")
	    private String payMethod;
	    //时段编码
	    @XmlElement(name="phaseCode")
	    private String phaseCode;
	    /**
         *开始日期
         */
	    @XmlElement(name="startDate")
	    private String startDate;
	    /**
         *结束日期
         */
	    @XmlElement(name="endDate")
	    private String endDate;
	    /**
         *结算状态
         */
	    @XmlElement(name="startDate")
	    private String settleFlag;
	    /**
	     * 身份证号
	     */
	    @XmlElement(name="idno")
	    private String idno;
	    /**
	     *就诊卡号
	     */
	    @XmlElement(name="codeCard")
	    private String codeCard;
	    /**
	     *住院号
	     */
	    @XmlElement(name="codeIp")
	    private String codeIp;
	    /**
	     *住院号
	     */               
	    @XmlElement(name="inPatientNo")
	    private String inPatientNo;
	    
	    /**
	     *发卡时卡号：
	     */               
	    @XmlElement(name="medicalCardNo")
	    private String medicalCardNo;
	    
	    /**
	     *就诊记录：
	     */               
	    @XmlElement(name="visitId")
	    private String visitId;
	    /**
	     *处方列表编号：
	     */               
	    @XmlElement(name="recipeIds")
	    private String recipeIds;
	    private String settle;
	    /**
	     *预约票号：
	     */               
	    @XmlElement(name="ticketNo")
	    private String ticketNo;
	    /**
	     *健康卡号：
	     */               
	    @XmlElement(name="hicNo")
	    private String hicNo;
	    
	    @XmlElement(name="registerTime")
	    private String registerTime;

		@XmlElement(name="empType")
	    private String empType;

	public String getEmpType() {
		return empType;
	}

	public void setEmpType(String empType) {
		this.empType = empType;
	}

	public String getRegisterTime() {
			return registerTime;
		}

		public void setRegisterTime(String registerTime) {
			this.registerTime = registerTime;
		}

		public String getTicketNo() {
			return ticketNo;
		}

		public void setTicketNo(String ticketNo) {
			this.ticketNo = ticketNo;
		}

		public String getSettle() {
			return settle;
		}

		public String getRecipeIds() {
			return recipeIds;
		}

		public void setRecipeIds(String recipeIds) {
			this.recipeIds = recipeIds;
		}

		public void setSettle(String settle) {
			this.settle = settle;
		}

		public String getPyCode() {
			return pyCode;
		}

		public void setPyCode(String pyCode) {
			this.pyCode = pyCode;
		}

		public String getDeviceid() {
			return deviceid;
		}

		public void setDeviceid(String deviceid) {
			this.deviceid = deviceid;
		}

		public void setDeptcode(String deptcode) {
			this.deviceid = deptcode;
		}

		public String getCodeOrg() {
			return codeOrg;
		}

		public void setCodeOrg(String codeOrg) {
			this.codeOrg = codeOrg;
		}

		public String getDoctCode() {
			return doctCode;
		}

		public void setDoctCode(String doctCode) {
			this.doctCode = doctCode;
		}

		public String getRegDate() {
			return regDate;
		}

		public void setRegDate(String regDate) {
			this.regDate = regDate;
		}

		public String getPatientId() {
			return patientId;
		}

		public void setPatientId(String patientId) {
			this.patientId = patientId;
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

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSex() {
			return sex;
		}

		public void setSex(String sex) {
			this.sex = sex;
		}

		public String getBirthday() {
			return birthday;
		}

		public void setBirthday(String birthday) {
			this.birthday = birthday;
		}

		public String getIdcardtype() {
			return idcardtype;
		}

		public void setIdcardtype(String idcardtype) {
			this.idcardtype = idcardtype;
		}

		public String getIdcardno() {
			return idcardno;
		}

		public void setIdcardno(String idcardno) {
			this.idcardno = idcardno;
		}

		public String getNation() {
			return nation;
		}

		public void setNation(String nation) {
			this.nation = nation;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getPhoneno() {
			return phoneno;
		}

		public void setPhoneno(String phoneno) {
			this.phoneno = phoneno;
		}

		public List<LbBankinfoVo> getBankinfoVo() {
			return bankinfoVo;
		}

		public void setBankinfoVo(List<LbBankinfoVo> bankinfoVo) {
			this.bankinfoVo = bankinfoVo;
		}

		public List<LbQrcodeinfoVo> getQrCodeInfoVo() {
			return qrCodeInfoVo;
		}

		public void setQrCodeInfoVo(List<LbQrcodeinfoVo> qrCodeInfoVo) {
			this.qrCodeInfoVo = qrCodeInfoVo;
		}

		public List<LbInsuranceinfoVo> getInsuranceInfo() {
			return insuranceInfo;
		}

		public void setInsuranceInfo(List<LbInsuranceinfoVo> insuranceInfo) {
			this.insuranceInfo = insuranceInfo;
		}

		public List<LbCashinfoVo> getCashInfo() {
			return cashInfo;
		}

		public void setCashInfo(List<LbCashinfoVo> cashInfo) {
			this.cashInfo = cashInfo;
		}

		public String getPayType() {
			return payType;
		}

		public void setPayType(String payType) {
			this.payType = payType;
		}

		public String getDtCardType() {
			return dtCardType;
		}

		public void setDtCardType(String dtCardType) {
			this.dtCardType = dtCardType;
		}
		public String getDtCardNo() {
			return dtCardNo;
		}

		public void setDtCardNo(String dtCardNo) {
			this.dtCardNo = dtCardNo;
		}

		public String getAreaId() {
			return areaId;
		}

		public void setAreaId(String areaId) {
			this.areaId = areaId;
		}

		public String getBookType() {
			return bookType;
		}

		public void setBookType(String bookType) {
			this.bookType = bookType;
		}

		public String getTypeCode() {
			return typeCode;
		}

		public void setTypeCode(String typeCode) {
			this.typeCode = typeCode;
		}

		public String getDeptCode() {
			return deptCode;
		}

		public void setDeptCode(String deptCode) {
			this.deptCode = deptCode;
		}

		public String getRankCode() {
			return rankCode;
		}

		public void setRankCode(String rankCode) {
			this.rankCode = rankCode;
		}

		public String getPhaseCod() {
			return phaseCod;
		}

		public void setPhaseCod(String phaseCod) {
			this.phaseCod = phaseCod;
		}

		public String getRegId() {
			return regId;
		}

		public void setRegId(String regId) {
			this.regId = regId;
		}

		public String getOrderNo() {
			return orderNo;
		}

		public void setOrderNo(String orderNo) {
			this.orderNo = orderNo;
		}

		public String getTotalFee() {
			return totalFee;
		}

		public void setTotalFee(String totalFee) {
			this.totalFee = totalFee;
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

		public String getServiceFee() {
			return serviceFee;
		}

		public void setServiceFee(String serviceFee) {
			this.serviceFee = serviceFee;
		}

		public String getOtherFee() {
			return otherFee;
		}

		public void setOtherFee(String otherFee) {
			this.otherFee = otherFee;
		}

		public String getFlowNo() {
			return flowNo;
		}

		public void setFlowNo(String flowNo) {
			this.flowNo = flowNo;
		}

		public String getPayTime() {
			return payTime;
		}

		public void setPayTime(String payTime) {
			this.payTime = payTime;
		}

		public String getAccountNo() {
			return accountNo;
		}

		public void setAccountNo(String accountNo) {
			this.accountNo = accountNo;
		}

		public String getMerchantId() {
			return merchantId;
		}

		public void setMerchantId(String merchantId) {
			this.merchantId = merchantId;
		}

		public String getPayMethod() {
			return payMethod;
		}

		public void setPayMethod(String payMethod) {
			this.payMethod = payMethod;
		}

		public String getPhaseCode() {
			return phaseCode;
		}

		public void setPhaseCode(String phaseCode) {
			this.phaseCode = phaseCode;
		}

		public String getStartDate() {
			return startDate;
		}

		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}

		public String getEndDate() {
			return endDate;
		}

		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}

		public String getSettleFlag() {
			return settleFlag;
		}

		public void setSettleFlag(String settleFlag) {
			this.settleFlag = settleFlag;
		}

		public String getWaitNo() {
			return waitNo;
		}

		public void setWaitNo(String waitNo) {
			this.waitNo = waitNo;
		}

		public String getCodePi() {
			return codePi;
		}

		public void setCodePi(String codePi) {
			this.codePi = codePi;
		}

		public String getIdno() {
			return idno;
		}

		public void setIdno(String idno) {
			this.idno = idno;
		}

		public String getCodeCard() {
			return codeCard;
		}

		public void setCodeCard(String codeCard) {
			this.codeCard = codeCard;
		}

		public String getRepCard() {
			return repCard;
		}

		public void setRepCard(String repCard) {
			this.repCard = repCard;
		}

		public String getCodeIp() {
			return codeIp;
		}

		public void setCodeIp(String codeIp) {
			this.codeIp = codeIp;
		}

		public List<LbPageInfo> getPageInfo() {
			return pageInfo;
		}

		public void setPageInfo(List<LbPageInfo> pageInfo) {
			this.pageInfo = pageInfo;
		}

		public String getInPatientNo() {
			return inPatientNo;
		}

		public void setInPatientNo(String inPatientNo) {
			this.inPatientNo = inPatientNo;
		}

		public String getMedicalCardNo() {
			return medicalCardNo;
		}

		public void setMedicalCardNo(String medicalCardNo) {
			this.medicalCardNo = medicalCardNo;
		}

		public String getVisitId() {
			return visitId;
		}

		public void setVisitId(String visitId) {
			this.visitId = visitId;
		}

		public String getPayAmt() {
			return payAmt;
		}

		public void setPayAmt(String payAmt) {
			this.payAmt = payAmt;
		}

		public Double getInsurePayment() {
			return insurePayment;
		}

		public void setInsurePayment(Double insurePayment) {
			this.insurePayment = insurePayment;
		}

		public Double getSelfPayment() {
			return selfPayment;
		}

		public void setSelfPayment(Double selfPayment) {
			this.selfPayment = selfPayment;
		}

		public String getHicNo() {
			return hicNo;
		}

		public void setHicNo(String hicNo) {
			this.hicNo = hicNo;
		}

}
