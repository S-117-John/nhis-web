package com.zebone.nhis.task.pub.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.quartz.modle.QrtzJobCfg;

/**
 * 清理系统日志定时任务
 * @author yangxue
 *
 */
@Service
public class ClearSysLogTaskService {
	/**
	 * 删除一个月以前的系统日志
	 * @param cfg
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map delLogTask(QrtzJobCfg cfg){
		Map<String, Object>  paramMap = JsonUtil.readValue(cfg.getJobparam(), Map.class);
		//系统日志
		int sysLogDays = MapUtils.getIntValue(paramMap, "sysLogDays", 14);
		//应用日志
		int appLogDays = MapUtils.getIntValue(paramMap, "appLogDays", 7);
		//获取设置的时间区间之前的日期
		String lastMonth = DateUtils.getSpecifiedDateStr(new Date(), -appLogDays)+"000000";
		String lastWeek = DateUtils.getSpecifiedDateStr(new Date(), -sysLogDays)+"000000";
		String sqlq = " delete from qrtz_job_log where create_time < to_date('"+lastWeek+"','YYYYMMDDHH24MISS')";
		String sqls = " delete from sys_log where create_time < to_date('"+lastWeek+"','YYYYMMDDHH24MISS')";
		String sqla = " delete from sys_applog where create_time < to_date('"+lastMonth+"','YYYYMMDDHH24MISS')";
		String sessionsql = " delete from sys_log_session where createtime < to_date('"+lastWeek+"','YYYYMMDDHH24MISS')";
		int delq = DataBaseHelper.execute(sqlq, new Object[]{});
		int dels = DataBaseHelper.execute(sqls, new Object[]{});
		int dela = DataBaseHelper.execute(sqla, new Object[]{});
		DataBaseHelper.execute(sessionsql, new Object[]{});
		Map<String,Object>  result = new HashMap<String,Object>();
		String msg = "删除定时任务日志"+delq+"条！删除系统日志"+dels+"条！删除系统应用日志"+dela+"条！";
		result.put("msg",msg);
		return result;
		
	}
}
