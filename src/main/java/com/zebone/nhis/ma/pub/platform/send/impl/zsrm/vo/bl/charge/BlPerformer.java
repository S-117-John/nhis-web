package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bl.charge;

public class BlPerformer {
	
    private BlLocation actor;

    public BlPerformer(BlLocation actor) {
        this.actor = actor;
    }
    
	public BlLocation getActor() {
		return actor;
	}
	
	public void setActor(BlLocation actor) {
		this.actor = actor;
	}

}