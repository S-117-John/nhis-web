package com.zebone.nhis.scm.st.vo;

import java.util.List;

import com.zebone.nhis.common.module.scm.st.PdCc;
import com.zebone.nhis.common.module.scm.st.PdCcDetail;

/***
 * 库存结账返回类
 * 
 * @author wangpeng
 * @date 2016年12月5日
 *
 */
public class PdCcVo {	
	
	private PdCc pdCc;
	
	private List<PdCcDetail> pdCcDetailList;

	public PdCc getPdCc() {
		return pdCc;
	}

	public void setPdCc(PdCc pdCc) {
		this.pdCc = pdCc;
	}

	public List<PdCcDetail> getPdCcDetailList() {
		return pdCcDetailList;
	}

	public void setPdCcDetailList(List<PdCcDetail> pdCcDetailList) {
		this.pdCcDetailList = pdCcDetailList;
	}
}
