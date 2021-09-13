package com.zebone.nhis.webservice.syx.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "address")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExtAddres {
	@XmlElement(name = "pk_addr")
	private String pkAddr;
	@XmlElement(name = "addrtype")
	private String addrtype;
	@XmlElement(name = "addtypeName")
	private String addtypeName;
	@XmlElement(name = "dt_region_prov")
	private String dtRegionProv;//省编码
	@XmlElement(name = "username")
	private String username;
	@XmlElement(name = "treat_card")
	private String treatCard;
	@XmlElement(name = "consignee")
	private String consignee;
	@XmlElement(name = "con_tel")
	private String conTel;
	@XmlElement(name = "provinces")
	private String provinces;
	@XmlElement(name = "city")
	private String city;
	@XmlElement(name = "zone")
	private String zone;
	@XmlElement(name = "addr_detail")
	private String addrDetail;
	@XmlElement(name = "fee")
	private String fee;
	
	@XmlElement(name = "pk_pi")
	private String pkPi;
	@XmlElement(name = "sort_no")
	private String sortNo;//排序号
	@XmlElement(name = "dt_addrtype")
	private String dtAddrtype;//地址类型
	@XmlElement(name = "name_prov")
	private String nameProv;//省名称
	@XmlElement(name = "dt_region_city")
	private String dtRegionCity;//市编码
	@XmlElement(name = "name_city")
	private String nameCity;//市名称
	@XmlElement(name = "dt_region_dist")
	private String dtRegionDist;//县区编码
	@XmlElement(name = "name_dist")
	private String nameDist;//县区名称
	@XmlElement(name = "addr")
	private String addr;//详细地址
	@XmlElement(name = "tel")
	private String tel;//联系电话
	@XmlElement(name = "name_rel")
	private String nameRel;//联系人姓名
	@XmlElement(name = "flag_use")
	private String flagUse;//常用地址标志
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTreatCard() {
		return treatCard;
	}
	public void setTreatCard(String treatCard) {
		this.treatCard = treatCard;
	}
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public String getConTel() {
		return conTel;
	}
	public void setConTel(String conTel) {
		this.conTel = conTel;
	}
	public String getProvinces() {
		return provinces;
	}
	public void setProvinces(String provinces) {
		this.provinces = provinces;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getAddrDetail() {
		return addrDetail;
	}
	public void setAddrDetail(String addrDetail) {
		this.addrDetail = addrDetail;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getPkAddr() {
		return pkAddr;
	}
	public void setPkAddr(String pkAddr) {
		this.pkAddr = pkAddr;
	}
	public String getAddrtype() {
		return addrtype;
	}
	public void setAddrtype(String addrtype) {
		this.addrtype = addrtype;
	}
	public String getAddtypeName() {
		return addtypeName;
	}
	public void setAddtypeName(String addtypeName) {
		this.addtypeName = addtypeName;
	}
	public String getDtRegionProv() {
		return dtRegionProv;
	}
	public void setDtRegionProv(String dtRegionProv) {
		this.dtRegionProv = dtRegionProv;
	}
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}
	public String getSortNo() {
		return sortNo;
	}
	public void setSortNo(String sortNo) {
		this.sortNo = sortNo;
	}
	public String getDtAddrtype() {
		return dtAddrtype;
	}
	public void setDtAddrtype(String dtAddrtype) {
		this.dtAddrtype = dtAddrtype;
	}
	public String getNameProv() {
		return nameProv;
	}
	public void setNameProv(String nameProv) {
		this.nameProv = nameProv;
	}
	public String getDtRegionCity() {
		return dtRegionCity;
	}
	public void setDtRegionCity(String dtRegionCity) {
		this.dtRegionCity = dtRegionCity;
	}
	public String getNameCity() {
		return nameCity;
	}
	public void setNameCity(String nameCity) {
		this.nameCity = nameCity;
	}
	public String getDtRegionDist() {
		return dtRegionDist;
	}
	public void setDtRegionDist(String dtRegionDist) {
		this.dtRegionDist = dtRegionDist;
	}
	public String getNameDist() {
		return nameDist;
	}
	public void setNameDist(String nameDist) {
		this.nameDist = nameDist;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getNameRel() {
		return nameRel;
	}
	public void setNameRel(String nameRel) {
		this.nameRel = nameRel;
	}
	public String getFlagUse() {
		return flagUse;
	}
	public void setFlagUse(String flagUse) {
		this.flagUse = flagUse;
	}
	
}
