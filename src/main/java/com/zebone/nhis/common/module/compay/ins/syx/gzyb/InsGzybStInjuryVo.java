package com.zebone.nhis.common.module.compay.ins.syx.gzyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: INS_GZYB_ST_INJURY - tabledesc
 * 
 * @since 2019-09-25 02:52:34
 */
@Table(value = "INS_GZYB_ST_INJURY")
public class InsGzybStInjuryVo extends BaseModule {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** PK_INSSTINJURY - 主键 */
	@PK
	@Field(value = "PK_INSSTINJURY", id = KeyId.UUID)
	private String pkInsstinjury;

	/** PK_INSST - 关联医保结算 */
	@Field(value = "PK_INSST")
	private String pkInsst;

	/** PK_ORG-所属机构 */
	@Field(value = "PK_ORG")
	private String pkOrg;
	
	/** EU_TYPE - 业务类型 */
	@Field(value = "EU_TYPE")
	private String euType;

	/** AMT_TOTAL - 总费用 */
	@Field(value = "AMT_TOTAL")
	private Double amtTotal;

	/** AMT_SELF - 个人支付金额 */
	@Field(value = "AMT_SELF")
	private Double amtSelf;

	/** AMT_INJURY - 工伤记账金额 */
	@Field(value = "AMT_INJURY")
	private Double amtInjury;

	/** AMT_CWFCBGR - 床位费超标个人金额 */
	@Field(value = "AMT_CWFCBGR")
	private Double amtCwfcbgr;

	/** AMT_CWFCBJZ - 床位费超标记账金额 */
	@Field(value = "AMT_CWFCBJZ")
	private Double amtCwfcbjz;

	/** AMT_ZFGR - 自费个人费用 */
	@Field(value = "AMT_ZFGR")
	private Double amtZfgr;

	/** AMT_ZFJZ - 自费记账费用 */
	@Field(value = "AMT_ZFJZ")
	private Double amtZfjz;

	/** AMT_GSJJGFDGR - 工伤基金共付段个人费用 */
	@Field(value = "AMT_GSJJGFDGR")
	private Double amtGsjjgfdgr;

	/** AMT_GSJJGFDJZ - 工伤基金共付段记账费用 */
	@Field(value = "AMT_GSJJGFDJZ")
	private Double amtGsjjgfdjz;

	/** AMT_CEYSGR - 超限额以上个人费用 */
	@Field(value = "AMT_CEYSGR")
	private Double amtCeysgr;

	/** AMT_CEYSJZ - 超限额以上记账费用 */
	@Field(value = "AMT_CEYSJZ")
	private Double amtCeysjz;

	/** CREATOR - 创建者 */
	@Field(value = "CREATOR")
	private String creator;

	/** CREATE_TIME - 创建时间 */
	@Field(value = "CREATE_TIME")
	private Date createTime;

	/** DEL_FLAG - 删除标识 */
	@Field(value = "DEL_FLAG")
	private String delFlag;

	/** TS - 时间戳 */
	@Field(value = "TS")
	private Date ts;

	public String getPkInsstinjury() {
		return this.pkInsstinjury;
	}

	public void setPkInsstinjury(String pkInsstinjury) {
		this.pkInsstinjury = pkInsstinjury;
	}

	public String getPkInsst() {
		return this.pkInsst;
	}

	public void setPkInsst(String pkInsst) {
		this.pkInsst = pkInsst;
	}

	public String getEuType() {
		return this.euType;
	}

	public void setEuType(String euType) {
		this.euType = euType;
	}

	public Double getAmtTotal() {
		return this.amtTotal;
	}

	public void setAmtTotal(Double amtTotal) {
		this.amtTotal = amtTotal;
	}

	public Double getAmtSelf() {
		return this.amtSelf;
	}

	public void setAmtSelf(Double amtSelf) {
		this.amtSelf = amtSelf;
	}

	public Double getAmtInjury() {
		return this.amtInjury;
	}

	public void setAmtInjury(Double amtInjury) {
		this.amtInjury = amtInjury;
	}

	public Double getAmtCwfcbgr() {
		return this.amtCwfcbgr;
	}

	public void setAmtCwfcbgr(Double amtCwfcbgr) {
		this.amtCwfcbgr = amtCwfcbgr;
	}

	public Double getAmtCwfcbjz() {
		return this.amtCwfcbjz;
	}

	public void setAmtCwfcbjz(Double amtCwfcbjz) {
		this.amtCwfcbjz = amtCwfcbjz;
	}

	public Double getAmtZfgr() {
		return this.amtZfgr;
	}

	public void setAmtZfgr(Double amtZfgr) {
		this.amtZfgr = amtZfgr;
	}

	public Double getAmtZfjz() {
		return this.amtZfjz;
	}

	public void setAmtZfjz(Double amtZfjz) {
		this.amtZfjz = amtZfjz;
	}

	public Double getAmtGsjjgfdgr() {
		return this.amtGsjjgfdgr;
	}

	public void setAmtGsjjgfdgr(Double amtGsjjgfdgr) {
		this.amtGsjjgfdgr = amtGsjjgfdgr;
	}

	public Double getAmtGsjjgfdjz() {
		return this.amtGsjjgfdjz;
	}

	public void setAmtGsjjgfdjz(Double amtGsjjgfdjz) {
		this.amtGsjjgfdjz = amtGsjjgfdjz;
	}

	public Double getAmtCeysgr() {
		return this.amtCeysgr;
	}

	public void setAmtCeysgr(Double amtCeysgr) {
		this.amtCeysgr = amtCeysgr;
	}

	public Double getAmtCeysjz() {
		return this.amtCeysjz;
	}

	public void setAmtCeysjz(Double amtCeysjz) {
		this.amtCeysjz = amtCeysjz;
	}
}
