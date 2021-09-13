package com.zebone.nhis.task.ex.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.pub.service.ExPvIpdailyTimeTaskService;
import com.zebone.nhis.ex.pub.service.FixedCostTimeTaskService;
import com.zebone.nhis.ex.pub.service.GenerateExlistTimeTaskService;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.quartz.modle.QrtzJobCfg;
/**
 * 护士站定时任务入口
 * @author yangxue
 *
 */
@Service
public class ExTaskService {
	
	@Resource
	private GenerateExlistTimeTaskService generateExlistTimeTaskService;
	@Resource
	private FixedCostTimeTaskService fixedCostTimeTaskService;
	@Resource
	private ExPvIpdailyTimeTaskService exPvIpdailyService;
	
	private Logger logger = LoggerFactory.getLogger("nhis.quartz");
	
	/**
	 * 固定计费定时任务
	 * @param cfg
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map fixedCostTask(QrtzJobCfg cfg){
		String pkOrg = cfg.getJgs();
		if(CommonUtils.isEmptyString(pkOrg))
			throw new BusException("未获取到机构信息！");
		String[] orgarr = pkOrg.split(",");
		List<Map<String,String>> resultArr = new ArrayList<Map<String,String>>();
		if(orgarr!=null&&orgarr.length>0){
			logger.info("================调用固定记费定时任务开始================");
			for(int i = 0; i<orgarr.length;i++){
				User u = new User();  
				u.setPkOrg(orgarr[i]);
				UserContext.setUser(u);
				Map<String,String> result = fixedCostTimeTaskService.exec(orgarr[i]);
				if(result!=null){
					resultArr.add(result);
				}
			}
			logger.info("================调用固定记费定时任务结束================");
		}
		if(resultArr == null || resultArr.size()<=0){
			return null;
		}else if(resultArr.size()==1){
			return resultArr.get(0);
		}else{
			StringBuilder msg = new StringBuilder("");
			StringBuilder name_dept_str = new StringBuilder("");
			for(Map<String,String> map : resultArr){
				if(!CommonUtils.isEmptyString(map.get("msg"))){
					msg.append(map.get("msg")+";");
				}
				if(!CommonUtils.isEmptyString(map.get("customStatus"))){
					msg.append(map.get("customStatus")+";");
				}
			}
			Map<String,String> result = new HashMap<String,String>();
			if(msg != null && !msg.toString().equals("")){
				result.put("msg",msg.toString());
			}
			if(name_dept_str != null && !name_dept_str.toString().equals("")){
				result.put("customStatus", name_dept_str.toString());
			}
			return result;
		}
	}
	/**
	 * 生成执行单定时任务
	 * @param cfg
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map exListTask(QrtzJobCfg cfg){
		String pkOrg = cfg.getJgs();
		if(CommonUtils.isEmptyString(pkOrg))
			throw new BusException("未获取到机构信息！");
		String[] orgarr = pkOrg.split(",");
		List<Map<String,String>> resultArr = new ArrayList<Map<String,String>>();
		if(orgarr!=null&&orgarr.length>0){
			for(int i = 0; i<orgarr.length;i++){
				User u = new User();  
				u.setPkOrg(orgarr[i]);
				UserContext.setUser(u);
				Map<String,String> result = generateExlistTimeTaskService.exec(orgarr[i]);
				if(result!=null){
					resultArr.add(result);
				}
			}
		}
		if(resultArr == null || resultArr.size()<=0){
			return null;
		}else if(resultArr.size()==1){
			return resultArr.get(0);
		}else{
			StringBuilder msg = new StringBuilder("");
			StringBuilder name_dept_str = new StringBuilder("");
			for(Map<String,String> map : resultArr){
				if(!CommonUtils.isEmptyString(map.get("msg"))){
					msg.append(map.get("msg")+";");
				}
				if(!CommonUtils.isEmptyString(map.get("customStatus"))){
					msg.append(map.get("customStatus")+";");
				}
			}
			Map<String,String> result = new HashMap<String,String>();
			if(msg != null && !msg.toString().equals("")){
				result.put("msg",msg.toString());
			}
			if(name_dept_str != null && !name_dept_str.toString().equals("")){
				result.put("customStatus", name_dept_str.toString());
			}
			return result;
		}
		
	}
	
	/**
	 * 生成病区日报定时任务
	 * @param cfg
	 */
	@SuppressWarnings("rawtypes")
	public void deptDayReportTask(QrtzJobCfg cfg){
		try {
			String pkOrg = cfg.getJgs();
			if(CommonUtils.isEmptyString(pkOrg)){
				throw new BusException("未获取到机构信息！");
			}
			String [] orgArr=pkOrg.split(",");
			if(orgArr!=null&&orgArr.length>0){
				exPvIpdailyService.statDeptReport(orgArr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
