package com.zebone.nhis.ex.oi.vo;

import java.util.ArrayList;
import java.util.List;

public class OiOrderChargeVO {
	private List<OiBlOpDtVO>  orderChargeList = new ArrayList<OiBlOpDtVO>();
    private List<OiBlOpDtVO> orderChargeDelList = new ArrayList<OiBlOpDtVO>();
	public List<OiBlOpDtVO> getOrderChargeList() {
		return orderChargeList;
	}
	public void setOrderChargeList(List<OiBlOpDtVO> orderChargeList) {
		this.orderChargeList = orderChargeList;
	}
	public List<OiBlOpDtVO> getOrderChargeDelList() {
		return orderChargeDelList;
	}
	public void setOrderChargeDelList(List<OiBlOpDtVO> orderChargeDelList) {
		this.orderChargeDelList = orderChargeDelList;
	}

}
