package com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BL_CC_DS  - 收费结算-操作员结账 扩展字段表
 *
 * @zrj 2020-06-21 09:24:57
 */
@Table(value="BL_CC_DS")
public class ZsbaBlCcDs extends BaseModule  {

	private static final long serialVersionUID = 1L;

	/** PK_CC - 操作员结账 扩展字段表主键 */
	@PK
	@Field(value="PK_CC_DS",id=KeyId.UUID)
    private String pkCcDt;

	/** PK_CC - 操作员结账主键 */
	@Field(value="PK_CC")
    private String pkCc;
	
	/** INV_REFUND_CODE - 退发票号 */
	@Field(value="INV_REFUND_CODE")
    private String invRefundCode;
	
	/** INV_REFUND_NUM - 退发票数量 */
	@Field(value="INV_REFUND_NUM")
    private Integer invRefundNum;
	
	/** INV_VOID_CODE - 作废发票号 */
	@Field(value="INV_VOID_CODE")
    private String invVoidCode;
	
	/** INV_VOID_CODE - 作废发票数量 */
	@Field(value="INV_VOID_NUM")
    private Integer invVoidNum;
	
	/** DEPO_REFUND_CODE - 退预交金票据号 */
	@Field(value="DEPO_REFUND_CODE")
    private String depoRefundCode;
	
	/** DEPO_REFUND_CODE - 退预交金票据数量 */
	@Field(value="DEPO_REFUND_NUM")
    private Integer depoRefundNum;
	
	/** DEPO_VOID_CODE - 作废预交金票据号 */
	@Field(value="DEPO_VOID_CODE")
    private String depoVoidCode;
	
	/** DEPO_VOID_NUL - 作废预交金票据数量 */
	@Field(value="DEPO_VOID_NUM")
    private Integer depoVoidNum;
	
	/** AMOUNT_IP 自付金额 */
	@Field(value="AMT_IP")
    private Double amtIp;

	/** AMOUNT_INS 医保金额 */
	@Field(value="AMT_INS")
    private Double amtIns;

	/** AMOUNT_OTHER 其他单位负担金额 */
	@Field(value="AMT_OTHER")
    private Double amtOther;

	/** AMOUNT_SS 实收金额 */
	@Field(value="AMT_SS")
    private Double amtSs;
	
	/** AMOUNT_SS_XJ 实收现金金额 */
	@Field(value="AMT_SS_XJ")
    private Double amtSsXj;

	/** AMT_GZ 个账金额，只统计中山的 */
	@Field(value="AMT_GZ")
    private Double amtGz;
	
	/** AMT_TC 统筹 */
	@Field(value="AMT_TC")
    private Double amtTc;
	
	public String getPkCcDt() {
		return pkCcDt;
	}

	public void setPkCcDt(String pkCcDt) {
		this.pkCcDt = pkCcDt;
	}

	public String getPkCc() {
		return pkCc;
	}

	public void setPkCc(String pkCc) {
		this.pkCc = pkCc;
	}

	public String getInvRefundCode() {
		return invRefundCode;
	}

	public void setInvRefundCode(String invRefundCode) {
		this.invRefundCode = invRefundCode;
	}

	public Integer getInvRefundNum() {
		return invRefundNum;
	}

	public void setInvRefundNum(Integer invRefundNum) {
		this.invRefundNum = invRefundNum;
	}

	public String getInvVoidCode() {
		return invVoidCode;
	}

	public void setInvVoidCode(String invVoidCode) {
		this.invVoidCode = invVoidCode;
	}

	public Integer getInvVoidNum() {
		return invVoidNum;
	}

	public void setInvVoidNum(Integer invVoidNum) {
		this.invVoidNum = invVoidNum;
	}

	public String getDepoRefundCode() {
		return depoRefundCode;
	}

	public void setDepoRefundCode(String depoRefundCode) {
		this.depoRefundCode = depoRefundCode;
	}

	public Integer getDepoRefundNum() {
		return depoRefundNum;
	}

	public void setDepoRefundNum(Integer depoRefundNum) {
		this.depoRefundNum = depoRefundNum;
	}

	public String getDepoVoidCode() {
		return depoVoidCode;
	}

	public void setDepoVoidCode(String depoVoidCode) {
		this.depoVoidCode = depoVoidCode;
	}

	public Integer getDepoVoidNum() {
		return depoVoidNum;
	}

	public void setDepoVoidNum(Integer depoVoidNum) {
		this.depoVoidNum = depoVoidNum;
	}

	public Double getAmtIp() {
		return amtIp;
	}

	public void setAmtIp(Double amtIp) {
		this.amtIp = amtIp;
	}

	public Double getAmtIns() {
		return amtIns;
	}

	public void setAmtIns(Double amtIns) {
		this.amtIns = amtIns;
	}

	public Double getAmtOther() {
		return amtOther;
	}

	public void setAmtOther(Double amtOther) {
		this.amtOther = amtOther;
	}

	public Double getAmtSs() {
		return amtSs;
	}

	public void setAmtSs(Double amtSs) {
		this.amtSs = amtSs;
	}

	public Double getAmtSsXj() {
		return amtSsXj;
	}

	public void setAmtSsXj(Double amtSsXj) {
		this.amtSsXj = amtSsXj;
	}

	public Double getAmtGz() {
		return amtGz;
	}

	public void setAmtGz(Double amtGz) {
		this.amtGz = amtGz;
	}

	public Double getAmtTc() {
		return amtTc;
	}

	public void setAmtTc(Double amtTc) {
		this.amtTc = amtTc;
	}
	
	
}