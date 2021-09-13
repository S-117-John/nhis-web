package com.zebone.nhis.webservice.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.webservice.dao.CnPubForWsMapper;

/**
 * 临床域webservcie专用公共服务
 * @author yangxue
 *
 */
@Service
public class CnPubForWsService {
	@Resource
	private CnPubForWsMapper cnPubForWsMapper;
	
	/**
	 * 查询医嘱信息
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryCnOrderWeb(Map<String,Object> paramMap){
		return cnPubForWsMapper.queryCnOrderWeb(paramMap);
	}
	
}
