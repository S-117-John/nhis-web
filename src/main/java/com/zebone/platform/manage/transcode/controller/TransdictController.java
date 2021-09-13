package com.zebone.platform.manage.transcode.controller;

import com.zebone.platform.manage.transcode.service.TransdictService;
import com.zebone.platform.modules.utils.RenderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/transdict")
@Controller
public class TransdictController {

	@Resource
	private TransdictService transdictService;
	
	/**
	 * 实现功能：获取trans_dict表中syscode类型
	 * 调用地点：弹窗edittranscode.html里
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getSyscodeTypes")
	public void getSyscodeTypes(HttpServletRequest request, HttpServletResponse response){
		Map map = new HashMap();
		map.put("data", transdictService.getSyscodeTypes("syscode"));
		RenderUtils.renderJson(response,map);
	}
	
	/**
	 * 实现功能：获取trans_dict表中proxytime类型
	 * 调用地点：弹窗edittranscode.html里
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getProxytimeTypes")
	public void getProxytimeTypes(HttpServletRequest request, HttpServletResponse response){
		Map map = new HashMap();
		map.put("data", transdictService.getSyscodeTypes("proxytime"));
		RenderUtils.renderJson(response,map);
	}
	
	/**
	 * 实现功能：获取trans_dict表中zt类型
	 * 调用地点：弹窗edittranscode.html里
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getZtList")
	public void getZtList(HttpServletRequest request, HttpServletResponse response){
		Map map = new HashMap();
		List<Map<String, Object>> ztlist = transdictService.getSyscodeTypes("zt");
		/*for(Map<String, Object> zt : ztlist){
			if("0".equals(zt.get("zttypename").toString())){
				zt.put("desc", "待开发");
			}else if("1".equals(zt.get("zttypename").toString())){
				zt.put("desc", "开发中");
			}else if("2".equals(zt.get("zttypename").toString())){
				zt.put("desc", "完成");
			}else if("9".equals(zt.get("zttypename").toString())){
				zt.put("desc", "需求");
			}
		}*/
		
		map.put("data", ztlist);
		RenderUtils.renderJson(response,map);
	}

	
	/**
	 * 实现功能：获取trans_dict表中zt类型
	 * 调用地点：transcode.html里
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getZtListToSearch")
	public void getZtListToSearch(HttpServletRequest request, HttpServletResponse response){
		Map map = new HashMap();
		List<Map<String, Object>> ztlist = transdictService.getSyscodeTypes("zt");
		/*for(Map<String, Object> zt : ztlist){
			if("0".equals(zt.get("zttypename").toString())){
				zt.put("desc", "待开发");
			}else if("1".equals(zt.get("zttypename").toString())){
				zt.put("desc", "开发中");
			}else if("2".equals(zt.get("zttypename").toString())){
				zt.put("desc", "完成");
			}else if("9".equals(zt.get("zttypename").toString())){
				zt.put("desc", "需求");
			}
		}*/
		//添加一个显示全部
		Map<String, Object> all = new HashMap<String, Object>();
		all.put("zttypename", "all");
		all.put("typedesc", "全部");
		ztlist.add(all);
		
		map.put("data", ztlist);
		RenderUtils.renderJson(response,map);
	}
	
	/**
	 * 实现功能：获取trans_dict表中isvisable类型
	 * 调用地点：弹窗quartz.html里
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getAllVisableTypes")
	public void getAllVisableTypes(HttpServletRequest request, HttpServletResponse response){
		Map map = new HashMap();
		List<Map<String,Object>> typelist = transdictService.getSyscodeTypes("isvisable");
		Map<String,Object> aMap = new HashMap<String,Object>();
		aMap.put("vistypename", "9");
		aMap.put("typedesc", "全部");
		aMap.put("typegroup", "isvisable");
		typelist.add(aMap);
		map.put("data", typelist);
		RenderUtils.renderJson(response,map);
	}
	
	/**
	 * 实现功能：获取trans_dict表中isvisable类型
	 * 调用地点：弹窗editquartz.html里
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getVisableTypes")
	public void getVisableTypes(HttpServletRequest request, HttpServletResponse response){
		Map map = new HashMap();
		map.put("data", transdictService.getSyscodeTypes("isvisable"));
		RenderUtils.renderJson(response,map);
	}
	
	/**
	 * 实现功能：获取trans_dict表中isvisable类型
	 * 调用地点：弹窗editquartz.html里
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getCronExpTypes")
	public void getCronExpTypes(HttpServletRequest request, HttpServletResponse response){
		Map map = new HashMap();
		map.put("data", transdictService.getSyscodeTypes("cronexp"));
		RenderUtils.renderJson(response,map);
	}
	
	/**
	 * 实现功能：获取trans_dict表中jobtype类型
	 * 调用地点：弹窗editquartzlog.html里
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getJobTypes")
	public void getJobTypes(HttpServletRequest request, HttpServletResponse response){
		Map map = new HashMap();
		map.put("data", transdictService.getSyscodeTypes("jobtype"));
		RenderUtils.renderJson(response,map);
	}
	
	/**
	 * 实现功能：获取sys_log表中type类型包括“全部”
	 * 调用地点：syslog.html里
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getAllSysLogTypes")
	public void getAllSysLogTypes(HttpServletRequest request, HttpServletResponse response){
		Map map = new HashMap();
		List<Map<String,Object>> typelist = transdictService.getSyscodeTypes("syslog");
		Map aMap = new HashMap<String, Object>();
		aMap.put("syslogtypename", "9");
		aMap.put("typedesc", "全部");
		aMap.put("typegroup", "syslog");
		typelist.add(aMap);
		map.put("data", typelist);
		RenderUtils.renderJson(response,map);
	}
	
	/**
	 * 实现功能：获取sys_log表中type类型
	 * 调用地点：looksyslog.html里
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getSysLogType")
	public void getSysLogType(HttpServletRequest request, HttpServletResponse response){
		Map map = new HashMap();
		map.put("data", transdictService.getSyscodeTypes("syslog"));
		RenderUtils.renderJson(response,map);
	}
	
	/**
	 * 实现功能：获取qrtz_job_log表中jobtype类型
	 * 调用地点：quartzlog.html里
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getAllJobTypes")
	public void getAllJobTypes(HttpServletRequest request, HttpServletResponse response){
		Map map = new HashMap();
		List<Map<String,Object>> typelist = transdictService.getSyscodeTypes("jobtype");
		Map<String,Object> aMap = new HashMap<String,Object>();
		aMap.put("jobtypename", "9");
		aMap.put("typedesc", "全部");
		aMap.put("typegroup", "jobtype");
		typelist.add(aMap);
		map.put("data", typelist);
		RenderUtils.renderJson(response,map);
	}
	
	/**
	 * 实现功能：获取qrtz_job_log表中jobtype类型
	 * 调用地点：lookquartzlog.html里
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getJobType")
	public void getJobType(HttpServletRequest request, HttpServletResponse response){
		Map map = new HashMap();
		map.put("data", transdictService.getSyscodeTypes("jobtype"));
		RenderUtils.renderJson(response,map);
	}
}
