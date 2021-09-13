package com.zebone.nhis.webservice.zsrm.vo.self;

import java.math.BigDecimal;

public class ResponseQryNoSettleVo {

    /**
     * 患者姓名
     */
    private String namePi;

    /**
     * 患者编码
     */
    private String codePi;

    /**
     * 就诊编码
     */
    private String codePv;

    /**
     * 门诊号
     */
    private String codeOp;

    /**
     * 就诊次数
     */
    private Integer opTimes;

    /**
     * 就诊时间
     */
    private String datePv;

    /**
     * 处方号
     */
    private String presNo;

    /**
     * 项目编码
     */
    private String itemCode;

    /**
     * 项目名称
     */
    private String nameOrd;

    /**
     * 项目分类
     */
    private String nameBill;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 医嘱序号（代替费用明细编码）
     */
    private String ordsn;

    /**
     * 单位
     */
    private String unit;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 数量
     */
    private Double quanCg;

    /**
     * 规格
     */
    private String spec;

    /**
     * 执行科室
     */
    private String nameDeptEx;

    /**
     * 开单科室
     */
    private String nameDept;

    /**
     * 开单时间
     */
    private String dateEnter;

    /**
     * 费用明细主键
     */
    private String pkCgop;

    /**
     * 开立工号
     */
    private String creatDocterNo;

    /**
     * 开立人
     */
    private String creatDocterName;
    
    /**
     * 第三方医保编码
     */
    private String dtExthp;

    /**
     * 第三方医保名称
     */
    private String nameExthp;
    
    public String getCreatDocterNo() {
        return creatDocterNo;
    }

    public void setCreatDocterNo(String creatDocterNo) {
        this.creatDocterNo = creatDocterNo;
    }

    public String getCreatDocterName() {
        return creatDocterName;
    }

    public void setCreatDocterName(String creatDocterName) {
        this.creatDocterName = creatDocterName;
    }

    public String getNamePi() {
        return namePi;
    }

    public void setNamePi(String namePi) {
        this.namePi = namePi;
    }

    public String getCodePi() {
        return codePi;
    }

    public void setCodePi(String codePi) {
        this.codePi = codePi;
    }

    public String getCodePv() {
        return codePv;
    }

    public void setCodePv(String codePv) {
        this.codePv = codePv;
    }

    public String getCodeOp() {
        return codeOp;
    }

    public void setCodeOp(String codeOp) {
        this.codeOp = codeOp;
    }

    public Integer getOpTimes() {
        return opTimes;
    }

    public void setOpTimes(Integer opTimes) {
        this.opTimes = opTimes;
    }

    public String getDatePv() {
        return datePv;
    }

    public void setDatePv(String datePv) {
        this.datePv = datePv;
    }

    public String getPresNo() {
        return presNo;
    }

    public void setPresNo(String presNo) {
        this.presNo = presNo;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getNameOrd() {
        return nameOrd;
    }

    public void setNameOrd(String nameOrd) {
        this.nameOrd = nameOrd;
    }

    public String getNameBill() {
        return nameBill;
    }

    public void setNameBill(String nameBill) {
        this.nameBill = nameBill;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOrdsn() {
        return ordsn;
    }

    public void setOrdsn(String ordsn) {
        this.ordsn = ordsn;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Double getQuanCg() {
        return quanCg;
    }

    public void setQuanCg(Double quanCg) {
        this.quanCg = quanCg;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getNameDeptEx() {
        return nameDeptEx;
    }

    public void setNameDeptEx(String nameDeptEx) {
        this.nameDeptEx = nameDeptEx;
    }

    public String getNameDept() {
        return nameDept;
    }

    public void setNameDept(String nameDept) {
        this.nameDept = nameDept;
    }

    public String getDateEnter() {
        return dateEnter;
    }

    public void setDateEnter(String dateEnter) {
        this.dateEnter = dateEnter;
    }

    public String getPkCgop() {
        return pkCgop;
    }

    public void setPkCgop(String pkCgop) {
        this.pkCgop = pkCgop;
    }

	public String getDtExthp() {
		return dtExthp;
	}

	public String getNameExthp() {
		return nameExthp;
	}

	public void setDtExthp(String dtExthp) {
		this.dtExthp = dtExthp;
	}

	public void setNameExthp(String nameExthp) {
		this.nameExthp = nameExthp;
	}
    
}
