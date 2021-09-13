package com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo;

import java.util.List;

/**
 * 月结算出入参
 * @author zim
 *
 */
public class MonthSettleData {

	private String tjlb; //统计类别
	
	private String tjjzrq; //统计截止日期
	
	private String fhz;//返回值
	
	private String msg;//返回信息
	
	private String jjywh;//交接业务号
	
	//private List<InsZsybRpt> insZsybRptList; //医保月结报表
	
	private List<InsZsBaYbRptMz> insZsybRptMzList;// 1：中山医保门诊月结明细   
	
	private List<InsZsBaYbRptZy> insZsybRptZyList; //2 : 中山医保住院医疗月结明细
	
	private List<InsZsBaYbRptLx> insZsybRptLxList; //4:中山医保离休月结明细
	
	private List<InsZsBaYbRptTdbz> insZsybRptTdbzList;//6:中山医保特定病种月结明细
	
	private List<InsZsBaYbRptGsmz> insZsybRptGsmzList;//9:中山医保工伤门诊月结明细
	
	private List<InsZsBaYbRptGszy> insZsybRptGszyList;//10:中山医保工伤住院月结明细
	
	//11:工伤康复门诊报表
	
	//12:工伤康复住院报表
	
	private List<InsZsBaYbRptSyzy>  insZsybRptSyzyList;//13: 中山医保生育住院月结明细
	
	private List<InsZsBaYbRptMzsd> insZsybRptMzsdList;//81:外部医保-中山医保民政双低月结明细
	
	private List<InsZsBaYbRptMzyf> insZsybRptMzyfList;//82:中山医保民政优抚月结明细(重点优抚人员)  83(一到六级残疾军人特殊医疗)
	
	private List<InsZsBaYbRptMzsdMz> insZsybRptMzsdMzList;//84：中山医保民政双低普通门诊月结明细
	
	//85：民政特殊医疗优抚对象普通门诊
	
	//86: 民政重点优抚对象普通门诊
	
	private List<InsZsBaYbRptSyzybx> insZsybRptSyzybxList;//71:中山医保生育保险住院月结明细
	
	private List<InsZsBaYbRptJhsy> insZsybRptJhsyList;//72：中山医保计划生育月结明细
	
	private List<InsZsBaYbRptCqjc> insZsybRptCqjcList; //73:中山医保产前检查月结明细

	public String getTjlb() {
		return tjlb;
	}

	public void setTjlb(String tjlb) {
		this.tjlb = tjlb;
	}

	public String getTjjzrq() {
		return tjjzrq;
	}

	public void setTjjzrq(String tjjzrq) {
		this.tjjzrq = tjjzrq;
	}

	public String getFhz() {
		return fhz;
	}

	public void setFhz(String fhz) {
		this.fhz = fhz;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getJjywh() {
		return jjywh;
	}

	public void setJjywh(String jjywh) {
		this.jjywh = jjywh;
	}

	public List<InsZsBaYbRptMz> getInsZsybRptMzList() {
		return insZsybRptMzList;
	}

	public void setInsZsybRptMzList(List<InsZsBaYbRptMz> insZsybRptMzList) {
		this.insZsybRptMzList = insZsybRptMzList;
	}

	public List<InsZsBaYbRptZy> getInsZsybRptZyList() {
		return insZsybRptZyList;
	}

	public void setInsZsybRptZyList(List<InsZsBaYbRptZy> insZsybRptZyList) {
		this.insZsybRptZyList = insZsybRptZyList;
	}

	public List<InsZsBaYbRptLx> getInsZsybRptLxList() {
		return insZsybRptLxList;
	}

	public void setInsZsybRptLxList(List<InsZsBaYbRptLx> insZsybRptLxList) {
		this.insZsybRptLxList = insZsybRptLxList;
	}

	public List<InsZsBaYbRptTdbz> getInsZsybRptTdbzList() {
		return insZsybRptTdbzList;
	}

	public void setInsZsybRptTdbzList(List<InsZsBaYbRptTdbz> insZsybRptTdbzList) {
		this.insZsybRptTdbzList = insZsybRptTdbzList;
	}

	public List<InsZsBaYbRptGsmz> getInsZsybRptGsmzList() {
		return insZsybRptGsmzList;
	}

	public void setInsZsybRptGsmzList(List<InsZsBaYbRptGsmz> insZsybRptGsmzList) {
		this.insZsybRptGsmzList = insZsybRptGsmzList;
	}

	public List<InsZsBaYbRptGszy> getInsZsybRptGszyList() {
		return insZsybRptGszyList;
	}

	public void setInsZsybRptGszyList(List<InsZsBaYbRptGszy> insZsybRptGszyList) {
		this.insZsybRptGszyList = insZsybRptGszyList;
	}

	public List<InsZsBaYbRptSyzy> getInsZsybRptSyzyList() {
		return insZsybRptSyzyList;
	}

	public void setInsZsybRptSyzyList(List<InsZsBaYbRptSyzy> insZsybRptSyzyList) {
		this.insZsybRptSyzyList = insZsybRptSyzyList;
	}

	public List<InsZsBaYbRptMzsd> getInsZsybRptMzsdList() {
		return insZsybRptMzsdList;
	}

	public void setInsZsybRptMzsdList(List<InsZsBaYbRptMzsd> insZsybRptMzsdList) {
		this.insZsybRptMzsdList = insZsybRptMzsdList;
	}

	public List<InsZsBaYbRptMzyf> getInsZsybRptMzyfList() {
		return insZsybRptMzyfList;
	}

	public void setInsZsybRptMzyfList(List<InsZsBaYbRptMzyf> insZsybRptMzyfList) {
		this.insZsybRptMzyfList = insZsybRptMzyfList;
	}

	public List<InsZsBaYbRptMzsdMz> getInsZsybRptMzsdMzList() {
		return insZsybRptMzsdMzList;
	}

	public void setInsZsybRptMzsdMzList(List<InsZsBaYbRptMzsdMz> insZsybRptMzsdMzList) {
		this.insZsybRptMzsdMzList = insZsybRptMzsdMzList;
	}

	public List<InsZsBaYbRptSyzybx> getInsZsybRptSyzybxList() {
		return insZsybRptSyzybxList;
	}

	public void setInsZsybRptSyzybxList(List<InsZsBaYbRptSyzybx> insZsybRptSyzybxList) {
		this.insZsybRptSyzybxList = insZsybRptSyzybxList;
	}

	public List<InsZsBaYbRptJhsy> getInsZsybRptJhsyList() {
		return insZsybRptJhsyList;
	}

	public void setInsZsybRptJhsyList(List<InsZsBaYbRptJhsy> insZsybRptJhsyList) {
		this.insZsybRptJhsyList = insZsybRptJhsyList;
	}

	public List<InsZsBaYbRptCqjc> getInsZsybRptCqjcList() {
		return insZsybRptCqjcList;
	}

	public void setInsZsybRptCqjcList(List<InsZsBaYbRptCqjc> insZsybRptCqjcList) {
		this.insZsybRptCqjcList = insZsybRptCqjcList;
	}
	
	
}
