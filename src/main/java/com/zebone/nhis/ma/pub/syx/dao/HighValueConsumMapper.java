package com.zebone.nhis.ma.pub.syx.dao;


import com.zebone.nhis.ma.pub.syx.vo.HighValueConsumVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface HighValueConsumMapper {

	public HighValueConsumVo qryHighValueConsum(HighValueConsumVo paramMap);
}
