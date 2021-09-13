package com.zebone.nhis.pro.zsba.mz.opcg.vo;

import java.util.Date;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;

public class OpCgCnOrder extends CnOrder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 100000000000000000L;

	private Date dateExpire;

	private Double priceCost;

	private String batchNo;

	// 部位
	private String descBody;

	// 部位编码
	private String dtBody;

	// 标本
	private String dtSamptype;

	// 体积或重量
	private Double vol;

	public Date getDateExpire() {

		return dateExpire;
	}

	public void setDateExpire(Date dateExpire) {

		this.dateExpire = dateExpire;
	}

	public Double getPriceCost() {

		return priceCost;
	}

	public void setPriceCost(Double priceCost) {

		this.priceCost = priceCost;
	}

	public String getDescBody() {

		return descBody;
	}

	public void setDescBody(String descBody) {

		this.descBody = descBody;
	}

	public String getDtSamptype() {

		return dtSamptype;
	}

	public void setDtSamptype(String dtSamptype) {

		this.dtSamptype = dtSamptype;
	}

	public String getBatchNo() {

		return batchNo;
	}

	public void setBatchNo(String batchNo) {

		this.batchNo = batchNo;
	}

	public Double getVol() {

		return vol;
	}

	public void setVol(Double vol) {

		this.vol = vol;
	}

	public String getDtBody() {

		return dtBody;
	}

	public void setDtBody(String dtBody) {

		this.dtBody = dtBody;
	}

}
