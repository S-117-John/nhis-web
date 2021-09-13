package com.zebone.nhis.bl.pub.syx.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OpStInfoVo {
	
	private String pkSettle;

    /** PK_PI - 患者主键 */
    private String pkPi;

    /** PK_PV - 就诊主键 */
    private String pkPv;
    
    private String codePv;
	
	private String dtSttype;
	
	private String typeName;
	
	private BigDecimal amountSt;
	
	private BigDecimal amountPi;

    /** AMOUNT_INSU - 第三方医保金额 */
    private BigDecimal amountInsu;

    /** DATE_ST - 结算日期 */
    private Date dateSt;
	
    private String nameEmpSt;
	
    private List<Map<String,Object>> opdtList = new ArrayList<>();

	public String getPkSettle() {
		return pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getDtSttype() {
		return dtSttype;
	}

	public void setDtSttype(String dtSttype) {
		this.dtSttype = dtSttype;
	}

	public BigDecimal getAmountSt() {
		return amountSt;
	}

	public void setAmountSt(BigDecimal amountSt) {
		this.amountSt = amountSt;
	}

	public BigDecimal getAmountPi() {
		return amountPi;
	}

	public void setAmountPi(BigDecimal amountPi) {
		this.amountPi = amountPi;
	}

	public BigDecimal getAmountInsu() {
		return amountInsu;
	}

	public void setAmountInsu(BigDecimal amountInsu) {
		this.amountInsu = amountInsu;
	}

	public Date getDateSt() {
		return dateSt;
	}

	public void setDateSt(Date dateSt) {
		this.dateSt = dateSt;
	}

	public String getNameEmpSt() {
		return nameEmpSt;
	}

	public void setNameEmpSt(String nameEmpSt) {
		this.nameEmpSt = nameEmpSt;
	}

	public List<Map<String, Object>> getOpdtList() {
		return opdtList;
	}

	public void setOpdtList(List<Map<String, Object>> opdtList) {
		this.opdtList = opdtList;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}
	
	
}
