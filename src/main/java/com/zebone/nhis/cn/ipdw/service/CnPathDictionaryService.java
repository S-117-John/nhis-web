package com.zebone.nhis.cn.ipdw.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zebone.nhis.cn.ipdw.dao.CnPathDictionaryMapper;
import com.zebone.nhis.cn.ipdw.vo.BdCpTaskVo;
import com.zebone.nhis.common.module.cn.cp.BdCpExp;
import com.zebone.nhis.common.module.cn.ipdw.BdCpReason;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/***
 * 路径字典接口
 * @author ASUS
 *
 */
@Service
public class CnPathDictionaryService {
	
	@Autowired
	private CnPathDictionaryMapper cnPathDictionaryMapper;
	
	/**
	 * 路径字典-过程原因-保存
	 */
	public void savePathCr(String param , IUser user) {
		
		List<BdCpReason> list = JsonUtil.readValue(param, new TypeReference<List<BdCpReason>>(){});
		
		if(list != null && list.size() > 0){
			for (BdCpReason bdCpReason : list) {
				if(StringUtils.isNotBlank(bdCpReason.getPkCpreason())){
					DataBaseHelper.updateBeanByPk(bdCpReason, false);
				}else{
					bdCpReason.setPkOrg("~                               ");
					DataBaseHelper.insertBean(bdCpReason);
				}
			}
		}
		
	}
	
	/**
	 * 路径字典-过程原因-查询
	 */
	public List<Map<String, Object>> qryPathCr(String param , IUser user) {
		
		return cnPathDictionaryMapper.qryPathCr();
	}
	
	/**
	 * 路径字典-过程原因-删除
	 */
	public void delPathCr(String param , IUser user) {
		
		List<BdCpReason> list = JsonUtil.readValue(param, new TypeReference<List<BdCpReason>>(){});
		
		if(list != null && list.size() > 0){
			
			List<String> names = cnPathDictionaryMapper.countCrUse(list);
			
			if(names != null && names.size() > 0){
				throw new BusException("禁止删除！");
			}
			
			cnPathDictionaryMapper.delPathCr(list);			
		}
	}
	
	/**
	 * 路径字典-变异数据-查询
	 */
	public List<Map<String, Object>> qryPathCe(String param , IUser user) {
		
		return cnPathDictionaryMapper.qryPathCe();
	}
	
	/**
	 * 路径字典-变异数据-删除：004004012011
	 */
	public void delPathCe(String param , IUser user) {
		
		List<BdCpExp> list = JsonUtil.readValue(param, new TypeReference<List<BdCpExp>>(){});
		
		if(list != null && list.size() > 0){
			
			List<String> names = cnPathDictionaryMapper.countCeUse(list);
			if(names != null && names.size() > 0){
				throw new BusException("禁止删除！");
			}
			cnPathDictionaryMapper.delPathCe(list);			
		}
	}
	
	/**
	 * 路径字典-变异数据-保存
	 */
	public void savePathCe(String param , IUser user) {
		
		List<BdCpExp> list = JsonUtil.readValue(param, new TypeReference<List<BdCpExp>>(){});
		
		if(list != null && list.size() > 0){
			for (BdCpExp bdCpExp : list) {
				if(StringUtils.isNotBlank(bdCpExp.getPkCpexp())){
					DataBaseHelper.updateBeanByPk(bdCpExp, false);
				}else{
					bdCpExp.setPkOrg("~                               ");
					DataBaseHelper.insertBean(bdCpExp);
				}
			}
		}
	}
	
	
	/**
	 * 路径字典-临床工作-查询：004004012012
	 */
	public List<Map<String, Object>> qryPathCt(String param , IUser user) {
		
		return cnPathDictionaryMapper.qryPathCt();
	}
	
	/**
	 * 路径字典-临床工作-删除
	 */
	public void delPathCt(String param , IUser user) {
		
		List<BdCpTaskVo> list = JsonUtil.readValue(param, new TypeReference<List<BdCpTaskVo>>(){});

		List<String> count = cnPathDictionaryMapper.countCtUse(list);
		if(count != null && count.size() > 0){
			throw new BusException("禁止删除！");
		}
		cnPathDictionaryMapper.delPathCt(list);
	}
	
	/**
	 * 路径字典-临床工作-保存
	 */
	public void savePathCt(String param , IUser user) {
		
		List<BdCpTaskVo> list = JsonUtil.readValue(param, new TypeReference<List<BdCpTaskVo>>(){});
		
		if(list != null && list.size() > 0){
			for (BdCpTaskVo bdCpTaskVo : list) {
				//修改
				if(StringUtils.isNotBlank(bdCpTaskVo.getPkCptask())){
					DataBaseHelper.updateBeanByPk(bdCpTaskVo, false);
				}else{
					bdCpTaskVo.setPkOrg("~                               ");
					DataBaseHelper.insertBean(bdCpTaskVo);
				}
			}
		}
	}
}
