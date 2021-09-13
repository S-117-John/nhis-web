package com.zebone.nhis.base.bd.vo;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.type.TypeReference;

import com.zebone.nhis.common.module.base.bd.res.BdResPc;
import com.zebone.nhis.common.module.base.bd.res.BdResPcArgu;
import com.zebone.nhis.common.module.base.bd.res.BdResPcDept;
import com.zebone.platform.modules.utils.JsonUtil;

public class PcAndArgusParam {

	/**
	 * 计算机工作站
	 */
	private BdResPc pc = new BdResPc();
	
	/**
	 * 工作站参数
	 */
	private List<BdResPcArgu> pcargus = new ArrayList<BdResPcArgu>();
	
	/*
	 * 工作站分诊台
	 */
	private List<BdResPcDept> pcDepts = new ArrayList<BdResPcDept>();

	public List<BdResPcDept> getPcDepts() {
		return pcDepts;
	}

	public void setPcDepts(List<BdResPcDept> pcDepts) {
		this.pcDepts = pcDepts;
	}

	public BdResPc getPc() {
		return pc;
	}

	public void setPc(BdResPc pc) {
		this.pc = pc;
	}

	public List<BdResPcArgu> getPcargus() {
		return pcargus;
	}

	public void setPcargus(List<BdResPcArgu> pcargus) {
		this.pcargus = pcargus;
	}
}
