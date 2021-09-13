package com.zebone.nhis.bl.pub.syx.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.bl.pub.vo.InvInfoVo;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;

public class BlInvSearch {
	
	private String pkPv;
	
	private Date dateBegin;
	
	private Date dateEnd;
	
	private List<String> pkCgips;
	
	private String flagHbPrint;
	
	private List<InvInfoVo> invos;
	
	private String dtSttype;
	
	//返回值参数
	private List<BlInvoice> invs = new ArrayList<BlInvoice>();
	private List<BlInvoiceDt> invDts = new ArrayList<BlInvoiceDt>();
	private Double cezf;//超额自费药
	private String flagGy;//是否是广州公医
	
	
	public String getDtSttype() {
		return dtSttype;
	}

	public void setDtSttype(String dtSttype) {
		this.dtSttype = dtSttype;
	}

	public Double getCezf() {
		return cezf;
	}

	public void setCezf(Double cezf) {
		this.cezf = cezf;
	}

	public String getFlagGy() {
		return flagGy;
	}

	public void setFlagGy(String flagGy) {
		this.flagGy = flagGy;
	}

	public List<BlInvoice> getInvs() {
		return invs;
	}

	public void setInvs(List<BlInvoice> invs) {
		this.invs = invs;
	}

	public List<BlInvoiceDt> getInvDts() {
		return invDts;
	}

	public void setInvDts(List<BlInvoiceDt> invDts) {
		this.invDts = invDts;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
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

	public List<String> getPkCgips() {
		return pkCgips;
	}

	public void setPkCgips(List<String> pkCgips) {
		this.pkCgips = pkCgips;
	}

	public String getFlagHbPrint() {
		return flagHbPrint;
	}

	public void setFlagHbPrint(String flagHbPrint) {
		this.flagHbPrint = flagHbPrint;
	}

	public List<InvInfoVo> getInvos() {
		return invos;
	}

	public void setInvos(List<InvInfoVo> invos) {
		this.invos = invos;
	}
}
