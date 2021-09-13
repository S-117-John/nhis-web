package com.zebone.nhis.compay.pub.syx.vo;

import com.zebone.platform.modules.dao.build.au.Field;

public class CostForecastItemVo {

	/* 分类代码 */
	@Field(value = "RATCODE")
	private String ratCode;
	/* 分类名称 */
	@Field(value = "RATNAME")
	private String ratName;
	/* 金额 */
	@Field(value = "AMOUNT")
	private Double amount;
	/* 数量 */
	@Field(value = "QUAN")
	private Double quan;
	/* 甲乙标志0 - 甲类，1 - 乙类，2- 全自费 */
	@Field(value = "EUSTAPLE")
	private String eustaple;
	/* 自负比例 */
	@Field(value = "RATIO")
	private String ratio;
	/* 项目名称 */
	@Field(value = "NAMECG")
	private String nameCg;

	public String getRatCode() {
		return ratCode;
	}

	public void setRatCode(String ratCode) {
		this.ratCode = ratCode;
	}

	public String getRatName() {
		return ratName;
	}

	public void setRatName(String ratName) {
		this.ratName = ratName;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getEustaple() {
		return eustaple;
	}

	public void setEustaple(String eustaple) {
		this.eustaple = eustaple;
	}

	public String getRatio() {
		return ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

	public String getNameCg() {
		return nameCg;
	}

	public void setNameCg(String nameCg) {
		this.nameCg = nameCg;
	}

	public Double getQuan() {
		return quan;
	}

	public void setQuan(Double quan) {
		this.quan = quan;
	}

}
