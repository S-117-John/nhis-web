package com.zebone.nhis.base.bd.vo;

import java.util.List;
import java.util.Set;

import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;

public class BdDefdocSaveParam {
	
	private String codeDefdoclist;
	
	private List<BdDefdoc> bdDefdoc;
	
	private List<String> delPkDefdocs;	//删除的公共字典信息主键

	public Set<String> getUpdatePkDefdocs() {
		return updatePkDefdocs;
	}

	public void setUpdatePkDefdocs(Set<String> updatePkDefdocs) {
		this.updatePkDefdocs = updatePkDefdocs;
	}

	private Set<String> updatePkDefdocs;//修改的公共字典信息主键



	public List<String> getDelPkDefdocs() {
		return delPkDefdocs;
	}

	public void setDelPkDefdocs(List<String> delPkDefdocs) {
		this.delPkDefdocs = delPkDefdocs;
	}

	public String getCodeDefdoclist() {
		return codeDefdoclist;
	}

	public void setCodeDefdoclist(String codeDefdoclist) {
		this.codeDefdoclist = codeDefdoclist;
	}

	public List<BdDefdoc> getBdDefdoc() {
		return bdDefdoc;
	}

	public void setBdDefdoc(List<BdDefdoc> bdDefdoc) {
		this.bdDefdoc = bdDefdoc;
	}

	
	

}
