package com.zebone.nhis.task.mz.service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.task.ma.service.MsgTaskService;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.quartz.modle.QrtzJobCfg;

/**
 * sch_sch排班处理服务
 * @author chengjia
 *
 */
@Service
public class SchTaskService {
	private static Logger loger = org.slf4j.LoggerFactory.getLogger(SchTaskService.class.getName());
	
	/***
	 * @Description  HL7消息日志定时清除，3天内消息清除
	 * 本来应该是所有消息全部清除，但灵璧项目出现过，需要重发所有患者出科消息，
	 * 故进行数据备份，是否备份可自行在msg文件中设置
	 *
	 * @auther tongjiaqi
	 * @Date 2019-11-26
	 * @Param [cfg]
	 * @return java.util.Map
	 */
		public Map delSchLogTask(QrtzJobCfg cfg) throws ParseException {
			Map<String,Object>  result = new HashMap<String,Object>();
			//获取3天前时间节点
			String  lastMonth = DateUtils.getSpecifiedDateStr(new Date(), -2)+"000000";
			
			 String sql="insert into SCH_SCH_P select * from SCH_SCH sch where sch.date_work < to_date(?,'YYYYMMDDHH24MISS')";
	 		 int inslq = DataBaseHelper.execute(sql,new Object[]{lastMonth});
	 			    if(inslq>0){
	 			    	String sqls= "delete from sch_ticket tic WHERE exists (select pk_sch from SCH_SCH sch where sch.pk_sch=tic.pk_sch and sch.date_work < to_date(?,'YYYYMMDDHH24MISS')) ";
	 			    	 int schTicketCoun=DataBaseHelper.execute(sqls,new Object[]{lastMonth});
	 			    	
	 			    	String sqlss= " delete from SCH_SCH where date_work < to_date( ?,'YYYYMMDDHH24MISS')";
	 			    	 int schCoun=DataBaseHelper.execute(sqlss,new Object[]{lastMonth});
	 			    	 
	 			    	 /*sql= " delete from pv_que where TS < to_date( "+lastMonth+",'YYYYMMDDHH24MISS')";
	 			    	 int pvQueCoun=DataBaseHelper.execute(sql,new Object[]{});
	 			    	 loger.info("删除定时pv_que数据" + pvQueCoun + "条！");
	 			    	 result.put("pvQueCoun","删除定时pv_que数据" + pvQueCoun + "条！");*/
	 			    }  

		    return result;
		}
}
