package com.zebone.nhis.cn.ipdw.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.cn.cp.BdCpExp;

public class CpExpVo {
	private List<BdCpExp>  cpexpList = new ArrayList<BdCpExp>();
    private List<BdCpExp> cpexpListDel = new ArrayList<BdCpExp>();
	public List<BdCpExp> getCpexpList() {
		return cpexpList;
	}
	public void setCpexpList(List<BdCpExp> cpexpList) {
		this.cpexpList = cpexpList;
	}
	public List<BdCpExp> getCpexpListDel() {
		return cpexpListDel;
	}
	public void setCpexpListDel(List<BdCpExp> cpexpListDel) {
		this.cpexpListDel = cpexpListDel;
	}

}
