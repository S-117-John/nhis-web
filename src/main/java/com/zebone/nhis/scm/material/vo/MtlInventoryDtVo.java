package com.zebone.nhis.scm.material.vo;

import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.scm.st.PdInvDetail;
import com.zebone.nhis.common.module.scm.st.PdInvSingle;


public class MtlInventoryDtVo extends PdInvDetail{
    private String pdcode;
    private String pdname;
    private String spec;
    private String factory;
    private Date dateFac;
    private String unit;
    private Double amtAcc;//账面金额
    private Double amt;//实盘金额（零售金额）
    private Double amtCost;//成本金额
    private String spcode;
    private Integer packSizePd;//零售包装量
    private String flagSingle;//单品标志
    
    private List<PdInvSingle> sinList;//单品记录
    
	public Integer getPackSizePd() {
		return packSizePd;
	}
	public void setPackSizePd(Integer packSizePd) {
		this.packSizePd = packSizePd;
	}
	public String getSpcode() {
		return spcode;
	}
	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}
	public Date getDateFac() {
		return dateFac;
	}
	public void setDateFac(Date dateFac) {
		this.dateFac = dateFac;
	}
	public Double getAmtCost() {
		return amtCost;
	}
	public void setAmtCost(Double amtCost) {
		this.amtCost = amtCost;
	}
	public Double getAmtAcc() {
		return amtAcc;
	}
	public void setAmtAcc(Double amtAcc) {
		this.amtAcc = amtAcc;
	}
	public Double getAmt() {
		return amt;
	}
	public void setAmt(Double amt) {
		this.amt = amt;
	}
	public String getPdcode() {
		return pdcode;
	}
	public void setPdcode(String pdcode) {
		this.pdcode = pdcode;
	}
	public String getPdname() {
		return pdname;
	}
	public void setPdname(String pdname) {
		this.pdname = pdname;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public List<PdInvSingle> getSinList() {
		return sinList;
	}
	public void setSinList(List<PdInvSingle> sinList) {
		this.sinList = sinList;
	}
	public String getFlagSingle() {
		return flagSingle;
	}
	public void setFlagSingle(String flagSingle) {
		this.flagSingle = flagSingle;
	}
    
    
}
