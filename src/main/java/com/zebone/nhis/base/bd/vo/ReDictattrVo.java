package com.zebone.nhis.base.bd.vo;

import java.util.ArrayList;
import java.util.List;

public class ReDictattrVo {

	private DictattrVo dictattrVo = new DictattrVo();
	
	private List<DictattrVo> updateDictattr = new ArrayList<DictattrVo>(); 
	
	private List<DictattrVo> delDictattr = new ArrayList<DictattrVo>(); 
	
	private List<DictattrVo> newDictattr = new ArrayList<DictattrVo>(); 

	public List<DictattrVo> getUpdateDictattr() {
		return updateDictattr;
	}

	public void setUpdateDictattr(List<DictattrVo> updateDictattr) {
		this.updateDictattr = updateDictattr;
	}

	public List<DictattrVo> getDelDictattr() {
		return delDictattr;
	}

	public void setDelDictattr(List<DictattrVo> delDictattr) {
		this.delDictattr = delDictattr;
	}

	public List<DictattrVo> getNewDictattr() {
		return newDictattr;
	}

	public void setNewDictattr(List<DictattrVo> newDictattr) {
		this.newDictattr = newDictattr;
	}

	public DictattrVo getDictattrVo() {
		return dictattrVo;
	}

	public void setDictattrVo(DictattrVo dictattrVo) {
		this.dictattrVo = dictattrVo;
	}
}
