package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.List;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="req")
public class QryAppSettleReqVo {
	
	@XmlElement(name="pkPv")
	private String pkPv;
	
	@XmlElement(name="euSttype")
	private String euSttype;
	
	@XmlElement(name="euStresult")
	private String euStresult;
	
	@XmlElement(name="dateEnd")
	private String dateEnd;
	
	@XmlElement(name="amountPrep")
	private BigDecimal amountPrep;
	
	@XmlElement(name="amountSt")
	private BigDecimal amountSt;
	
	@XmlElement(name="amountPi")
	private BigDecimal amountPi;
	
	@XmlElement(name="amountInsu")
	private BigDecimal amountInsu;
	
	@XmlElement(name="blDeposit")
	private List<QryAppBlDeposit> deposit;
	
	@XmlElement(name="codeIp")
	private String codeIp;
	
	@XmlElement(name="namePi")
	private String namePi;
	
	@XmlElement(name="userCardId")
	private String userCardId;
	
	@XmlElement(name="dateBegin")
	private String dateBegin;

	@XmlElement(name="pkDeptSt")
	private String pkDeptSt;

	@XmlElement(name="pkEmpSt")
	private String pkEmpSt;

	@XmlElement(name="nameEmpSt")
	private String nameEmpSt;

	public String getPkDeptSt() {
		return pkDeptSt;
	}

	public void setPkDeptSt(String pkDeptSt) {
		this.pkDeptSt = pkDeptSt;
	}

	public String getPkEmpSt() {
		return pkEmpSt;
	}

	public void setPkEmpSt(String pkEmpSt) {
		this.pkEmpSt = pkEmpSt;
	}

	public String getNameEmpSt() {
		return nameEmpSt;
	}

	public void setNameEmpSt(String nameEmpSt) {
		this.nameEmpSt = nameEmpSt;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getEuSttype() {
		return euSttype;
	}

	public void setEuSttype(String euSttype) {
		this.euSttype = euSttype;
	}

	public String getEuStresult() {
		return euStresult;
	}

	public void setEuStresult(String euStresult) {
		this.euStresult = euStresult;
	}

	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

	public BigDecimal getAmountPrep() {
		return amountPrep;
	}

	public void setAmountPrep(BigDecimal amountPrep) {
		this.amountPrep = amountPrep;
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

	public List<QryAppBlDeposit> getDeposit() {
		return deposit;
	}

	public void setDeposit(List<QryAppBlDeposit> deposit) {
		this.deposit = deposit;
	}

	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	public String getUserCardId() {
		return userCardId;
	}

	public void setUserCardId(String userCardId) {
		this.userCardId = userCardId;
	}

	public String getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(String dateBegin) {
		this.dateBegin = dateBegin;
	}
}
