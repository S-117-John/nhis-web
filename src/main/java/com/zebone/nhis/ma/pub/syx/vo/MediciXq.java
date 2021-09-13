package com.zebone.nhis.ma.pub.syx.vo;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("medici_xq")
public class MediciXq {
	@XStreamImplicit
	private List<Xq> xqs;

	public List<Xq> getXqs() {
		if(xqs == null) xqs = new ArrayList<Xq>();
		return xqs;
	}

	public void setXqs(List<Xq> xqs) {
		this.xqs = xqs;
	}
	
}
