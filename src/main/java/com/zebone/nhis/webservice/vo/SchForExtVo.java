package com.zebone.nhis.webservice.vo;

import com.zebone.nhis.common.module.sch.plan.SchSch;

public class SchForExtVo extends SchSch{

	private String schresName;
	
	private String schsrvName;
	
	private String nameDateslot;//日期分组名称
	
	private Integer cntUnused;//未使用可约号
	
	private Double price;//排班服务参考价格

	public String getSchresName() {
		return schresName;
	}

	public void setSchresName(String schresName) {
		this.schresName = schresName;
	}

	public String getSchsrvName() {
		return schsrvName;
	}

	public void setSchsrvName(String schsrvName) {
		this.schsrvName = schsrvName;
	}

	public String getNameDateslot() {
		return nameDateslot;
	}

	public void setNameDateslot(String nameDateslot) {
		this.nameDateslot = nameDateslot;
	}

	public Integer getCntUnused() {
		return cntUnused;
	}

	public void setCntUnused(Integer cntUnused) {
		this.cntUnused = cntUnused;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	
}
