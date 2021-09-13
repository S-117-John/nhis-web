package com.zebone.nhis.cn.ipdw.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.cn.cp.CpRecExp;
import com.zebone.nhis.common.module.cn.ipdw.CnOrdHerb;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnSignCa;
import com.zebone.nhis.common.module.cn.ipdw.CpRecExpParam;

public class HerbOrder {
	private CnOrder order;
	/**继承CnOrder,添加新字段，非数据库项**/
	private OrderVo orderVo;
	private List<CnOrdHerb> herbs;
	private List<CnOrdHerb> delHerbs;
	private List<CnSignCa> cnSignCaList = new ArrayList<CnSignCa>();
	private List<CpRecExp> cpRecExp = new ArrayList<CpRecExp>();

	private String nameDiag;
	private String nameSymp;

	private String ifry;
	
	
	public List<CnOrdHerb> getDelHerbs() {
		return delHerbs;
	}
	public void setDelHerbs(List<CnOrdHerb> delHerbs) {
		this.delHerbs = delHerbs;
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
	public List<CnSignCa> getCnSignCaList() {
		return cnSignCaList;
	}
	public void setCnSignCaList(List<CnSignCa> cnSignCaList) {
		this.cnSignCaList = cnSignCaList;
	}
	public List<CpRecExp> getCpRecExp() {
		return cpRecExp;
	}
	public void setCpRecExp(List<CpRecExp> cpRecExp) {
		this.cpRecExp = cpRecExp;
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

	public OrderVo getOrderVo() {
		return orderVo;
	}

	public void setOrderVo(OrderVo orderVo) {
		this.orderVo = orderVo;
	}

	public String getIfry() {
		return ifry;
	}

	public void setIfry(String ifry) {
		this.ifry = ifry;
	}
}
