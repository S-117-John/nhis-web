package com.zebone.nhis.ma.pub.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("pdetail")
public class Pdetail {
	private String user_name;
	private String age;
	private String gender;
	private String tel;
	private String is_suffering;
	private String suffering_num;
	private String amount;
	private String is_pregnant;
	private String money;
	private String ji_fried;
	private String type;
	private String is_within;
	private String other_pres_num;
	private String special_instru;
	private String bed_num;
	private String hos_depart;
	private String hospital_num;
	private String disease_code;
	private String doctor;
	private String prescript_remark;
	private String is_hos;
	private MediciXq medici_xq;
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getIs_suffering() {
		return is_suffering;
	}
	public void setIs_suffering(String is_suffering) {
		this.is_suffering = is_suffering;
	}
	public String getSuffering_num() {
		return suffering_num;
	}
	public void setSuffering_num(String suffering_num) {
		this.suffering_num = suffering_num;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getIs_pregnant() {
		return is_pregnant;
	}
	public void setIs_pregnant(String is_pregnant) {
		this.is_pregnant = is_pregnant;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getJi_fried() {
		return ji_fried;
	}
	public void setJi_fried(String ji_fried) {
		this.ji_fried = ji_fried;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIs_within() {
		return is_within;
	}
	public void setIs_within(String is_within) {
		this.is_within = is_within;
	}
	public String getOther_pres_num() {
		return other_pres_num;
	}
	public void setOther_pres_num(String other_pres_num) {
		this.other_pres_num = other_pres_num;
	}
	public String getSpecial_instru() {
		return special_instru;
	}
	public void setSpecial_instru(String special_instru) {
		this.special_instru = special_instru;
	}
	public String getBed_num() {
		return bed_num;
	}
	public void setBed_num(String bed_num) {
		this.bed_num = bed_num;
	}
	public String getHos_depart() {
		return hos_depart;
	}
	public void setHos_depart(String hos_depart) {
		this.hos_depart = hos_depart;
	}
	public String getHospital_num() {
		return hospital_num;
	}
	public void setHospital_num(String hospital_num) {
		this.hospital_num = hospital_num;
	}
	public String getDisease_code() {
		return disease_code;
	}
	public void setDisease_code(String disease_code) {
		this.disease_code = disease_code;
	}
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public String getPrescript_remark() {
		return prescript_remark;
	}
	public void setPrescript_remark(String prescript_remark) {
		this.prescript_remark = prescript_remark;
	}
	public String getIs_hos() {
		return is_hos;
	}
	public void setIs_hos(String is_hos) {
		this.is_hos = is_hos;
	}
	public MediciXq getMedici_xq() {
		if(medici_xq == null)medici_xq = new MediciXq();
		return medici_xq;
	}
	public void setMedici_xq(MediciXq medici_xq) {
		this.medici_xq = medici_xq;
	}
	
	
}
