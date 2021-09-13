package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("asContent")
public class AsContent {
	@XStreamAsAttribute
	private String classCode;
	
	private Quantity quantity;
	private ContainerPackagedProduct containerPackagedProduct;
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public Quantity getQuantity() {
		if(quantity==null) quantity=new Quantity();
		return quantity;
	}
	public void setQuantity(Quantity quantity) {
		this.quantity = quantity;
	}
	public ContainerPackagedProduct getContainerPackagedProduct() {
		if(containerPackagedProduct==null) containerPackagedProduct=new ContainerPackagedProduct();
		return containerPackagedProduct;
	}
	public void setContainerPackagedProduct(
			ContainerPackagedProduct containerPackagedProduct) {
		this.containerPackagedProduct = containerPackagedProduct;
	}
	
}
