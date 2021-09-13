package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bl.charge;

public class BlPriceOverride {
	
    private Double value;

    public BlPriceOverride(Double value) {
        this.value = value;
    }
    
	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

}