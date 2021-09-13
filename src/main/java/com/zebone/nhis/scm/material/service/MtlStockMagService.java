package com.zebone.nhis.scm.material.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.scm.material.dao.MtlPdStMapper;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 库存管理
 * @author wj
 */

@Service
public class MtlStockMagService {
	
	@Resource
	private MtlPdStMapper pdStMapper;
	
	/**
	 * 查看物品库存信息
	 * @param param{code,name,dtDosage,dtAnti,dtPois,dateBegin,dateEnd,price,flagUp,flagDown}
	 * @param user
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryPdStoreInfoList(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param,Map.class);
    	if(map == null)
    		map = new HashMap<String,Object>();
    	map.put("pkStore", ((User)user).getPkStore());
    	if(CommonUtils.isNotNull(map.get("dateBegin"))){
			map.put("dateBegin", CommonUtils.getString(map.get("dateBegin")).substring(0, 8)+"000000");
		}
		if(CommonUtils.isNotNull(map.get("dateEnd"))){
			map.put("dateEnd", CommonUtils.getString(map.get("dateEnd")).substring(0, 8)+"235959");
		}
		List<Map<String, Object>> list = pdStMapper.queryPdStInfoList(map);
    	return list;
    }
    
    /**
     * 查看可用批次
     * @param param{pkStore,pkPd}
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryPdBatchList(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param,Map.class);
    	if(map == null || CommonUtils.isNull(map.get("pkStore")) ||CommonUtils.isNull(map.get("pkPd")))
    		throw new BusException("未获取到当前操作的仓库和物品信息！");
    	List<Map<String, Object>> list = pdStMapper.queryPdBatchList(map);
    	return list;
    }
   
    /**
     * 查看调价历史
     * @param param{pkPd,dateBegin,dateEnd}
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryPriceHist(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param,Map.class);
    	if(map == null)
    		map = new HashMap<String,Object>();
    	map.put("pkStore", ((User)user).getPkStore());
    	if(CommonUtils.isNotNull(map.get("dateBegin"))){
			map.put("dateBegin", CommonUtils.getString(map.get("dateBegin")).substring(0, 8)+"000000");
		}
		if(CommonUtils.isNotNull(map.get("dateEnd"))){
			map.put("dateEnd", CommonUtils.getString(map.get("dateEnd")).substring(0, 8)+"235959");
		}
		List<Map<String, Object>> list = pdStMapper.queryHisRePriceList(map);
    	return list;
    }
    
    /**
     * 查看单品记录
     * @param param{pkPd}
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryPdSingleList(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param,Map.class);
    	if(map == null)
    		map = new HashMap<String,Object>();
    	map.put("pkStore", ((User)user).getPkStore());
		List<Map<String, Object>> list = pdStMapper.queryPdSingleList(map);
    	return list;
    }

}
