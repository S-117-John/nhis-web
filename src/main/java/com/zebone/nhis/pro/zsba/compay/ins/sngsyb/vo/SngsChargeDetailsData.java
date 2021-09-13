package com.zebone.nhis.pro.zsba.compay.ins.sngsyb.vo;

public class SngsChargeDetailsData {

	private String bka063;//录入人工号	
	private String bka064;//录入人姓名	
	private String ake005;//医院药品项目编码	 
	private String ake006;//医院药品项目名称	 
	private String bka051;//费用发生日期	格式：yyyyMMdd 
	private String bka056;//单价	精确到小数点后4位
	private String bka057;//用量	精确到小数点后2位
	private String bka058;//金额	精确到小数点后2位,负数时为退费
	private String aaz213;//费用序列号	
	private String aka036;//限制使用标志	0：否，1:是
	private String bka061;//伤病分离标志 1----因病 0----因伤
	private String aaz267;//限制使用标志   使用有限制使用条件的“五目”项目，符合限制使用条件的，本参数传 1；不符合限制使用条件的，本参数不上传或为空
	
	
	public String getBka063() {
		return bka063;
	}
	public void setBka063(String bka063) {
		this.bka063 = bka063;
	}
	public String getBka064() {
		return bka064;
	}
	public void setBka064(String bka064) {
		this.bka064 = bka064;
	}
	public String getAke005() {
		return ake005;
	}
	public void setAke005(String ake005) {
		this.ake005 = ake005;
	}
	public String getAke006() {
		return ake006;
	}
	public void setAke006(String ake006) {
		this.ake006 = ake006;
	}
	public String getBka051() {
		return bka051;
	}
	public void setBka051(String bka051) {
		this.bka051 = bka051;
	}
	public String getBka056() {
		return bka056;
	}
	public void setBka056(String bka056) {
		this.bka056 = bka056;
	}
	public String getBka057() {
		return bka057;
	}
	public void setBka057(String bka057) {
		this.bka057 = bka057;
	}
	public String getBka058() {
		return bka058;
	}
	public void setBka058(String bka058) {
		this.bka058 = bka058;
	}
	public String getAaz213() {
		return aaz213;
	}
	public void setAaz213(String aaz213) {
		this.aaz213 = aaz213;
	}
	public String getAka036() {
		return aka036;
	}
	public void setAka036(String aka036) {
		this.aka036 = aka036;
	}
	public String getBka061() {
		return bka061;
	}
	public void setBka061(String bka061) {
		this.bka061 = bka061;
	}
	public String getAaz267() {
		return aaz267;
	}
	public void setAaz267(String aaz267) {
		this.aaz267 = aaz267;
	}
}
