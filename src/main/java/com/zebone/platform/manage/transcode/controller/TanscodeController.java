package com.zebone.platform.manage.transcode.controller;

import com.zebone.nhis.common.module.base.transcode.SysLog;
import com.zebone.nhis.common.module.base.transcode.SysServiceRegister;
import com.zebone.nhis.common.module.base.transcode.SysTestCase;
import com.zebone.nhis.common.module.base.transcode.TransCode;
import com.zebone.nhis.common.support.RedisUtils;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.holder.SecurityHolder;
import com.zebone.platform.manage.transcode.service.TranscodeService;
import com.zebone.platform.manage.transcode.vo.QueryForm;
import com.zebone.platform.manage.transcode.vo.SysLogQueryForm;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.modules.utils.RenderUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/transcode")
@Controller
public class TanscodeController {

	@Resource
    TranscodeService transcodeService;
	
	/**
	 * 实现功能：加载sys_service_register分页
	 * 调用地点：transcode.html查询按钮
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getTranscodePage")
	public void getTranscodePage(HttpServletRequest request, HttpServletResponse response){
		/**分页参数**/
		int pageIndex = Integer.parseInt(request.getParameter("pageIndex"))+1;
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		
		QueryForm queform = new QueryForm();
		/**点击左侧节点查询右侧交易号**/
		String node = request.getParameter("node");
		if(node != null){
			queform = JsonUtil.readValue(node, QueryForm.class);
		}
		
