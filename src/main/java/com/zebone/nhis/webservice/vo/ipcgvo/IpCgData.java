package com.zebone.nhis.webservice.vo.ipcgvo;

import javax.xml.bind.annotation.XmlElement;

/**
 * 7.15.查询住院费用明细 data
 * @ClassName: IpCgData   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: zhangheng 
 * @date: 2019年4月20日 下午4:55:13     
 * @Copyright: 2019
 */
public class IpCgData {
	private IpCgDataVo ipCgDataVo;

	// 返回参数第三层
	@XmlElement(name = "hospExpeList")
	public IpCgDataVo getIpCgDataVo() {
		return ipCgDataVo;
	}

	public void setIpCgDataVo(IpCgDataVo ipCgDataVo) {
		this.ipCgDataVo = ipCgDataVo;
	}

}
