package com.zebone.platform.quartz.service.imp;

import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.quartz.CommonJob;
import com.zebone.platform.quartz.modle.QrtzJobCfg;
import com.zebone.platform.quartz.modle.QrtzJobLog;
import com.zebone.platform.quartz.service.IJobHandler;
import com.zebone.platform.quartz.vo.JobCfgQueryForm;
import com.zebone.platform.quartz.vo.JobLogQueryForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Properties;


@Service("ttschedulerFactory")
public class SchedulerFactoryService implements com.zebone.platform.quartz.service.ISchedulerFactoryService {
	
	 static Logger logger = LogManager.getLogger(SchedulerFactoryService.class.getName());


	@Autowired
	private IJobHandler jobHandler;
	
	private Scheduler scheduler;
	@Autowired
	private SehedulerService sehedulerDao;
	@Autowired
	private DataSource dataSource;
	@Autowired
	private Properties applicationProperties;
	
	@Autowired
	public JdbcTemplate jdbcTemplate;
	
	@Autowired
	public ServiceLocator serviceLocator;


	@Override
	public List<Map<String, Object>> loadJob(JobCfgQueryForm jobCfgQueryForm){
		List<Map<String, Object>> joblist = null;
	    try{ 
	    	joblist = sehedulerDao.loadSehedulerYwclTable(jobCfgQueryForm);
	    }catch(Exception e){ 
		    throw new RuntimeException(e.getMessage());
	    }
		return joblist;
	}

