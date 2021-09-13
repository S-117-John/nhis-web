package com.zebone.nhis.ex.pub.vo;

import java.util.Date;

public class DeptCgItemVo {
	private String nameOrd;
	private Double quanTotal;
	// pv表
	private String bedNo;
	private String namePi;
	private String pkPi;
	private String euPvtype;
	private String pkPv;
	// item表
	private String itemname;// 计费项目
	private String pkUnit;
	private String flagPd;
	private String itemcode;
	private String catecode;//分类编码
	private String pkItem;
	private Double price;// 参考价格

	private String pkDeptOrd;
	private String pkOrgExec; // 当前机构
	private String pkDeptExec; // 执行科室当前
	private String pkOrgApp;//开立机构
	private String pkDeptNsApp;//开立科室
	private String pkDeptPv;//就诊科室
	private Double count;
	private String flagCg; // 0，不记费，1，记费
	private Date dateItem; // 费用时间
	private Integer euDirect;
	private Date cgBegin;
	private Date cgEnd;
	private String euCgmode;
	private Double dosage;// 计费数量
	private Date dateBill;// 最近计费日期，未赋值

	// 定时服务应用字段
	private String pkOrgSys;
	private String pkEmpSys;
	private String nameEmpSys;
	private String pkDeptSys;
	private String flagSys;
	
	private Date dateOut;//出院日期
	//包床标志
	private String spec;


	public String getCatecode() {
		return catecode;
	}

	public void setCatecode(String catecode) {
		this.catecode = catecode;
	}

	public Date getDateOut() {
		return dateOut;
	}

	public void setDateOut(Date dateOut) {
		this.dateOut = dateOut;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getPkOrgApp() {
		return pkOrgApp;
	}

	public void setPkOrgApp(String pkOrgApp) {
		this.pkOrgApp = pkOrgApp;
	}


	public String getPkDeptNsApp() {
		return pkDeptNsApp;
	}

	public void setPkDeptNsApp(String pkDeptNsApp) {
		this.pkDeptNsApp = pkDeptNsApp;
	}

	public String getPkDeptPv() {
		return pkDeptPv;
	}

	public void setPkDeptPv(String pkDeptPv) {
		this.pkDeptPv = pkDeptPv;
	}

	public String getNameOrd() {
		return nameOrd;
	}

	public void setNameOrd(String nameOrd) {
		this.nameOrd = nameOrd;
	}

	public Double getQuanTotal() {
		return quanTotal;
	}

	public void setQuanTotal(Double quanTotal) {
		this.quanTotal = quanTotal;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getEuPvtype() {
		return euPvtype;
	}

	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public String getPkUnit() {
		return pkUnit;
	}

	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}

	public String getFlagPd() {
		return flagPd;
	}

	public void setFlagPd(String flagPd) {
		this.flagPd = flagPd;
	}

	public String getItemcode() {
		return itemcode;
	}

	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}

	public String getPkItem() {
		return pkItem;
	}

	public void setPkItem(String pkItem) {
		this.pkItem = pkItem;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getPkDeptOrd() {
		return pkDeptOrd;
	}

	public void setPkDeptOrd(String pkDeptOrd) {
		this.pkDeptOrd = pkDeptOrd;
	}

	public String getPkOrgExec() {
		return pkOrgExec;
	}

	public void setPkOrgExec(String pkOrgExec) {
		this.pkOrgExec = pkOrgExec;
	}

	public String getPkDeptExec() {
		return pkDeptExec;
	}

	public void setPkDeptExec(String pkDeptExec) {
		this.pkDeptExec = pkDeptExec;
	}

	public Double getCount() {
		return count;
	}

	public void setCount(Double count) {
		this.count = count;
	}

	public String getFlagCg() {
		return flagCg;
	}

	public void setFlagCg(String flagCg) {
		this.flagCg = flagCg;
	}

	public Date getDateItem() {
		return dateItem;
	}

	public void setDateItem(Date dateItem) {
		this.dateItem = dateItem;
	}

	public Integer getEuDirect() {
		return euDirect;
	}

	public void setEuDirect(Integer euDirect) {
		this.euDirect = euDirect;
	}

	public Date getCgBegin() {
		return cgBegin;
	}

	public void setCgBegin(Date cgBegin) {
		this.cgBegin = cgBegin;
	}

	public Date getCgEnd() {
		return cgEnd;
	}

	public void setCgEnd(Date cgEnd) {
		this.cgEnd = cgEnd;
	}

	public String getEuCgmode() {
		return euCgmode;
	}

	public void setEuCgmode(String euCgmode) {
		this.euCgmode = euCgmode;
	}

	public Double getDosage() {
		return dosage;
	}

	public void setDosage(Double dosage) {
		this.dosage = dosage;
	}

	public Date getDateBill() {
		return dateBill;
	}

	public void setDateBill(Date dateBill) {
		this.dateBill = dateBill;
	}

	public String getPkOrgSys() {
		return pkOrgSys;
	}

	public void setPkOrgSys(String pkOrgSys) {
		this.pkOrgSys = pkOrgSys;
	}

	public String getPkEmpSys() {
		return pkEmpSys;
	}

	public void setPkEmpSys(String pkEmpSys) {
		this.pkEmpSys = pkEmpSys;
	}

	public String getNameEmpSys() {
		return nameEmpSys;
	}

	public void setNameEmpSys(String nameEmpSys) {
		this.nameEmpSys = nameEmpSys;
	}

	public String getPkDeptSys() {
		return pkDeptSys;
	}

	public void setPkDeptSys(String pkDeptSys) {
		this.pkDeptSys = pkDeptSys;
	}

	public String getFlagSys() {
		return flagSys;
	}

	public void setFlagSys(String flagSys) {
		this.flagSys = flagSys;
	}

	@Override
	public Object clone() {
		DeptCgItemVo o = null;
		try {
			o = (DeptCgItemVo) super.clone();
			// o.name=(String[])name.clone();//其实也很简单^_^
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

}
