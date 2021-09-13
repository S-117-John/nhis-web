package com.zebone.nhis.bl.pub.vo;

import java.util.Date;

public class BlOpDrugStorePriceInfo {

	/** 传入参数 **/
	private String pkDept;// 药房科室(对临床来说就是患者就诊科室，通过业务线与之对应的执行科室)

	private String pkPd;// 物品主键

	private Double quanAp;// 本次记费的药品数量和记费单位对应

	private String pkOrg;

	private String namePd;

	/** 返回参数 **/
	private Integer packSize;// 当前仓库的包装量

	private Double priceCost;// 成本价

	private Double price;// 当前仓库对应零售价格

	private Date dateExpire;// 失效日期

	private String batchNo;// 批号

	private Integer packSizePd;// 字典表（零售）包装量

	private String pkUnit;// 仓库单位

	public String getPkDept() {

		return pkDept;
	}

	public void setPkDept(String pkDept) {

		this.pkDept = pkDept;
	}

	public String getPkPd() {

		return pkPd;
	}

	public void setPkPd(String pkPd) {

		this.pkPd = pkPd;
	}

	public Double getPriceCost() {

		return priceCost;
	}

	public void setPriceCost(Double priceCost) {

		this.priceCost = priceCost;
	}

	public Integer getPackSize() {

		return packSize;
	}

	public void setPackSize(Integer packSize) {

		this.packSize = packSize;
	}

	public Double getPrice() {

		return price;
	}

	public void setPrice(Double price) {

		this.price = price;
	}

	public Date getDateExpire() {

		return dateExpire;
	}

	public void setDateExpire(Date dateExpire) {

		this.dateExpire = dateExpire;
	}

	public String getBatchNo() {

		return batchNo;
	}

	public void setBatchNo(String batchNo) {

		this.batchNo = batchNo;
	}

	public Integer getPackSizePd() {

		return packSizePd;
	}

	public void setPackSizePd(Integer packSizePd) {

		this.packSizePd = packSizePd;
	}

	public Double getQuanAp() {

		return quanAp;
	}

	public void setQuanAp(Double quanAp) {

		this.quanAp = quanAp;
	}

	public String getPkOrg() {

		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {

		this.pkOrg = pkOrg;
	}

	public String getPkUnit() {

		return pkUnit;
	}

	public void setPkUnit(String pkUnit) {

		this.pkUnit = pkUnit;
	}

	public String getNamePd() {

		return namePd;
	}

	public void setNamePd(String namePd) {

		this.namePd = namePd;
	}

}
