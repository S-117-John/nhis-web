package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bl;

import java.io.Serializable;
import java.util.Date;

/**
 * 记费对外入口参数类型
 * 
 * 说明：字段上注释为true的是必填项，false为可选项(如果为null,会重新查，势必会影响速度，建议传入)
 * 
 * @author gongxy 2016-09-14
 */
public class BlPubParamVo implements Serializable {

	private static final long serialVersionUID = 000000000000001L;

	/**
	 * 机构true
	 */
	private String pkOrg;

	/**
	 * 处方主键 false
	 */
	private String pkPres;

	/**
	 * 就诊类型 false
	 */
	private String euPvType;
	/**
	 * 规格
	 */
	private String spec;

	/**
	 * 就诊主键 true
	 */
	private String pkPv;

	/**
	 * 患者主键 true ---
	 */
	private String pkPi;

	/**
	 * 医嘱编码主键 false 记医嘱项目与药品费时必传
	 */
	private String pkOrd;

	/**
	 * 临床医嘱主键 false
	 */
	private String pkCnord;

	/**
	 * 收费项目主键 false 只记收费项目时必传，记医嘱项目与药品费时不允许传
	 */
	private String pkItem;

	/**
	 * 医嘱记费数量 true
	 */
	private Double quanCg;

	/**
	 * 执行机构 true
	 */
	private String pkOrgEx;

	/**
	 * 开立机构 true
	 */
	private String pkOrgApp;

	/**
	 * 执行科室 true
	 */
	private String pkDeptEx;

	/**
	 * 开立科室 true
	 */
	private String pkDeptApp;

	private String pkDeptNsApp;

	/**
	 * 开立医生true
	 */
	private String pkEmpApp;

	/**
	 * 开立医生姓名true
	 */
	private String nameEmpApp;

	/**
	 * 物品标志 true
	 */
	private String flagPd;
	
	/**
	 * 适应症标志 0非适应症，1适应症；药品记费使用
	 */
	private String flagFit;
	
	/**
	 * 适应症信息
	 */
	private String descFit;

	// 批号
	private String batchNo;

	// 失效日期
	private Date dateExpire;

	// 零售单位
	private String pkUnitPd;

	// 包装量（如果是药品，必传）
	private Integer packSize;

	// 药品成本单价
	private Double priceCost;

	// 药品名称
	private String namePd;
	/**
	 * 药品是否已取价格 --已取价格药品不走询价流程
	 */
	private String flagHasPdPrice;
	/**
	 * 挂号费用标志 false 如果是挂号费用必须为1 否则全为0
	 */
	private String flagPv;

	/**
	 * 服务发生日期true
	 */
	private Date dateHap;

	/**
	 * 记费科室true
	 */
	private String pkDeptCg;

	/**
	 * 记费人员true
	 */
	private String pkEmpCg;

	/**
	 * 记费人员名称true
	 */
	private String nameEmpCg;

	/**
	 * 关联执行单主键
	 */
	private String pkOrdexdt;

	/**
	 * 婴儿标志
	 */
	private String infantNo;

	/**
	 * 如果是药品，此单价从外部直接传入
	 * 
	 * 零售单价
	 */
	private Double price;
	/**
	 * 附加项目标志 0 非附加，1 附加项目--yangxue添加2018.8.6
	 */
	private String euAdditem;
	
	/**
	 *  医嘱开始日期，检验项目合并记费使用--中二
	 */
	private Date dateStart;
	
	/**
	 * 医嘱类型编码，检验项目合并记费使用--中二
	 */
	private String codeOrdtype;
	
	/**
	 * 医嘱号，检查类医嘱记费使用--中二
	 */
	private Integer ordsn;
	
	/**
	 * 父医嘱号，检查类医嘱记费使用--中二
	 */
	private Integer ordsnParent;
	
	/**
	 * 标本类型,检查类医嘱记费使用--中二
	 */
	private String dtSamptype;
	
	/**
	 * 自动执行记费标志(此标志为true则bl_ip_dt记费部门、记费人员、记费人员姓名为空)--中二
	 */
	private String flagSign;
	
	/**
	 * 高值耗材材料编码 --中二
	 */
	private String barcode;
	
	/**
	 * 执行医生--中二
	 * */
	private String pkEmpEx;
	
	/** 执行医生姓名--中二*/
	private String nameEmpEx;
	
	/** 记费类型--中二*/
	private String euBltype;
	
	/**关联医嘱--中二*/
	private String pkCnordRl;
	
	/**
	 * 医技业务处理页面，判断是否为新增的收费项目--中二
	 */
	private String isNewItem;
	
	
	
	public String getDescFit() {
		return descFit;
	}

	public void setDescFit(String descFit) {
		this.descFit = descFit;
	}

	public String getIsNewItem() {
		return isNewItem;
	}

	public void setIsNewItem(String isNewItem) {
		this.isNewItem = isNewItem;
	}
	
	
	public String getPkEmpEx() {
		return pkEmpEx;
	}

	public void setPkEmpEx(String pkEmpEx) {
		this.pkEmpEx = pkEmpEx;
	}

	public String getNameEmpEx() {
		return nameEmpEx;
	}

	public void setNameEmpEx(String nameEmpEx) {
		this.nameEmpEx = nameEmpEx;
	}

	public String getEuBltype() {
		return euBltype;
	}

	public void setEuBltype(String euBltype) {
		this.euBltype = euBltype;
	}

	public String getPkCnordRl() {
		return pkCnordRl;
	}

