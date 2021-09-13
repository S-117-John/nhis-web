package com.zebone.nhis.cn.ipdw.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.cn.ipdw.CnLabApply;
import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;
import com.zebone.nhis.common.module.cn.ipdw.CnSignCa;

public class ApplyCanlParam {

	public List<String> pkCnOrds = new ArrayList<String>();
	public List<CnRisApplyVo> saveRisList = new ArrayList<CnRisApplyVo>();
	public List<CnLabApplyVo> saveLisList = new ArrayList<CnLabApplyVo>();
	List<CnSignCa> cnSignCaList = new ArrayList<CnSignCa>();
	public List<String> getPkCnOrds() {
		return pkCnOrds;
	}
	public void setPkCnOrds(List<String> pkCnOrds) {
		this.pkCnOrds = pkCnOrds;
	}
	public List<CnRisApplyVo> getSaveRisList() {
		return saveRisList;
	}
	public void setSaveRisList(List<CnRisApplyVo> saveRisList) {
		this.saveRisList = saveRisList;
	}
	public List<CnLabApplyVo> getSaveLisList() {
		return saveLisList;
	}
	public void setSaveLisList(List<CnLabApplyVo> saveLisList) {
		this.saveLisList = saveLisList;
	}
	public List<CnSignCa> getCnSignCaList() {
		return cnSignCaList;
	}
	public void setCnSignCaList(List<CnSignCa> cnSignCaList) {
		this.cnSignCaList = cnSignCaList;
	}
	
}
