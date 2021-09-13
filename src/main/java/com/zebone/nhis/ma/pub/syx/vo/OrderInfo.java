package com.zebone.nhis.ma.pub.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("orderInfo")
public class OrderInfo {
	private Head head;
	private Data data;
	
	private String ward_sn;
	private String page_no;
	private String atf_no;
	private String flag;
	private String bylx;
	private String inpatient_no;
	private String p_id;
	private String name;
	private String age;
	//private String ward_sn;
	private String ward_name;
	private String doctor;
	private String bed_no;
	private String comment;
	private String drug_code;
	private String drugname;
	private String specification;
	private String dosage;
	private String dos_unit;
	private String amount;
	private String total;
	private String occ_time;
	private String detail_sn;
	private String execdalistid;
	
	public Head getHead() {
		if(head == null)head = new Head();
		return head;
	}
	public void setHead(Head head) {
		this.head = head;
	}
	public Data getData() {
		if(data == null) data = new Data();
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}
	public String getWard_sn() {
		return ward_sn;
	}
	public String getPage_no() {
		return page_no;
	}
	public String getAtf_no() {
		return atf_no;
	}
	public String getFlag() {
		return flag;
	}
	public String getBylx() {
		return bylx;
	}
	public String getInpatient_no() {
		return inpatient_no;
	}
	public String getP_id() {
		return p_id;
	}
	public String getName() {
		return name;
	}
	public String getAge() {
		return age;
	}
	public String getWard_name() {
		return ward_name;
	}
	public String getDoctor() {
		return doctor;
	}
	public String getBed_no() {
		return bed_no;
	}
	public String getComment() {
		return comment;
	}
	public String getDrug_code() {
		return drug_code;
	}
	public String getDrugname() {
		return drugname;
	}
	public String getSpecification() {
		return specification;
	}
	public String getDosage() {
		return dosage;
	}
	public String getDos_unit() {
		return dos_unit;
	}
	public String getAmount() {
		return amount;
	}
	public String getTotal() {
		return total;
	}
	public String getOcc_time() {
		return occ_time;
	}
	public String getDetail_sn() {
		return detail_sn;
	}
	public String getExecdalistid() {
		return execdalistid;
	}
	public void setWard_sn(String ward_sn) {
		this.ward_sn = ward_sn;
	}
	public void setPage_no(String page_no) {
		this.page_no = page_no;
	}
	public void setAtf_no(String atf_no) {
		this.atf_no = atf_no;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public void setBylx(String bylx) {
		this.bylx = bylx;
	}
	public void setInpatient_no(String inpatient_no) {
		this.inpatient_no = inpatient_no;
	}
	public void setP_id(String p_id) {
		this.p_id = p_id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public void setWard_name(String ward_name) {
		this.ward_name = ward_name;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public void setBed_no(String bed_no) {
		this.bed_no = bed_no;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public void setDrug_code(String drug_code) {
		this.drug_code = drug_code;
	}
	public void setDrugname(String drugname) {
		this.drugname = drugname;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public void setDosage(String dosage) {
		this.dosage = dosage;
	}
	public void setDos_unit(String dos_unit) {
		this.dos_unit = dos_unit;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public void setOcc_time(String occ_time) {
		this.occ_time = occ_time;
	}
	public void setDetail_sn(String detail_sn) {
		this.detail_sn = detail_sn;
	}
	public void setExecdalistid(String execdalistid) {
		this.execdalistid = execdalistid;
	}
	
	
}
