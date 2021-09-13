package com.zebone.nhis.ma.pub.syx.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ExtSendHipMapper {
	
	public List<Map<String, Object>> qrySendDispMap(String presNo);
	
	public Map<String, Object> qryPiAdderss(String pkAddr);
	/**
	 *  查询未发送包药机记录服务
	 */
	public List<Map<String, Object>> selectNotDispensingMap(Map<String,Object> map);
}
