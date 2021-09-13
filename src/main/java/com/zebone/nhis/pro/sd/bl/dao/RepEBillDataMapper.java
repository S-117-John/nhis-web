package com.zebone.nhis.pro.sd.bl.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface RepEBillDataMapper {
	
	/**查询结算发票信息*/
	public List<Map<String,Object>> qryStInvInfo(Map<String,Object> paramMap);
	
}
