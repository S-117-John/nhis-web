package com.zebone.nhis.pro.zsba.mz.pub.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zebone.nhis.pro.zsba.common.support.JsonResult;
import com.zebone.nhis.pro.zsba.common.support.StringTools;
import com.zebone.nhis.pro.zsba.task.ZsbaMzWxAliTradeService;
import com.zebone.nhis.pro.zsba.task.ZsbaNmTaskService;
import com.zebone.platform.quartz.modle.QrtzJobCfg;

@Controller
@RequestMapping("/static/mz/test")
public class TestController {
	

	@Autowired ZsbaMzWxAliTradeService mzWxAliTradeService;
	
	@Autowired ZsbaNmTaskService zsbaNmTaskService;

	/**
	 * 自动微信推送
	 * http://127.0.0.1:8080/HISGL/static/mz/sgs/testNmPush
	 * @param request
	 * @param response
	 */
	@RequestMapping("/testNmPush")
	public void testNmPush(HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		zsbaNmTaskService.executePush(new QrtzJobCfg());
			jsonData = JsonResult.toJsonObject(new JsonResult().success());
		} catch (Exception e) {
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[executePush]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
	/**
	 * 自动退款任务
	 * http://127.0.0.1:8080/HISGL/static/mz/sgs/testTradeRefund
	 * @param requestBody
	 * @param request
	 * @param response
	 */
	@RequestMapping("/testTradeRefund")
	public void testTradeRefund(HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		mzWxAliTradeService.executeTradeRefund(new QrtzJobCfg());
			jsonData = JsonResult.toJsonObject(new JsonResult().success());
		} catch (Exception e) {
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[testTradeRefund]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	

}
