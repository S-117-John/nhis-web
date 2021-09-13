package com.zebone.nhis.ex.nis.ns.vo;
/**
 * 草药物品信息
 * @author yangxue
 *
 */
public class PdHerbInfoVo {
	private String pkPd;//对于草药医嘱，用于存储草药物品信息
	private String pkUnit;//草药医嘱对应物品的单位
	private Double price;//草药医嘱的物品价格
	private Integer packSize;//草药医嘱的包装量
	private String nameUnit;//草药医嘱物品对应的仓库单位
	private Double packSizeP;//草药医嘱的零售包装量
	private String pdname;
	private String spec;
	
	
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
	public Double getPackSizeP() {
		return packSizeP;
	}
	public void setPackSizeP(Double packSizeP) {
		this.packSizeP = packSizeP;
	}
	public String getPkPd() {
		return pkPd;
	}
	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
	}
	public String getPkUnit() {
		return pkUnit;
	}
	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getPackSize() {
		return packSize;
	}
	public void setPackSize(Integer packSize) {
		this.packSize = packSize;
	}
	public String getNameUnit() {
		return nameUnit;
	}
	public void setNameUnit(String nameUnit) {
		this.nameUnit = nameUnit;
	}

}
