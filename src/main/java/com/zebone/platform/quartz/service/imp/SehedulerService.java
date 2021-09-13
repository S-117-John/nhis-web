package com.zebone.platform.quartz.service.imp;

import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.quartz.modle.QrtzJobCfg;
import com.zebone.platform.quartz.modle.QrtzJobLog;
import com.zebone.platform.quartz.vo.JobCfgQueryForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


//写接口
@Component
public class SehedulerService{
	
	public List<Map<String, Object>> loadSehedulerYwclTable(JobCfgQueryForm jobCfgQueryForm){
		String sql = "";
		sql = "select c.jobname,c.job_group as jobgroup,c.jobcname,c.cron_expression as cronexpression,c.jobobject,c.jobnote,c.isvisible,c.islog,c.logformat,c.jobparam,c.jgs"
				+ " from qrtz_job_cfg c where 1=1";
		//查询条件
		if(jobCfgQueryForm != null){
			//状态
			if("0".equals(jobCfgQueryForm.getIsvisible()) || "1".equals(jobCfgQueryForm.getIsvisible())){
				sql += " and c.isvisible = "+jobCfgQueryForm.getIsvisible();
			}
			//任务名称
			if(jobCfgQueryForm.getJobcname() != null && jobCfgQueryForm.getJobcname() != ""){
				sql += " and c.jobcname like '"+jobCfgQueryForm.getJobcname()+"%'";
			}
			//任务编码
			if(jobCfgQueryForm.getJobname() != null && jobCfgQueryForm.getJobname() != ""){
				sql += " and c.jobname like '"+jobCfgQueryForm.getJobname()+"%'";
			}
		}
		
		List<Map<String, Object>> joblist = DataBaseHelper.queryForList(sql);
		return joblist;
	}
	
	public Map<String, Object> getSehedulerYwclTable(QrtzJobCfg jobCfg){
		Map<String, Object> m = DataBaseHelper.queryForMap("select c.jobname,c.job_group as jobgroup,c.jobcname,c.cron_expression as cronexpression,c.jobobject,c.jobnote,c.isvisible,c.islog,c.logformat,c.jobparam"
				+ ",c.jgs from qrtz_job_cfg c where c.jobname = ?",jobCfg.getJobname());
		return m;
	}
	
	public void setSehedulerYwclTable(QrtzJobCfg jobCfg, String isvisible){
		String sql = "update qrtz_job_cfg set isvisible = "+isvisible+" where jobname = ?";
		DataBaseHelper.update(sql,new Object[]{jobCfg.getJobname()});
	}
	
	public void addSehedulerYwclTable(QrtzJobCfg jobCfg) {
		String msg="";
		// 判断任务代码是否已经存在
	
		int userCnt = DataBaseHelper.queryForScalar(
				"select count(1) from QRTZ_JOB_CFG a where a.JOBNAME = ?",Integer.class,
				new Object[] { jobCfg.getJobname() });
		if (userCnt > 0) {
			msg = "已存在该任务代码!请重新输入!";	
			throw new RuntimeException(msg);

		} else {
			jobCfg.setJobgroup(jobCfg.getJobname()+"_Group");
			DataBaseHelper.insertBean(jobCfg);
		}
	}
	
	public void updateSehedulerYwclTable(QrtzJobCfg jobCfg) {
		String msg="";
		int userCnt = DataBaseHelper.queryForScalar(
				"select count(1) from QRTZ_JOB_CFG a where a.JOBNAME = ?",Integer.class,
				new Object[] { jobCfg.getJobname() });
		if (userCnt <= 0) {
			msg = "此任务已经不存在!";	
			throw new RuntimeException(msg);

		} else {
			DataBaseHelper.updateBeanByPk(jobCfg,false);
		}
		
	}
	public void deleteSehedulerYwclTable(List<QrtzJobCfg> jobCfglist){
		if(jobCfglist != null && jobCfglist.size() != 0){
			for(QrtzJobCfg jobCfg : jobCfglist){
				
				DataBaseHelper.update("delete from QRTZ_JOB_CFG where JOBNAME =? and JOB_GROUP =?",jobCfg.getJobname(), jobCfg.getJobgroup());
			}
		}
	}
	
	
	/**
	 * 记录定时任务运行日志
	 * @param data
	 */
	public void addSehedulerLog(QrtzJobLog jobLog) {
		DataBaseHelper.insertBean(jobLog);

	}
	
	public void addLog(Map data) {
		Object[] value = new Object[4];
		Object[] obj = new Object[3];
		obj[0] = data.get("MSG_CONTENT_ID");
		//System.out.println(obj[0]);
		if(obj[0]!=null){
			value[0] = data.get("SEND_USER_ID");
			value[2] = data.get("TITLE");
			value[3] = data.get("MSG_CONTENT_ID");
		
			
			obj[1] = data.get("NAME");
			obj[2] = data.get("C_VALUE");
			String str=data.get("RECEIVE_USER_ID").toString();
			String [] array= str.split(",");
			for (int i = 0; i < array.length; i++) {
				value[1]=array[i];
				DataBaseHelper.update("insert into CPT_MESSAGE(ID,TYPE,STATUS,SEND_USER_ID,RECEIVE_USER_ID,TITLE,MSG_CONTENT_ID)values (seq_cpt_message.nextval,1,0,?,?,?,?)",value);
			}
			DataBaseHelper.update("insert into CPT_MESSAGE_CONTENT(ID,NAME,C_VALUE)values (?,?,?)",obj);
		}
	}
	
	/**
	 * 任务运行
	 */
	@SuppressWarnings("unchecked")
	public void insertLog(QrtzJobLog  log ){
		String message = null;
		//数据库是BGK编码，varchar2(4000)按照字节长度来截取，否则有中文时按照字符截取是不行滴
		if(log!=null && StringUtils.isNotBlank((message = log.getJobmessage())) && message.length()>4000){
			try{
				byte [] b = message.getBytes("GBK");
				message = new String(Arrays.copyOf(b,3998),"GBK");
			} catch (Exception e){
			}
			log.setJobmessage(message);
		}
		DataBaseHelper.insertBean(log);
	}
	
}
