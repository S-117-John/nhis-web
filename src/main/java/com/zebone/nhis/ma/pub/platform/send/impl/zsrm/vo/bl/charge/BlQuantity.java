package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bl.charge;


public class BlQuantity {
    public BlQuantity(){}
    public BlQuantity(Double value,String unit) {
        this.value = value;
        this.unit = unit;
    }

    private String unit;
    private Double value;
    
	public String getUnit() {
		return unit;
	}
	public Double getValue() {
		return value;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public void setValue(Double value) {
		this.value = value;
	}
    
}