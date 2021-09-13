package com.zebone.nhis.pro.zsba.mz.pub.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.pro.zsba.common.support.JsonResult;
import com.zebone.nhis.pro.zsba.common.support.StringTools;
import com.zebone.nhis.pro.zsba.compay.pub.service.PayPosTrService;
import com.zebone.nhis.pro.zsba.compay.pub.vo.PayPosTr;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.service.qgyb.ZsbaQGService;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsPiInfoVo;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsQgybPreSt;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsQgybSt;
import com.zebone.nhis.pro.zsba.mz.pub.service.ZsbaYbPosService;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;

import net.sf.json.JSONObject;

/**
 * 全国医保结算中转接口
 * @author lipz
 */
@Controller
@RequestMapping("/static/mz/qg")
public class ZsbaQgSettleApiController {
	
	@Autowired ZsbaQGService zsbaQGService;
	@Autowired PayPosTrService payPosTrService;
	@Autowired ZsbaYbPosService ZsbaYbPosService;
	
	/**
	 * 全国医保-查询人员参保信息
	 * @param requestBody
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getPersonInfo")
	public void getPersonInfo(@RequestBody String requestBody, 
			HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		
    		JSONObject queryMap = new JSONObject();
			queryMap.put("pkPv", paramMap.get("pkPv"));
			
			Map<String, Object> piMap = ZsbaYbPosService.getPiMasterByPkPv(paramMap.get("pkPv"));
			if(piMap!=null) {
				queryMap.put("pkPi", piMap.get("pkPi"));
				queryMap.put("mdtrt_cert_no", piMap.get("idNo"));
				queryMap.put("mdtrt_cert_type", "02");
				
				//预结算
				User user = new User();
				user.setPkOrg("89ace0e12aba439991c0eb001aaf02f7");
				user.setPkEmp(paramMap.get("codeEmp"));
				user.setCodeEmp(paramMap.get("codeEmp"));
				user.setNameEmp(paramMap.get("nameEmp"));
				UserContext.setUser(user);
				
				InsPiInfoVo pInfo = zsbaQGService.getPersonInfo(queryMap.toString(), user);
				
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("insuTypeAndPlaceList", pInfo.getInsuTypeAndPlaceList());
				
				jsonData = JsonResult.toJsonObject(new JsonResult(resultMap).success());
			}else {
				jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.NOT_EXIST, "根据就诊主键【"+paramMap.get("pkPv")+"】未获取到主索引信息"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[getPersonInfo]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
	
	/**
	 * 门诊收费-预结算
	 * @param requestBody
	 * @param request
	 * @param response
	 */
	@RequestMapping("/opQgPreSett")
	public void opQgPreSett(@RequestBody String requestBody, 
			HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		
    		JSONObject queryMap = new JSONObject();
			queryMap.put("pkPv", paramMap.get("pkPv"));
			queryMap.put("opterNo", paramMap.get("codeEmp"));
			queryMap.put("mac", paramMap.get("mac"));
			queryMap.put("ip", paramMap.get("ip"));
			
			//预结算
			User user = new User();
			user.setPkOrg("89ace0e12aba439991c0eb001aaf02f7");
			user.setPkEmp(paramMap.get("codeEmp"));
			user.setCodeEmp(paramMap.get("codeEmp"));
			user.setNameEmp(paramMap.get("nameEmp"));
			UserContext.setUser(user);
			
			Map<String,Object> data = zsbaQGService.mzHpHuaJia(queryMap.toString(), user);
			InsQgybPreSt qgPreSt = ApplicationUtils.mapToBean(data, InsQgybPreSt.class);
			jsonData = JsonResult.toJsonObject(new JsonResult(qgPreSt).success());
		} catch (Exception e) {
			e.printStackTrace();
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[opQgPreSett]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}

