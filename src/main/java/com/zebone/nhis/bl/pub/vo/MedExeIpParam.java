package com.zebone.nhis.bl.pub.vo;

import java.util.Date;

import com.zebone.nhis.common.module.ex.nis.ns.ExOrderOcc;

public class MedExeIpParam extends BlPubParamVo {

	private static final long serialVersionUID = 1L;
	private ExOrderOcc exOrdOcc ;
	private String flagBl;
	private String Note;
	private Date dateOcc;
	
	public Date getDateOcc() {
		return dateOcc;
	}

	public void setDateOcc(Date dateOcc) {
		this.dateOcc = dateOcc;
	}

	public String getNote() {
		return Note;
	}

	public void setNote(String note) {
		Note = note;
	}

	public ExOrderOcc getExOrdOcc() {
		return exOrdOcc;
	}

	public void setExOrdOcc(ExOrderOcc exOrdOcc) {
		this.exOrdOcc = exOrdOcc;
	}

	public String getFlagBl() {
		return flagBl;
	}

	public void setFlagBl(String flagBl) {
		this.flagBl = flagBl;
	}	

}
