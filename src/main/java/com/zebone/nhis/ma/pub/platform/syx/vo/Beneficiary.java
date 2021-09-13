package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("beneficiary")
public class Beneficiary {
	@XStreamAsAttribute
	private String typeCode;
	@XStreamAsAttribute
	private String classCode;
	private Beneficiary beneficiary ;//自己嵌套自己的对象，无法初始化
	private Code code;
	
	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public Beneficiary getBeneficiary() {
		if(beneficiary==null) beneficiary=new Beneficiary();
		return beneficiary;
	}

	public void setBeneficiary(Beneficiary beneficiary) {
		this.beneficiary = beneficiary;
	}

	public Code getCode() {
		if(code==null)code=new Code();
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}


}