		/**查询条件QueryForm对象的属性**/
		String queryform = request.getParameter("queryform");
		if(queryform != null){
			queform = JsonUtil.readValue(queryform, QueryForm.class);
		}
		Map map = new HashMap();
		Page page = transcodeService.getTranscodePage(pageIndex,pageSize,queform);
		map.put("data", page.getRows());
		map.put("total", page.getTotalCount());
		RenderUtils.renderJson(response,map);
	}
	
	/**
	 * 实现功能：模糊查询所有like '00*00*00*%'的交易号
	 * 调用地点：transcode.html输入交易号，回车或点击查询
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/fuzzySearchTranscodes")
	public void fuzzySearchTranscodes(HttpServletRequest request, HttpServletResponse response){
		String transcode = request.getParameter("transcode");
		SysServiceRegister register = JsonUtil.readValue(transcode, SysServiceRegister.class);
		List<Map<String, Object>> registerlist = transcodeService.fuzzySearchTranscodes(register);
		RenderUtils.renderJson(response,registerlist);
	}
	
	/**
	 * 实现功能：点击编辑时能够获取交易号信息
	 * 调用地点：弹窗edittranscode.html里
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getTranscode")
	public void getTranscode(HttpServletRequest request, HttpServletResponse response){
		String json = request.getParameter("data");
		SysServiceRegister sysregister = JsonUtil.readValue(json, SysServiceRegister.class);
		Map<String, Object> register = transcodeService.getTranscode(sysregister);
		RenderUtils.renderJson(response,register);
	}
	
	/**
	 * 实现功能：清除缓存
	 * 调用地点：transcode.html页面清除缓存按钮
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/cleanCache")
	public void cleanCache(HttpServletRequest request, HttpServletResponse response){
		RedisUtils.delCache("sys:trancode");
		RedisUtils.delCache("sys:sysparam");
	}
	
	/**
	 * 实现功能：加载左侧trans_code树
	 * 调用地点：transCodeSet.html左侧
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/gettranscodeSet")
	public void gettranscodeSet(HttpServletRequest request, HttpServletResponse response){
		RenderUtils.renderJson(response,transcodeService.gettranscodeSet());
	}
	
	/**
	 * 实现功能：加载代理名称管理
	 * 调用地点：main.html左侧
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/loadAllProxyNames")
	public void loadAllProxyNames(HttpServletRequest request, HttpServletResponse response){
		RenderUtils.renderJson(response,transcodeService.loadAllProxyNames());
	}
	
	/**
	 * 实现功能：代理名称下拉
	 * 调用地点：edittranscode.html中代理下拉列表
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getRelativeProxyNames")
	public void getProxyNames(HttpServletRequest request, HttpServletResponse response){
		String json = request.getParameter("param");
		TransCode transCode = JsonUtil.readValue(json, TransCode.class);
		RenderUtils.renderJson(response,transcodeService.getRelativeProxyNames(transCode));
	}
	
	/**
	 * 实现功能：通过第三层节点寻找自身的真正代理
	 * 调用地点：edittranscode.html中代理下拉列表
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getSelfProxy")
	public void getSelfProxy(HttpServletRequest request, HttpServletResponse response){
		String json = request.getParameter("param");
		TransCode transCode = JsonUtil.readValue(json, TransCode.class);
		RenderUtils.renderJson(response,transcodeService.getSelfProxy(transCode));
	}
	
	/**
	 * 实现功能：加载模块树（所有第三层叶子节点及相关的父节点）
	 * 调用地点：弹窗edittranscode.html里的模块treeselect
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/gettranscodeSetNoTwoLevelLeaf")
	public void gettranscodeSetNoTwoLevelLeaf(HttpServletRequest request, HttpServletResponse response){
		Map map = new HashMap();
		//map.put("data", transcodeService.gettranscodeSet());
		RenderUtils.renderJson(response,transcodeService.gettranscodeSetNoTwoLevelLeaf());
	}
	
	/**
	 * 实现功能：加载左侧trans_code树（需要显示相应层级的编码）
	 * 调用地点：transcode.html左侧
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/gettranscodeSetToShow")
	public void gettranscodeSetToShow(HttpServletRequest request, HttpServletResponse response){
		List<Map<String, Object>> transcodeSetlist = transcodeService.gettranscodeSet();
		List<Map<String, Object>> transcodeShowlist = new ArrayList<Map<String, Object>>();
		if(transcodeSetlist!=null && transcodeSetlist.size()!=0){
			for(Map<String, Object> transcodeSet : transcodeSetlist){
				String text = (String) transcodeSet.get("text");
				String id = (String) transcodeSet.get("id");
				if(id.length()>=3){
					String textshow = text + "[" + id.substring(id.length()-3, id.length()) + "]";
					transcodeSet.put("textshow", textshow);
				}else{
					transcodeSet.put("textshow", text);
				}
			}
		}
		transcodeShowlist = transcodeSetlist;
		RenderUtils.renderJson(response,transcodeShowlist);
	}
	/**
	 * 实现功能：左侧点击树节点，显示右侧交易码字典信息
	 * 调用地点：transCodeSet.html左侧
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/gettranscodeSetByTj")
	public void gettranscodeSetByTj(HttpServletRequest request, HttpServletResponse response){
		Map map = new HashMap();
		String id = request.getParameter("data");
		RenderUtils.renderJson(response,transcodeService.gettranscodeSetByTj(id));
	}
	/*@RequestMapping(value="/saveTransCodeModul")
	public void saveTransCodeModul(HttpServletRequest request,HttpServletResponse response){
		Map map = new HashMap();
		String id = request.getParameter("data");
		List<TransCode> tr=JsonUtil.readValue(id, new TypeReference<List<TransCode>>(){});
		map.put("data", transcodeService.gettranscodeSet());
		transcodeService.saveTransCodeModul(tr);
	}
	*/

	/**
	 * 实现功能：通过id查找trans_code表中记录
	 * 调用地点：transcode.html中左侧选中节点，点击新增，弹窗edittranscode.html里的模块treeselect
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/gettranscodeSetById")
	public void gettranscodeSetById(HttpServletRequest request, HttpServletResponse response){
		Map map = new HashMap();
		String json = request.getParameter("data");
		TransCode transcode = JsonUtil.readValue(json, TransCode.class);
		RenderUtils.renderJson(response,transcodeService.gettranscodeSetById(transcode));
	}
	/**
	 * 实现功能：按照名称模糊查询
	 * 调用地点：transCodeSet.html左侧查找按钮
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/gettranscodeSetByText")
	public void gettranscodeSetByText(HttpServletRequest request, HttpServletResponse response){
		Map map = new HashMap();
		String json = request.getParameter("data");
		RenderUtils.renderJson(response,transcodeService.getTransCodeSetByText(json));
	}
	/**
	 * 实现功能：按照名称模糊查询
	 * 调用地点：transcode.html左侧查找按钮
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/gettranscodeSetToShowByText")
	public void gettranscodeSetToShowByText(HttpServletRequest request, HttpServletResponse response){
		Map map = new HashMap();
		String json = request.getParameter("data");
		List<Map<String, Object>> transcodeSetlist = transcodeService.getTransCodeSetByText(json);
		
		List<Map<String, Object>> transcodeShowlist = new ArrayList<Map<String, Object>>();
		if(transcodeSetlist!=null && transcodeSetlist.size()!=0){
			for(Map<String, Object> transcodeSet : transcodeSetlist){
				String text = (String) transcodeSet.get("text");
				String id = (String) transcodeSet.get("id");
				String textshow = text + "[" + id.substring(id.length()-3, id.length()) + "]";
				transcodeSet.put("textshow", textshow);
			}
		}
		transcodeShowlist = transcodeSetlist;
		RenderUtils.renderJson(response,transcodeShowlist);
	}
	
	/**
	 * 实现功能：通过transcode加载测试用例
	 * 调用地点：test.html测试用例弹窗左侧datagrid
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getTestCasesByTranscode")
	public void getTestCasesByTranscode(HttpServletRequest request, HttpServletResponse response){
		String json = request.getParameter("data");
		SysServiceRegister register = JsonUtil.readValue(json, SysServiceRegister.class);
		List<Map<String, Object>> caselist = transcodeService.getTestCasesByTranscode(register);
		RenderUtils.renderJson(response,caselist);
	}
	
	/**
	 * 实现功能：保存测试用例名
	 * 调用地点：test.html测试用例弹窗左侧datagrid
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/saveTestCaseYlmcs")
	public void saveTestCases(HttpServletRequest request, HttpServletResponse response){
		//设置session
		UserContext.setUser(SecurityHolder.getContext().user);
		
		String json = request.getParameter("data");
		List<SysTestCase> caselist= JsonUtil.readValue(json, new TypeReference<List<SysTestCase>>(){});
		String id="";
		if(caselist!=null && caselist.size()!=0){
			for(SysTestCase ca : caselist){
				if(ca.getId()!=null){//有id更新
					id = transcodeService.updateCaseYlmc(ca);
				}else{//无id新增
					id = transcodeService.addCaseYlmc(ca);
				}
			}
		}
		RenderUtils.renderJson(response,id);
	}
	
	/**
	 * 实现功能：删除测试用例
	 * 调用地点：test.html测试用例弹窗左侧datagrid
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/delTestCase")
	public void delTestCase(HttpServletRequest request, HttpServletResponse response){
		String json = request.getParameter("data");
		SysTestCase ca = JsonUtil.readValue(json, SysTestCase.class);
		transcodeService.delTestCase(ca);
	}
	
	/**
	 * 实现功能：更新测试用例入参数据和用户名
	 * 调用地点：test.html测试用例弹窗右侧
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/saveTestCaseRcsj")
	public void saveTestCaseRcsj(HttpServletRequest request, HttpServletResponse response){
		//设置session
		UserContext.setUser(SecurityHolder.getContext().user);
		String json = request.getParameter("data");
		System.out.println("=================="+json);
		SysTestCase ca = JsonUtil.readValue(json, SysTestCase.class);
		if(ca.getId() != null && !"".equals(ca.getId())){//有id更新
			transcodeService.updateCaseRcsj(ca);
		}else{//无id新增
			transcodeService.addCaseRcsj(ca);
		}
	}
	
	/****************************系统日志  sys_log  部分********************************/
	/**
	 * 实现功能：加载sys_log分页
	 * 调用地点：syslog.html查询按钮
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getSysLogPage")
	public void getSysLogPage(HttpServletRequest request, HttpServletResponse response){
		/**分页参数**/
		int pageIndex = Integer.parseInt(request.getParameter("pageIndex"))+1;
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		
		SysLogQueryForm queform = new SysLogQueryForm();
		/**点击左侧节点查询右侧交易号**/
		String node = request.getParameter("node");
		if(node != null){
			queform = JsonUtil.readValue(node, SysLogQueryForm.class);
		}
		
		/**查询条件QueryForm对象的属性**/
		String queryform = request.getParameter("queryform");
		if(queryform != null){
			queform = JsonUtil.readValue(queryform, SysLogQueryForm.class);
		}
		Map map = new HashMap();
		Page page = transcodeService.getSysLogPage(pageIndex,pageSize,queform);
		map.put("data", page.getRows());
		map.put("total", page.getTotalCount());
		RenderUtils.renderJson(response,map);
	}
	
	/**
	 * 实现功能：根据主键查询系统日志
	 * 调用地点：looksyslog.html页面弹出时，显示选择的日志信息
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getSysLogByPklog")
	public void getSysLogByPklog(HttpServletRequest request, HttpServletResponse response){
		String param = request.getParameter("param");
		SysLog syslog = JsonUtil.readValue(param, SysLog.class);
		Map<String, Object> logmap = null;
		if(syslog != null && syslog.getPklog() != null && syslog.getPklog() != ""){
			logmap = transcodeService.getSysLogByPklog(syslog);
		}
		RenderUtils.renderJson(response,logmap);
	}
	
}
