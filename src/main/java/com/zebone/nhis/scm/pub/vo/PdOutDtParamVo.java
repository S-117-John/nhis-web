package com.zebone.nhis.scm.pub.vo;

import java.util.Date;

/**
 * 物品出库明细参数
 * @author yangxue
 *
 */
public class PdOutDtParamVo implements Cloneable{
	
	
	public Object clone() {  
		PdOutDtParamVo o = null;  
        try {  
            o = (PdOutDtParamVo) super.clone();  
        } catch (CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return o;  
    }  
	
	private String pkPd;
	private String batchNo;
	private Double price;
	private Double priceCost;
	private Double priceStore;//当前仓库单位下的零售价格
	private Integer packSize;
	private String pkUnitPack;//发退单位
	private Double quanOutPack;//实发退数量-包装单位
	private Double quanOutMin;//实发退数量-基本单位
	private Double quanMin;//入库数量--基本单位
	//private Double quanApp;//申请数量
	private Double quanOutstore;//入库明细的已出库数量
	private String pkPdstdt;//出入库明细主键 
	private String pkStore;//发退仓库
	private String pkPv;
	private String pkCnord;
	private String pkPdapdt;
	private Date dateExpire;
	private Date dateChk;//入库日期
	private Date dateFac;//生产日期



	private Integer packSizePd;//零售单位的包装量
	private String spec;//规格
	private String pkItemcate;//所属费用类别
	private String name;//药品名称
	private Integer packSizeMax;//箱包装
	private String posiNo;//货架号
	private String flagSingle;
	private String unitName;
	private String flagUse;//在用属性

	public String getFlagUse() {
		return flagUse;
	}

	public void setFlagUse(String flagUse) {
		this.flagUse = flagUse;
	}
	public String getPosiNo() {
		return posiNo;
	}
	public void setPosiNo(String posiNo) {
		this.posiNo = posiNo;
	}
	public Integer getPackSizeMax() {
		return packSizeMax;
	}
	public void setPackSizeMax(Integer packSizeMax) {
		this.packSizeMax = packSizeMax;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getPkItemcate() {
		return pkItemcate;
	}
	public void setPkItemcate(String pkItemcate) {
		this.pkItemcate = pkItemcate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getPriceStore() {
		return priceStore;
	}
	public void setPriceStore(Double priceStore) {
		this.priceStore = priceStore;
	}
	public Integer getPackSizePd() {
		return packSizePd;
	}
	public void setPackSizePd(Integer packSizePd) {
		this.packSizePd = packSizePd;
	}
	public Double getQuanOutPack() {
		return quanOutPack;
	}
	public void setQuanOutPack(Double quanOutPack) {
		this.quanOutPack = quanOutPack;
	}
	public Double getQuanOutMin() {
		return quanOutMin;
	}
	public void setQuanOutMin(Double quanOutMin) {
		this.quanOutMin = quanOutMin;
	}
	public Double getQuanOutstore() {
		return quanOutstore;
	}
	public void setQuanOutstore(Double quanOutstore) {
		this.quanOutstore = quanOutstore;
	}
	public String getPkPdapdt() {
		return pkPdapdt;
	}
	public void setPkPdapdt(String pkPdapdt) {
		this.pkPdapdt = pkPdapdt;
	}
	public Date getDateExpire() {
		return dateExpire;
	}
	public void setDateExpire(Date dateExpire) {
		this.dateExpire = dateExpire;
	}
	
	public Date getDateChk() {
		return dateChk;
	}
	public void setDateChk(Date dateChk) {
		this.dateChk = dateChk;
	}
	public Date getDateFac() {
		return dateFac;
	}
	public void setDateFac(Date dateFac) {
		this.dateFac = dateFac;
	}
	public String getPkPd() {
		return pkPd;
	}
	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
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
	public String getPkUnitPack() {
		return pkUnitPack;
	}
	public void setPkUnitPack(String pkUnitPack) {
		this.pkUnitPack = pkUnitPack;
	}
	
	public Double getQuanMin() {
		return quanMin;
	}
	public void setQuanMin(Double quanMin) {
		this.quanMin = quanMin;
	}
	
	public String getPkPdstdt() {
		return pkPdstdt;
	}
	public void setPkPdstdt(String pkPdstdt) {
		this.pkPdstdt = pkPdstdt;
	}
	public String getPkStore() {
		return pkStore;
	}
	public void setPkStore(String pkStore) {
		this.pkStore = pkStore;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getPkCnord() {
		return pkCnord;
	}
	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}

	public String getFlagSingle() {
		return flagSingle;
	}

	public void setFlagSingle(String flagSingle) {
		this.flagSingle = flagSingle;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
}
