package com.zebone.nhis.cn.opdw.dao;

import com.zebone.nhis.cn.opdw.vo.SyxPvDoc;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SyxCnOpHistPvMapper {

	List<Map<String,Object>> qryHistoryOrders(Map<String,Object> map);

	List<Map<String,Object>> qryHistoryOrdersEx(Map<String,Object> map);

	List<SyxPvDoc> qryHistoryDocs(Map<String, Object> paramMap);

	List<SyxPvDoc> qryHistoryDocsNoneDocData(Map<String, Object> paramMap);
	
	List<Map<String,Object>> qryCommonDiag(Map<String, Object> paramMap);
	//查询旧系统门诊病历--博爱
	List<SyxPvDoc> qryHistoryDocsOldBoai(Map<String, Object> paramMap);
}
