package com.zebone.platform.manage.transcode.controller;

import com.zebone.nhis.common.module.base.transcode.SysServiceRegister;
import com.zebone.nhis.common.module.base.transcode.TransCode;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.RedisUtils;
import com.zebone.platform.Application;
import com.zebone.platform.manage.transcode.service.TranscodeService;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.modules.utils.RenderUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 仅供研发人员操作的控制器，防止外部人员误操作
 * 
 * @author Xulj
 *
 */
@RequestMapping("/oper")
@Controller
public class OperController {

	@Resource
    TranscodeService transcodeService;
	
	/**
	 * 实现功能：新增，修改后保存
	 * 调用地点：弹窗edittranscode.html
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/saveTranscode")
	public void saveTranscode(HttpServletRequest request, HttpServletResponse response){
		
		String oper = Application.getApplicationSettings().getProperty("transcode.oper","true");
		if(!oper.equals("true")){
			return ;
		}
		
		String json = request.getParameter("data");
		String kind = request.getParameter("kind");
		SysServiceRegister sysregister = JsonUtil.readValue(json, SysServiceRegister.class);
		//保存前判断是否已经存在该记录
		if("new".equals(kind)){
			Map<String, Object> register = transcodeService.getTranscode(sysregister);
			Map alert = new HashMap();
			if(register == null || register.isEmpty()){
				transcodeService.saveTranscode(sysregister);
				alert.put("alert", "success");
				RenderUtils.renderJson(response,alert);
			}else{
				alert.put("alert", "error");
				RenderUtils.renderJson(response,alert);
			}
		}else if("edit".equals(kind)){//更新
			transcodeService.updateTranscode(sysregister);
		}
	}
	
	/**
	 * 实现功能：删除sys_service_register交易号
	 * 调用地点：transcode.html右侧删除按钮
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/delTranscode")
	public void delTranscode(HttpServletRequest request, HttpServletResponse response){
		
		String oper = Application.getApplicationSettings().getProperty("transcode.oper","true");
		if(!oper.equals("true")){
			return ;
		}
		
		String sysregisterlist = request.getParameter("sysregisterlist");
		List<SysServiceRegister> registerlist = JsonUtil.readValue(sysregisterlist, new TypeReference<List<SysServiceRegister>>() {
		});
		transcodeService.delTranscode(registerlist);
	}
	
	/**
	 * 实现功能：更新代理名称管理
	 * 调用地点：main.html右侧proxyManager.html
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/saveProxyNames")
	public void saveProxyNames(HttpServletRequest request, HttpServletResponse response){
		
		String oper = Application.getApplicationSettings().getProperty("transcode.oper","true");
		if(!oper.equals("true")){
			return ;
		}
		
		String json = request.getParameter("transCode");
		TransCode transCode = JsonUtil.readValue(json, TransCode.class);
		if(transCode.getId()!=null && transCode.getId()!=""){
			if(transCode.getProxyname() != null){
				transcodeService.updateProxyNames(transCode);
			}
		}else{
			transcodeService.saveProxyNames(transCode);
		}
	}
	
	/**
	 * 实现功能：删除交易码字典马戏信息
	 * 调用地点：transCodeSet.html右侧
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/delTransCodeModul")
	public void delTransCodeModul(HttpServletRequest request, HttpServletResponse response){
		
		String oper = Application.getApplicationSettings().getProperty("transcode.oper","true");
		if(!oper.equals("true")){
			return ;
		}
		
		Map map = new HashMap();
		String id = request.getParameter("data");
		List<TransCode> tr= JsonUtil.readValue(id, new TypeReference<List<TransCode>>(){});
		boolean delStatus=transcodeService.delTransCodeModul(tr);
		RenderUtils.renderJson(response, delStatus);
	}
	
	/**
	 * 实现功能：右侧新增和修改交易码字典信息
	 * 调用地点：transCodeSet.html右侧详细信息显示
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/changeTransCodeModul")
	public void changeTransCodeModul(HttpServletRequest request, HttpServletResponse response){
		String oper = Application.getApplicationSettings().getProperty("transcode.oper","true");
		if(!oper.equals("true")){
			return ;
		}
		Map map = new HashMap();
		String id = request.getParameter("data");
		List<TransCode> tr= JsonUtil.readValue(id, new TypeReference<List<TransCode>>(){});
		transcodeService.changeTransCodeModul(tr);
	}
	
	/**
	 * 实现功能：弹出测试窗体
	 * 调用地点：transcode.html页面测试按钮
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/test")
	public String gotoTest(HttpServletRequest request, HttpServletResponse response){
		
		String oper = Application.getApplicationSettings().getProperty("transcode.oper","true");
		if(!oper.equals("true")){
			return "transcode/nopermission";
		}
		return "transcode/test";
	}
	
	/**
	 * 实现功能：重置代理
	 * 调用地点：transcode.html页面重置代理按钮
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/resetRegisterProxy")
	public void resetRegisterProxy(HttpServletRequest request, HttpServletResponse response){
		String oper = Application.getApplicationSettings().getProperty("transcode.oper","true");
		if(!oper.equals("true")){
			return ;
		}
		transcodeService.resetRegisterProxy();
		RedisUtils.delCache("sys:trancode");
	}
	/**
	 * 实现功能：在线加密字符串(base64方式)
	 * 调用地点：test.html测试用例弹窗左侧datagrid
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getBaseEncryption")
	public void getBaseEncryption(HttpServletRequest request, HttpServletResponse response){
		String json = request.getParameter("data");
		String aesstr = JsonUtil.getFieldValue(json, "aesstr");
		if(CommonUtils.isEmptyString(aesstr))
			return;
		Map<String,String> result = new HashMap<String,String>();
		result.put("aesstr", aesstr);
		result.put("aesresult", CommonUtils.encode(aesstr.getBytes()));
		RenderUtils.renderJson(response,result);
	}
	/**
	 * 实现功能：清除集群环境缓存数据
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/clearClusterCache")
	public void clearClusterCache(HttpServletRequest request, HttpServletResponse response){
		String json = request.getParameter("data");
		String pattern = JsonUtil.getFieldValue(json, "pattern");
		if(CommonUtils.isEmptyString(pattern))
			return;
		Set<String> keys = RedisUtils.getRedisTemplate().keys(pattern);
		RedisUtils.getRedisTemplate().delete(keys);
		Map<String,String> result = new HashMap<String,String>();
		result.put("pattern", pattern);
		result.put("msg", "清除成功！");
		RenderUtils.renderJson(response,result);
	}
    /**
     * 实现功能：获取缓存中所有的key
     * @param request
     * @param response
     */
    @RequestMapping(value="/getClusterCacheKeys")
    public void getClusterCacheKeys(HttpServletRequest request, HttpServletResponse response){
        Set<String> keys = com.zebone.platform.modules.utils.RedisUtils.getRedisTemplate().keys("*");
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("allKeys",keys);
        RenderUtils.renderJson(response,result);
    }
	
}
