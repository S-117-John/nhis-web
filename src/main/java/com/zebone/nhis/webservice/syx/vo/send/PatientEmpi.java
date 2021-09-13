package com.zebone.nhis.webservice.syx.vo.send;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name = "PATIENT")
@XmlAccessorType(XmlAccessType.FIELD)
public class PatientEmpi {

	@XmlElement(name = "SOURCE_CODE")
	private String SOURCE_CODE;

	@XmlElement(name = "SOURCE_ID")
	private String SOURCE_ID;

	@XmlElement(name = "MPI")
	private String MPI;

	@XmlElement(name = "PI_NAME")
	private String PI_NAME;

	@XmlElement(name = "CODE_SEX")
	private String CODE_SEX;

	@XmlElement(name = "NAME_SEX")
	private String NAME_SEX;

	@XmlElement(name = "BIRTHDAY")
	private String BIRTHDAY;

	@XmlElement(name = "CODE_TYPE_INSURANCE")
	private String CODE_TYPE_INSURANCE;

	@XmlElement(name = "NAME_TYPE_INSURANCE")
	private String NAME_TYPE_INSURANCE;

	@XmlElement(name = "CODE_MARI")
	private String CODE_MARI;

	@XmlElement(name = "NAME_MARI")
	private String NAME_MARI;

	@XmlElement(name = "CODE_OCCU")
	private String CODE_OCCU;

	@XmlElement(name = "NAME_OCCU")
	private String NAME_OCCU;

	@XmlElement(name = "BLOOD_TYPE")
	private String BLOOD_TYPE;

	@XmlElement(name = "RH")
	private String RH;

	@XmlElement(name = "CODE_EDU")
	private String CODE_EDU;

	@XmlElement(name = "NAME_EDU")
	private String NAME_EDU;

	@XmlElement(name = "CODE_DEGREE")
	private String CODE_DEGREE;

	@XmlElement(name = "NAME_DEGREE")
	private String NAME_DEGREE;

	@XmlElement(name = "CODE_COUNTRY")
	private String CODE_COUNTRY;

	@XmlElement(name = "NAME_COUNTRY")
	private String NAME_COUNTRY;

	@XmlElement(name = "CODE_NATION")
	private String CODE_NATION;

	@XmlElement(name = "NAME_NATION")
	private String NAME_NATION;

	@XmlElement(name = "TELENO")
	private String TELENO;

	@XmlElement(name = "MOBILENO")
	private String MOBILENO;

	@XmlElement(name = "EMAIL")
	private String EMAIL;

	@XmlElement(name = "NAME_WORKUNIT")
	private String NAME_WORKUNIT;

	@XmlElement(name = "ADDR_WORKUNIT")
	private String ADDR_WORKUNIT;

	@XmlElement(name = "PHONE_WORKUNIT")
	private String PHONE_WORKUNIT;

	@XmlElement(name = "POSTCODE_WORKUNIT")
	private String POSTCODE_WORKUNIT;

	@XmlElement(name = "CREATE_DATE")
	private String CREATE_DATE;

	@XmlElement(name = "CODE_CREATEUSER")
	private String CODE_CREATEUSER;

	@XmlElement(name = "NAME_CREATEUSER")
	private String NAME_CREATEUSER;

	@XmlElement(name = "EXT_DATA")
	private String EXT_DATA;

	@XmlElement(name = "LEVEL_SECRECY")
	private String LEVEL_SECRECY;

	@XmlElementWrapper(name = "CARD_LIST")
	@XmlElement(name = "CARD")
	private List<Card> cards;

	@XmlElementWrapper(name = "ADDRESS_LIST")
	@XmlElement(name = "ADDRESS")
	private List<Address> AddressList;

	@XmlElementWrapper(name = "LINKMAN_LIST")
	@XmlElement(name = "LINKMAN")
	private List<Linkman> linkmans;

	public String getSOURCE_CODE() {
		return SOURCE_CODE;
	}

	public void setSOURCE_CODE(String sOURCE_CODE) {
		SOURCE_CODE = sOURCE_CODE;
	}

	public String getSOURCE_ID() {
		return SOURCE_ID;
	}

	public void setSOURCE_ID(String sOURCE_ID) {
		SOURCE_ID = sOURCE_ID;
	}

	public String getMPI() {
		return MPI;
	}

	public void setMPI(String mPI) {
		MPI = mPI;
	}

	public String getPI_NAME() {
		return PI_NAME;
	}

	public void setPI_NAME(String pI_NAME) {
		PI_NAME = pI_NAME;
	}

	public String getCODE_SEX() {
		return CODE_SEX;
	}

	public void setCODE_SEX(String cODE_SEX) {
		CODE_SEX = cODE_SEX;
	}

	public String getNAME_SEX() {
		return NAME_SEX;
	}

	public void setNAME_SEX(String nAME_SEX) {
		NAME_SEX = nAME_SEX;
	}

	public String getBIRTHDAY() {
		return BIRTHDAY;
	}

	public void setBIRTHDAY(String bIRTHDAY) {
		BIRTHDAY = bIRTHDAY;
	}

	public String getCODE_TYPE_INSURANCE() {
		return CODE_TYPE_INSURANCE;
	}

	public void setCODE_TYPE_INSURANCE(String cODE_TYPE_INSURANCE) {
		CODE_TYPE_INSURANCE = cODE_TYPE_INSURANCE;
	}

	public String getNAME_TYPE_INSURANCE() {
		return NAME_TYPE_INSURANCE;
	}

	public void setNAME_TYPE_INSURANCE(String nAME_TYPE_INSURANCE) {
		NAME_TYPE_INSURANCE = nAME_TYPE_INSURANCE;
	}

