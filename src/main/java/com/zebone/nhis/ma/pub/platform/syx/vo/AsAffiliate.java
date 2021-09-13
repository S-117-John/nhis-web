package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("asAffiliate")
public class AsAffiliate {
	@XStreamAsAttribute
	private String classCode;
	private Code code;
	private EffectiveTime effectiveTime;
	private Scoper2 scoper2;
	private AffiliatedPrincipalOrganization affiliatedPrincipalOrganization;
	
	
	public AffiliatedPrincipalOrganization getAffiliatedPrincipalOrganization() {
		if(affiliatedPrincipalOrganization == null) {
			affiliatedPrincipalOrganization  = new AffiliatedPrincipalOrganization();
		}
		return affiliatedPrincipalOrganization;
	}
	public void setAffiliatedPrincipalOrganization(AffiliatedPrincipalOrganization affiliatedPrincipalOrganization) {
		this.affiliatedPrincipalOrganization = affiliatedPrincipalOrganization;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public Code getCode() {
		if(code==null) code=new Code();
		return code;
	}
	public void setCode(Code code) {
		this.code = code;
	}
	public EffectiveTime getEffectiveTime() {
		if(effectiveTime==null) effectiveTime=new EffectiveTime();
		return effectiveTime;
	}
	public void setEffectiveTime(EffectiveTime effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
	public Scoper2 getScoper2() {
		if(scoper2==null) scoper2=new Scoper2();
		return scoper2;
	}
	public void setScoper2(Scoper2 scoper2) {
		this.scoper2 = scoper2;
	}
	
	
}
