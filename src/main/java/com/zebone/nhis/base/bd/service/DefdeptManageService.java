package com.zebone.nhis.base.bd.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.bd.dao.DefdeptManageMapper;
import com.zebone.nhis.base.bd.vo.BdOuDeptVo;
import com.zebone.nhis.common.module.base.bd.res.BdDefdept;
import com.zebone.nhis.common.module.base.bd.res.BdDefdeptMap;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@SuppressWarnings("all")
@Service
public class DefdeptManageService {
	
	@Resource
	private DefdeptManageMapper defdeptManageMapper;
	
	/***
	 * 交易号001001004011
	 * 查询自定义科室
	 * @param param{dtDefdepttype:自定义科室类型}
	 * @param user
	 * @return
	 */
	public List<BdDefdept> qryDept(String param,IUser user) {
		Map paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap==null) return null;
		String dtDefdepttype=(String)paramMap.get("dtDefdepttype");
		
		List<BdDefdept> defdepts = defdeptManageMapper.qryDefdept(dtDefdepttype);
		
		return defdepts;
	}
	
	/**
	 * 交易号001001004012
	 * 查询基础科室
	 * @param param {dtDefdepttype:自定义科室类型,pkOrrg:当前机构}
	 * @param user
	 * @return
	 */
	public List<BdOuDeptVo> qryBdOuDepts(String param,IUser user) {
		Map paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap==null) return null;
		
		 User us = (User)user;
		 paramMap.put("pkOrg", us.getPkOrg());
		 
		 return defdeptManageMapper.qryBdOuDept(paramMap);
	}
	
	/**
	 * 交易号001001004013
	 * 新增
	 * @param param {deptInfo:自定义科室表,ouDepts:科室关系映射表}
	 * @param user
	 */
	public void saveDefdept(String param,IUser user) {
		Map paramMap = JsonUtil.readValue(param, Map.class);
		JsonNode jsonNodeObj = JsonUtil.getJsonNode(param, "deptInfo");
		BdDefdept bdDefdept=new BdDefdept();
		if (jsonNodeObj!=null) {
			 bdDefdept = JsonUtil.readValue(jsonNodeObj, BdDefdept.class);
		}
		HashMap map = new HashMap();
		map.put("codeDept", bdDefdept.getCodeDept());
		map.put("pkDefdept", bdDefdept.getPkDefdept());
		map.put("nameDept", bdDefdept.getNameDept());
		if(defdeptManageMapper.qryCountCode(map)>0){
			throw new BusException("编码不唯一!"); 
		}
		if(defdeptManageMapper.qryCountName(map)>0){
			throw new BusException("名称不唯一!"); 
		}
		if(StringUtils.isNotBlank(bdDefdept.getPkDefdept())){
			DataBaseHelper.updateBeanByPk(bdDefdept,false);
			defdeptManageMapper.delDefdeptMap(bdDefdept.getPkDefdept());
		}else {
			if (bdDefdept==null) return;
			bdDefdept.setPkOrg("~");
			if (bdDefdept.getSortno()==null) {
				bdDefdept.setSortno(0);
			}
			
			
			//保存自定义科室
			DataBaseHelper.insertBean(bdDefdept);
		}
		List<BdDefdeptMap> bdDefdeptMaps=new ArrayList<BdDefdeptMap>();
		JsonNode jsonNodeList = JsonUtil.getJsonNode(param, "ouDepts");
		if (jsonNodeList!=null) {
			bdDefdeptMaps = JsonUtil.readValue(jsonNodeList, new TypeReference<List<BdDefdeptMap>>(){});
		}
		
		//保存关系映射
		if(bdDefdeptMaps!=null&&bdDefdeptMaps.size()>0){
			for (BdDefdeptMap bdDefdeptMap : bdDefdeptMaps) {
				bdDefdeptMap.setPkOrg(((User)user).getPkOrg());
				bdDefdeptMap.setPkDefdept(bdDefdept.getPkDefdept());
				DataBaseHelper.insertBean(bdDefdeptMap);
			}
		}
	}
	
	/**
	 * 交易号001001004014
	 * 删除
	 * @param param{"pkDefdept":自定义科室主键}
	 * @param user
	 */
	public void delDefdept(String param,IUser user) {
		Map paramMap = JsonUtil.readValue(param, Map.class);
		String pkDefdept= (String) paramMap.get("pkDefdept");
		if(StringUtils.isNoneBlank(pkDefdept)){
			Integer qryCount = defdeptManageMapper.qryCount(pkDefdept);
			if (qryCount>0) {
				throw new BusException("该节点非末级节点!"); 
			}
			defdeptManageMapper.delDefdeptMap(pkDefdept);
			defdeptManageMapper.delDefdept(pkDefdept);
		}
		return;
	}
	
}
