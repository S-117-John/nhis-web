package com.zebone.nhis.scm.pub.vo;

import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.scm.st.PdSingle;
import com.zebone.nhis.common.module.scm.st.PdStDetail;

public class PdStDtVo extends PdStDetail implements Cloneable{
	
	private String pdcode;
	private String pdname;
	private String spec;
	private String factory;
	private String unit;
	private Double quanStk;//当前库存
	private String pkPdpudt;//采购入库明细主键
	private Double quanPuMin;//采购数量
	private Double quanInMin;//已入库数量
	private String spcode;
	private String isEnough;//出库数量是否足够申请数量，不够N，够Y
	//private Double quanOutstore;//已出库数量 --父类已存在
	private Double quanActMin;//出库数量，主要针对可用量大于需要出库数量时
	private String pkDtin;//当该对象存储的为出库记录时，对应的入库明细主键
	private String pkPdPlandt;//对应调拨明细主键，当出库类型为调拨出库时，关联调拨明细单主键
	private String pkSupplyer;//付款时使用
	private String unitPd;//零售单位
	private Integer packSizePd;
	private Integer packSizeMax;//箱包装
	private String posiNo;//货位号
	private String validCnt; //有效期
	private String euValidUnit; //有效期单位
	private String flagSingle; //单品标志
	private String flagUse; //再用属性
	private Date datebegin; //启用日期
	private String pkStore; //物品所在仓库
	private String pkDept; //物品所在部门
	private Date dateValidReg;//注册效期
	
	private List<PdSingle> sinList;//单品记录明细
	private List<PdStDtBatchVo> batchList;
	private String apprNo;//批准文号

	//杨雪添加，由待出库至出库界面保存库存中价格使用
	private Double priceOrg;//库存中的原始零售价
	private Double priceCostOrg;//库存中的原始成本价

	private Double quanPackQ;//请领数

	/** bd_pd中的条码，查询使用*/
	private String barcode;

	private Integer packSizestore;//仓库物品包装量

	private String unitNameStore;//仓库包装单位

	/** 没有进行转换之前的单单价*/
	private Double priceCurrent;
	/** 没有进行转换之前的成本单价*/
	private Double priceCostCurrent;
	
	private String supplyer;
	
	private String ypbm;//药品编码
	private String scph;//生产批号
	private String psdmxbh;//配送单明细编号
	
	/**
	 * 药品来源
	 */
	private String euSource;

	private Double priceCostOld;//采购退回修改成本单价用

	public Double getPriceCostOld() {
		return priceCostOld;
	}

	public void setPriceCostOld(Double priceCostOld) {
		this.priceCostOld = priceCostOld;
	}

	/** 单品*/
	public static final String FLAG_SINGLE_T = "1";

	public Date getDateValidReg() {
		return dateValidReg;
	}

	public void setDateValidReg(Date dateValidReg) {
		this.dateValidReg = dateValidReg;
	}

