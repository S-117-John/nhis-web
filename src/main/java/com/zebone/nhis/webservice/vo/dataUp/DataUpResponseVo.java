package com.zebone.nhis.webservice.vo.dataUp;


import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.zebone.nhis.webservice.vo.RespCommonVo;

@XmlRootElement(name = "res")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataUpResponseVo {
	/**
	 * 处理结果：0-成功
	 */
	@XmlElement(name = "resultCode")
    public String resultCode;
	
	/**
	 * 返回状态描述
	 */
	@XmlElement(name = "resultDesc")
    public String resultDesc;
	
	/**
	 * 门诊人次
	 */
	@XmlElement(name = "mzrs")
    public String mzrs;
	/**
	 * 急诊人次
	 */
	@XmlElement(name = "jzrs")
    public String jzrs;
	
	/**
	 * 门诊手术
	 */
	@XmlElement(name = "mzss")
    public String mzss;
	
	/**
	 * 处方数量
	 */
	@XmlElement(name = "cfsl")
    public String cfsl;
	
	/**
	 * 待诊人次
	 */
	@XmlElement(name = "dzrs")
    public String dzrs;
	
	/**
	 * 治疗单数
	 */
	@XmlElement(name = "zlds")
    public String zlds;
	
	/**
	 * 待取药
	 */
	@XmlElement(name = "dqys")
    public String dqys;
	
	/**
	 * 专家
	 */
	@XmlElement(name = "zj")
    public String zj;
	/**
	 * 普通
	 */
	@XmlElement(name = "pt")
    public String pt;
	
	/**
	 * 急诊处方数
	 */
	@XmlElement(name = "jzcf")
    public String jzcf;
	
	/**
	 * 抽血人次
	 */
	@XmlElement(name = "cxrs")
    public String cxrs;
	
	/**
	 * 输液人次
	 */
	@XmlElement(name = "syrs")
    public String syrs;
	
	/**
	 * 注射人次
	 */
	@XmlElement(name = "zsrs")
    public String zsrs;
	
	/**
	 * 预约人次
	 */
	@XmlElement(name = "yyrs")
    public String yyrs;
	
	@XmlElement(name = "items")
    public List<RespItemsVo> items;
	
	@XmlElement(name = "wardInfo")
    public List<RespWardVo> wardInfo;
	
	/**
	 * 在院人次
	 */
	@XmlElement(name = "zyrs")
    public String zyrs;
	/**
	 * 入院人次
	 */
	@XmlElement(name = "ryrs")
    public String ryrs;
	/**
	 * 出院人数
	 */
	@XmlElement(name = "cyrs")
    public String cyrs;
	/**
	 * 危重人数
	 */
	@XmlElement(name = "wzrs")
    public String wzrs;
	/**
	 * 手术人数
	 */
	@XmlElement(name = "ssrs")
    public String ssrs;
	/**
	 * 床位使用率(百分比%)
	 */
	@XmlElement(name = "cwsyl")
    public String cwsyl;
	/**
	 * 平均住院日
	 */
	@XmlElement(name = "pjzyr")
    public String pjzyr;
	
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultDesc() {
		return resultDesc;
	}
	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}
	public String getMzrs() {
		return mzrs;
	}
	public void setMzrs(String mzrs) {
		this.mzrs = mzrs;
	}
	public String getJzrs() {
		return jzrs;
	}
	public void setJzrs(String jzrs) {
		this.jzrs = jzrs;
	}
	public String getMzss() {
		return mzss;
	}
	public void setMzss(String mzss) {
		this.mzss = mzss;
	}
	public String getCfsl() {
		return cfsl;
	}
	public void setCfsl(String cfsl) {
		this.cfsl = cfsl;
	}
	public String getDzrs() {
		return dzrs;
	}
	public void setDzrs(String dzrs) {
		this.dzrs = dzrs;
	}
	public String getDqys() {
		return dqys;
	}
	public void setDqys(String dqys) {
		this.dqys = dqys;
	}
	public String getZj() {
		return zj;
	}
	public void setZj(String zj) {
		this.zj = zj;
	}
	public String getPt() {
		return pt;
	}
	public void setPt(String pt) {
		this.pt = pt;
	}
	public String getJzcf() {
		return jzcf;
	}
	public void setJzcf(String jzcf) {
		this.jzcf = jzcf;
	}
	public String getCxrs() {
		return cxrs;
	}
	public void setCxrs(String cxrs) {
		this.cxrs = cxrs;
	}
	public String getSyrs() {
		return syrs;
	}
	public void setSyrs(String syrs) {
		this.syrs = syrs;
	}
	public String getZsrs() {
		return zsrs;
	}
	public void setZsrs(String zsrs) {
		this.zsrs = zsrs;
	}
	public String getYyrs() {
		return yyrs;
	}
	public void setYyrs(String yyrs) {
		this.yyrs = yyrs;
	}
	public List<RespItemsVo> getItems() {
		return items;
	}
	public void setItems(List<RespItemsVo> items) {
		this.items = items;
	}
	public List<RespWardVo> getWardInfo() {
		return wardInfo;
	}
	public void setWardInfo(List<RespWardVo> wardInfo) {
		this.wardInfo = wardInfo;
	}
	public String getZyrs() {
		return zyrs;
	}
	public void setZyrs(String zyrs) {
		this.zyrs = zyrs;
	}
	public String getRyrs() {
		return ryrs;
	}
	public void setRyrs(String ryrs) {
		this.ryrs = ryrs;
	}
	public String getCyrs() {
		return cyrs;
	}
	public void setCyrs(String cyrs) {
		this.cyrs = cyrs;
	}
	public String getWzrs() {
		return wzrs;
	}
	public void setWzrs(String wzrs) {
		this.wzrs = wzrs;
	}
	public String getSsrs() {
		return ssrs;
	}
	public void setSsrs(String ssrs) {
		this.ssrs = ssrs;
	}
	public String getCwsyl() {
		return cwsyl;
	}
	public void setCwsyl(String cwsyl) {
		this.cwsyl = cwsyl;
	}
	public String getPjzyr() {
		return pjzyr;
	}
	public void setPjzyr(String pjzyr) {
		this.pjzyr = pjzyr;
	}
	public String getZlds() {
		return zlds;
	}
	public void setZlds(String zlds) {
		this.zlds = zlds;
	}
	
	
}
