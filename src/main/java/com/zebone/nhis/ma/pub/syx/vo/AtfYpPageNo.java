package com.zebone.nhis.ma.pub.syx.vo;


import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * 
 * 东山包药机，接口表,该表位于sqlserver数据库中（中山二院）
 * 发药记录表
 *
 */
@Table(value = "ATF_YP_PAGE_NO")
public class AtfYpPageNo {
	
	@Field(value = "PAGE_NO")
	private String pageNo;
	
	@Field(value = "WARD_SN")
	private String wardSn;
	
	@Field(value = "GROUP_NO")
	private String groupNo;
	
	@Field(value = "SUBMIT_TIME")
	private Date submitTime;
	
	@Field(value = "CONFIRM_TIME")
	private String confirmTime;
	
	@Field(value = "CONFIRM_OPOER")
	private String confirmOpoer;
	
	@Field(value = "ATF_NO")
	private String atfNo;
	
	@Field(value = "FLAG")
	private String flag;
	
	@Field(value = "BYLX")
	private String bylx;

	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	public String getWardSn() {
		return wardSn;
	}

	public void setWardSn(String wardSn) {
		this.wardSn = wardSn;
	}

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public String getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(String confirmTime) {
		this.confirmTime = confirmTime;
	}

	public String getConfirmOpoer() {
		return confirmOpoer;
	}

	public void setConfirmOpoer(String confirmOpoer) {
		this.confirmOpoer = confirmOpoer;
	}

	public String getAtfNo() {
		return atfNo;
	}

	public void setAtfNo(String atfNo) {
		this.atfNo = atfNo;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getBylx() {
		return bylx;
	}

	public void setBylx(String bylx) {
		this.bylx = bylx;
	}
}
