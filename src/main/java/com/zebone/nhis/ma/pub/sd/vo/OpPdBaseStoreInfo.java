package com.zebone.nhis.ma.pub.sd.vo;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Med")
public class OpPdBaseStoreInfo {
	/**
	 * 药品编码
	 */
	@XmlElement(name="MedOnlyCode")
	private String medOnlyCode;

	/**
	 * 药品名称
	 */
	@XmlElement(name="MedName")
	private String medName;

	/**
	 * 药品规格
	 */
	@XmlElement(name="MedUnit")
	private String medUnit;

	/**
	 * 药品发药单位
	 */
	@XmlElement(name="MedPack")
	private String medPack;

	/**
	 * 药品包装单位
	 */
	@XmlElement(name="MedUnitPack")
	private String medUnitPack;

	/**
	 * 转换系数
	 */
	@XmlElement(name="MedConvercof")
	private Integer medConvercof;

	/**
	 * 药品拼音码
	 */
	@XmlElement(name="MedPYCode")
	private String medPyCode;

	/**
	 * 药品生产厂家
	 */
	@XmlElement(name="MedFactory")
	private String medFactory;

	/**
	 * 药品价格
	 */
	@XmlElement(name="MedUnitPrice")
	private BigDecimal medUnitPrice;

	/**
	 * 药品货柜码
	 */
	@XmlElement(name="MedShelf")
	private String medShelf;

	

	public String getMedOnlyCode() {
		return medOnlyCode;
	}

	public void setMedOnlyCode(String medOnlyCode) {
		this.medOnlyCode = medOnlyCode;
	}

	public String getMedName() {
		return medName;
	}

	public void setMedName(String medName) {
		this.medName = medName;
	}

	public String getMedUnit() {
		return medUnit;
	}

	public void setMedUnit(String medUnit) {
		this.medUnit = medUnit;
	}

	public String getMedPack() {
		return medPack;
	}

	public void setMedPack(String medPack) {
		this.medPack = medPack;
	}

	public String getMedUnitPack() {
		return medUnitPack;
	}

	public void setMedUnitPack(String medUnitPack) {
		this.medUnitPack = medUnitPack;
	}

	public Integer getMedConvercof() {
		return medConvercof;
	}

	public void setMedConvercof(Integer medConvercof) {
		this.medConvercof = medConvercof;
	}

	public String getMedPyCode() {
		return medPyCode;
	}

	public void setMedPyCode(String medPyCode) {
		this.medPyCode = medPyCode;
	}

	public String getMedFactory() {
		return medFactory;
	}

	public void setMedFactory(String medFactory) {
		this.medFactory = medFactory;
	}

	public BigDecimal getMedUnitPrice() {
		return medUnitPrice;
	}

	public void setMedUnitPrice(BigDecimal medUnitPrice) {
		this.medUnitPrice = medUnitPrice;
	}

	public String getMedShelf() {
		return medShelf;
	}

	public void setMedShelf(String medShelf) {
		this.medShelf = medShelf;
	}


}
