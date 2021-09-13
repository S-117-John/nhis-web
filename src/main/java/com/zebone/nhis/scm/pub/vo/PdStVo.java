package com.zebone.nhis.scm.pub.vo;

import java.util.List;

import com.zebone.nhis.common.module.scm.st.PdSt;

public class PdStVo extends PdSt{
    private String spr;//供应商
    private String nameOrg;//出入库机构
    private String nameDept;//出入库部门
    private String nameStore;//出入库仓库
    private String sttype;//业务类型-名称
    private String pkPdplan;//对应出库处理的待入库单主键，若此字段不为空，表示由待入库生成的入库单
	private String cgjhbh;//采购计划编号
	private String gysbm;//供应商编码

	/**是否前端确认批次--（备用，若后面全部使用前台选择好批次出库，不用后端做批次确认设置这里为true即可）*/
	private Boolean batchConfirmInPage;

	private List<Object[]> delDtList;//删除的明细主键
    
    private List<PdStDtVo> dtlist;


	public List<Object[]> getDelDtList() {
		return delDtList;
	}

	public void setDelDtList(List<Object[]> delDtList) {
		this.delDtList = delDtList;
	}
	
	public String getPkPdplan() {
		return pkPdplan;
	}

	public void setPkPdplan(String pkPdplan) {
		this.pkPdplan = pkPdplan;
	}

	public String getSttype() {
		return sttype;
	}

	public void setSttype(String sttype) {
		this.sttype = sttype;
	}

	public String getNameOrg() {
		return nameOrg;
	}

	public void setNameOrg(String nameOrg) {
		this.nameOrg = nameOrg;
	}

	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	public String getNameStore() {
		return nameStore;
	}

	public void setNameStore(String nameStore) {
		this.nameStore = nameStore;
	}

	public List<PdStDtVo> getDtlist() {
		return dtlist;
	}

	public void setDtlist(List<PdStDtVo> dtlist) {
		this.dtlist = dtlist;
	}

	public String getSpr() {
		return spr;
	}

	public void setSpr(String spr) {
		this.spr = spr;
	}

	public Boolean getBatchConfirmInPage() {
		return batchConfirmInPage;
	}

	public void setBatchConfirmInPage(Boolean batchConfirmInPage) {
		this.batchConfirmInPage = batchConfirmInPage;
	}

	public String getCgjhbh() {
		return cgjhbh;
	}

	public void setCgjhbh(String cgjhbh) {
		this.cgjhbh = cgjhbh;
	}

	public String getGysbm() {
		return gysbm;
	}

	public void setGysbm(String gysbm) {
		this.gysbm = gysbm;
	}	
}
