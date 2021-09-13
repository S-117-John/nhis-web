package com.zebone.nhis.pro.zsba.scm.vo;

import java.util.Date;
import java.util.List;

/**
 * 住院发药数据传输层
 * @author gongxy
 * 
 */
public class IpDeDrugBaDto {

	// 请领病区主键
	private String pkAppDeptNs;

	// 请领单主键
	private String[] pkPdaps;

	// 请领单明细主键集合
	private String[] pkPdapDts;
	// 请领单明细主键
	private String pkPdapDt;

	// 请领开始日期
	private Date dateStart;

	// 请领结束日期
	private Date dateEnd;

	// 发放类型编码
	private String codeDecate;
	
	// 发放类型主键
	private String pkPddecate;
	// 发放类型名称
	private String nameDecate;

	// 住院号
	private String[] pvIpCodes;

	// 发放分类whereSql
	private String deCateWhereSql;

	// 当前登录仓库主键
	private String pkPdStock;

	// 机构主键
	private String pkOrg;

	// 当前科室
	private String pkDept;

	// 发退方向
	private String euDirect;

	// 是否是处方请领单
	private String isPres;


	// 是否是科室领药单
	private String flagDept;

	// 发药单号
	private String codeDe;

	// 批次
	private String batchNo;

	// 患者姓名
	private String namePi;

	// 物品主键
	private String pkPd;
	// 物品名称
	private String namePd;

	// 静配标志
	private String flagPivas;

	// 打印标志
	private String flagPrt;

	// 发放人员
	private String pkEmpDe;

	// 请领类型
	private String euAptype;

	// 状态
	private String euStatus;

	// 请领单主键
	private String pkPdap;

	// 请领单号
	private String codeAp;

	// 床号
	private String bedNo;
	
	//处方号码
	private String presNo;
	
	//住院号
	private String codeIp;
	
	/** 每页行数 */
	private int pageSize;
	/** 页码 */
	private int pageIndex;
	// 是否加急
	private String flagEmer;

	public Integer getDistributionType() {
		return distributionType;
	}

	public void setDistributionType(Integer distributionType) {
		this.distributionType = distributionType;
	}

	/**发药业务类型：
	 * 0 公用，1 医嘱发药，2 科室发药
	 * */
	private Integer  distributionType;
	
	//仓库


	public String getFlagEmer() {
		return flagEmer;
	}

	public void setFlagEmer(String flagEmer) {
		this.flagEmer = flagEmer;
	}

	public String getDtPrestype() {
		return dtPrestype;
	}

	public void setDtPrestype(String dtPrestype) {
		this.dtPrestype = dtPrestype;
	}

	private String pkStore;
	
	/**
	 * 发药人
	 */
	private String nameEmpDe;

	private String nameApDept;

	private String dtPrestype;

	public String getNameApDept() {
		return nameApDept;
	}

	public void setNameApDept(String nameApDept) {
		this.nameApDept = nameApDept;
	}

	public String getPkPdapDt() {
		return pkPdapDt;
	}

	public void setPkPdapDt(String pkPdapDt) {
		this.pkPdapDt = pkPdapDt;
	}

	private List<String> pkDeptApList;

	public String getPresNo() {
		return presNo;
	}

	public void setPresNo(String presNo) {
		this.presNo = presNo;
	}

	public String getNamePd() {
		return namePd;
	}

	public void setNamePd(String namePd) {
		this.namePd = namePd;
	}

	public String getBedNo() {

		return bedNo;
	}

	public void setBedNo(String bedNo) {

		this.bedNo = bedNo;
	}

	public String getCodeDe() {

		return codeDe;
	}

	public String getFlagPrt() {

		return flagPrt;
	}

	public String getCodeAp() {
		return codeAp;
	}

	public void setCodeAp(String codeAp) {
		this.codeAp = codeAp;
	}

	public String getEuAptype() {

		return euAptype;
	}

	public String getPkPdap() {

		return pkPdap;
	}

	public void setPkPdap(String pkPdap) {

		this.pkPdap = pkPdap;
	}

	public void setEuAptype(String euAptype) {

		this.euAptype = euAptype;
	}

