package com.zebone.nhis.ma.pub.syx.vo;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("subject")
public class Subject {
	private Address address;
	private OrderInfo orderInfo;
	private String  pk_dept_de;
	private String  code_de;
	private String  pk_pddecate;
	private String  action;
	private BdItem  bd_item;
	private BdOrd  bd_ord;
	
	@XStreamImplicit // 去除显示
	private List<OrderInfo> orderInfoList;
	public Address getAddress() {
		if(address == null)address = new Address();
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public OrderInfo getOrderInfo() {
		if(orderInfo == null)orderInfo = new OrderInfo();
		return orderInfo;
	}

	public void setOrderInfo(OrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}

	public List<OrderInfo> getOrderInfoList() {
		if (orderInfoList == null) {
			orderInfoList = new ArrayList<OrderInfo>(); 
		}
		return orderInfoList;
	}

	public void setOrderInfoList(List<OrderInfo> orderInfoList) {
		this.orderInfoList = orderInfoList;
	}

	public String getPk_dept_de() {
		return pk_dept_de;
	}

	public String getCode_de() {
		return code_de;
	}

	public String getPk_pddecate() {
		return pk_pddecate;
	}

	public void setPk_dept_de(String pk_dept_de) {
		this.pk_dept_de = pk_dept_de;
	}

	public void setCode_de(String code_de) {
		this.code_de = code_de;
	}

	public void setPk_pddecate(String pk_pddecate) {
		this.pk_pddecate = pk_pddecate;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public BdItem getBd_item() {
		if (bd_item == null) {
			 bd_item = new BdItem();
		}
		return bd_item;
	}

	public void setBd_item(BdItem bd_item) {
		this.bd_item = bd_item;
	}

	public BdOrd getBd_ord() {
		if (bd_ord == null) {
			bd_ord = new BdOrd();
		}
		return bd_ord;
	}

	public void setBd_ord(BdOrd bd_ord) {
		this.bd_ord = bd_ord;
	}
	
	
}
