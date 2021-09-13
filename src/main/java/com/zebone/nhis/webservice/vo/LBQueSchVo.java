package com.zebone.nhis.webservice.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/*
 * 科室医生排班号源
 */
@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class LBQueSchVo {
	/*
	 * 号源编码
	 */
	@XmlElement(name="regId")
	private String regId;
	/*
	 *挂号日期/预约日期
	 */
	@XmlElement(name="regDate")
	private String regDate;
	/*
	 *订单编号
	 */
	@XmlElement(name="orderNo")
	private String  orderNo;
	/*
	 *订单时间
	 */
	@XmlElement(name="orderTime")
	private String orderTime;
	/*
	 *预约类型
	 *1：自助挂号；2：预约挂号
	 */
	@XmlElement(name="bookType")
	private String bookType;
	/*
	 *号类编号
	 */
	@XmlElement(name="typeCode")
	private String typeCode;
	/*
	 *号类描述
	 *(普通 专家、特需等)
	 */
	@XmlElement(name="typeName")
	private String typeName;
	/*
	 *科室编号
	 */
	@XmlElement(name="deptCode")
	private String deptCode;
	/*
	 *科室名称
	 */
	@XmlElement(name="deptName")
	private String deptName;
	/*
	 *医生编号
	 */
	@XmlElement(name="doctCode")
	private String doctCode;
	/*
	 *医生姓名
	 */
	@XmlElement(name="doctName")
	private String doctName;
	/*
	 *医生描述
	 */
	@XmlElement(name="doctDesc")
	private String doctDesc;
	/*
	 *医生级别编号
	 */
	@XmlElement(name="rankCode")
	private String rankCode;
	/*
	 *医生级别名称
	 *例如:医生 医师 主治医师
	 */
	@XmlElement(name="rankName")
	private String rankName;
	/*
	 *时段编码
	 */
	@XmlElement(name="phaseCode")
	private String phaseCode;
	/*
	 *排版时段名称
	 *排班描述，例如：上午
	 */
	@XmlElement(name="phaseDescName")
	private String phaseDescName;
	/*
	 *时段描述
	 *排班描述，例如：8:00-9:00
	 */
	@XmlElement(name="phaseDesc")
	private String phaseDesc;
	/*
	 *全部号源数
	 */
	@XmlElement(name="allCount")
	private String allCount;
	/*
	 *全部可预约号源数
	 */
	@XmlElement(name="apptCount")
	private String apptCount;

	/*
	 *已挂号数
	 */
	@XmlElement(name="outCount")
	private String outCount;
	/*
	 *剩余号源数
	 */
	@XmlElement(name="haveCount")
	private String haveCount;
	//该时段可预约数量
	@XmlElement(name="availableCount")
	private String availableCount;
	/*
	 *排班状态
	 *1:可预约；2:已满诊；3:已停诊
	 */
	@XmlElement(name="status")
	private String status;
	/*
	 *自助预约，微信预约、电话预约、窗口预约等
	 */
	@XmlElement(name="reserveChannel")
	private String reserveChannel;
	/*
	 *总挂号费
	 */
	@XmlElement(name="totalFee")
	private String totalFee;
	/*
	 *支付总金额
	 */
	@XmlElement(name="payFee")
	private String payFee;
	/*
	 *挂号费
	 */
	@XmlElement(name="regFee")
	private String regFee;
	/*
	 *检查费
	 */
	@XmlElement(name="treatFee")
	private String treatFee;
	/*
	 *服务费
	 */
	@XmlElement(name="serviceFee")
	private String serviceFee;
	/*
	 *其它费用
	 */
	@XmlElement(name="otherFee")
	private String otherFee;
	/*
	 *候诊地点
	 */
	@XmlElement(name="location")
	private String location;
	/*
	 *是否可取号
	 */
	@XmlElement(name="isAvailable")
	private String  isAvailable;
	/*
	 *是否已支付
	 */
	@XmlElement(name="isPay")
	private String isPay;
	/*
	 *是否可退号
	 */
	@XmlElement(name="isPay")
	private String canReject;
	/*
	 *挂号状态
	 */
	@XmlElement(name="regStatus")
	private String regStatus;
	/*
	 *预约票号
	 */
	@XmlElement(name="ticketNo")
	private String ticketNo;
	/*
	 *医生介绍
	 */
	@XmlElement(name="spec")
	private String spec;
	/*
	 *可是介绍
	 */
	@XmlElement(name="deptDesc")
	private String deptDesc;
	/*
	 *诊室信息
	 */
	@XmlElement(name="unitName")
	private String unitName;
	/**
	 * 科室响应消息体
	 */
	@XmlElement(name = "counts")
	private List<LbQueSchCount> schCount;
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getReserveChannel() {
		return reserveChannel;
	}
	public void setReserveChannel(String reserveChannel) {
		this.reserveChannel = reserveChannel;
	}
	public String getPayFee() {
		return payFee;
	}
	public void setPayFee(String payFee) {
		this.payFee = payFee;
	}
	public String getIsAvailable() {
		return isAvailable;
	}
	public void setIsAvailable(String isAvailable) {
		this.isAvailable = isAvailable;
	}
	public String getIsPay() {
		return isPay;
	}
	public void setIsPay(String isPay) {
		this.isPay = isPay;
	}
	public String getCanReject() {
		return canReject;
	}
	public void setCanReject(String canReject) {
		this.canReject = canReject;
	}
	public String getRegStatus() {
		return regStatus;
	}
	public void setRegStatus(String regStatus) {
		this.regStatus = regStatus;
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
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
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDoctCode() {
		return doctCode;
	}
	public void setDoctCode(String doctCode) {
		this.doctCode = doctCode;
	}
	public String getDoctName() {
		return doctName;
	}
	public void setDoctName(String doctName) {
		this.doctName = doctName;
	}
	public String getDoctDesc() {
		return doctDesc;
	}
	public void setDoctDesc(String doctDesc) {
		this.doctDesc = doctDesc;
	}
	public String getRankCode() {
		return rankCode;
	}
	public void setRankCode(String rankCode) {
		this.rankCode = rankCode;
	}
	public String getRankName() {
		return rankName;
	}
	public void setRankName(String rankName) {
		this.rankName = rankName;
	}
	public String getPhaseCode() {
		return phaseCode;
	}
	public void setPhaseCode(String phaseCode) {
		this.phaseCode = phaseCode;
	}
	public String getPhaseDesc() {
		return phaseDesc;
	}
	public void setPhaseDesc(String phaseDesc) {
		this.phaseDesc = phaseDesc;
	}
	public String getAllCount() {
		return allCount;
	}
	public void setAllCount(String allCount) {
		this.allCount = allCount;
	}

	public String getApptCount() {
		return apptCount;
	}

	public void setApptCount(String apptCount) {
		this.apptCount = apptCount;
	}

	public String getOutCount() {
		return outCount;
	}
	public void setOutCount(String outCount) {
		this.outCount = outCount;
	}
	public String getHaveCount() {
		return haveCount;
	}
	public void setHaveCount(String haveCount) {
		this.haveCount = haveCount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getTicketNo() {
		return ticketNo;
	}
	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}
	public String getAvailableCount() {
		return availableCount;
	}
	public void setAvailableCount(String availableCount) {
		this.availableCount = availableCount;
	}
	public List<LbQueSchCount> getSchCount() {
		return schCount;
	}
	public void setSchCount(List<LbQueSchCount> schCount) {
		this.schCount = schCount;
	}
	public String getPhaseDescName() {
		return phaseDescName;
	}
	public void setPhaseDescName(String phaseDescName) {
		this.phaseDescName = phaseDescName;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getDeptDesc() {
		return deptDesc;
	}
	public void setDeptDesc(String deptDesc) {
		this.deptDesc = deptDesc;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
}
