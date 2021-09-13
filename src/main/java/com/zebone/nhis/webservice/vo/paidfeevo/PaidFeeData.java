package com.zebone.nhis.webservice.vo.paidfeevo;

import javax.xml.bind.annotation.XmlElement;

/**
 * 7.3.查询门诊已缴费用 Data
 * @ClassName: PaidFeeData   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: zhangheng 
 * @date: 2019年4月20日 上午11:35:21     
 * @Copyright: 2019
 */
public class PaidFeeData {
	private PaidFeeDataVo paidFeeDataVo;

	// 返回参数第三层
	@XmlElement(name = "paidChargeList")
	public PaidFeeDataVo getPaidFeeDataVo() {
		return paidFeeDataVo;
	}

	public void setPaidFeeDataVo(PaidFeeDataVo paidFeeDataVo) {
		this.paidFeeDataVo = paidFeeDataVo;
	}

}
