package com.zebone.nhis.webservice.vo.ipcgdayvo;

import javax.xml.bind.annotation.XmlElement;

/**
 * 7.16.查询住院一日清单
 * @ClassName: IpCgDayData   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: zhangheng 
 * @date: 2019年4月22日 上午10:59:10     
 * @Copyright: 2019
 */
public class IpCgDayData {
	private IpCgDayDataVo ipCgDayDataVo;

	// 返回参数第三层
	@XmlElement(name = "dayDetaList")
	public IpCgDayDataVo getIpCgDayDataVo() {
		return ipCgDayDataVo;
	}

	public void setIpCgDayDataVo(IpCgDayDataVo ipCgDayDataVo) {
		this.ipCgDayDataVo = ipCgDayDataVo;
	}

}
