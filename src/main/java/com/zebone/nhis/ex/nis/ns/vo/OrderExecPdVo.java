package com.zebone.nhis.ex.nis.ns.vo;

import java.util.List;

public class OrderExecPdVo {
	private Integer eu_medicontr;  		//废弃（精神毒麻）
	private String pk_pd;				//物品主键
	private String pk_unit_med;		//医学单位主键
	private String pk_unit_base;		//基本单位主键
	private String pk_unit_cur;		//基本单位主键
	private String factor_cb;			//当前单位，基本单位比值
	private String factor_fb;			//主计量单位，基本单位比值
	private String factor_mb;			//医学单位，基本单位比值
	private String measrate;			//主计量单位，当前单位比值
	private double price_s;			//售价
	private double price_p;			//进价
	private List<Double> exce_quan;	//单次使用量列表（为了配合执行信息）    --当前单位
	private String dt_pois;				//毒麻
	private Integer eu_val;				//是否贵重
	private Integer eu_muputype;		//包装取整
	
	private String medname;				//物品名称
	private String spec;		//规格
	private String name_unit;		//当前单位名称
	public Integer getEu_medicontr() {
		return eu_medicontr;
	}
	public void setEu_medicontr(Integer eu_medicontr) {
		this.eu_medicontr = eu_medicontr;
	}
	public String getPk_pd() {
		return pk_pd;
	}
	public void setPk_pd(String pk_pd) {
		this.pk_pd = pk_pd;
	}
	
	public String getFactor_cb() {
		return factor_cb;
	}
	public void setFactor_cb(String factor_cb) {
		this.factor_cb = factor_cb;
	}
	public String getFactor_fb() {
		return factor_fb;
	}
	public void setFactor_fb(String factor_fb) {
		this.factor_fb = factor_fb;
	}
	public String getFactor_mb() {
		return factor_mb;
	}
	public void setFactor_mb(String factor_mb) {
		this.factor_mb = factor_mb;
	}
	public String getMeasrate() {
		return measrate;
	}
	public void setMeasrate(String measrate) {
		this.measrate = measrate;
	}
	public double getPrice_s() {
		return price_s;
	}
	public void setPrice_s(double price_s) {
		this.price_s = price_s;
	}
	public double getPrice_p() {
		return price_p;
	}
	public void setPrice_p(double price_p) {
		this.price_p = price_p;
	}
	public List<Double> getExce_quan() {
		return exce_quan;
	}
	public void setExce_quan(List<Double> exce_quan) {
		this.exce_quan = exce_quan;
	}
	public String getDt_pois() {
		return dt_pois;
	}
	public void setDt_pois(String dt_pois) {
		this.dt_pois = dt_pois;
	}
	public Integer getEu_val() {
		return eu_val;
	}
	public void setEu_val(Integer eu_val) {
		this.eu_val = eu_val;
	}
	public Integer getEu_muputype() {
		return eu_muputype;
	}
	public void setEu_muputype(Integer eu_muputype) {
		this.eu_muputype = eu_muputype;
	}
	public String getMedname() {
		return medname;
	}
	public void setMedname(String medname) {
		this.medname = medname;
	}
	public String getPk_unit_med() {
		return pk_unit_med;
	}
	public void setPk_unit_med(String pk_unit_med) {
		this.pk_unit_med = pk_unit_med;
	}
	public String getPk_unit_base() {
		return pk_unit_base;
	}
	public void setPk_unit_base(String pk_unit_base) {
		this.pk_unit_base = pk_unit_base;
	}
	public String getPk_unit_cur() {
		return pk_unit_cur;
	}
	public void setPk_unit_cur(String pk_unit_cur) {
		this.pk_unit_cur = pk_unit_cur;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getName_unit() {
		return name_unit;
	}
	public void setName_unit(String name_unit) {
		this.name_unit = name_unit;
	}
	
	
	
	
}
