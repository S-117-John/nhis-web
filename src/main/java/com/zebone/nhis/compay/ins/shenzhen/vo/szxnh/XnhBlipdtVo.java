package com.zebone.nhis.compay.ins.shenzhen.vo.szxnh;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;

public class XnhBlipdtVo {
			
	@Field(value = "PK_CGIP")
	private String pkCgip;
	/** CODE_CG - 记费编码 */
	@Field(value = "CODE_CG")
	private String codeCg;
	/** NAME_CG - 记费名称 */
	@Field(value = "NAME_CG")
	private String nameCg;
	/** PK_UNIT - 为物品时记录物品当前包装单位 */
	@Field(value = "PK_UNIT")
	private String pkUnit;
	/** SPEC - 规格 */
	@Field(value = "SPEC")
	private String spec;
	/** PRICE - 当前单价 对物品来说：取物品当前包装单位下单价 */
	@Field(value = "PRICE")
	private Double price;
	/** QUAN - 数量 对物品来说：取物品当前包装单位下单价 */
	@Field(value = "QUAN")
	private Double quan;
	/** AMOUNT - 金额 */
	@Field(value = "AMOUNT")
	private Double amount;
	/** NAME_EMP_APP - 开立医生姓名 */
	@Field(value = "NAME_EMP_APP")
	private String nameEmpApp;
	/** DATE_HAP - 费用发生日期 */
	@Field(value = "DATE_HAP")
	private Date dateHap;
	/** 创建时间 */
	@Field(value = "CREATE_TIME")
	public Date createTime;
	@Field(value = "MODITY_TIME")
	public Date modityTime;
	// / <summary>
	// / 费用类别代码
	// / </summary>
	@Field(value = "CODE_INVCATEITEM")
	private String codeinvcateitem;
	// / <summary>
	// / 费用类别名称
	// / </summary>
	@Field(value = "NAME_INVCATEITEM")
	private String nameinvcateitem;

	@Field(value = "ITEM_CODE")
	private String itemCode;
	// / <summary>
	// / 可报销金额
	// / </summary>
	@Field(value = "KBXAMOUNT")
	private BigDecimal kbxamount;
	// / <summary>
	// / 农合项目代码N707-16
	// / </summary>
	@Field(value = "CODE_CENTER")
	private String codecenter;
	// / <summary>
	// / 农合项目名称N707-17
	// / </summary>
	@Field(value = "NAME_ITEM")
	private String nameitem;
	// / <summary>
	// / 国产/进口标识代码
	// / </summary>
	@Field(value = "DT_ABRD")
	private String dtabrd;
	// / <summary>
	// / 国产/进口标识名称
	// / </summary>
	@Field(value = "NAME_ABRD")
	private String nameabrd;
	// / <summary>
	// / 就医地目录外
	// / </summary>
	@Field(value = "EU_STAPLE")
	private String eustaple;
	// / <summary>
	// / 加收标志
	// / </summary>
	@Field(value = "FLAG_SPEC")
	private String flagSpec;
	@Field(value = "FLAG_PDIND")
	private String flagpdind;

	@Field(value = "AMOUNT_ADD")
	private Double amountAdd;
	@Field(value = "RATIO_ADD")
	private Double ratioAdd;
	public String getFlagpdind() {
		return flagpdind;
	}

	public void setFlagpdind(String flagpdind) {
		this.flagpdind = flagpdind;
	}

	public Double getAmountAdd() {
		return amountAdd;
	}

	public void setAmountAdd(Double amountAdd) {
		this.amountAdd = amountAdd;
	}

	public Double getRatioAdd() {
		return ratioAdd;
	}

	public void setRatioAdd(Double ratioAdd) {
		this.ratioAdd = ratioAdd;
	}

	public String getCodeinvcateitem() {
		return codeinvcateitem;
	}

	public void setCodeinvcateitem(String codeinvcateitem) {
		this.codeinvcateitem = codeinvcateitem;
	}

	public String getNameinvcateitem() {
		return nameinvcateitem;
	}

	public void setNameinvcateitem(String nameinvcateitem) {
		this.nameinvcateitem = nameinvcateitem;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public BigDecimal getKbxamount() {
		return kbxamount;
	}

	public void setKbxamount(BigDecimal kbxamount) {
		this.kbxamount = kbxamount;
	}

	public String getCodecenter() {
		return codecenter;
	}

	public void setCodecenter(String codecenter) {
		this.codecenter = codecenter;
	}

	public String getNameitem() {
		return nameitem;
	}

	public void setNameitem(String nameitem) {
		this.nameitem = nameitem;
	}

	public String getDtabrd() {
		return dtabrd;
	}

	public void setDtabrd(String dtabrd) {
		this.dtabrd = dtabrd;
	}

	public String getNameabrd() {
		return nameabrd;
	}

	public void setNameabrd(String nameabrd) {
		this.nameabrd = nameabrd;
	}

	public String getEustaple() {
		return eustaple;
	}

	public void setEustaple(String eustaple) {
		this.eustaple = eustaple;
	}

	public String getPkCgip() {
		return pkCgip;
	}

	public void setPkCgip(String pkCgip) {
		this.pkCgip = pkCgip;
	}

	public String getCodeCg() {
		return codeCg;
	}

	public void setCodeCg(String codeCg) {
		this.codeCg = codeCg;
	}

	public String getNameCg() {
		return nameCg;
	}

	public void setNameCg(String nameCg) {
		this.nameCg = nameCg;
	}

	public String getPkUnit() {
		return pkUnit;
	}

	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getQuan() {
		return quan;
	}

	public void setQuan(Double quan) {
		this.quan = quan;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getNameEmpApp() {
		return nameEmpApp;
	}

	public void setNameEmpApp(String nameEmpApp) {
		this.nameEmpApp = nameEmpApp;
	}

	public Date getDateHap() {
		return dateHap;
	}

	public void setDateHap(Date dateHap) {
		this.dateHap = dateHap;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}

	public String getFlagSpec() {
		return flagSpec;
	}

	public void setFlagSpec(String flagSpec) {
		this.flagSpec = flagSpec;
	}

}
