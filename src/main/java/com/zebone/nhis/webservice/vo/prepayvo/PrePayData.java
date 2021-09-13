package com.zebone.nhis.webservice.vo.prepayvo;

import javax.xml.bind.annotation.XmlElement;

/**
 * 7.14.查询住院预交金充值记录 DATA
 * @ClassName: PrePayData   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: zhangheng 
 * @date: 2019年4月20日 下午2:29:49     
 * @Copyright: 2019
 */
public class PrePayData {
	private PrePayDataVo prePayDataVo;

	// 返回参数第三层
	@XmlElement(name = "rechargeRecList")
	public PrePayDataVo getPrePayDataVo() {
		return prePayDataVo;
	}

	public void setPrePayDataVo(PrePayDataVo prePayDataVo) {
		this.prePayDataVo = prePayDataVo;
	}

}
