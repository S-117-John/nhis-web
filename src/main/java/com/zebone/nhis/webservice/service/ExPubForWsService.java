package com.zebone.nhis.webservice.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.webservice.dao.ExPubForWsMapper;

/**
 * 执行域webservcie专用公共服务
 * @author yangxue
 *
 */
@Service
public class ExPubForWsService {
	@Resource
	private ExPubForWsMapper exPubForWsMapper;
	/**
	 * 查询执行单（主要用于查询执行确认与取消执行的执行单列表查询）
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryExecListByCon(Map<String,Object> paramMap){
		return exPubForWsMapper.queryExecListByCon(paramMap);
	}
}