	public String getEuStatus() {

		return euStatus;
	}

	public void setEuStatus(String euStatus) {

		this.euStatus = euStatus;
	}

	public void setFlagPrt(String flagPrt) {

		this.flagPrt = flagPrt;
	}

	public void setCodeDe(String codeDe) {

		this.codeDe = codeDe;
	}

	public String getBatchNo() {

		return batchNo;
	}

	public void setBatchNo(String batchNo) {

		this.batchNo = batchNo;
	}

	public String getNamePi() {

		return namePi;
	}

	public void setNamePi(String namePi) {

		this.namePi = namePi;
	}

	public String getPkPd() {

		return pkPd;
	}

	public void setPkPd(String pkPd) {

		this.pkPd = pkPd;
	}

	public String getFlagPivas() {

		return flagPivas;
	}

	public void setFlagPivas(String flagPivas) {

		this.flagPivas = flagPivas;
	}

	public String getPkEmpDe() {

		return pkEmpDe;
	}

	public void setPkEmpDe(String pkEmpDe) {

		this.pkEmpDe = pkEmpDe;
	}

	public String getEuDirect() {

		return euDirect;
	}

	public void setEuDirect(String euDirect) {

		this.euDirect = euDirect;
	}

	public String getIsPres() {

		return isPres;
	}

	public void setIsPres(String isPres) {

		this.isPres = isPres;
	}

	public String getPkDept() {

		return pkDept;
	}

	public void setPkDept(String pkDept) {

		this.pkDept = pkDept;
	}

	public String[] getPkPdapDts() {

		return pkPdapDts;
	}

	public void setPkPdapDts(String[] pkPdapDts) {

		this.pkPdapDts = pkPdapDts;
	}

	public String getPkOrg() {

		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {

		this.pkOrg = pkOrg;
	}

	public String getPkPdStock() {

		return pkPdStock;
	}

	public void setPkPdStock(String pkPdStock) {

		this.pkPdStock = pkPdStock;
	}

	public String getDeCateWhereSql() {

		return deCateWhereSql;
	}

	public void setDeCateWhereSql(String deCateWhereSql) {

		this.deCateWhereSql = deCateWhereSql;
	}

	public String getPkAppDeptNs() {

		return pkAppDeptNs;
	}

	public void setPkAppDeptNs(String pkAppDeptNs) {

		this.pkAppDeptNs = pkAppDeptNs;
	}

	public String[] getPkPdaps() {

		return pkPdaps;
	}

	public void setPkPdaps(String[] pkPdaps) {

		this.pkPdaps = pkPdaps;
	}

	public Date getDateStart() {

		return dateStart;
	}

	public void setDateStart(Date dateStart) {

		this.dateStart = dateStart;
	}

	public Date getDateEnd() {

		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {

		this.dateEnd = dateEnd;
	}

	public String getCodeDecate() {

		return codeDecate;
	}

	public void setCodeDecate(String codeDecate) {

		this.codeDecate = codeDecate;
	}

	public String[] getPvIpCodes() {

		return pvIpCodes;
	}

	public void setPvIpCodes(String[] pvIpCodes) {

		this.pvIpCodes = pvIpCodes;
	}

	public String getFlagDept() {

		return flagDept;
	}

	public void setFlagDept(String flagDept) {

		this.flagDept = flagDept;
	}

	public String getPkPddecate() {
		return pkPddecate;
	}

	public void setPkPddecate(String pkPddecate) {
		this.pkPddecate = pkPddecate;
	}

	public String getNameDecate() {
		return nameDecate;
	}

	public void setNameDecate(String nameDecate) {
		this.nameDecate = nameDecate;
	}

	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	public List<String> getPkDeptApList() {
		return pkDeptApList;
	}

	public void setPkDeptApList(List<String> pkDeptApList) {
		this.pkDeptApList = pkDeptApList;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public String getPkStore() {
		return pkStore;
	}

	public void setPkStore(String pkStore) {
		this.pkStore = pkStore;
	}

	public String getNameEmpDe() {
		return nameEmpDe;
	}

	public void setNameEmpDe(String nameEmpDe) {
		this.nameEmpDe = nameEmpDe;
	}
	
	
}
