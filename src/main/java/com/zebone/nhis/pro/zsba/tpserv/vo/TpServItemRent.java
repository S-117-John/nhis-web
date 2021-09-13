package com.zebone.nhis.pro.zsba.tpserv.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: tp_serv_item_rent - 第三方服务项目出租 
 *
 * @since 2017-09-07 12:29:02
 */
@Table(value="TP_SERV_ITEM_RENT")
public class TpServItemRent  extends BaseModule {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1826734400122986771L;

	/** PK_RENT - 项目出租主键 */
	@PK
	@Field(value="PK_RENT",id=KeyId.UUID)
    private String pkRent;
	
	/** FK_DEPT - 就诊 */
	@Field(value="PK_PV")
    private String pkPv;

    /** FK_PATIENT - 患者主键 */
	@Field(value="FK_PATIENT")
    private String fkPatient;

    /** PATIENT_NAME - 患者名称 */
	@Field(value="PATIENT_NAME")
    private String patientName;

    /** PHONE - 联系人手机号码 */
	@Field(value="PHONE")
    private String phone;

    /** ADDRESS - 寄送地址 */
	@Field(value="ADDRESS")
    private String address;

    /** EXPRESS_COMPANY - 物流公司名称 */
	@Field(value="EXPRESS_COMPANY")
    private String expressCompany;

    /** TRACKING_NUMBER - 快递单号 */
	@Field(value="TRACKING_NUMBER")
    private String trackingNumber;

    /** FK_ITEM - 项目主键 */
	@Field(value="FK_ITEM")
    private String fkItem;

    /** ITEM_NAME - 项目名称 */
	@Field(value="ITEM_NAME")
    private String itemName;

    /** FK_ITEM_DEVICE - 项目设备主键(为空时是服务/护工费等) */
	@Field(value="FK_ITEM_DEVICE")
    private String fkItemDevice;

    /** PRICE - 租凭单价 */
	@Field(value="PRICE")
    private BigDecimal price;

    /** NUM - 租凭数量 */
	@Field(value="NUM")
    private Integer num;

    /** DATE_NUM - 租凭天数/周数/月数 */
	@Field(value="DATE_NUM")
    private Integer dateNum;

    /** RENT_METHOD - 出租方式，0：按天<天数*数量*单价>，1：按数量<数量*单价> */
	@Field(value="RENT_METHOD")
    private String rentMethod;

    /** PLEDGE_STATE - 押金状态，0：无押金，1：有押金 */
	@Field(value="PLEDGE_STATE")
    private String pledgeState;

    /** CASH_PLEDGE - 押金，为空则不需要缴纳押金 */
	@Field(value="CASH_PLEDGE")
    private BigDecimal cashPledge;

    /** RENT_STATE - 出租状态，0：出租中，1：出租完结 */
	@Field(value="RENT_STATE")
    private String rentState;

    /** SJ_DATE_NUM - 实际天数，结算时填写 */
	@Field(value="SJ_DATE_NUM")
    private Integer sjDateNum;

    /** AMOUNT_TOTAL - 总费用，结算时填写 */
	@Field(value="AMOUNT_TOTAL")
    private BigDecimal amountTotal;

    /** DIFFER_AMOUNT - 差额(正数：收，负数：退，0：直接完成)，结算时填写 */
	@Field(value="DIFFER_AMOUNT")
    private BigDecimal differAmount;

	/** PK_RENT_BACK - 退费时，记录对应的收费主键。可为空表示无对应记费的退费 */
	@Field(value="PK_RENT_BACK")
    private String pkRentBack;
	
	/** SETTLE_FLAG - 结算标志 */
	@Field(value="SETTLE_FLAG")
    private String settleFlag;
	
	/** PK_SERV_SETTLE- 结算主键 */
	@Field(value="PK_SERV_SETTLE")
    private String pkServSettle;
	
	/** DATE_CG- 记费日期 */
	@Field(value="DATE_CG")
    private Date dateCg;
	
	/** PK_DEPT_CG- 记费部门 */
	@Field(value="PK_DEPT_CG")
    private String pkDeptCg;
	
	/** PK_EMP_CG- 记费人员 */
	@Field(value="PK_EMP_CG")
    private String pkEmpCg;
	
	/** NAME_EMP_CG- 记费人员名称 */
	@Field(value="NAME_EMP_CG")
    private String nameEmpCg;
	
    /** MODITY_TIME - 修改时间 */
	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;

	public String getPkRent() {
		return pkRent;
	}

	public void setPkRent(String pkRent) {
		this.pkRent = pkRent;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getFkPatient() {
		return fkPatient;
	}

	public void setFkPatient(String fkPatient) {
		this.fkPatient = fkPatient;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
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

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public String getFkItem() {
		return fkItem;
	}

	public void setFkItem(String fkItem) {
		this.fkItem = fkItem;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getFkItemDevice() {
		return fkItemDevice;
	}

	public void setFkItemDevice(String fkItemDevice) {
		this.fkItemDevice = fkItemDevice;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getDateNum() {
		return dateNum;
	}

	public void setDateNum(Integer dateNum) {
		this.dateNum = dateNum;
	}

	public String getRentMethod() {
		return rentMethod;
	}

	public void setRentMethod(String rentMethod) {
		this.rentMethod = rentMethod;
	}

	public String getPledgeState() {
		return pledgeState;
	}

	public void setPledgeState(String pledgeState) {
		this.pledgeState = pledgeState;
	}

	public BigDecimal getCashPledge() {
		return cashPledge;
	}

	public void setCashPledge(BigDecimal cashPledge) {
		this.cashPledge = cashPledge;
	}

	public String getRentState() {
		return rentState;
	}

	public void setRentState(String rentState) {
		this.rentState = rentState;
	}

	public Integer getSjDateNum() {
		return sjDateNum;
	}

	public void setSjDateNum(Integer sjDateNum) {
		this.sjDateNum = sjDateNum;
	}

	public BigDecimal getAmountTotal() {
		return amountTotal;
	}

	public void setAmountTotal(BigDecimal amountTotal) {
		this.amountTotal = amountTotal;
	}

	public BigDecimal getDifferAmount() {
		return differAmount;
	}

	public void setDifferAmount(BigDecimal differAmount) {
		this.differAmount = differAmount;
	}

	public String getPkRentBack() {
		return pkRentBack;
	}

	public void setPkRentBack(String pkRentBack) {
		this.pkRentBack = pkRentBack;
	}

	public String getSettleFlag() {
		return settleFlag;
	}

	public void setSettleFlag(String settleFlag) {
		this.settleFlag = settleFlag;
	}

	public String getPkServSettle() {
		return pkServSettle;
	}

	public void setPkServSettle(String pkServSettle) {
		this.pkServSettle = pkServSettle;
	}

	public Date getDateCg() {
		return dateCg;
	}

	public void setDateCg(Date dateCg) {
		this.dateCg = dateCg;
	}

	public String getPkDeptCg() {
		return pkDeptCg;
	}

	public void setPkDeptCg(String pkDeptCg) {
		this.pkDeptCg = pkDeptCg;
	}

	public String getPkEmpCg() {
		return pkEmpCg;
	}

	public void setPkEmpCg(String pkEmpCg) {
		this.pkEmpCg = pkEmpCg;
	}

	public String getNameEmpCg() {
		return nameEmpCg;
	}

	public void setNameEmpCg(String nameEmpCg) {
		this.nameEmpCg = nameEmpCg;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}
}