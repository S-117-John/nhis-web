package com.zebone.nhis.scm.opds.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.scm.opds.dao.ScmOpPresQryMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 门诊处方查询服务
 * @author yangxue
 *
 */
@Service
public class ScmOpPresQryService {
	@Resource
	private ScmOpPresQryMapper scmOpPresQryMapper;
	/**
	 * 查询处方执行明细（发药明细）
	 * @param map{pkPresocc}
	 * @return
	 */
	public List<Map<String,Object>> queryPresOccDetail(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap == null || CommonUtils.isNull(paramMap.get("pkPresocc"))){
			throw new BusException("未获取到处方执行主键！");
		}
		return scmOpPresQryMapper.queryPresOccDetail(paramMap);
	}
	
	/**
	 * 查询处方信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryPresInfoList(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null)return null;
		return scmOpPresQryMapper.qryPresInfoList(paramMap);
		
	}
}
