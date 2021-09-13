package com.zebone.nhis.base.price.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.price.dao.BdDefDocOfRisMapper;
import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.nhis.common.module.base.bd.price.BdItemDefdoc;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 标本|容器收费项目维护
 * @author wj
 *
 */
@Service
public class BdDefdocOfRisService {

	@Resource
	private BdDefDocOfRisMapper bdDefDocOfRisMapper;

	/**
	 * 查询标本|容器 - 对应码表数据
	 * @param param
	 *    {codeDefdoclist：对应码表的分类 , code ：模糊匹配的参数}
	 * @param user 当前用户
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryDefdocOfRis(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(null == paramMap)
			throw new BusException("未获取到待查询的入参！");
		List<Map<String, Object>> defdocOfRisList = bdDefDocOfRisMapper.queryDefdocOfRis(paramMap);
		return defdocOfRisList;
	}
	
	/**
	 * 保存标本|容器 - 对应码表数据
	 * 1、校验名称、编码唯一
	 * 2、保存相关数据并返回
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> saveDefdocOfRis(String param, IUser user){
		BdDefdoc defdoc = JsonUtil.readValue(param,BdDefdoc.class);
		if(null == defdoc)
			throw new BusException("未获取到待保存的入参！");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		//1、校验编码、名称 全局唯一
		paramMap.put("code", defdoc.getCode());
		paramMap.put("pkDefdoc", defdoc.getPkDefdoc());
		paramMap.put("codeDefdoclist", defdoc.getCodeDefdoclist());
		int countCode = bdDefDocOfRisMapper.queryDefdocCntByCon(paramMap);
		if(countCode > 0)
			throw new BusException("编码【"+defdoc.getCode()+"】已经存在，请重新输入！");
		
		paramMap.remove("code");
		paramMap.put("name", defdoc.getName());
		int countName = bdDefDocOfRisMapper.queryDefdocCntByCon(paramMap);
		if(countName > 0)
			throw new BusException("名称【"+defdoc.getName()+"】已经存在，请重新输入！");
		
		defdoc.setPkOrg("~");
		if(CommonUtils.isEmptyString(defdoc.getPkDefdoc()))
			DataBaseHelper.insertBean(defdoc);
		else
			DataBaseHelper.updateBeanByPk(defdoc);
		
		//获取最新的记录
		paramMap.put("codeDefdoc", defdoc.getCode());
		List<Map<String, Object>> defdocList = bdDefDocOfRisMapper.queryDefdocOfRis(paramMap);
		return null == defdocList || defdocList.size() < 1 ? null : defdocList.get(0);
	}

	/**
	 *  删除标本|容器 - 对应码表数据
	 * @param param
	 * @param user
	 * @return
	 */
	public void deleteDefdocOfRis(String param, IUser user) {
		BdDefdoc defdoc = JsonUtil.readValue(param,BdDefdoc.class);
		if(null == defdoc)
			throw new BusException("未获取到待保存的入参！");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkDefdoc", defdoc.getPkDefdoc());
		paramMap.put("codeDefdoclist", defdoc.getCodeDefdoclist());
		bdDefDocOfRisMapper.deleteDefdocOfRis(paramMap);//删除对应码表记录
		
		paramMap.put("codeDefdoc", defdoc.getCode());
		bdDefDocOfRisMapper.deleteItemDefdocOfRis(paramMap);//删除全部的对应收费记录
	}

	/**
	 * 查询标本|容器 - 对应收费项目
	 * @param param
	 *    {codeDefdoclist：对应码表的分类 , codeDefdoc ：对应码表的编码}
	 * @param user 当前用户
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryItemDefdocOfRis(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(null == paramMap)
			throw new BusException("未获取到待查询的入参！");
		List<Map<String, Object>> defdocOfRisList = bdDefDocOfRisMapper.queryItemDefdocOfRis(paramMap);
		return defdocOfRisList;
	}

	/**
	 * 保存标本|容器 - 对应收费项目
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> saveItemDefdocOfRis(String param, IUser user){
		List<BdItemDefdoc> dtList = JsonUtil.readValue(param, new TypeReference<List<BdItemDefdoc>>(){});
		if(null == dtList || dtList.size() < 1)
			return null;
		for (BdItemDefdoc item : dtList) {
			item.setPkOrg("~");
			if(CommonUtils.isEmptyString(item.getPkItemdefdoc()))
				DataBaseHelper.insertBean(item);
			else
				DataBaseHelper.updateBeanByPk(item,false);	
		}
		
		//获取最新的记录
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("codeDefdoc", dtList.get(0).getCodeDefdoc());
		paramMap.put("codeDefdoclist", dtList.get(0).getCodeDefdoclist());
		List<Map<String, Object>> defdocOfRisList = bdDefDocOfRisMapper.queryItemDefdocOfRis(paramMap);
		return defdocOfRisList;
	}

	/**
	 *  删除标本|容器 - 对应收费项目
	 * @param param
	 * @param user
	 * @return
	 */
	public void deleteItemDefdocOfRis(String param, IUser user) {
		String pkItemdefdoc = JsonUtil.readValue(param, String.class);
		if(null == pkItemdefdoc || CommonUtils.isEmptyString(pkItemdefdoc))
			throw new BusException("未获取到待删除的入参！");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkItemdefdoc", pkItemdefdoc);
		bdDefDocOfRisMapper.deleteItemDefdocOfRis(paramMap);//删除全部的对应收费记录
	}
}
