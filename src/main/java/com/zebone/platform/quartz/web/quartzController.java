package com.zebone.platform.quartz.web;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.quartz.modle.QrtzJobCfg;
import com.zebone.platform.quartz.service.IJobLogHandler;
import com.zebone.platform.quartz.service.ISchedulerFactoryService;
import com.zebone.platform.quartz.vo.JobCfgQueryForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/quartz")
@Controller
public class quartzController{
	
	public ISchedulerFactoryService ttschedulerFactory;
	
	public IJobLogHandler jobLongHandler;
	
	
	private Logger logger = LogManager.getLogger(quartzController.class);
	
	
	public IJobLogHandler getJobHandler(){
		if(jobLongHandler == null){
			Object ofacorty = null;
			try{
				ofacorty = ServiceLocator.getInstance().getBean("jobLogHandler");
			}catch(Exception e){}
			 
			if(ofacorty == null){
				ofacorty = ServiceLocator.getInstance().getBean("jobLogHandlerRpc");
			}
			
			jobLongHandler = (IJobLogHandler)ofacorty;
			return jobLongHandler;
		}else{
			return jobLongHandler;
		}
	}
	
	public ISchedulerFactoryService getSchedulerFactory(){
		if(ttschedulerFactory == null){
			Object ofacorty = null;
			try{
				ofacorty = ServiceLocator.getInstance().getBean("ttschedulerFactory");
			}catch(Exception e){}
			 
			if(ofacorty == null){
				ofacorty = ServiceLocator.getInstance().getBean("ttschedulerFactoryRpc");
			}
			
			ttschedulerFactory = (ISchedulerFactoryService)ofacorty;
			return ttschedulerFactory;
		}else{
			return ttschedulerFactory;
		}
	}
	
	
	/**
	 * 加载任务
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/loadQuartz")
	public void loadQuartz(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String queryform = request.getParameter("queryform");
		JobCfgQueryForm jobCfgQueryForm = null;
		if(queryform != null){
			jobCfgQueryForm = JsonUtil.readValue(queryform, JobCfgQueryForm.class);
		}
		
		List<Map<String, Object>> joblist = getSchedulerFactory().loadJob(jobCfgQueryForm);
		this.renderJson(response,joblist);
	}
	
	/**
	 * 加载所有机构
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/loadAllOrgs")
	public void loadAllOrgs(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> orgslist = getSchedulerFactory().queryAllOrgs();
		this.renderJson(response,orgslist);
	}
	
	/**
	 * 修改任务表qrtz_job_cfg授权机构jgs
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/savejobCFGjgs")
	public void savejobCFGjgs(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map m = new HashMap();
		String param = request.getParameter("param");
		QrtzJobCfg jobCfg = JsonUtil.readValue(param, QrtzJobCfg.class);
		try{
			 getSchedulerFactory().updateJgsByName(jobCfg);
			 getSchedulerFactory().updateJob(jobCfg);
		}catch(Exception e){
			logger.error("修改授权机构失败："+e.getMessage(),e);
			m.put("code", "-1");
			m.put("msg", "修改授权机构失败："+e.getMessage());
			
		}
		this.renderJson(response,m);
	}
	
	/**
	 * 通过编码（主键）获取任务
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/getQuartzByJobname")
	public void getQuartzByJobname(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String param = request.getParameter("param");
		
		QrtzJobCfg jobCfg = JsonUtil.readValue(param, QrtzJobCfg.class);
		
		Map<String, Object> jobCfgMap =  getSchedulerFactory().getQuartzByJobname(jobCfg);
		
		this.renderJson(response,jobCfgMap);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/addQuartz")
	public void addQuartz(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map m = new HashMap();
		String param = request.getParameter("param");
		QrtzJobCfg jobCfg = JsonUtil.readValue(param, QrtzJobCfg.class);
		try{
			 getSchedulerFactory().addJob(jobCfg);
			m.put("code", "1");
			m.put("msg", "添加任务成功!");
		}catch(Exception e){
			logger.error("添加任务失败："+e.getMessage(),e);
			m.put("code", "-1");
			m.put("msg", "添加任务失败："+e.getMessage());
			
		}
		this.renderJson(response,m);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/updateQuartz")
	public void updateQuartz(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map m = new HashMap();
		String param = request.getParameter("param");
		QrtzJobCfg jobCfg = JsonUtil.readValue(param, QrtzJobCfg.class);
		try{

			 getSchedulerFactory().updateJob(jobCfg);
			m.put("code", "1");
			m.put("msg", "修改任务成功!");
		}catch(Exception e){
			logger.error("修改任务失败："+e.getMessage());
			m.put("code", "-1");
			m.put("msg", "修改任务失败："+e.getMessage());
			
		}
		this.renderJson(response,m);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/autoRunJob")
	public void autoRunJob(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map m = new HashMap();
		try{
			String param = request.getParameter("param");
			QrtzJobCfg jobCfg = JsonUtil.readValue(param, QrtzJobCfg.class);
			getJobHandler().autoRunJob(jobCfg);
			m.put("code", "1");
			m.put("msg", "手动执行任务成功!");
		}catch(Exception e){
			logger.error("手动执行任务失败："+e.getMessage());
			m.put("code", "-1");
			m.put("msg", "手动执行任务失败："+e.getMessage());
			
		}
		this.renderJson(response,m);
	}
	/**
	 *停止所有任务
	 *注意：如果使用此服务停止所有服务  ， 然后增加新任务都是暂停状态 并且重启应用服务器后 所有任务不会立即执行 全部进入暂停状态
	 *    
	 * 那么必须使用 startAllJob.do服务恢复任务  如果使用单个启动命令（startJob.do） 只能启动单个任务。
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/stopAllJob")
	public void stopQuartz(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map m = new HashMap();
		try{	
			 getSchedulerFactory().stopQuartz();
			m.put("code", "1");
			m.put("msg", "停止所有任务成功!");
		}catch(Exception e){
			logger.error("停止所有任务失败："+e.getMessage());
			m.put("code", "-1");
			m.put("msg", "停止调度服务失败："+e.getMessage());	
		}
		this.renderJson(response,m);
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/startAllJob")
	public void startQuartz(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map m = new HashMap();
		try{	
			 getSchedulerFactory().startQuartz();
			m.put("code", "1");
			m.put("msg", "启动所有任务成功!");
		}catch(Exception e){
			logger.error("启动所有任务失败："+e.getMessage());
			m.put("code", "-1");
			m.put("msg", "启动所有任务失败："+e.getMessage());	
		}
		this.renderJson(response,m);
	}
	
	@RequestMapping("/stopJob")
	public void stopJob(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map m = new HashMap();
		try{		
			String param = request.getParameter("param");
			QrtzJobCfg jobCfg = JsonUtil.readValue(param, QrtzJobCfg.class);
			 getSchedulerFactory().stopJob(jobCfg);
	
			m.put("code", "1");
			m.put("msg", "暂停任务成功!");
		}catch(Exception e){
			logger.error("暂停任务失败："+e.getMessage());
			m.put("code", "-1");
			m.put("msg", "暂停任务失败："+e.getMessage());
			
		}
		this.renderJson(response,m);
	}
	
	@RequestMapping("/startJob")
	public void startJob(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map m = new HashMap();
		try{		
			String param = request.getParameter("param");
			QrtzJobCfg jobCfg = JsonUtil.readValue(param, QrtzJobCfg.class);
			 getSchedulerFactory().startJob(jobCfg);

			m.put("code", "1");
			m.put("msg", "恢复任务成功!");
		}catch(Exception e){
			logger.error("恢复任务失败："+e.getMessage());
			m.put("code", "-1");
			m.put("msg", "恢复任务失败："+e.getMessage());
			
		}
		this.renderJson(response,m);
	}
	
	
	@RequestMapping("/deleteJob")
	public void deleteJob(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map m = new HashMap();
		String param = request.getParameter("param");
		List<QrtzJobCfg> jobCfglist = JsonUtil.readValue(param, new TypeReference<List<QrtzJobCfg>>() {
		});
		try{		
			 getSchedulerFactory().deleteJob(jobCfglist);
			m.put("code", "1");
			m.put("msg", "删除任务成功!");
		}catch(Exception e){
			logger.error("删除任务失败："+e.getMessage());
			m.put("code", "-1");
			m.put("msg", "删除任务失败："+e.getMessage());
			
		}
		this.renderJson(response,m);
	}
	

	public  static 	Map<String,String> getRequestMap(HttpServletRequest request){
		
		Enumeration enu = request.getParameterNames();
		Map<String,String> rmap = new HashMap<String,String>();
		
		while (enu.hasMoreElements()) {
			String xsql = (String) enu.nextElement();
			String value = request.getParameter(xsql);
			rmap.put(xsql, value);
		}

		return rmap;
	}
	
	
	public  void renderJson(HttpServletResponse response, Object o) {
		render(response, JSONObject.toJSONString(o, SerializerFeature.WriteMapNullValue),"application/json; charset=UTF-8");
	}
	
	
	public  void render(HttpServletResponse response, String text,
                        String contentType) {
		try {
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType(contentType);
			response.getWriter().write(text);
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