	public void setPkCnordRl(String pkCnordRl) {
		this.pkCnordRl = pkCnordRl;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getDtSamptype() {
		return dtSamptype;
	}

	public void setDtSamptype(String dtSamptype) {
		this.dtSamptype = dtSamptype;
	}

	public Integer getOrdsn() {
		return ordsn;
	}

	public void setOrdsn(Integer ordsn) {
		this.ordsn = ordsn;
	}

	public Integer getOrdsnParent() {
		return ordsnParent;
	}

	public void setOrdsnParent(Integer ordsnParent) {
		this.ordsnParent = ordsnParent;
	}

	public String getFlagSign() {
		return flagSign;
	}

	public void setFlagSign(String flagSign) {
		this.flagSign = flagSign;
	}

	public String getFlagFit() {
		return flagFit;
	}

	public void setFlagFit(String flagFit) {
		this.flagFit = flagFit;
	}

	public String getCodeOrdtype() {
		return codeOrdtype;
	}

	public void setCodeOrdtype(String codeOrdtype) {
		this.codeOrdtype = codeOrdtype;
	}

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public String getEuAdditem() {
		return euAdditem;
	}

	public void setEuAdditem(String euAdditem) {
		this.euAdditem = euAdditem;
	}

	public String getInfantNo() {

		return infantNo;
	}

	public void setInfantNo(String infantNo) {

		this.infantNo = infantNo;
	}

	public String getPkDeptNsApp() {

		return pkDeptNsApp;
	}

	public void setPkDeptNsApp(String pkDeptNsApp) {

		this.pkDeptNsApp = pkDeptNsApp;
	}

	public Double getPrice() {

		return price;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getPkOrdexdt() {

		return pkOrdexdt;
	}

	public void setPkOrdexdt(String pkOrdexdt) {

		this.pkOrdexdt = pkOrdexdt;
	}

	public String getFlagPd() {

		return flagPd;
	}

	public void setFlagPd(String flagPd) {

		this.flagPd = flagPd;
	}

	public String getFlagPv() {

		return flagPv;
	}

	public void setFlagPv(String flagPv) {

		this.flagPv = flagPv;
	}

	public String getPkPres() {

		return pkPres;
	}

	public void setPkPres(String pkPres) {

		this.pkPres = pkPres;
	}

	public String getPkOrg() {

		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {

		this.pkOrg = pkOrg;
	}

	public String getEuPvType() {

		return euPvType;
	}

	public void setEuPvType(String euPvType) {

		this.euPvType = euPvType;
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

	public String getPkOrd() {

		return pkOrd;
	}

	public void setPkOrd(String pkOrd) {

		this.pkOrd = pkOrd;
	}

	public Double getQuanCg() {

		return quanCg;
	}

	public void setQuanCg(Double quanCg) {

		this.quanCg = quanCg;
	}

	public String getPkCnord() {
		return pkCnord;
	}

	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}

	public String getPkOrgEx() {

		return pkOrgEx;
	}

	public void setPkOrgEx(String pkOrgEx) {

		this.pkOrgEx = pkOrgEx;
	}

	public String getPkOrgApp() {

		return pkOrgApp;
	}

	public void setPkOrgApp(String pkOrgApp) {

		this.pkOrgApp = pkOrgApp;
	}

	public String getPkDeptEx() {

		return pkDeptEx;
	}

	public void setPkDeptEx(String pkDeptEx) {

		this.pkDeptEx = pkDeptEx;
	}

	public String getPkDeptApp() {

		return pkDeptApp;
	}

	public void setPkDeptApp(String pkDeptApp) {

		this.pkDeptApp = pkDeptApp;
	}

	public String getPkEmpApp() {

		return pkEmpApp;
	}

	public void setPkEmpApp(String pkEmpApp) {

		this.pkEmpApp = pkEmpApp;
	}

	public String getNameEmpApp() {

		return nameEmpApp;
	}

	public void setNameEmpApp(String nameEmpApp) {

		this.nameEmpApp = nameEmpApp;
	}

	public String getPkItem() {

		return pkItem;
	}

	public void setPkItem(String pkItem) {

		this.pkItem = pkItem;
	}

	public Date getDateHap() {

		return dateHap;
	}

	public void setDateHap(Date dateHap) {

		this.dateHap = dateHap;
	}

	public static long getSerialversionuid() {

		return serialVersionUID;
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

	public String getBatchNo() {

		return batchNo;
	}

	public void setBatchNo(String batchNo) {

		this.batchNo = batchNo;
	}

	public Date getDateExpire() {

		return dateExpire;
	}

	public void setDateExpire(Date dateExpire) {

		this.dateExpire = dateExpire;
	}

	public String getPkUnitPd() {

		return pkUnitPd;
	}

	public void setPkUnitPd(String pkUnitPd) {

		this.pkUnitPd = pkUnitPd;
	}

	public Integer getPackSize() {

		return packSize;
	}

	public void setPackSize(Integer packSize) {

		this.packSize = packSize;
	}

	public Double getPriceCost() {

		return priceCost;
	}

	public void setPriceCost(Double priceCost) {

		this.priceCost = priceCost;
	}

	public String getNamePd() {

		return namePd;
	}

	public void setNamePd(String namePd) {

		this.namePd = namePd;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getFlagHasPdPrice() {
		return flagHasPdPrice;
	}

	public void setFlagHasPdPrice(String flagHasPdPrice) {
		this.flagHasPdPrice = flagHasPdPrice;
	}
}
