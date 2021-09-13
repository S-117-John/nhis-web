package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("asCitizen")
public class AsCitizen {
	
	private PoliticalNation politicalNation;

	public PoliticalNation getPoliticalNation() {
		if(politicalNation == null)politicalNation = new PoliticalNation();
		return politicalNation;
	}

	public void setPoliticalNation(PoliticalNation politicalNation) {
		this.politicalNation = politicalNation;
	}
	
	
}
