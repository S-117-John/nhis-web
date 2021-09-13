package com.zebone.nhis.ma.pub.sd.vo;
/**
 * 清单项目明细
 * @author Administrator
 *
 */
public class ListDetailVo {
	
	/**
	 * 明细流水号
	 * false
	 */
	private String listDetailNo;
	
	/**
	 * 收费项目代码
	 * false
	 */
	private String chargeCode;
	
	/**
	 * 收费项目名称
	 * false
	 */
	private String chargeName;
	
	/**
	 * 处方编码
	 * false
	 */
	private String prescribeCode;
	
	/**
	 * 药品类别编码
	 * false
	 */
	private String listTypeCode ;
	
	/**
	 * 药品类别名称
	 * false
	 */
	private String listTypeName;
	
	/**
	 * 编码
	 * false
	 */
	private String code;
	
	/**
	 * 药品名称
	 * true
	 */
	private String name;
	
	/**
	 * 剂型
	 * false
	 */
	private String form;
	
	/**
	 * 规格
	 * false
	 */
	private String specification;
	
	/**
	 * 计量单位 
	 * false
	 */
	private String unit;
	
	/**
	 * 单价
	 * true
	 */
	private Double std;
	
	/**
	 * 数量
	 * true
	 */
	private Double number;
	
	/**
	 * 金额
	 * true
	 */
	private Double amt;
	
	/**
	 * 自费金额
	 * true
	 */
	private Double selfAmt;
	
	/**
	 * 应收费用
	 * false
	 */
	private Double receivableAmt;
	
	/**
	 * 医保药品分类
	 * false
	 *  1：无自负/甲
		2：有自负/乙
		3：全自负/丙
	 */
	private String medicalCareType;
	
	/**
	 * 医保项目类型
	 * false
	 */
	private String medCareItemType;
	
	/**
	 * 医保报销比例
	 * false
	 */
	private Double medReimburseRate;
	
	/**
	 * 备注
	 * false
	 */
	private String remark;
	
	/**
	 * 序号
	 * false
	 */
	private Integer sortNo;
	
	/**
	 * 费用类型
	 * false
	 */
	private String chrgtype;
	
	/**
	 * 做金额比较，不上传平台
	 */
	private Double amtChk;
	
	/**
	 * 做金额比较，不上传平台
	 */
	private Double selfAmtChk;

	public Double getAmtChk() {
		return amtChk;
	}

	public void setAmtChk(Double amtChk) {
		this.amtChk = amtChk;
	}

	public Double getSelfAmtChk() {
		return selfAmtChk;
	}

	public void setSelfAmtChk(Double selfAmtChk) {
		this.selfAmtChk = selfAmtChk;
	}

	public String getListDetailNo() {
		return listDetailNo;
	}

	public void setListDetailNo(String listDetailNo) {
		this.listDetailNo = listDetailNo;
	}

	public String getChargeCode() {
		return chargeCode;
	}

	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}

	public String getChargeName() {
		return chargeName;
	}

	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}

	public String getPrescribeCode() {
		return prescribeCode;
	}

	public void setPrescribeCode(String prescribeCode) {
		this.prescribeCode = prescribeCode;
	}

	public String getListTypeCode() {
		return listTypeCode;
	}

	public void setListTypeCode(String listTypeCode) {
		this.listTypeCode = listTypeCode;
	}

	public String getListTypeName() {
		return listTypeName;
	}

	public void setListTypeName(String listTypeName) {
		this.listTypeName = listTypeName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getStd() {
		return std;
	}

	public void setStd(Double std) {
		this.std = std;
	}

	public Double getNumber() {
		return number;
	}

	public void setNumber(Double number) {
		this.number = number;
	}

	public Double getAmt() {
		return amt;
	}

	public void setAmt(Double amt) {
		this.amt = amt;
	}

	public Double getSelfAmt() {
		return selfAmt;
	}

	public void setSelfAmt(Double selfAmt) {
		this.selfAmt = selfAmt;
	}

	public Double getReceivableAmt() {
		return receivableAmt;
	}

	public void setReceivableAmt(Double receivableAmt) {
		this.receivableAmt = receivableAmt;
	}

	public String getMedicalCareType() {
		return medicalCareType;
	}

	public void setMedicalCareType(String medicalCareType) {
		this.medicalCareType = medicalCareType;
	}

	public String getMedCareItemType() {
		return medCareItemType;
	}

	public void setMedCareItemType(String medCareItemType) {
		this.medCareItemType = medCareItemType;
	}

	public Double getMedReimburseRate() {
		return medReimburseRate;
	}

	public void setMedReimburseRate(Double medReimburseRate) {
		this.medReimburseRate = medReimburseRate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public String getChrgtype() {
		return chrgtype;
	}

	public void setChrgtype(String chrgtype) {
		this.chrgtype = chrgtype;
	}
	
	
}
