package com.zebone.nhis.task.reg.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.task.reg.vo.SughRegIpInputOrderReqVo;
import com.zebone.nhis.task.reg.vo.SughRegIpOutputOrderReqVo;
import com.zebone.nhis.task.reg.vo.SughRegRequest;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SughRegOrderMapper {
	
	public List<SughRegRequest> qryPvInfoList(Map<String,Object> paramMap);
	
	public List<SughRegRequest> qryPvRetInfoList(Map<String,Object> paramMap);

	public List<SughRegIpInputOrderReqVo> qryPvIpInfoList(Map<String,Object> paramMap);

	public List<SughRegIpOutputOrderReqVo> qryPvIpOutInfoList(Map<String,Object> paramMap);
}
