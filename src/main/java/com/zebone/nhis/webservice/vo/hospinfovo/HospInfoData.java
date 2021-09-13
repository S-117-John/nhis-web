package com.zebone.nhis.webservice.vo.hospinfovo;

import javax.xml.bind.annotation.XmlElement;

/**
 * 患者住院信息
 * @ClassName: HospInfoData   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: zhangheng 
 * @date: 2019年4月18日 下午5:56:43     
 * @Copyright: 2019
 */
public class HospInfoData {
	private HospInfoDataVo hospInfoDataVo;

	// 返回参数第三层
	@XmlElement(name = "hospInfoList")
	public HospInfoDataVo getHospInfoDataVo() {
		return hospInfoDataVo;
	}

	public void setHospInfoDataVo(HospInfoDataVo hospInfoDataVo) {
		this.hospInfoDataVo = hospInfoDataVo;
	}

}
