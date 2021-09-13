package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("manufacturedProduct1")
public class ManufacturedProduct1 {
	@XStreamAsAttribute
	private String classCode;
	private Id id;
	private ManufacturedProduct manufacturedProduct;
	
	private SubjectOf3 subjectOf3;
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public Id getId() {
		if(id==null) id=new Id();
		return id;
	}
	public void setId(Id id) {
		this.id = id;
	}
	public ManufacturedProduct getManufacturedProduct() {
		if(manufacturedProduct==null) manufacturedProduct=new ManufacturedProduct();
		return manufacturedProduct;
	}
	public void setManufacturedProduct(ManufacturedProduct manufacturedProduct) {
		this.manufacturedProduct = manufacturedProduct;
	}
	public SubjectOf3 getSubjectOf3() {
		if(subjectOf3==null) subjectOf3=new SubjectOf3();
		return subjectOf3;
	}
	public void setSubjectOf3(SubjectOf3 subjectOf3) {
		this.subjectOf3 = subjectOf3;
	}
	
}
