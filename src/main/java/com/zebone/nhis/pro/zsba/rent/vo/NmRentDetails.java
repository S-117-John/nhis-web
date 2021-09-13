package com.zebone.nhis.pro.zsba.rent.vo;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * 非医疗费用-设备出租明细
 * @author lipz
 *
 */
@Table(value="nm_rent_details")
public class NmRentDetails extends BaseModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4265585872548132978L;
	
	
	@PK
	@Field(value="pk_rent_details",id=KeyId.UUID)
	private String pkRentDetails;// 出租明细主键
	
	@Field(value="pk_rent")
    private String pkRent;// 出租主键
	
	@Field(value="times")
    private Integer times;// 就诊次数
	
	@Field(value="day_num")
    private Integer dayNum;// 出租天数
	
	@Field(value="date_begin")
    private Date dateBegin;// 开始日期
	
	@Field(value="date_end")
    private Date dateEnd;// 结束日期

	@Field(value="modity_time")
    private Date modityTime;//

	
	
	public String getPkRentDetails() {
		return pkRentDetails;
	}
	public void setPkRentDetails(String pkRentDetails) {
		this.pkRentDetails = pkRentDetails;
	}

	public String getPkRent() {
		return pkRent;
	}
	public void setPkRent(String pkRent) {
		this.pkRent = pkRent;
	}

	public Integer getTimes() {
		return times;
	}
	public void setTimes(Integer times) {
		this.times = times;
	}

	public Integer getDayNum() {
		return dayNum;
	}
	public void setDayNum(Integer dayNum) {
		this.dayNum = dayNum;
	}

	public Date getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}

	public Date getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public Date getModityTime() {
		return modityTime;
	}
	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}
		

	
}
