package com.zebone.nhis.pro.zsba.mz.opcg.vo;

import java.util.List;

import com.zebone.nhis.common.module.cn.ipdw.CnOrdHerb;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnPrescription;

public class OpOrdInsteadEntryDto {

	private List<OpCgCnOrder> cnOrders;

	private List<CnOrdHerb> cnOrdHerbs;

	private List<CnOrdHerb> cnOrdHerbDeletes;

	private CnPrescription opPres;

	private String pkCnord;

	private CnOrder cnOrder;

	private String pkPres;

	private String pkPv;

	private String codeOrdtype;

	public String getPkPv() {

		return pkPv;
	}

	public void setPkPv(String pkPv) {

		this.pkPv = pkPv;
	}

	public String getPkPres() {

		return pkPres;
	}

	public void setPkPres(String pkPres) {

		this.pkPres = pkPres;
	}

	public List<OpCgCnOrder> getCnOrders() {

		return cnOrders;
	}

	public void setCnOrders(List<OpCgCnOrder> cnOrders) {

		this.cnOrders = cnOrders;
	}

	public CnPrescription getOpPres() {

		return opPres;
	}

	public void setOpPres(CnPrescription opPres) {

		this.opPres = opPres;
	}

	public List<CnOrdHerb> getCnOrdHerbs() {

		return cnOrdHerbs;
	}

	public void setCnOrdHerbs(List<CnOrdHerb> cnOrdHerbs) {

		this.cnOrdHerbs = cnOrdHerbs;
	}

	public CnOrder getCnOrder() {

		return cnOrder;
	}

	public void setCnOrder(CnOrder cnOrder) {

		this.cnOrder = cnOrder;
	}

	public String getPkCnord() {

		return pkCnord;
	}

	public void setPkCnord(String pkCnord) {

		this.pkCnord = pkCnord;
	}

	public List<CnOrdHerb> getCnOrdHerbDeletes() {

		return cnOrdHerbDeletes;
	}

	public void setCnOrdHerbDeletes(List<CnOrdHerb> cnOrdHerbDeletes) {

		this.cnOrdHerbDeletes = cnOrdHerbDeletes;
	}

	public String getCodeOrdtype() {

		return codeOrdtype;
	}

	public void setCodeOrdtype(String codeOrdtype) {

		this.codeOrdtype = codeOrdtype;
	}

}
