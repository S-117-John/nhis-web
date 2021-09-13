package com.zebone.nhis.cn.opdw.vo;

import java.util.List;

import com.zebone.nhis.common.module.cn.ipdw.CnOrdHerb;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.pi.PatInfo;

public class SyxOpHerbInfo {
	
	private PatInfo patInfo;
	private CnOrder order;
	private List<CnOrdHerb> herbs;
	private List<CnOrdHerb> delHerbs;
	private String nameDiag;
	private String nameSymp;
	private String pkDiag;
	private String codeDiag;
	private String pkSymp;
	private String codeSymp;
	//处方表备注Note
	private String presNote;
	/**
	 * 草药代煎费用逻辑，需要匹配医院。0：默认逻辑；1：代表人民医院
	 */
	private String djf;

	
	public PatInfo getPatInfo() {
		return patInfo;
	}
	public void setPatInfo(PatInfo patInfo) {
		this.patInfo = patInfo;
	}
	public CnOrder getOrder() {
		return order;
	}
	public void setOrder(CnOrder order) {
		this.order = order;
	}
	public List<CnOrdHerb> getHerbs() {
		return herbs;
	}
	public void setHerbs(List<CnOrdHerb> herbs) {
		this.herbs = herbs;
	}
	public List<CnOrdHerb> getDelHerbs() {
		return delHerbs;
	}
	public void setDelHerbs(List<CnOrdHerb> delHerbs) {
		this.delHerbs = delHerbs;
	}

	public String getNameDiag() {
		return nameDiag;
	}

	public void setNameDiag(String nameDiag) {
		this.nameDiag = nameDiag;
	}

	public String getNameSymp() {
		return nameSymp;
	}

	public void setNameSymp(String nameSymp) {
		this.nameSymp = nameSymp;
	}

	public String getPkDiag() {
		return pkDiag;
	}

	public void setPkDiag(String pkDiag) {
		this.pkDiag = pkDiag;
	}

	public String getCodeDiag() {
		return codeDiag;
	}

	public void setCodeDiag(String codeDiag) {
		this.codeDiag = codeDiag;
	}

	public String getPkSymp() {
		return pkSymp;
	}

	public void setPkSymp(String pkSymp) {
		this.pkSymp = pkSymp;
	}

	public String getCodeSymp() {
		return codeSymp;
	}

	public void setCodeSymp(String codeSymp) {
		this.codeSymp = codeSymp;
	}

	public String getPresNote() {
		return presNote;
	}

	public void setPresNote(String presNote) {
		this.presNote = presNote;
	}

	public String getDjf() {
		return djf;
	}

	public void setDjf(String djf) {
		this.djf = djf;
	}
}
