package com.zebone.nhis.ma.pub.syx.vo;

import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("data")
public class Data {
	private String order_time;
	private String treat_card;
	private String reg_num;
	private String addr_str;
	private String consignee;
	private String con_tel;
	private String send_goods_time;
	private String is_hos_addr;
	private Prescript prescript;
	private String pkPv;
	private Date birthDate;
	private String agePv;
	private String ordsn;
	private String oper_name;
	private String reason;
	private String order_id;
	
	public String getOper_name() {
		return oper_name;
	}
	public void setOper_name(String oper_name) {
		this.oper_name = oper_name;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getOrder_time() {
		return order_time;
	}
	public void setOrder_time(String order_time) {
		this.order_time = order_time;
	}
	public String getTreat_card() {
		return treat_card;
	}
	public void setTreat_card(String treat_card) {
		this.treat_card = treat_card;
	}
	public String getReg_num() {
		return reg_num;
	}
	public void setReg_num(String reg_num) {
		this.reg_num = reg_num;
	}
	public String getAddr_str() {
		return addr_str;
	}
	public void setAddr_str(String addr_str) {
		this.addr_str = addr_str;
	}
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public String getCon_tel() {
		return con_tel;
	}
	public void setCon_tel(String con_tel) {
		this.con_tel = con_tel;
	}
	public String getSend_goods_time() {
		return send_goods_time;
	}
	public void setSend_goods_time(String send_goods_time) {
		this.send_goods_time = send_goods_time;
	}
	public String getIs_hos_addr() {
		return is_hos_addr;
	}
	public void setIs_hos_addr(String is_hos_addr) {
		this.is_hos_addr = is_hos_addr;
	}
	public Prescript getPrescript() {
		if(prescript == null)prescript = new Prescript();
		return prescript;
	}
	public void setPrescript(Prescript prescript) {
		this.prescript = prescript;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getAgePv() {
		return agePv;
	}
	public void setAgePv(String agePv) {
		this.agePv = agePv;
	}
	public String getOrdsn() {
		return ordsn;
	}
	public void setOrdsn(String ordsn) {
		this.ordsn = ordsn;
	}
	
	
}
