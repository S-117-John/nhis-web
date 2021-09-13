package com.zebone.nhis.bl.pub.syx.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.compay.ins.syx.gzyb.InsGzgyChk;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ItemApproValMapper {
	
	/**查询审批信息数据*/
	List<InsGzgyChk> qryApproValList(Map<String,Object> paramMap);
	
	/**更新审批记录*/
	int updateApproVal(InsGzgyChk gzgyChk);
	
}
