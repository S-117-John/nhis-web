package com.zebone.nhis.base.pub.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jasig.cas.client.util.CommonUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.pub.dao.BdPubMapper;
import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 基础数据公共服务
 * @author yx
 *
 */
@Service
public class BdPubService {
	@Resource
	private BdPubMapper bdPubMapper;
    /**
     * 根据编码获取公共字典数据标准编码,名称
     * @param code
     * @param codeDefDocList
     * @return
     */
	public BdDefdoc getBdDefDocStd(String code,String codeDefDocList){
		if(CommonUtils.isEmpty(code)||CommonUtils.isEmpty(codeDefDocList))
			throw new BusException("请传入字典分类及字典编码！");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("code", code);
		paramMap.put("codeDefDocList", codeDefDocList);
		BdDefdoc doc = bdPubMapper.queryBdDefDocByCodeAndDefDocList(paramMap);
		return doc;
	}
	
	/**
     * 根据用户主键、功能编码check用户的功能权限（用于对非菜单按钮权限的控制）
     * @param pkOrg
     * @param codeUser
     * @param codeOper
     * @return
     */
	public List<Map<String,Object>> qryUserOper(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		return bdPubMapper.qryUserOper(paramMap);
	}
}