	@Override
	public Map<String, Object> getQuartzByJobname(QrtzJobCfg jobCfg){
		Map<String, Object> jobCfgMap = null;
		try {
			jobCfgMap = sehedulerDao.getSehedulerYwclTable(jobCfg);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return jobCfgMap;
	}
	
	@Override
	public void addJob(QrtzJobCfg jobCfg){
		try{ 
			//?????????????????????????????????
			sehedulerDao.addSehedulerYwclTable(jobCfg);
			addQuartz(jobCfg);
		  }catch(Exception e){ 
			  logger.error("??????????????????????????????????????????",e);
			  throw new RuntimeException(e);
		  }
	}
	
	
	@Override
	public void autoRunJob(QrtzJobCfg jobcfg){
		jobHandler.autoRunJob(jobcfg);
	}
	
	
	@Override
	public void updateJob(QrtzJobCfg jobCfg){
		try{ 
			String job = jobCfg.getJobname();
			if(scheduler == null){
	    		throw new BusException("????????????????????????");
	    	}
			JobKey jobKey = new JobKey(job,  job+"_Group");
	    	if(scheduler.getJobDetail(jobKey) != null){
	    		scheduler.deleteJob(jobKey);
	    	}
			sehedulerDao.updateSehedulerYwclTable(jobCfg);
			addQuartz(jobCfg);
		  }catch(Exception e){ 
			  throw new RuntimeException(e.getMessage());
		  }
	}
	
	public static String getParamString(Map data , String key){
		Object okey = data.get(key); 
		if(okey !=null && !okey.toString().trim().equals("")){
			return okey.toString();
		}else{
			throw new RuntimeException("???????????????????????????");
		}
	}
	
	
	@Override
	@PostConstruct
	public void initQuartz(){
		CommonJob.setJobService(jobHandler);
		try{

			String initv = applicationProperties.getProperty("org.quartz.init","false");
			if(!"true".equals(initv)){
				return;
			}
			DataBaseHelper.setJdbcTemplate(jdbcTemplate);
			
			SchedulerFactoryBean sfb = new SchedulerFactoryBean();
			sfb.setConfigLocation(new ClassPathResource("quartz.properties"));
			sfb.setDataSource(dataSource);
			sfb.afterPropertiesSet();
			scheduler = sfb.getObject(); 
	
			scheduler.start();
		  }catch(Exception e){ 
			  CommonJob.setJobService(jobHandler);
			  throw new RuntimeException(e.getMessage());
		  }
	}

	@Override
	public void startQuartz(){
		try{
			if(scheduler == null){
	    		throw new BusException("????????????????????????");
	    	}
		    scheduler.resumeAll(); 
		  }catch(Exception e){ 
			  throw new RuntimeException(e.getMessage());
		  }
	}
	
	
	@Override
	public void stopQuartz(){
		try{  
			if(scheduler == null){
	    		throw new BusException("????????????????????????");
	    	}
			scheduler.pauseAll();
		  }catch(Exception e){ 
			  throw new RuntimeException(e.getMessage());
		  }
	}
	

	@Override
	public void stopJob(QrtzJobCfg jobCfg) throws BusException {
		//?????????????????????????????????????????????
		Map<String, Object> m = sehedulerDao.getSehedulerYwclTable(jobCfg);
		String isvisible = m.get("isvisible").toString();
		if("1".equals(isvisible)){//0--??????  1--?????????
			throw new BusException("????????????????????????????????????");
		}else{
			//??????
			try{  
				sehedulerDao.setSehedulerYwclTable(jobCfg, "1");
				
				if(scheduler == null){
		    		throw new BusException("????????????????????????");
		    	}
				
				String job = jobCfg.getJobname();
				JobKey jobKey = new JobKey(job,  job+"_Group");
				scheduler.deleteJob(jobKey);
			}catch(Exception e){ 
				throw new RuntimeException(e.getMessage());
			}
		}
	}
	
	
	@Override
	public void startJob(QrtzJobCfg jobCfg) throws BusException {
		Map<String, Object> m = sehedulerDao.getSehedulerYwclTable(jobCfg);
		String isvisible = m.get("isvisible").toString();
		if("0".equals(isvisible)){//0--??????  1--?????????
			throw new BusException("????????????????????????????????????");
		}else{
			//??????
			try{  
				sehedulerDao.setSehedulerYwclTable(jobCfg, "0");
				String job = jobCfg.getJobname();
				JobKey jobKey = new JobKey(job,  job+"_Group");
			    if(scheduler.getJobDetail(jobKey) != null){
			    	scheduler.deleteJob(jobKey);
			    }
				addQuartz(jobCfg);
			}catch(Exception e){ 
				throw new RuntimeException(e.getMessage());
			}
		}
	}
	

	@Override
	public void deleteJob(List<QrtzJobCfg> jobCfglist){
		try{  
			sehedulerDao.deleteSehedulerYwclTable(jobCfglist);	
			
			if(scheduler == null){
	    		throw new BusException("????????????????????????");
	    	}
			
		    for(QrtzJobCfg cfg : jobCfglist ){
		    	String job = cfg.getJobname();
				JobKey jobKey = new JobKey(job,  job+"_Group");
		    	if(scheduler.getJobDetail(jobKey) != null){
		    		scheduler.deleteJob(jobKey);
		    	}
		    
		    }
		  }catch(Exception e){ 
			  throw new RuntimeException(e.getMessage());
		  }
	}
	
	
	
	@Override
	public Scheduler getScheduler() {
		return scheduler;
	}

	
	@Override
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	

	@Override
	public IJobHandler getJobService() {
		return jobHandler;
	}


	@Override
	public void setJobService(IJobHandler jobService) {
		this.jobHandler = jobService;
	}
	
	
	@Override
	public void addQuartz(QrtzJobCfg jobCfg ){
		try{ 
			String job = jobCfg.getJobname();
			Object exe = jobCfg.getCronexpression();
			String trigger = job+"_trigger";
			//??????????????????
		    JobDataMap jobmap = new JobDataMap();
		    jobmap.put("jobBody",jobCfg);
		    //??????????????????
			JobDetail jobDetail = JobBuilder.newJob(CommonJob.class)
					.withIdentity(new JobKey(job, job + "_Group"))
					.setJobData(jobmap)
					.build();

			//?????????????????????
			CronScheduleBuilder cronScheduleBuilder;
			final String defaultCron = "0/10 * * * * ?";
			try{
				cronScheduleBuilder = CronScheduleBuilder.cronSchedule((exe !=null && !exe.toString().trim().equals(""))?exe.toString():defaultCron);
			}catch(Exception e){
				cronScheduleBuilder = CronScheduleBuilder.cronSchedule(defaultCron);
			}
			CronTrigger cronTrigger = TriggerBuilder.newTrigger()
					.withIdentity(trigger, trigger + "_Group")
					.withSchedule(cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed()).build();

			if(scheduler == null){
	    		throw new BusException("????????????????????????");
	    	}
	    	
	    	//??????????????????
		    scheduler.scheduleJob(jobDetail,cronTrigger);
		    
		    if(!scheduler.isStarted()){
		    	scheduler.start(); 
		    }
		  }catch(Exception e){ 
			  e.printStackTrace();
			  logger.error("???????????????????????????", e);
			  throw new RuntimeException(e.getMessage());
		  }
	}

	
	@Override
	public Page<?> loadQuartzLog(int pageIndex, int pageSize, JobLogQueryForm jobLogQueryForm) {
		String sql = "";
		sql = "select id,jobname,jobcname,jobmessage,jobtype,create_time from qrtz_job_log where 1=1";
		if(jobLogQueryForm != null){
			//??????
			if(0 == jobLogQueryForm.getJobtype() || 1 == jobLogQueryForm.getJobtype() || 2 == jobLogQueryForm.getJobtype()){
				sql += " and jobtype = " + jobLogQueryForm.getJobtype();
			}
			//??????????????????
			if(jobLogQueryForm.getJobcname() != null && jobLogQueryForm.getJobcname() != ""){
				sql += " and jobcname like '" +jobLogQueryForm.getJobcname()+ "%'";
			}
			//??????????????????
			if(jobLogQueryForm.getJobname() != null && jobLogQueryForm.getJobname() != ""){
				sql += " and jobname like '" +jobLogQueryForm.getJobname()+ "%'";
			}
		}
		sql += " order by create_time desc";
		Page<?> joblogpage = DataBaseHelper.queryForPage(sql, pageIndex, pageSize);
		return joblogpage;
	}

	@Override
	public Map<String, Object> getQuartzLogById(QrtzJobLog joblog) {
		Map<String, Object> queryForMap = null;
		if(joblog != null){
			queryForMap = DataBaseHelper.queryForMap("select * from qrtz_job_log where id = ?", joblog.getId());
		}
		return queryForMap;
	}

	@Override
	public List<Map<String, Object>> queryAllOrgs() {
		// ???????????????????????????
		List<Map<String, Object>> orgslist = DataBaseHelper.queryForList("select pk_org,code_org,name_org,pk_father,eu_level,flag_active from bd_ou_org where del_flag='0'");
		return orgslist;
	}

	@Override
	public void updateJgsByName(QrtzJobCfg jobCfg) {
		DataBaseHelper.update("update qrtz_job_cfg set jgs = ? where jobname = ?", jobCfg.getJgs(), jobCfg.getJobname());
	}

}
