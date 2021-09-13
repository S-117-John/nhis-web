package com.zebone.nhis.task.ma.service;

import ca.uhn.hl7v2.model.v24.segment.MSH;

import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.zb.comm.Hl7MsgHander;
import com.zebone.nhis.ma.pub.platform.zb.dao.MsgRecMapper;
import com.zebone.nhis.ma.pub.platform.zb.utils.MsgUtils;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.quartz.modle.QrtzJobCfg;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Hl7消息任务处理服务
 * @author chengjia
 *
 */
@Service
public class MsgTaskService {

	private static Logger loger = org.slf4j.LoggerFactory.getLogger(MsgTaskService.class.getName());

	@Resource
	private	MsgRecMapper msgRecMapper;
	
	@Resource
	private	MsgUtils msgUtils;
	
	@Resource
	private	Hl7MsgHander msgHander;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	private String msg;

	public Map executeTask(QrtzJobCfg cfg){
		String pkOrgs = cfg.getJgs();
		if(CommonUtils.isEmptyString(pkOrgs)) throw new BusException("未获取到机构信息！");
		Date begin = new Date();
		String[] orgs = pkOrgs.split(",");
		String pkOrgStr="";
		if(orgs!=null&&orgs.length>0){
			for(int i = 0; i<orgs.length;i++){
				if(pkOrgStr.equals("")){
					pkOrgStr="('"+orgs[i];
				}else{
					pkOrgStr=pkOrgStr+"','"+orgs[i];
				}
			}
			pkOrgStr=" and rec.pk_org in "+ pkOrgStr+"')";
		}
		
		Calendar now = Calendar.getInstance();
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.add(Calendar.DATE, 1);
		String endDate = sdf.format(now.getTime());
		now.add(Calendar.MONTH, -1);
		String beginDate = sdf.format(now.getTime());
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("beginDate", beginDate);
		map.put("endDate", endDate);
		map.put("pkOrgStr", pkOrgStr);
		map.put("msgStatus", "SAVE");
		List<SysMsgRec> list=msgRecMapper.queryMsgList(map);
		if(list!=null&&list.size()>0){
			for (SysMsgRec rec : list) {
				msg = rec.getMsgContent();
				if(msg==null||msg.equals("")) continue;
				
				MSH msh=MsgUtils.getMessageMsh(msg);
				String msgId=msh.getMessageControlID().getValue();
				
				msgHander.sendMsg(msgId, msg,false);
			}
		}
		return null;
	}
/***
 * @Description  HL7消息日志定时清除，3天内消息清除
 * 本来应该是所有消息全部清除，但灵璧项目出现过，需要重发所有患者出科消息，
 * 故进行数据备份，是否备份可自行在msg文件中设置
 *
 * @auther wuqiang
 * @Date 2019-11-03
 * @Param [cfg]
 * @return java.util.Map
 */
	public Map delHL7LogTask(QrtzJobCfg cfg) throws ParseException {
		Map<String,Object>  result = new HashMap<String,Object>();
		String msg="";
		String table = ApplicationUtils.getPropertyValue("msg.back.table","");
		Integer days=7;
		Map<String,Object> paramMap = JsonUtil.readValue(cfg.getJobparam(), Map.class);
		if(paramMap!=null && CommonUtils.isNotNull(paramMap.get("days"))){
			days= MapUtils.getInteger(paramMap,"days");
			if(CommonUtils.isNull(days) ||days==0){
				days=7;
			}
		}
		//msg 配置文件配置表名 进行数据备份，否则直接物理删除
		if (!table.isEmpty()){
			//直接删除4天前所有日志
            String backNtime=DateUtils.getSpecifiedDateStr(new Date(), -days)+"000000";
			
            String sql="select * from all_tables where ALL_TABLES.TABLE_NAME=?";
            int count=DataBaseHelper.queryForScalar(sql,Integer.class,"SYS_MSG_REC_BACK");
            if (count<=0){
            	sql= "CREATE table SYS_MSG_REC_BACK as select * from SYS_MSG_REC ";
            	DataBaseHelper.execute(sql);
            	sql= "delete from SYS_MSG_REC_BACK where TS > to_date( "+backNtime+",'YYYYMMDDHH24MISS')";
			    DataBaseHelper.execute(sql,new Object[]{});
 		   }else{
 			  sql="insert into SYS_MSG_REC_BACK select * from SYS_MSG_REC rec where rec.TS < to_date(?,'YYYYMMDDHH24MISS')";
 			    int inslq = DataBaseHelper.execute(sql,backNtime);
 			    result.put("inslq","移动到SCH_SCH_P数据"+inslq+"条！");
 			    if(inslq>0){
 			    	 sql= " delete from SYS_MSG_REC where TS < to_date( "+backNtime+",'YYYYMMDDHH24MISS')";
 			    	 DataBaseHelper.execute(sql,new Object[]{});
 			    }   
 		   }
		    return result;
		} else {
			//获取9天前时间节点
			String lastMonth = DateUtils.getSpecifiedDateStr(new Date(), -days) + "000000";
			String sqlss = " delete from SYS_MSG_REC where trans_date < to_date( ?,'YYYYMMDDHH24MISS')";
			int coun = DataBaseHelper.execute(sqlss, new Object[]{lastMonth});
			msg = "删除定时任务日志" + coun + "条！";
			result.put("msg", msg);
			return result;
		}
	}


 }