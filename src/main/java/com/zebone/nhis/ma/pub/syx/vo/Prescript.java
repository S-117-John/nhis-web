package com.zebone.nhis.ma.pub.syx.vo;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("prescript")
public class Prescript {
	@XStreamImplicit
	private List<Pdetail> pdetails;

	public List<Pdetail> getPdetails() {
		if(pdetails == null)pdetails = new ArrayList<Pdetail>();
		return pdetails;
	}

	public void setPdetails(List<Pdetail> pdetails) {
		this.pdetails = pdetails;
	}
	
	
}
