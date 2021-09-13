package com.zebone.nhis.webservice.vo.schInfovo;

import javax.xml.bind.annotation.XmlElement;

/**
 * 根据科室查询某段时间出诊专家
 * @ClassName: SchInfoData   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: zhangheng 
 * @date: 2019年4月16日 下午3:54:22     
 * @Copyright: 2019
 */
public class SchInfoData {
	private SchInfoDataVo schInfoDataVo;

	public SchInfoDataVo getSchInfoDataVo() {
		return schInfoDataVo;
	}

	// 返回参数第三层
	@XmlElement(name = "doctInfoList")
	public void setSchInfoDataVo(SchInfoDataVo schInfoDataVo) {
		this.schInfoDataVo = schInfoDataVo;
	}

}