	/**
	 * 门诊收费-结算
	 * @param requestBody
	 * @param request
	 * @param response
	 */
	@RequestMapping("/opQgSett")
	public void opQgSett(@RequestBody String requestBody, 
			HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		
    		JSONObject queryMap = new JSONObject();
			queryMap.put("pkPv", paramMap.get("pkPv"));
			queryMap.put("ybPreSettlParam", paramMap.get("ybPreSettlParam"));
    		
			//结算
			User user = new User();
			user.setPkOrg("89ace0e12aba439991c0eb001aaf02f7");
			user.setPkEmp(paramMap.get("codeEmp"));
			user.setCodeEmp(paramMap.get("codeEmp"));
			user.setNameEmp(paramMap.get("nameEmp"));
			UserContext.setUser(user);
			
			InsQgybSt qgSt = zsbaQGService.mzHpJiaokuan(queryMap.toString(), user);
			jsonData = JsonResult.toJsonObject(new JsonResult(qgSt).success());
		} catch (Exception e) {
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[opQgSett]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
	/**
	 * 门诊收费-取消结算
	 * @param requestBody
	 * @param request
	 * @param response
	 */
	@RequestMapping("/opQgCancelSett")
	public void opQgCancelSett(@RequestBody String requestBody, 
			HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		
    		JSONObject queryMap = new JSONObject();
			queryMap.put("pkPv", paramMap.get("pkPv"));
			queryMap.put("ybPkSettle", paramMap.get("ybPkSettle"));
    		
			//取消结算
			User user = new User();
			user.setPkOrg("89ace0e12aba439991c0eb001aaf02f7");
			user.setPkEmp(paramMap.get("codeEmp"));
			user.setCodeEmp(paramMap.get("codeEmp"));
			user.setNameEmp(paramMap.get("nameEmp"));
			UserContext.setUser(user);
			
			Map<String, Object> qgCancelSt = zsbaQGService.mzHpSetttleCancel(queryMap.toString(), user);
			jsonData = JsonResult.toJsonObject(new JsonResult(qgCancelSt).success());
		} catch (Exception e) {
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[opQgCancelSett]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
	
	/**
	 * 保存个帐交易数据
	 * @param requestBody
	 * @param request
	 * @param response
	 */
	@RequestMapping("/savePosTrans")
	public void savePosTrans(@RequestBody String requestBody, 
			HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		JSONObject posTrans = JSONObject.fromObject(paramMap.get("posTrans"));

			User user = new User();
			user.setPkOrg(posTrans.getString("pkOrg"));
			user.setPkEmp(posTrans.getString("creator"));
			user.setCodeEmp(posTrans.getString("creator"));
			user.setNameEmp("自助机");
			
			String sql = "select emp.PK_ORG, job.PK_DEPT, emp.PK_EMP, emp.NAME_EMP from BD_OU_EMPLOYEE emp INNER JOIN BD_OU_EMPJOB job on job.PK_EMP=emp.PK_EMP where emp.CODE_EMP=?";
			List<Map<String,Object>> data = DataBaseHelper.queryForList(sql, posTrans.getString("creator"));
			if(!data.isEmpty()){
				user.setPkOrg(data.get(0).get("pkOrg").toString());
				user.setPkEmp(data.get(0).get("pkEmp").toString());
			}
			
			UserContext.setUser(user);
			
			PayPosTr payPosTr = payPosTrService.savePayPosTr(posTrans.toString(), user);
			jsonData = JsonResult.toJsonObject(new JsonResult(payPosTr).success());
		} catch (Exception e) {
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[opQgSett]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
	/**
	 * 将医保交易数据存到第三方表
	 * @param requestBody
	 * @param request
	 * @param response
	 */
	@RequestMapping("/saveYbPosToExtPay")
	public void saveYbPosToExtPay(@RequestBody String requestBody, 
			HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		JSONObject posTrans = JSONObject.fromObject(paramMap.get("posTrans"));
    		
    		String pkExtPay = ZsbaYbPosService.saveYbPosToExtPay(posTrans);
    		
    		jsonData = JsonResult.toJsonObject(new JsonResult(pkExtPay).success());
    	}catch (Exception e) {
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[saveYbPosToExtPay]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
	/**
	 * 根据主键删除第三方交易记录
	 * @param requestBody
	 * @param request
	 * @param response
	 */
	@RequestMapping("/deleteExtPay")
	public void deleteExtPay(@RequestBody String requestBody, 
			HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		String pkExtPay = paramMap.get("pkExtPay");
    		
    		int num = ZsbaYbPosService.deleteExtPay(pkExtPay);
    		
    		jsonData = JsonResult.toJsonObject(new JsonResult(num).success());
    	}catch (Exception e) {
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[deleteExtPay]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
	/**
	 * 结算完成时将医保交易-第三方交易数据生成到付款表，并关联结算表
	 * @param requestBody
	 * @param request
	 * @param response
	 */
	@RequestMapping("/saveDepoByExtPay")
	public void saveDepoByExtPay(@RequestBody String requestBody, 
			HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		String pkExtPay = paramMap.get("pkExtPay");
    		String pkSettle = paramMap.get("pkSettle");
    		String insSettId = paramMap.get("insSettId");
    		if(StringUtils.isNotEmpty(pkExtPay) && StringUtils.isNotEmpty(pkSettle)){
    			String pkDepo = ZsbaYbPosService.saveDepoByExtPay(pkExtPay, pkSettle, insSettId);
    			if(StringUtils.isNotEmpty(pkDepo)){
    				jsonData = JsonResult.toJsonObject(new JsonResult().success());
    			}else{
    				jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.NOT_EXIST, "返回付款记录主键为空"));
    			}
    		}else{
    			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.PARAMS_ERROR, "参数错误"));
    		}
    	}catch (Exception e) {
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[saveDepoByExtPay]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
		
	}
}
