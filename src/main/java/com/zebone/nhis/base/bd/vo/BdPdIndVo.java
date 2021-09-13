package com.zebone.nhis.base.bd.vo;

import java.util.List;

import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.common.module.scm.pub.BdPdInd;
import com.zebone.nhis.common.module.scm.pub.BdPdIndhp;
import com.zebone.nhis.common.module.scm.pub.BdPdIndpd;

public class BdPdIndVo{
	
	private BdPdInd bdPdInd;
	
	private List<BdPdIndpd> pdList;
	
	private List<BdPdIndhp> hpList;
	
	private List<BdPdIndpd> bdpds;
	
	private String chkCodeInd;
	
	private String chkNameInd;
	
	public String getChkCodeInd() {
		return chkCodeInd;
	}

	public void setChkCodeInd(String chkCodeInd) {
		this.chkCodeInd = chkCodeInd;
	}

	public String getChkNameInd() {
		return chkNameInd;
	}

	public void setChkNameInd(String chkNameInd) {
		this.chkNameInd = chkNameInd;
	}

	public BdPdInd getBdPdInd() {
		return bdPdInd;
	}

	public void setBdPdInd(BdPdInd bdPdInd) {
		this.bdPdInd = bdPdInd;
	}

	public List<BdPdIndhp> getHpList() {
		return hpList;
	}

	public void setHpList(List<BdPdIndhp> hpList) {
		this.hpList = hpList;
	}

	public List<BdPdIndpd> getPdList() {
		return pdList;
	}

	public void setPdList(List<BdPdIndpd> pdList) {
		this.pdList = pdList;
	}

	public List<BdPdIndpd> getBdpds() {
		return bdpds;
	}

	public void setBdpds(List<BdPdIndpd> bdpds) {
		this.bdpds = bdpds;
	}

}
