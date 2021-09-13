package com.zebone.nhis.cn.opdw.vo;

import com.zebone.nhis.bl.opcg.vo.OpCgCnOrder;
import com.zebone.nhis.common.module.cn.opdw.BdOpEmrTempCate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lix
 * @version 1.0
 * @date 2020/9/16 15:19
 * @description
 * @currentMinute code_houtai
 */
public class BdOpEmrTempCateVo extends BdOpEmrTempCate {
	private List<BdOpEmrTempVo> bdOpEmrTempVo = new ArrayList<BdOpEmrTempVo>();

	public List<BdOpEmrTempVo> getBdOpEmrTempVo() {
		return bdOpEmrTempVo;
	}

	public void setBdOpEmrTempVo(List<BdOpEmrTempVo> bdOpEmrTempVo) {
		this.bdOpEmrTempVo = bdOpEmrTempVo;
	}
}
