package com.zebone.nhis.pro.zsba.base.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 免收费用服务（博爱版）
 * @author admin
 *
 */
@Mapper
public interface ZsbaBlConsultationFreeMapper {
	//查询免收费用信息
	 List<Map<String, Object>> qryBlConFreeApplyList(Map<String, Object> paramMap);
	
}
