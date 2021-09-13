package com.zebone.nhis.pro.sd.bl.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface BankMisAccChkMapper {
	
	/**查询门诊第三方收退款记录*/
	public List<Map<String,Object>> qryOpDepoInfo(Map<String,Object> paramMap);
	
	/**根据发票号查询第三方订单信息*/
	public Map<String,Object> qryExtInfoByCodeInv(Map<String,Object> paramMap);
	
}
