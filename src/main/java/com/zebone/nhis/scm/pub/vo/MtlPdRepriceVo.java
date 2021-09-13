package com.zebone.nhis.scm.pub.vo;

import java.util.List;

import com.zebone.nhis.common.module.scm.st.PdReprice;
import com.zebone.nhis.scm.pub.vo.MtlPdRepriceDtVo;

@SuppressWarnings("serial")
public class MtlPdRepriceVo extends PdReprice {
	
	private String reptype;// 调价类型

	private List<MtlPdRepriceDtVo> dtlist;
	
	private List<Object[]> delDtList;//删除的明细主键
	

	public List<Object[]> getDelDtList() {
		return delDtList;
	}

	public void setDelDtList(List<Object[]> delDtList) {
		this.delDtList = delDtList;
	}

	public List<MtlPdRepriceDtVo> getDtlist() {
		return dtlist;
	}

	public void setDtlist(List<MtlPdRepriceDtVo> dtlist) {
		this.dtlist = dtlist;
	}

	public String getReptype() {
		return reptype;
	}

	public void setReptype(String reptype) {
		this.reptype = reptype;
	}

}
