package com.zebone.nhis.compay.pub.vo;
/**
 * 根据排班资源信息和附加费信息获取详细计费项目明细
 * @author Administrator
 *
 */
public class InsItemsBySchsrvData {
	private String pkItem; //":"收费项目主键",
	private String itemName; //收费项目名称",
	private double quan;//"数量",
	private double price;//"单价",
	private double amount; //:"金额",
	private String spec;//"规格"
	private String unitName; //"单位名称",
	private String code; //编码
	public String getPkItem() {
		return pkItem;
	}
	public void setPkItem(String pkItem) {
		this.pkItem = pkItem;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public double getQuan() {
		return quan;
	}
	public void setQuan(double quan) {
		this.quan = quan;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
