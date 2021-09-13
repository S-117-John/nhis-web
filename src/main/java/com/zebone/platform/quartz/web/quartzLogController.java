package com.zebone.platform.quartz.web;

import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.modules.utils.RenderUtils;
import com.zebone.platform.quartz.modle.QrtzJobLog;
import com.zebone.platform.quartz.service.ISchedulerFactoryService;
import com.zebone.platform.quartz.vo.JobLogQueryForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/quartzlog")
@Controller
public class quartzLogController {

	private Logger logger = LogManager.getLogger(quartzLogController.class);
	
	
	public ISchedulerFactoryService ttschedulerFactory;
	
	
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
	 * 加载任务日志
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/loadQuartzLog")
	public void loadQuartzLog(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String queryform = request.getParameter("queryform");
		JobLogQueryForm jobLogQueryForm = null;
		if(queryform != null){
			jobLogQueryForm = JsonUtil.readValue(queryform, JobLogQueryForm.class);
		}
		
		/**分页参数**/
		int pageIndex = Integer.parseInt(request.getParameter("pageIndex"))+1;
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		
		Map map = new HashMap();
		Page joblogpage = getSchedulerFactory().loadQuartzLog(pageIndex,pageSize,jobLogQueryForm);
		map.put("data", joblogpage.getRows());
		map.put("total", joblogpage.getTotalCount());
		RenderUtils.renderJson(response,map);
	}
	
	/**
	 * 通过主键加载任务日志
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/getQuartzLogById")
	public void getQuartzLogById(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String param = request.getParameter("param");
		QrtzJobLog joblog = JsonUtil.readValue(param, QrtzJobLog.class);
		Map<String, Object> logmap = getSchedulerFactory().getQuartzLogById(joblog);
		RenderUtils.renderJson(response,logmap);
	}
	

	


}
