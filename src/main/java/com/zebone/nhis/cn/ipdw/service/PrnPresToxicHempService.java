package com.zebone.nhis.cn.ipdw.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.ipdw.dao.PrnPresToxicHempMapper;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 毒麻处方打印
 * @author haohan
 *
 */
@Service
public class PrnPresToxicHempService {
	
	@Autowired
	private PrnPresToxicHempMapper toxicHempMapper;
	
	/**
	 * 查询毒麻处方明细 004004011007
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getPresDt(String param,IUser user){
		Map<String,Object> qryParam = JsonUtil.readValue(param, Map.class);
		qryParam.put("pkDeptEx", UserContext.getUser().getPkDept());
		return toxicHempMapper.getPresDt(qryParam);
	}
	
	/**
	 * 查询处方汇总单 004004011008
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getPresSummary(String param,IUser user){
		List<String> pkPres=JsonUtil.readValue(param, new TypeReference<List<String>>() {
		});
		return toxicHempMapper.getPresSummary(pkPres);
	}
	
	/**
	 * 查询处方汇总单打印信息  004004011009
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getPresSummaryPrint(String param,IUser user){
		List<String> pkPres=JsonUtil.readValue(param, new TypeReference<List<String>>() {
		});
		List<Map<String,Object>> sourceList = toxicHempMapper.getPresSummary(pkPres);
		
		//先把相同药品名称的放在一个list
		Map<String, List<Map<String, Object>>> map = new HashMap<>();
		for (Map<String, Object> m : sourceList) {
			String name = (String)m.get("nameCg");
			//通过药品名称去找是否有重复的，有就添加
			List<Map<String, Object>> list = map.get(name);
			if(list != null && list.size() > 0){
				list.add(m);
			}else{
				list = new ArrayList<>();
				list.add(m);
				map.put(name, list);
			}
		}
		//把每个药品名称对应的信息封装成一个list
		List<Map<String, Object>> allList = new ArrayList<>();
		for (String key : map.keySet()) {
			List<Map<String, Object>> list = map.get(key);
			
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> item = list.get(i);
				if(list.size() > 1 && i != 0){
					item.put("name", "");
				}else{
					item.put("name", item.get("nameCg"));
				}
				allList.add(item);
			}
		}
		
		return allList;
	}
}