	public Double getQuanPackQ() {
		return quanPackQ;
	}
	public void setQuanPackQ(Double quanPackQ) {
		this.quanPackQ = quanPackQ;
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

	public Integer getPackSizePd() {
		return packSizePd;
	}
	public void setPackSizePd(Integer packSizePd) {
		this.packSizePd = packSizePd;
	}
	public String getUnitPd() {
		return unitPd;
	}
	public void setUnitPd(String unitPd) {
		this.unitPd = unitPd;
	}
	public String getPkSupplyer() {
		return pkSupplyer;
	}
	public void setPkSupplyer(String pkSupplyer) {
		this.pkSupplyer = pkSupplyer;
	}
	public String getPkPdPlandt() {
		return pkPdPlandt;
	}
	public void setPkPdPlandt(String pkPdPlandt) {
		this.pkPdPlandt = pkPdPlandt;
	}
	public String getPkDtin() {
		return pkDtin;
	}
	public void setPkDtin(String pkDtin) {
		this.pkDtin = pkDtin;
	}
	public Double getQuanActMin() {
		return quanActMin;
	}
	public void setQuanActMin(Double quanActMin) {
		this.quanActMin = quanActMin;
	}
//	public Double getQuanOutstore() {
//		return quanOutstore;
//	}
//	public void setQuanOutstore(Double quanOutstore) {
//		this.quanOutstore = quanOutstore;
//	}
	public String getIsEnough() {
		return isEnough;
	}
	public void setIsEnough(String isEnough) {
		this.isEnough = isEnough;
	}
	public String getSpcode() {
		return spcode;
	}
	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}
	public String getPkPdpudt() {
		return pkPdpudt;
	}
	public void setPkPdpudt(String pkPdpudt) {
		this.pkPdpudt = pkPdpudt;
	}
	public String getPdcode() {
		return pdcode;
	}
	public void setPdcode(String pdcode) {
		this.pdcode = pdcode;
	}
	public String getPdname() {
		return pdname;
	}
	public void setPdname(String pdname) {
		this.pdname = pdname;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getQuanStk() {
		return quanStk;
	}
	public void setQuanStk(Double quanStk) {
		this.quanStk = quanStk;
	}
	public Double getQuanPuMin() {
		return quanPuMin;
	}
	public void setQuanPuMin(Double quanPuMin) {
		this.quanPuMin = quanPuMin;
	}
	public Double getQuanInMin() {
		return quanInMin;
	}
	public void setQuanInMin(Double quanInMin) {
		this.quanInMin = quanInMin;
	}
	public Double getPriceOrg() {
		return priceOrg;
	}
	public void setPriceOrg(Double priceOrg) {
		this.priceOrg = priceOrg;
	}
	public Double getPriceCostOrg() {
		return priceCostOrg;
	}
	public void setPriceCostOrg(Double priceCostOrg) {
		this.priceCostOrg = priceCostOrg;
	}
	public String getValidCnt() {
		return validCnt;
	}
	public void setValidCnt(String validCnt) {
		this.validCnt = validCnt;
	}
	public String getEuValidUnit() {
		return euValidUnit;
	}
	public void setEuValidUnit(String euValidUnit) {
		this.euValidUnit = euValidUnit;
	}
	public String getFlagSingle() {
		return flagSingle;
	}
	public void setFlagSingle(String flagSingle) {
		this.flagSingle = flagSingle;
	}
	public String getFlagUse() {
		return flagUse;
	}
	public void setFlagUse(String flagUse) {
		this.flagUse = flagUse;
	}
	public List<PdSingle> getSinList() {
		return sinList;
	}
	public void setSinList(List<PdSingle> sinList) {
		this.sinList = sinList;
	}
	public Date getDatebegin() {
		return datebegin;
	}
	public void setDatebegin(Date datebegin) {
		this.datebegin = datebegin;
	}
	public String getPkStore() {
		return pkStore;
	}
	public void setPkStore(String pkStore) {
		this.pkStore = pkStore;
	}
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public PdStDtVo clone() {
		PdStDtVo o = null;
        try {
            o = (PdStDtVo) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getApprNo() {
		return apprNo;
	}
	public void setApprNo(String apprNo) {
		this.apprNo = apprNo;
	}
	public Integer getPackSizestore() {
		return packSizestore;
	}
	public void setPackSizestore(Integer packSizestore) {
		this.packSizestore = packSizestore;
	}
	public String getUnitNameStore() {
		return unitNameStore;
	}
	public void setUnitNameStore(String unitNameStore) {
		this.unitNameStore = unitNameStore;
	}

	public List<PdStDtBatchVo> getBatchList() {
		return batchList;
	}

	public void setBatchList(List<PdStDtBatchVo> batchList) {
		this.batchList = batchList;
	}

	public Double getPriceCurrent() {
		return priceCurrent;
	}

	public void setPriceCurrent(Double priceCurrent) {
		this.priceCurrent = priceCurrent;
	}

	public Double getPriceCostCurrent() {
		return priceCostCurrent;
	}

	public void setPriceCostCurrent(Double priceCostCurrent) {
		this.priceCostCurrent = priceCostCurrent;
	}
	public String getSupplyer() {
		return supplyer;
	}
	public void setSupplyer(String supplyer) {
		this.supplyer = supplyer;
	}

	public String getEuSource() {
		return euSource;
	}

	public void setEuSource(String euSource) {
		this.euSource = euSource;
	}

	public String getYpbm() {
		return ypbm;
	}

	public void setYpbm(String ypbm) {
		this.ypbm = ypbm;
	}

	public String getScph() {
		return scph;
	}

	public void setScph(String scph) {
		this.scph = scph;
	}

	public String getPsdmxbh() {
		return psdmxbh;
	}

	public void setPsdmxbh(String psdmxbh) {
		this.psdmxbh = psdmxbh;
	}

}
