package com.zebone.platform.quartz.service.imp;


import com.zebone.nhis.common.support.MessageUtils;
import com.zebone.platform.Application;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.quartz.modle.QrtzJobCfg;
import com.zebone.platform.quartz.modle.QrtzJobLog;
import com.zebone.platform.quartz.service.IJobHandler;
import com.zebone.platform.web.support.DataOption;
import com.zebone.platform.web.support.DataSupport;
import com.zebone.platform.web.support.ResponseJson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

/**
 * 任务处理的统一代理类
 * @author lingjun
 *
 */

@Service
public class JobHandler implements IJobHandler {
	

	 static Logger logger = LogManager.getLogger(JobHandler.class);


	@SuppressWarnings("unused")
	@Autowired
	private SehedulerService sehedulerDao;
	
	/**
	 * 任务运行
	 */
	@SuppressWarnings("unchecked")
	public void run(JobExecutionContext context){
		
		QrtzJobCfg jobcfg =  (QrtzJobCfg) context.getJobDetail().getJobDataMap().get("jobBody");
		autoRunJob(jobcfg);
		
	}
	
   /**
    * 根据任务运行参数，执行具体任务接口
    */
	@SuppressWarnings("unchecked")
	public void autoRunJob(QrtzJobCfg jobcfg){
		
		QrtzJobLog  log = new QrtzJobLog ();
		log.setJobname(jobcfg.getJobname());		
		log.setJobcname(jobcfg.getJobcname());
		DataOption actionoption = new DataOption();
		
		try{
		
			if(jobcfg.getJobobject() != null && !jobcfg.getJobobject().trim().equals("")){
	
				String[] jobos = jobcfg.getJobobject().split("\\.");
				actionoption.setJclass(jobos[0]);
				actionoption.setJmethod(jobos[1]);
				Date exBegin = new Date();//执行开始时间
				
				ResponseJson rs = JobHandler.doJava(actionoption, jobcfg);
	            String logmsg = "执行" + jobcfg.getJobcname() + calDruingTime(exBegin);
				if(rs.getStatus() >= 0){
					log.setJobtype((short) 0);
					if(jobcfg.getLogformat() != null){ //保存日志信息
						if(rs.getData() != null && rs.getData() instanceof Map){
							//杨雪添加，支持自定义任务状态
							Object customStatus = ((Map)rs.getData()).get("customStatus");
							if(customStatus != null && !"".equals(customStatus.toString()) ){
								log.setJobtype((short) 1);
								log.setJobmessage(logmsg + "任务部分失败 !" + customStatus.toString());
							}else{
								log.setJobmessage(logmsg + "任务成功 !" + MessageUtils.getMessageByFormat(jobcfg.getLogformat(),(Map)rs.getData()));
							}
						}else{
							log.setJobmessage(logmsg + "任务成功！" + jobcfg.getLogformat());
						}
					}else{
						log.setJobmessage(logmsg + "任务成功！");
					}
					sehedulerDao.insertLog(log);
				}else{
					if(rs.getStatus() == (short)-1){
						log.setJobtype((short) 1);
					}else{
						log.setJobtype((short) 2);
					}
					log.setJobmessage(logmsg+"任务失败，提示信息："+rs.getDesc());
					sehedulerDao.insertLog(log);
				}
			}
		}catch(Exception e){
			log.setJobtype((short) 2);
			log.setJobmessage("执行"+jobcfg.getJobcname()+"任务失败，提示信息："+e.getMessage());
			sehedulerDao.insertLog(log);
		}
		
	}
	
	

	
	private static String serviceScope = null;
	/**
	 * 
	 * @param actionoption
	 * @param p
	 * @param user
	 * @return    status( -1 :业务类型异常   -2：业务处理中异常信息     -3：框架中异常   -11 ：数据插入长度超长  )
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	
	private static ResponseJson doJava(DataOption actionoption, QrtzJobCfg jobcfg ) {
		ResponseJson result  = new ResponseJson();
		try {
			if(serviceScope == null){
				serviceScope = Application.getApplicationSettings().getProperty("service.scope", "");
			}

			Object bean = null;
			String beanName = actionoption.getJclass().substring(actionoption.getJclass().lastIndexOf(".")+1);
			beanName = beanName.substring(0,1).toLowerCase()+beanName.substring(1);
			
			String nbeanName =serviceScope + beanName;	
			
			try{
				bean = ServiceLocator.getInstance().getBean(nbeanName);
			}catch(Exception ec){
				bean = ServiceLocator.getInstance().getBean(beanName);
			}
	
			Method method = bean.getClass().getMethod(actionoption.getJmethod(), QrtzJobCfg.class);

		
			// 通过反射执行方法，得到结果
		     Object obj =  method.invoke(bean, jobcfg);
		     if(obj != null){
		    	 if(obj instanceof ResponseJson){
		    		 return (ResponseJson)obj;
		    	 }else if(obj instanceof Page){
		    		Page page = (Page)obj;
		    		result.setData(page.getRows());
		    		result.setTotal(page.getTotalCount());
		    	 }else{
		    		 result.setData(obj);
		    	 }
		     }
		} catch (InvocationTargetException e) {
			String msg = "";  
		  
            Throwable targetEx = ((InvocationTargetException) e).getTargetException();  
            if (targetEx != null) {  
            	
            	
                msg = targetEx.getMessage();  
                
                if(targetEx instanceof BusException){
                	logger.error("【"+actionoption.getJclass()+"."+actionoption.getJmethod()+"()】服务方法业务执行异常：" + msg);
                	result.setStatus(-1);
        			result.setDesc(msg);
        			return result;
                }else if(targetEx instanceof UncategorizedSQLException){

                	SQLException sqle =  ((UncategorizedSQLException) targetEx).getSQLException();
                	
                	logger.error("【"+actionoption.getJclass()+"."+actionoption.getJmethod()+"()】服务方法数据执行异常：" + sqle.getMessage());
                	if(sqle.getErrorCode() == 12899){ //字符超长
                       	result.setStatus(-11);
            			result.setDesc(sqle.getMessage());
            			return result;
                	}
                }
                else{
                	logger.error("【"+actionoption.getJclass()+"."+actionoption.getJmethod()+"()】服务方法处理异常：" + msg,targetEx);
                }
                result.setErrorMessage(DataSupport.getErrorMessage(targetEx));
            }  
			result.setDesc(msg);
			result.setStatus(-2);			
			
		}catch (BusException e) {
			logger.error("【"+actionoption.getJclass()+"."+actionoption.getJmethod()+"()】反射执行方法异常：" + e.getMessage(),e);
			result.setStatus(-1);
			result.setDesc(e.getMessage());
		} 
		catch (Exception e) {
			logger.error("【"+actionoption.getJclass()+"."+actionoption.getJmethod()+"()】反射执行方法异常：" + e.getMessage(),e);
			result.setStatus(-3);
			result.setDesc("反射执行方法异常：" + e.getMessage());
			result.setErrorMessage(DataSupport.getErrorMessage(e));

		}
		return result;
	}
	
	/**
	 * 计算任务耗时
	 * @param begin
	 * @return
	 */
	private  String calDruingTime(Date begin){
		long dur = new Date().getTime() - begin.getTime();
		long minutes = dur/1000/60 ;
		long seconds = dur/1000-(60 *minutes);
		long mills = dur - (60 *1000* minutes) - seconds*1000;
		return "耗时约:"+minutes+"分"+seconds+"秒"+ mills+"毫秒。";
	}
}


