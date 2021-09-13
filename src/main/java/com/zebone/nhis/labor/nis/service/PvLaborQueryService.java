package com.zebone.nhis.labor.nis.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.labor.nis.dao.PvLaborQueryMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 出产房患者查询
 * @author yangxue
 *
 */
@Service
public class PvLaborQueryService {
	@Resource
    private PvLaborQueryMapper pvLaborQueryMapper;
	/**
	 * 出产房患者查询
	 * @param param{pkOrg,namePi,bedNo,pkDept,pkDeptNs,dateBegin,dateEnd,codePv,flagIn}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryLaborPati(String param,IUser user){
		 Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	 String endDate = CommonUtils.getString(map.get("dateEnd"));
    	 if(endDate!=null&&!endDate.equals("")){
    		 endDate = endDate.substring(0, 8)+"235959";
    		 map.put("dateEnd", endDate);
    	 }
    	 return pvLaborQueryMapper.queryPvLabor(map);
	}
	
	
}
