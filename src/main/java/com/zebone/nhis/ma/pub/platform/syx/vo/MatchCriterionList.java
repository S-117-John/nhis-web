package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("matchCriterionList")
public class MatchCriterionList {
	private MinimumDegreeMatch minimumDegreeMatch;

	public MinimumDegreeMatch getMinimumDegreeMatch() {
		if(minimumDegreeMatch==null)minimumDegreeMatch=new MinimumDegreeMatch();
		return minimumDegreeMatch;
	}

	public void setMinimumDegreeMatch(MinimumDegreeMatch minimumDegreeMatch) {
		this.minimumDegreeMatch = minimumDegreeMatch;
	}
	
}
