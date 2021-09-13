package com.zebone.nhis.arch.vo;

import java.util.List;

import com.zebone.nhis.common.module.arch.BdArchPrtcate;
import com.zebone.nhis.common.module.arch.BdArchPrtcateDoctype;

public class ArchPrtcateForSave {
	
	private BdArchPrtcate prtcate;
	
	private List<BdArchPrtcateDoctype> types;

	public BdArchPrtcate getPrtcate() {
		return prtcate;
	}

	public void setPrtcate(BdArchPrtcate prtcate) {
		this.prtcate = prtcate;
	}

	public List<BdArchPrtcateDoctype> getTypes() {
		return types;
	}

	public void setTypes(List<BdArchPrtcateDoctype> types) {
		this.types = types;
	}
	
}
