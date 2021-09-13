package com.zebone.nhis.ma.pub.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("address")
public class Address {
	private String pk_addr;
	private String addrtype;
	private String addtypeName;
	private String dt_region_prov;
	private String name_prov;
	private String dt_region_city;
	private String name_city;
	private String dt_region_dist;
	private String name_dist;
	private String addr;
	private String name_rel;
	private String tel;
	private String sort_no;
	private String flag_use;
	private String amt_fee;
	public String getPk_addr() {
		return pk_addr;
	}
	public void setPk_addr(String pk_addr) {
		this.pk_addr = pk_addr;
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
	public String getDt_region_prov() {
		return dt_region_prov;
	}
	public void setDt_region_prov(String dt_region_prov) {
		this.dt_region_prov = dt_region_prov;
	}
	public String getName_prov() {
		return name_prov;
	}
	public void setName_prov(String name_prov) {
		this.name_prov = name_prov;
	}
	public String getDt_region_city() {
		return dt_region_city;
	}
	public void setDt_region_city(String dt_region_city) {
		this.dt_region_city = dt_region_city;
	}
	public String getName_city() {
		return name_city;
	}
	public void setName_city(String name_city) {
		this.name_city = name_city;
	}
	public String getDt_region_dist() {
		return dt_region_dist;
	}
	public void setDt_region_dist(String dt_region_dist) {
		this.dt_region_dist = dt_region_dist;
	}
	public String getName_dist() {
		return name_dist;
	}
	public void setName_dist(String name_dist) {
		this.name_dist = name_dist;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getName_rel() {
		return name_rel;
	}
	public void setName_rel(String name_rel) {
		this.name_rel = name_rel;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getSort_no() {
		return sort_no;
	}
	public void setSort_no(String sort_no) {
		this.sort_no = sort_no;
	}
	public String getFlag_use() {
		return flag_use;
	}
	public void setFlag_use(String flag_use) {
		this.flag_use = flag_use;
	}
	public String getAmt_fee() {
		return amt_fee;
	}
	public void setAmt_fee(String amt_fee) {
		this.amt_fee = amt_fee;
	}
	
}
