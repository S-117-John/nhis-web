package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bl.charge;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Identifier;

import java.util.List;

public class BlEncounter {
	
    private Identifier identifier;
    
    @JSONField(name = "class")
    private BlClas clas;
    
    private BlLocation location;
    
    private List<BlDiagnosis> diagnosis;

    public Identifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

	public BlClas getClas() {
		return clas;
	}

	public List<BlDiagnosis> getDiagnosis() {
		return diagnosis;
	}

	public void setClas(BlClas clas) {
		this.clas = clas;
	}

	public void setDiagnosis(List<BlDiagnosis> diagnosis) {
		this.diagnosis = diagnosis;
	}

	public BlLocation getLocation() {
		return location;
	}

	public void setLocation(BlLocation location) {
		this.location = location;
	}
	
}