package com.zebone.nhis.pro.zsba.cn.ipdw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.pro.zsba.cn.ipdw.dao.OpOrdBaMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 *手术第三方查询
 */
@Service
public class ZsbaPtOpThisOrdHandler {

	@Resource
	private OpOrdBaService opOrdBaService;
	
	@Autowired
    private OpOrdBaMapper opOrder;
	/**
	 * 获取第三方手术相关信息
	 * 交易号：022003026053
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getThiInfoOrdBa(String param, IUser user) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		DataSourceRoute.putAppId("pt_op");//切换到 博爱手术相关数据源
		List<Map<String,Object>> ptOpList = opOrdBaService.getThiInfoOrdBa(paramMap);
		DataSourceRoute.putAppId("default");//切换到 NHIS
		if(null != ptOpList && !ptOpList.isEmpty()) {
			List<String> codeEmps = new ArrayList<>();
			codeEmps.add(MapUtils.getString(ptOpList.get(0), "surgeonId"));
			codeEmps.add(MapUtils.getString(ptOpList.get(0), "firstAssistantId"));
			codeEmps.add(MapUtils.getString(ptOpList.get(0), "secondAssistantId"));
			codeEmps.add(MapUtils.getString(ptOpList.get(0), "thirdAssistantId"));
			codeEmps.add(MapUtils.getString(ptOpList.get(0), "anesthesiaDoctorId"));
			codeEmps.add(MapUtils.getString(ptOpList.get(0), "secondOperationNurseId"));
			List<Map<String,Object>> pkEmpList =  opOrder.queryPkEmpInfo(codeEmps);
			pkEmpList.forEach(map ->{
				if(MapUtils.getString(map, "codeEmp").equals(MapUtils.getString(ptOpList.get(0), "surgeonId"))) {
					ptOpList.get(0).put("phyop", MapUtils.getString(map, "pkEmp"));
				}
	            if(MapUtils.getString(map, "codeEmp").equals(MapUtils.getString(ptOpList.get(0), "firstAssistantId"))) {
	            	ptOpList.get(0).put("asis", MapUtils.getString(map, "pkEmp"));
				}
	            if(MapUtils.getString(map, "codeEmp").equals(MapUtils.getString(ptOpList.get(0), "secondAssistantId"))) {
	            	ptOpList.get(0).put("asist", MapUtils.getString(map, "pkEmp"));
	    		}
	            if(MapUtils.getString(map, "codeEmp").equals(MapUtils.getString(ptOpList.get(0), "thirdAssistantId"))) {
	            	ptOpList.get(0).put("asisto", MapUtils.getString(map, "pkEmp"));
	            }
	            if(MapUtils.getString(map, "codeEmp").equals(MapUtils.getString(ptOpList.get(0), "anesthesiaDoctorId"))) {
	            	ptOpList.get(0).put("anae", MapUtils.getString(map, "pkEmp"));
	            }
	            if(MapUtils.getString(map, "codeEmp").equals(MapUtils.getString(ptOpList.get(0), "secondOperationNurseId"))) {
	            	ptOpList.get(0).put("circul", MapUtils.getString(map, "pkEmp"));
	            }
			});
		}
		return ptOpList;
	}
}
