package com.zebone.platform.quartz.service;

import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.quartz.modle.QrtzJobCfg;
import com.zebone.platform.quartz.modle.QrtzJobLog;
import com.zebone.platform.quartz.vo.JobCfgQueryForm;
import com.zebone.platform.quartz.vo.JobLogQueryForm;
import org.quartz.Scheduler;

import java.util.List;
import java.util.Map;

public interface ISchedulerFactoryService {

	List<Map<String, Object>> loadJob(JobCfgQueryForm jobCfgQueryForm);

	Map<String, Object> getQuartzByJobname(QrtzJobCfg jobCfg);

	void addJob(QrtzJobCfg jobCfg);

	/**
	 * 手动执行任务
	 * @param jobcfg
	 */
	void autoRunJob(QrtzJobCfg jobcfg);

	void updateJob(QrtzJobCfg jobCfg);

	/**
	 * 系统启动时初始化任务
	 */
	void initQuartz();

	/**
	 * 恢复所有任务
	 */
	void startQuartz();

	/**
	 * 停止所有任务
	 */
	void stopQuartz() ;

	/**
	 * 停止调度任务 ， （删除调度器中调度任务）
	 * @param jobCfg
	 */
	void stopJob(QrtzJobCfg jobCfg) throws BusException;

	/**
	 * 启动调度任务 ， （增加调度器中调度任务）
	 * @param jobCfg
	 */
	void startJob(QrtzJobCfg jobCfg) throws BusException;

	/**
	 * 删除调度任务
	 * @param jobCfg
	 */
	void deleteJob(List<QrtzJobCfg> jobCfglist);

	Scheduler getScheduler();

	void setScheduler(Scheduler scheduler);

	IJobHandler getJobService();

	void setJobService(IJobHandler jobService);

	/**
	 * 增加调度任务
	 * @param jobCfg
	 */
	void addQuartz(QrtzJobCfg jobCfg);
	
	/**日志的相关操作*/
	public Page<?> loadQuartzLog(int pageIndex, int pageSize, JobLogQueryForm jobLogQueryForm);
	
	public Map<String, Object> getQuartzLogById(QrtzJobLog joblog);
	
	/**机构相关操作*/
	public List<Map<String, Object>> queryAllOrgs();
	
	public void updateJgsByName(QrtzJobCfg jobCfg);
}