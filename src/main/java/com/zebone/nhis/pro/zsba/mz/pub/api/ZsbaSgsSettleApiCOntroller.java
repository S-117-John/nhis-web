package com.zebone.nhis.pro.zsba.mz.pub.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zebone.nhis.pro.zsba.common.support.JsonResult;
import com.zebone.nhis.pro.zsba.common.support.StringTools;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.service.sgs.ZsbaSgsService;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsSgsPi;
import com.zebone.platform.common.support.User;

import net.sf.json.JSONObject;


/**
 * 省工伤医保结算中转接口
 * @author lipz
 */
@Controller
@RequestMapping("/static/mz/sgs")
public class ZsbaSgsSettleApiCOntroller {
	
	@Autowired ZsbaSgsService zsbaSgsService;
	
	/**
	 * 医保预结算
	 * @param requestBody
	 * @param request
	 * @param response
	 */
	@RequestMapping("/sgsPreSett")
	public void sgsPreSett(@RequestBody String requestBody, 
			HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		
    		User user = new User();
			user.setCodeEmp(paramMap.get("codeEmp"));
			user.setNameEmp(paramMap.get("nameEmp"));
			//获取患者医保就诊登记信息
    		JSONObject patMap = new JSONObject();
    		patMap.put("pkPv", paramMap.get("pkPv"));
    		InsSgsPi isPi = zsbaSgsService.getPersonInfo(patMap.toString(), user);
			//医保预结算
			JSONObject queryMap = new JSONObject();
			queryMap.put("pkPv", paramMap.get("pkPv"));
			queryMap.put("yBPatientInfo", isPi);
			Map<String,Object> data = zsbaSgsService.mzHpHuaJia(queryMap.toString(), user);
			
			jsonData = JsonResult.toJsonObject(new JsonResult(data).success());
		} catch (Exception e) {
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[sgsPreSett]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
	/**
	 * 医保结算
	 * @param requestBody
	 * @param request
	 * @param response
	 */
	@RequestMapping("/sgsSett")
	public void sgsSett(@RequestBody String requestBody, 
			HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		
    		User user = new User();
			user.setCodeEmp(paramMap.get("codeEmp"));
			user.setNameEmp(paramMap.get("nameEmp"));
			//医保结算
			JSONObject queryMap = new JSONObject();
			queryMap.put("pkPv", paramMap.get("pkPv"));
			queryMap.put("ybPreSettlParam", paramMap.get("ybPreSettlParam"));
			Map<String,Object> data = zsbaSgsService.mzHpJiaokuan(queryMap.toString(), user);
			
			jsonData = JsonResult.toJsonObject(new JsonResult(data).success());
		} catch (Exception e) {
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[sgsSett]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
	/**
	 * 医保取消结算
	 * @param requestBody
	 * @param request
	 * @param response
	 */
	@RequestMapping("/sgsCancelSett")
	public void sgsCancelSett(@RequestBody String requestBody, 
			HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		
    		User user = new User();
			user.setCodeEmp(paramMap.get("codeEmp"));
			user.setNameEmp(paramMap.get("nameEmp"));
			//医保结算
			JSONObject queryMap = new JSONObject();
			queryMap.put("pkPv", paramMap.get("pkPv"));
			queryMap.put("ybPkSettle", paramMap.get("ybPkSettle"));
			Map<String,Object> data = zsbaSgsService.mzHpSetttleCancel(queryMap.toString(), user);
			
			jsonData = JsonResult.toJsonObject(new JsonResult(data).success());
		} catch (Exception e) {
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[sgsCancelSett]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}

}
