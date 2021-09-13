package com.zebone.nhis.webservice.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbSHResponseVo {
	/**
	 * 公共返回体
	 */
	@XmlElement(name = "respCommon")
    public RespCommonVo CommonVo;

	/**
	 * 科室响应消息体
	 */
	@XmlElement(name = "items")
    public List<BdOuDeptVo> DeptList;
	/**
	 * 医生响应消息体
	 */
	@XmlElement(name = "items")
	public List<LbQuedocinfoResTemVo> ResTemList;
	/**
	 *科室医生排班号源
	 */
	@XmlElement(name = "items")
    public List<LBQueSchVo> queSchVo;
	
	/**
	 *住院预交金查询响应
	 */
	@XmlElement(name = "items")
    public List<LbQueBlBeposit> queBlBeposit;
	
	/**
	 *门诊患者信息查询响应
	 */
	@XmlElement(name = "items")
    public List<LbQueOpPvInfoResTemVo> quePiOpInfo;
	/**
	 * 科室响应消息体
	 */
	@XmlElement(name = "doctors")
    public List<BdOuEmpPhoto> doctors;
	/**
	 *住院费用明细响应
	 */
	@XmlElement(name = "items")
    public List<LbBlCgIpVo> lbBlCgIpVo;

	@XmlElement(name = "payInfos")
	List<LbQueOpUnpaidResTemVo> lbUnpaidResTemVo;
	/**
	 * 排班列表响应消息体
	 */
	@XmlElement(name = "schAppts")
    public List<PskqSchApptVo> schAppts;
	/**
	 *门诊代缴处方响应
	 */
	@XmlElement(name = "recipes")
	private List<LbRecipesVo> recipes;
	//诊疗卡费
	@XmlElement(name = "cardFee")
	public String cardFee;
	//卡押金费
	@XmlElement(name = "depositFee")
	private String depositFee;
	//其他费用
	@XmlElement(name = "otherFee")
	private String otherFee;
	//医院发票号
	@XmlElement(name = "invoiceNo")
	private String invoiceNo;
	//挂号时间
	@XmlElement(name = "operTime")
	private String operTime;
	//等候编号
    @XmlElement(name = "waitNo")
	private String waitNo;	
	//候诊地点
	@XmlElement(name = "location")
	private String location;
	//科室名称
	@XmlElement(name = "deptName")
	private String deptName;
	//医生编码
	@XmlElement(name = "doctCode")
	private String doctCode;
	//医生名称
	@XmlElement(name = "doctName")
	private String doctName;	
	//级别名称
	@XmlElement(name = "rankName")
	private String rankName;
	//号类名称
	@XmlElement(name = "typeName")
	private String typeName;
	//总费用
	@XmlElement(name = "totalFee")
	private String totalFee;
	//支付总金额
	@XmlElement(name = "payFee")
	private String payFee;
	//支付总金额
	@XmlElement(name = "orderNo")
	private String orderNo;
	//医院交易流水
	@XmlElement(name = "tranSerNo")
	private String tranSerNo;
	//操作时间
	@XmlElement(name = "operateTime")
	private String operateTime;
	//账户余额
	@XmlElement(name = "balance")
	private String balance;
	//账户押金
	@XmlElement(name = "cashPledge")
	private String cashPledge;
	//账户总额
	@XmlElement(name = "rental")
	private String rental;
	//号源编码
	@XmlElement(name = "regId")
	private String regId;
	//出诊诊室
	@XmlElement(name = "unitName")
	private String unitName;
	
	/**
	 *患者ID
	 */
	@XmlElement(name="patientId")
    private String patientId;
	
	/**
	 *患者编码
	 */
	@XmlElement(name="patientCode")
    private String patientCode;
	
	/**
	 *总记录数
	 */
	@XmlElement(name="totalNum")
    private String totalNum;
	
	/**
	 *就诊记录编码
	 */
	@XmlElement(name="visitId")
    private String visitId;
	/**
	 *诊断
	 */
	@XmlElement(name="diagnosis")
    private String diagnosis;
	
	/**
	 *就诊卡类型
	 */
	@XmlElement(name="dtCardType")
    private String dtCardType;
	/**
	 *就诊卡号
	 */
	@XmlElement(name="dtcardNo")
    private String dtcardNo;
	/**
	 *门诊号
	 */
	@XmlElement(name="codeOp")
    private String codeOp;
	
	/**
	 *门诊流水号
	 */
	@XmlElement(name="codeNo")
    private String codeNo;
	
	/**
	 *是否需要退款状态
	 */
	@XmlElement(name="ReturnState")
    private String ReturnState;
	
	/**
	 *就诊时间段hh:mm:ss-hh:mm:ss
	 */
	@XmlElement(name="rangeTime")
    private String rangeTime;
	/**
	 *预约订单编码
	 */
	@XmlElement(name="orderCode")
    private String orderCode;
	/**
	 *证件卡类型
	 */
	@XmlElement(name="cardType")
    private String cardType;
    /**
	 *证件号码
	 */
	@XmlElement(name="cardNo")
    private String cardNo;
	
	public String getRangeTime() {
		return rangeTime;
	}

	public void setRangeTime(String rangeTime) {
		this.rangeTime = rangeTime;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
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

	public String getReturnState() {
		return ReturnState;
	}

	public void setReturnState(String returnState) {
		ReturnState = returnState;
	}

	public RespCommonVo getCommonVo() {
		return CommonVo;
	}

	public void setCommonVo(RespCommonVo commonVo) {
		CommonVo = commonVo;
	}

	public List<BdOuDeptVo> getDeptList() {
		return DeptList;
	}

	public void setDeptList(List<BdOuDeptVo> deptList) {
		DeptList = deptList;
	}

	public List<LBQueSchVo> getQueSchVo() {
		return queSchVo;
	}

	public void setQueSchVo(List<LBQueSchVo> queSchVo) {
		this.queSchVo = queSchVo;
	}

	public String getCardFee() {
		return cardFee;
	}

	public void setCardFee(String cardFee) {
		this.cardFee = cardFee;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getOperTime() {
		return operTime;
	}

	public void setOperTime(String operTime) {
		this.operTime = operTime;
	}

	public String getWaitNo() {
		return waitNo;
	}

	public void setWaitNo(String waitNo) {
		this.waitNo = waitNo;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDoctName() {
		return doctName;
	}

	public void setDoctName(String doctName) {
		this.doctName = doctName;
	}

	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getPayFee() {
		return payFee;
	}

	public void setPayFee(String payFee) {
		this.payFee = payFee;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getTranSerNo() {
		return tranSerNo;
	}

	public void setTranSerNo(String tranSerNo) {
		this.tranSerNo = tranSerNo;
	}

	public String getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getPatientCode() {
		return patientCode;
	}

	public void setPatientCode(String patientCode) {
		this.patientCode = patientCode;
	}

	public String getDepositFee() {
		return depositFee;
	}

	public void setDepositFee(String depositFee) {
		this.depositFee = depositFee;
	}

	public String getOtherFee() {
		return otherFee;
	}

	public void setOtherFee(String otherFee) {
		this.otherFee = otherFee;
	}

	public List<LbQueBlBeposit> getQueBlBeposit() {
		return queBlBeposit;
	}

	public void setQueBlBeposit(List<LbQueBlBeposit> queBlBeposit) {
		this.queBlBeposit = queBlBeposit;
	}

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public List<LbBlCgIpVo> getLbBlCgIpVo() {
		return lbBlCgIpVo;
	}

	public void setLbBlCgIpVo(List<LbBlCgIpVo> lbBlCgIpVo) {
		this.lbBlCgIpVo = lbBlCgIpVo;
	}

	public String getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}

	public List<LbQueOpPvInfoResTemVo> getQuePiOpInfo() {
		return quePiOpInfo;
	}

	public void setQuePiOpInfo(List<LbQueOpPvInfoResTemVo> quePiOpInfo) {
		this.quePiOpInfo = quePiOpInfo;
	}

	public String getVisitId() {
		return visitId;
	}

	public void setVisitId(String visitId) {
		this.visitId = visitId;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public List<LbQueOpUnpaidResTemVo> getLbUnpaidResTemVo() {
		return lbUnpaidResTemVo;
	}

	public void setLbUnpaidResTemVo(List<LbQueOpUnpaidResTemVo> lbUnpaidResTemVo) {
		this.lbUnpaidResTemVo = lbUnpaidResTemVo;
	}

	public List<LbRecipesVo> getRecipes() {
		return recipes;
	}

	public void setRecipes(List<LbRecipesVo> recipes) {
		this.recipes = recipes;
	}

	public String getDtCardType() {
		return dtCardType;
	}

	public void setDtCardType(String dtCardType) {
		this.dtCardType = dtCardType;
	}

	public String getDtcardNo() {
		return dtcardNo;
	}

	public void setDtcardNo(String dtcardNo) {
		this.dtcardNo = dtcardNo;
	}

	public String getCodeOp() {
		return codeOp;
	}

	public void setCodeOp(String codeOp) {
		this.codeOp = codeOp;
	}

	public String getCodeNo() {
		return codeNo;
	}

	public void setCodeNo(String codeNo) {
		this.codeNo = codeNo;
	}

	public String getCashPledge() {
		return cashPledge;
	}

	public void setCashPledge(String cashPledge) {
		this.cashPledge = cashPledge;
	}

	public String getRental() {
		return rental;
	}

	public void setRental(String rental) {
		this.rental = rental;
	}

	public String getDoctCode() {
		return doctCode;
	}

	public void setDoctCode(String doctCode) {
		this.doctCode = doctCode;
	}

	public List<LbQuedocinfoResTemVo> getResTemList() {
		return ResTemList;
	}

	public void setResTemList(List<LbQuedocinfoResTemVo> resTemList) {
		ResTemList = resTemList;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public List<BdOuEmpPhoto> getDoctors() {
		return doctors;
	}

	public void setDoctors(List<BdOuEmpPhoto> doctors) {
		this.doctors = doctors;
	}

	public List<PskqSchApptVo> getSchAppts() {
		return schAppts;
	}

	public void setSchAppts(List<PskqSchApptVo> schAppts) {
		this.schAppts = schAppts;
	}
	
}
