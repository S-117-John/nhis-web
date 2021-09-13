package com.zebone.nhis.scm.purchase.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.common.module.scm.pub.BdSupplyer;
import com.zebone.nhis.scm.purchase.dao.PuSearchMapper;
import com.zebone.nhis.scm.purchase.vo.PuSearchQueryDrugBySupplyerResult;
import com.zebone.nhis.scm.purchase.vo.PuSearchQuerySupplyerByDrugResult;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;

/*
 * 采购查询
 */
@SuppressWarnings("unchecked")
@Service
public class PuSearchService {
	
	@Resource
	private PuSearchMapper puSearchMapper;
	
	//通过仓库和日期精确查询供应商
	public List<BdSupplyer> querySupplyerByParam(String param,IUser user){
		
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		
		map = setDate(map);
		
		return puSearchMapper.querySupplyerByParam(map);
	}
	
	//根据供应商查询药品明细
	public List<PuSearchQueryDrugBySupplyerResult> queryDrugBySupplyer(String param,IUser user){
		
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		
		map = setDate(map);
		
		return puSearchMapper.queryDrugBySupplyer(map);
	}
	
	//根据条件查询采购入库的药品
	public List<BdPd> queryDrugPyParam(String param,IUser user){
		
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		
		map = setDate(map);
		
		return puSearchMapper.queryDrugPyParam(map);
	}
	
	//根据药品查询关联的采购供应商
	public List<PuSearchQuerySupplyerByDrugResult> querySupplyerByDrug(String param,IUser user){
		
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		
		map = setDate(map);
		
		return puSearchMapper.querySupplyerByDrug(map);
	}
	
	public List<Map<String, Object>> querySupplyerPrint(String param,IUser user){
		
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		
		map = setDate(map);
		
		List<Map<String,Object>> list = puSearchMapper.querySupplyerPrint(map);
		
		Map<String, Object> existsMap = new HashMap<>();
		for (Map<String, Object> m : list) {
			String value = (String)m.get("SUPPLYERNAME");
			if(existsMap.containsKey(value)){
				m.put("SUPPLYERNAME", "");
			}else{
				existsMap.put(value, "");
			}
		}
		
		return list;
	}
	
	//对日期进行临界处理
	private Map<String, Object> setDate(Map<String, Object> map){
		
		Object dateBegin = map.get("dateBegin");
		if(dateBegin != null){
			String dateBeginStr = (String)dateBegin;
			map.put("dateBegin", dateBeginStr.substring(0, 8)+"000000");
		}
		Object dateEnd = map.get("dateEnd");
		if(dateEnd != null){
			String dateEndStr = (String)dateEnd;
			map.put("dateEnd", dateEndStr.substring(0, 8)+"235959");
		}
		
		return map;
	}
}