	public String getCODE_MARI() {
		return CODE_MARI;
	}

	public void setCODE_MARI(String cODE_MARI) {
		CODE_MARI = cODE_MARI;
	}

	public String getNAME_MARI() {
		return NAME_MARI;
	}

	public void setNAME_MARI(String nAME_MARI) {
		NAME_MARI = nAME_MARI;
	}

	public String getCODE_OCCU() {
		return CODE_OCCU;
	}

	public void setCODE_OCCU(String cODE_OCCU) {
		CODE_OCCU = cODE_OCCU;
	}

	public String getNAME_OCCU() {
		return NAME_OCCU;
	}

	public void setNAME_OCCU(String nAME_OCCU) {
		NAME_OCCU = nAME_OCCU;
	}

	public String getBLOOD_TYPE() {
		return BLOOD_TYPE;
	}

	public void setBLOOD_TYPE(String bLOOD_TYPE) {
		BLOOD_TYPE = bLOOD_TYPE;
	}

	public String getRH() {
		return RH;
	}

	public void setRH(String rH) {
		RH = rH;
	}

	public String getCODE_EDU() {
		return CODE_EDU;
	}

	public void setCODE_EDU(String cODE_EDU) {
		CODE_EDU = cODE_EDU;
	}

	public String getNAME_EDU() {
		return NAME_EDU;
	}

	public void setNAME_EDU(String nAME_EDU) {
		NAME_EDU = nAME_EDU;
	}

	public String getCODE_DEGREE() {
		return CODE_DEGREE;
	}

	public void setCODE_DEGREE(String cODE_DEGREE) {
		CODE_DEGREE = cODE_DEGREE;
	}

	public String getNAME_DEGREE() {
		return NAME_DEGREE;
	}

	public void setNAME_DEGREE(String nAME_DEGREE) {
		NAME_DEGREE = nAME_DEGREE;
	}

	public String getCODE_COUNTRY() {
		return CODE_COUNTRY;
	}

	public void setCODE_COUNTRY(String cODE_COUNTRY) {
		CODE_COUNTRY = cODE_COUNTRY;
	}

	public String getNAME_COUNTRY() {
		return NAME_COUNTRY;
	}

	public void setNAME_COUNTRY(String nAME_COUNTRY) {
		NAME_COUNTRY = nAME_COUNTRY;
	}

	public String getCODE_NATION() {
		return CODE_NATION;
	}

	public void setCODE_NATION(String cODE_NATION) {
		CODE_NATION = cODE_NATION;
	}

	public String getNAME_NATION() {
		return NAME_NATION;
	}

	public void setNAME_NATION(String nAME_NATION) {
		NAME_NATION = nAME_NATION;
	}

	public String getTELENO() {
		return TELENO;
	}

	public void setTELENO(String tELENO) {
		TELENO = tELENO;
	}

	public String getMOBILENO() {
		return MOBILENO;
	}

	public void setMOBILENO(String mOBILENO) {
		MOBILENO = mOBILENO;
	}

	public String getEMAIL() {
		return EMAIL;
	}

	public void setEMAIL(String eMAIL) {
		EMAIL = eMAIL;
	}

	public String getNAME_WORKUNIT() {
		return NAME_WORKUNIT;
	}

	public void setNAME_WORKUNIT(String nAME_WORKUNIT) {
		NAME_WORKUNIT = nAME_WORKUNIT;
	}

	public String getADDR_WORKUNIT() {
		return ADDR_WORKUNIT;
	}

	public void setADDR_WORKUNIT(String aDDR_WORKUNIT) {
		ADDR_WORKUNIT = aDDR_WORKUNIT;
	}

	public String getPHONE_WORKUNIT() {
		return PHONE_WORKUNIT;
	}

	public void setPHONE_WORKUNIT(String pHONE_WORKUNIT) {
		PHONE_WORKUNIT = pHONE_WORKUNIT;
	}

	public String getPOSTCODE_WORKUNIT() {
		return POSTCODE_WORKUNIT;
	}

	public void setPOSTCODE_WORKUNIT(String pOSTCODE_WORKUNIT) {
		POSTCODE_WORKUNIT = pOSTCODE_WORKUNIT;
	}

	public String getCREATE_DATE() {
		return CREATE_DATE;
	}

	public void setCREATE_DATE(String cREATE_DATE) {
		CREATE_DATE = cREATE_DATE;
	}

	public String getCODE_CREATEUSER() {
		return CODE_CREATEUSER;
	}

	public void setCODE_CREATEUSER(String cODE_CREATEUSER) {
		CODE_CREATEUSER = cODE_CREATEUSER;
	}

	public String getNAME_CREATEUSER() {
		return NAME_CREATEUSER;
	}

	public void setNAME_CREATEUSER(String nAME_CREATEUSER) {
		NAME_CREATEUSER = nAME_CREATEUSER;
	}

	public String getEXT_DATA() {
		return EXT_DATA;
	}

	public void setEXT_DATA(String eXT_DATA) {
		EXT_DATA = eXT_DATA;
	}

	public String getLEVEL_SECRECY() {
		return LEVEL_SECRECY;
	}

	public void setLEVEL_SECRECY(String lEVEL_SECRECY) {
		LEVEL_SECRECY = lEVEL_SECRECY;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	public List<Address> getAddressList() {
		return AddressList;
	}

	public void setAddressList(List<Address> addressList) {
		AddressList = addressList;
	}

	public List<Linkman> getLinkmans() {
		return linkmans;
	}

	public void setLinkmans(List<Linkman> linkmans) {
		this.linkmans = linkmans;
	}
}
