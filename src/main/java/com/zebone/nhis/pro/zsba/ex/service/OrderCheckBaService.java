package com.zebone.nhis.pro.zsba.ex.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.pub.support.ExSysParamUtil;
import com.zebone.nhis.pro.zsba.ex.dao.OrderBaMapper;
import com.zebone.nhis.pro.zsba.ex.support.OrderCheckSortByOrdUtilBA;
import com.zebone.nhis.pro.zsba.ex.vo.OrderCheckVO;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class OrderCheckBaService {
	
	@Resource
	private OrderBaMapper OrderBaMapper ;
	
	
	 /**
     * 根据患者获取待核对医嘱列表
     * @param param{dateStopEnd,pkPvs,euAlways:0:长期，1：临时，9：全部,flagN:新开new,新停stop,作废cancel,其他other}
     * @param user
     * @return
     * @throws ParseException 
     */
    public List<OrderCheckVO> queryOrderCheckList(String param, IUser user) throws ParseException{
    	 Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	 Map<String,Object> newMap = new HashMap<String,Object>();
    	 List<String> pvlist = (List)paramMap.get("pkPvs");
    	 if(pvlist == null || pvlist.size()<=0)
    		 throw new BusException("未获取到患者就诊信息！");
    	 newMap.put("pkPvs", pvlist);
    	 String pk_dept_cur = ((User)user).getPkDept();
    	 newMap.put("pkDeptNs", pk_dept_cur);
    	 if(Application.isSqlServer()){
    		 newMap.put("dbType", "sqlserver");
    	 }else{
    		 newMap.put("dbType", "oracle");
    	 }
    	 if(paramMap.get("flagN")!=null){
    		 newMap.put("flagN", paramMap.get("flagN"));
    	 }
    	 if(paramMap.get("euAlways")!=null&&!paramMap.get("euAlways").equals("9")){
    		newMap.put("euAlways", paramMap.get("euAlways"));
    	 }
    	 if(paramMap.get("ordtype")!=null && !CommonUtils.isEmptyString(paramMap.get("ordtype").toString())){
     		newMap.put("ordtype", paramMap.get("ordtype"));
     	 }
    	 if(paramMap.get("flagEmer")!=null && !CommonUtils.isEmptyString(paramMap.get("flagEmer").toString())){
    		 newMap.put("flagEmer", paramMap.get("flagEmer"));
    	 }
    	 String dateStopEnd  = ExSysParamUtil.getCheckStopEndTime();
    	 newMap.put("dateStopEnd", dateStopEnd);
    	 //排序方式
    	 String ordOrder = ExSysParamUtil.getOrdByOrder();
    	 newMap.put("ordOrder", ordOrder);
    	 
    	 List<OrderCheckVO> ordlist = new ArrayList<OrderCheckVO>();
 		 ordlist = OrderBaMapper.queryOrderCheckList(newMap);
    	 if(ordlist!=null&&ordlist.size()>0){
    		 new OrderCheckSortByOrdUtilBA().ordGroup(ordlist);
    	 }
 		 return  ordlist;
    }
    /**
     * 根据患者获取已核对医嘱列表
     * @param param{pkPvs,euAlways:0:长期，1：临时，9：全部}
     * @param user
     * @return
     * @throws ParseException 
     */
    public List<OrderCheckVO> queryOrderEcList(String param, IUser user) throws ParseException{
    	 Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	 Map<String,Object> newMap = new HashMap<String,Object>();
    	 List<String> pvlist = (List)paramMap.get("pkPvs");
    	 if(pvlist == null || pvlist.size()<=0)
    		 throw new BusException("未获取到患者就诊信息！");
    	 newMap.put("pkPvs", pvlist);
    	 String pk_dept_cur = ((User)user).getPkDept();
    	 newMap.put("pkDeptNs", pk_dept_cur);
    	 if(paramMap.get("euAlways")!=null&&!paramMap.get("euAlways").equals("9")){
    		newMap.put("euAlways", paramMap.get("euAlways"));
    	 }
    	 if(paramMap.get("flagN")!=null){
    		 newMap.put("flagN", paramMap.get("flagN"));
    	 }
    	 if(paramMap.get("ordtype")!=null && !CommonUtils.isEmptyString(paramMap.get("ordtype").toString())){
      		newMap.put("ordtype", paramMap.get("ordtype"));
      	 }
    	 if(Application.isSqlServer()){
    		 newMap.put("dbType", "sqlserver");
    	 }else{
    		 newMap.put("dbType", "oracle");
    	 }
    	 if(paramMap.get("flagEmer")!=null && !CommonUtils.isEmptyString(paramMap.get("flagEmer").toString())){
    		 newMap.put("flagEmer", paramMap.get("flagEmer"));
    	 }
    	 if(newMap.get("flagN") !=null && "new".equals(CommonUtils.getString(newMap.get("flagN")))){
    		 newMap.put("statusOrd", " in ('2','3') ");
    		 newMap.put("chkDate", null);
    	 }if(newMap.get("flagN") !=null && "cancel".equals(CommonUtils.getString(newMap.get("flagN")))){
    		 newMap.put("statusOrd", " in ('9') ");
    		 newMap.put("chkDate", null);
    	 }else{
    		 newMap.put("statusOrd", " >= '2' ");
    	 }

		//博爱项目---医嘱核对---已核对状态下可根据核对时间范围查询
		String flagN = paramMap.get("flagN").toString();
		String dateBegin = "";
		String dateEnd = "";
		if(paramMap.containsKey("dateBegin") && paramMap.containsKey("dateEnd")){
			dateBegin = paramMap.get("dateBegin").toString();
			dateEnd = paramMap.get("dateEnd").toString();
			dateBegin = dateBegin.substring(0,8);
			dateEnd = dateEnd.substring(0,8);
		}
		if(paramMap.get("dateBegin")!=null && !CommonUtils.isEmptyString(paramMap.get("dateBegin").toString())){
			if("new".equals(flagN)){
				newMap.put("dateBegin", dateBegin);
			}else  if("stop".equals(flagN)){
				newMap.put("dateBeginStop", dateBegin);
			}else{
				newMap.put("dateBeginCancel", dateBegin);
			}
		}
		if(paramMap.get("dateEnd")!=null && !CommonUtils.isEmptyString(paramMap.get("dateEnd").toString())){
			if("new".equals(flagN)){
				newMap.put("dateEnd", dateEnd);
			}else if("stop".equals(flagN)){
				newMap.put("dateEndStop", dateEnd);
			}else{
				newMap.put("dateEndCancel", dateEnd);
			}
		}

    	 //排序方式
    	 String ordOrder = ExSysParamUtil.getOrdByOrder();
    	 newMap.put("ordOrder", ordOrder);

    	 List<OrderCheckVO> ordlist = new ArrayList<OrderCheckVO>();
  		 ordlist = OrderBaMapper.queryOrderEcList(newMap);
    	 if(ordlist!=null&&ordlist.size()>0){   		 
    		 new OrderCheckSortByOrdUtilBA().ordGroup(ordlist);
    	 }
    	return ordlist;
    }
 
}
