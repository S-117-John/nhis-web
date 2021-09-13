package com.zebone.nhis.ex.nis.fee.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PrepayMapper {
   /**
    * 根据病区，预交金比率查询患者预交金
    * @param map{pkDeptNs,num}
    * @return
    */
	public List<Map<String,Object>> queryPrePay(Map<String,Object> map);

   public List<Map<String, Object>> queryPrePaySql(Map<String, Object> map);
}
	
