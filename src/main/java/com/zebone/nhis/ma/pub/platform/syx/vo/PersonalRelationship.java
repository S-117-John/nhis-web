package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("personalRelationship")
public class PersonalRelationship {
	private Code code ;
	private Telecom telecom;
	private RelationshipHolder1 relationshipHolder1;

	public Code getCode() {
		if(code==null) code=new Code();
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public Telecom getTelecom() {
		if(telecom==null) telecom=new Telecom();
		return telecom;
	}

	public void setTelecom(Telecom telecom) {
		this.telecom = telecom;
	}

	public RelationshipHolder1 getRelationshipHolder1() {
		if(relationshipHolder1==null) relationshipHolder1=new RelationshipHolder1();
		return relationshipHolder1;
	}

	public void setRelationshipHolder1(RelationshipHolder1 relationshipHolder1) {
		this.relationshipHolder1 = relationshipHolder1;
	}

}
