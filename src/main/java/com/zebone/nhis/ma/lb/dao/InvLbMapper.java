package com.zebone.nhis.ma.lb.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.ma.lb.vo.InvLbVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface InvLbMapper {

	List<InvLbVo> queryInv(Map<String,Object> map);
	
	List<Map<String,Object>> queryInvHistory(Map<String,Object> map);
	
	List<Map<String,Object>> queryInvHistoryByDeposit(Map<String,Object> map);
}
