package com.zebone.nhis.bl.pub.vo;

import java.util.List;

import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;

/**
 * 记退费统一返回参数类型
 * @author Administrator
 * 
 */
public class BlPubReturnVo {

	/**
	 * 住院收费明细
	 */
	private List<BlIpDt> bids;

	/**
	 * 门诊收费明细
	 */
	private List<BlOpDt> bods;
	

	public List<BlIpDt> getBids() {

		return bids;
	}

	public void setBids(List<BlIpDt> bids) {

		this.bids = bids;
	}

	public List<BlOpDt> getBods() {

		return bods;
	}

	public void setBods(List<BlOpDt> bods) {

		this.bods = bods;
	}


}
