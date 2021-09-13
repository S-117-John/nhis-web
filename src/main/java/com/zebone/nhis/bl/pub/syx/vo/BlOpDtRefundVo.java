package com.zebone.nhis.bl.pub.syx.vo;

import java.math.BigDecimal;
import java.util.Date;

public class BlOpDtRefundVo {
	 private String pkPres;//处方主键
     private String presNo;//处方号
     private String nameDept;//开立科室
     private Date datePres;//开立时间
     private String nameEmpOrd;//开立医生
     private Double ords ;//   --付数，dt_prestype='02' 才显示
     private BigDecimal presAmount; //处方金额
     private String nameOrd;//医嘱名称
     private String pkCnord;//医嘱主键
	 private String pkCgop;//记费主键
	 private String nameCg;//名称
	 private String spec;//规格
	 private Double quan;//数量
	 private String unit;//单位
	 private BigDecimal price;//单价
	 private BigDecimal amount;//金额
	 private Double quanBack;//可退药量
	 private BigDecimal amountBack;//退费金额
	 private String status;//状态
	 private String pkSettle;//结算主键
	 private String flagPv;//挂号费标志
	 private String dtPrestype;//处方类型 02时显示付数
	 private String flagPd;//药品标志
	 private String pkPresocc;//处方执行主键
	 private String pkPresoccdt;//处方执行明细主键
	 private String flagCheck;//是否选择退费
	 private String pkPv;//就诊主键
	 private String pkPi;//患者主键
	 private String flagInsu;//医保上传标志
	 private String pkPd;//药品主键
	 
	public String getPkPd() {
		return pkPd;
	}
	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
	}
	public String getFlagInsu() {
		return flagInsu;
	}
	public void setFlagInsu(String flagInsu) {
		this.flagInsu = flagInsu;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}
	public String getFlagCheck() {
		return flagCheck;
	}
	public void setFlagCheck(String flagCheck) {
		this.flagCheck = flagCheck;
	}
	public String getPkPresocc() {
		return pkPresocc;
	}
	public void setPkPresocc(String pkPresocc) {
		this.pkPresocc = pkPresocc;
	}
	public String getPkPresoccdt() {
		return pkPresoccdt;
	}
	public void setPkPresoccdt(String pkPresoccdt) {
		this.pkPresoccdt = pkPresoccdt;
	}
	public String getDtPrestype() {
		return dtPrestype;
	}
	public void setDtPrestype(String dtPrestype) {
		this.dtPrestype = dtPrestype;
	}
	public String getFlagPd() {
		return flagPd;
	}
	public void setFlagPd(String flagPd) {
		this.flagPd = flagPd;
	}
	public String getPkPres() {
		return pkPres;
	}
	public void setPkPres(String pkPres) {
		this.pkPres = pkPres;
	}
	public String getPresNo() {
		return presNo;
	}
	public void setPresNo(String presNo) {
		this.presNo = presNo;
	}
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	public Date getDatePres() {
		return datePres;
	}
	public void setDatePres(Date datePres) {
		this.datePres = datePres;
	}
	public String getNameEmpOrd() {
		return nameEmpOrd;
	}
	public void setNameEmpOrd(String nameEmpOrd) {
		this.nameEmpOrd = nameEmpOrd;
	}
	public Double getOrds() {
		return ords;
	}
	public void setOrds(Double ords) {
		this.ords = ords;
	}
	public BigDecimal getPresAmount() {
		return presAmount;
	}
	public void setPresAmount(BigDecimal presAmount) {
		this.presAmount = presAmount;
	}
	public String getNameOrd() {
		return nameOrd;
	}
	public void setNameOrd(String nameOrd) {
		this.nameOrd = nameOrd;
	}
	public String getPkCnord() {
		return pkCnord;
	}
	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}
	public String getPkCgop() {
		return pkCgop;
	}
	public void setPkCgop(String pkCgop) {
		this.pkCgop = pkCgop;
	}
	public String getNameCg() {
		return nameCg;
	}
	public void setNameCg(String nameCg) {
		this.nameCg = nameCg;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public Double getQuan() {
		return quan;
	}
	public void setQuan(Double quan) {
		this.quan = quan;
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
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Double getQuanBack() {
		return quanBack;
	}
	public void setQuanBack(Double quanBack) {
		this.quanBack = quanBack;
	}
	public BigDecimal getAmountBack() {
		return amountBack;
	}
	public void setAmountBack(BigDecimal amountBack) {
		this.amountBack = amountBack;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPkSettle() {
		return pkSettle;
	}
	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}
	public String getFlagPv() {
		return flagPv;
	}
	public void setFlagPv(String flagPv) {
		this.flagPv = flagPv;
	}
	 
	 
     
}
