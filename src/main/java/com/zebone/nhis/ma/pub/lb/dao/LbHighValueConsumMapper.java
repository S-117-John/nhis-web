package com.zebone.nhis.ma.pub.lb.dao;

import java.util.List;

import com.zebone.nhis.ma.pub.lb.vo.HighValuePdstVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface LbHighValueConsumMapper {
	
	/***
	 * 构建出库参数
	 * @param barcodes
	 * @return
	 */
	public List<HighValuePdstVo> queryOutstparam(List<String> barcodes);
}
