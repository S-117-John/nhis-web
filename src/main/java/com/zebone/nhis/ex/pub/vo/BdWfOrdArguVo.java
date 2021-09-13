package com.zebone.nhis.ex.pub.vo;

import com.zebone.nhis.common.module.base.bd.wf.BdWfOrdArgu;

/**
 * 组合一下开立科室与执行科室参数
 * @author yangxue
 *
 */
public class BdWfOrdArguVo extends BdWfOrdArgu{
	private BdWfOrdArguDeptVo wfDeptVO;

	public BdWfOrdArguDeptVo getWfDeptVO() {
		return wfDeptVO;
	}

	public void setWfDeptVO(BdWfOrdArguDeptVo wfDeptVO) {
		this.wfDeptVO = wfDeptVO;
	}
	
}
