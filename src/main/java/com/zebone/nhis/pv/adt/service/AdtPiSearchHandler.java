package com.zebone.nhis.pv.adt.service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pv.adt.dao.AdtPiSearhMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class AdtPiSearchHandler {
	
	@Resource
	private AdtPiSearhMapper adtPiSearchMapper;
	
	/**
	 * 交易码:003004003005
	 * 患者手术信息查询
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> searchOperator(String param,IUser user){
		long startTime=System.currentTimeMillis();   //获取开始时间   //获取开始时间
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap.get("pkPv")==null){
			throw new BusException("请传入患者就诊主键！");
		}
		List<Map<String,Object>> viewList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> opList = adtPiSearchMapper.queryPatiOpList(paramMap);
		/*Set<String> applyList = new HashSet<>();
		for(Map<String,Object> op : opList){
			applyList.add(op.get("codeApply").toString());
		}
		try{
			DataSourceRoute.putAppId("Operator");// 切换数据源
		    viewList = DataBaseHelper.queryForList("select * from V_OPERATION_INFO where " + CommonUtils.convertSetToSqlInPart(applyList, "his_apply_id"));
		    System.out.println("-----------viewList size------"+viewList.size());
		    for(Map<String,Object> opMap : opList){
		    	if(null!=opMap.get("codeApply")){
		    		String codeApply =(String)opMap.get("codeApply");
		    	    for(Map<String,Object> viewMap : viewList){
		    	    	if(codeApply.equals(viewMap.get("hisApplyId"))){
		    	    		opMap.put("inci_type_name", getPropValueStr(viewMap, "inciTypeName"));
		    	    		opMap.put("operate_start_time", getPropValueStr(viewMap, "operateStartTime"));
		    	    		opMap.put("operate_end_time", getPropValueStr(viewMap, "operateEndTime"));
		    	    		if(null!=viewMap.get("operateEndTime")&&null!=viewMap.get("operateStartTime")){
		    	    			   //目前持续时间取分钟数
		    			    	   Integer minutes =  DateUtils.getMinsBetween((Date)viewMap.get("operateStartTime"),(Date)viewMap.get("operateEndTime"));
		    			    	   opMap.put("operate_last_time", minutes);
		    			    	   System.out.print("----------------"+minutes);
		    			    }
		    	    		opMap.put("operation_doctor", getPropValueStr(viewMap, "operationDoctor"));
		    	    		opMap.put("operass_doctor", getPropValueStr(viewMap, "operassDoctor"));
		    	    		opMap.put("anes_doctor", getPropValueStr(viewMap, "anesDoctor"));
		    	    		opMap.put("anesass_doctor",getPropValueStr(viewMap, "anesassDoctor"));
		    	    		opMap.put("wash_nurse", getPropValueStr(viewMap, "washNurse"));
		    	    		opMap.put("circuit_nurse", getPropValueStr(viewMap, "circuitNurse"));
		    	    		opMap.put("dept_name",getPropValueStr(viewMap, "deptName"));
		    	    		opMap.put("operation_date", getPropValueStr(viewMap, "operationDate"));
		    	    		opMap.put("asa_level_name",  getPropValueStr(viewMap, "asaLevelName"));
		    	    	}
		    	    }
		    	}
		    }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		   DataSourceRoute.putAppId("default");// 切换数据源
		}*/
		System.out.println("---------opList---------"+ JsonUtil.writeValueAsString(opList));
		long endTime=System.currentTimeMillis();   //获取开始时间
		System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
		return opList;
	}
	
	public static String getPropValueStr(Map<String, Object> map, String key) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		String value = "";
		if (map.containsKey(key)) {
			Object obj = map.get(key);
			value = obj == null ? "" : obj.toString();
		}
		return value;
	}


}
