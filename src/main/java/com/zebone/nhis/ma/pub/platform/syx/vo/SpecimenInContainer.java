package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("specimenInContainer")
public class SpecimenInContainer {
	@XStreamAsAttribute
	private String classCode;
	
	private ContainerAdditiveMaterial containerAdditiveMaterial;

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public ContainerAdditiveMaterial getContainerAdditiveMaterial() {
		if(containerAdditiveMaterial== null) containerAdditiveMaterial =new ContainerAdditiveMaterial();
		return containerAdditiveMaterial;
	}

	public void setContainerAdditiveMaterial(
			ContainerAdditiveMaterial containerAdditiveMaterial) {
		this.containerAdditiveMaterial = containerAdditiveMaterial;
	}
	
	
}
