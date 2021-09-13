package com.zebone.nhis.task.ex.service;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.quartz.modle.QrtzJobCfg;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 【定时任务】--增加删除临床医嘱定时任务
 * @author dell
 *
 */
@Service
public class ExCnNoticeTaskService {
	private static Logger log = LoggerFactory.getLogger("nhis.quartz");
	/**
	 * 定时*删除临床医嘱提示信息
	 * @param cfg
	 * @return
	 */
	@Transactional
	public Map executeTask(QrtzJobCfg cfg){
		Map<String, Object> paramMap = JsonUtil.readValue(cfg.getJobparam(), Map.class);
		int count=DataBaseHelper.execute("delete cn_notice where eu_status ='2'");
		int sysLogDays = MapUtils.getIntValue(paramMap, "sysLogDays", 14);
		//获取设置的时间区间之前的日期
		String lastWeek = DateUtils.getSpecifiedDateStr(new Date(), -sysLogDays)+"000000";
		String sqlq = " delete from cn_notice where create_time < to_date('"+lastWeek+"','YYYYMMDDHH24MISS')";
		int delq = DataBaseHelper.execute(sqlq, new Object[]{});
		Map<String,Object>  result = new HashMap<String,Object>();
		String msg = "删除临床消息"+delq+"条！";
		result.put("msg",msg);
		return result;
	}
}
