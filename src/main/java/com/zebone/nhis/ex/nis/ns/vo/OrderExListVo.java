package com.zebone.nhis.ex.nis.ns.vo;

import java.util.Date;
import java.util.List;

import com.zebone.nhis.ex.pub.vo.OrderExecVo;

public class OrderExListVo {

	private static final long serialVersionUID = 1773831336977753853L;
	private Double count;					//执行次数
	private List<OrderExecVo> exce_list;	//执行单次信息
	private String code_ordtype;			//服务类型编码
	private boolean isFreq;					//是否是非变动医嘱
	private String flagPivas;//静配标志
	private String pkDeptOcc;//执行科室
	private String nameDeptOcc;//执行科室名称
	private boolean isHerb;					//是否是草药
	private Double quan_total;			//总用量当前单位
	private Double quan_med;				//总用量医学单位
	private String message;					//提示信息，  扩展字段现在没有用
	private Date date_end;			//请领结束日期时间，小时频次记录最后执行时间
	private boolean isPres;					//是否是处方
	
	private String pkUnit;//--用量单位
	private Double packSize;//包装量
	private String pkUnitStore;//仓库对应的包装单位
	private int packSizeStore;//仓库对应的包装量
	
	private PdHerbInfoVo herbvo;//草药物品信息


	private Date datePlan;//计划执行时间

	private String paramDay;//ex0059参数

	private List<String> pkexoccs;

	public List<String> getPkexoccs() {
		return pkexoccs;
	}

	public void setPkexoccs(List<String> pkexoccs) {
		this.pkexoccs = pkexoccs;
	}

	public String getParamDay() {
		return paramDay;
	}

	public void setParamDay(String paramDay) {
		this.paramDay = paramDay;
	}

	public Date getDatePlan() {
		return datePlan;
	}

	public void setDatePlan(Date datePlan) {
		this.datePlan = datePlan;
	}

	public String getPkDeptOcc() {
		return pkDeptOcc;
	}
	public void setPkDeptOcc(String pkDeptOcc) {
		this.pkDeptOcc = pkDeptOcc;
	}
	public String getNameDeptOcc() {
		return nameDeptOcc;
	}
	public void setNameDeptOcc(String nameDeptOcc) {
		this.nameDeptOcc = nameDeptOcc;
	}
	public String getFlagPivas() {
		return flagPivas;
	}
	public void setFlagPivas(String flagPivas) {
		this.flagPivas = flagPivas;
	}
	public String getPkUnit() {
		return pkUnit;
	}
	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}
	public Double getPackSize() {
		return packSize;
	}
	public void setPackSize(Double packSize) {
		this.packSize = packSize;
	}
	public String getPkUnitStore() {
		return pkUnitStore;
	}
	public void setPkUnitStore(String pkUnitStore) {
		this.pkUnitStore = pkUnitStore;
	}
	public int getPackSizeStore() {
		return packSizeStore;
	}
	public void setPackSizeStore(int packSizeStore) {
		this.packSizeStore = packSizeStore;
	}
	public PdHerbInfoVo getHerbvo() {
		return herbvo;
	}
	public void setHerbvo(PdHerbInfoVo herbvo) {
		this.herbvo = herbvo;
	}
	public Double getCount() {
		return count;
	}
	public void setCount(Double count) {
		this.count = count;
	}
	public List<OrderExecVo> getExce_list() {
		return exce_list;
	}
	public void setExce_list(List<OrderExecVo> exce_list) {
		this.exce_list = exce_list;
	}
	
	public String getCode_ordtype() {
		return code_ordtype;
	}
	public void setCode_ordtype(String code_ordtype) {
		this.code_ordtype = code_ordtype;
	}
	public boolean isFreq() {
		return isFreq;
	}
	public void setFreq(boolean isFreq) {
		this.isFreq = isFreq;
	}
	public boolean isHerb() {
		return isHerb;
	}
	public void setHerb(boolean isHerb) {
		this.isHerb = isHerb;
	}
	public Double getQuan_total() {
		return quan_total;
	}
	public void setQuan_total(Double quan_total) {
		this.quan_total = quan_total;
	}
	public Double getQuan_med() {
		return quan_med;
	}
	public void setQuan_med(Double quan_med) {
		this.quan_med = quan_med;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	public Date getDate_end() {
		return date_end;
	}
	public void setDate_end(Date date_end) {
		this.date_end = date_end;
	}
	public boolean isPres() {
		return isPres;
	}
	public void setPres(boolean isPres) {
		this.isPres = isPres;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
