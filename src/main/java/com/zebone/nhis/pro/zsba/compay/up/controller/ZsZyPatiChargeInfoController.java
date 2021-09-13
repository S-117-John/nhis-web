package com.zebone.nhis.pro.zsba.compay.up.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zebone.nhis.pro.zsba.common.support.JsonResult;
import com.zebone.nhis.pro.zsba.common.support.StringTools;
import com.zebone.nhis.pro.zsba.compay.up.service.TpPayService;
import com.zebone.nhis.pro.zsba.compay.up.service.ZsBlCgIpQueryServcie;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 住院患者信息、费用概要、住院一日清单汇总、住院一日清单明细
 * @author 	lipz
 * @date	2020-01-02
 */
@Controller
@RequestMapping("/static/charge")
public class ZsZyPatiChargeInfoController {
	
	@Autowired ZsBlCgIpQueryServcie cgIpQueryServcie; 
	@Autowired TpPayService wxAliPayService;
	
	/**
	 * 查询患者进本信息及费用概要
	 * @param requestBody
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getPatInfoAndChargeSummary")
    public void getPatInfoAndChargeSummary(@RequestBody String requestBody, 
    		HttpServletRequest request, HttpServletResponse response){ 
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		String patientId = paramMap.get("patientId");
    		String times = paramMap.get("times");
    		if(StringUtils.isNotEmpty(patientId) && StringUtils.isNotEmpty(times)){
    			Map<String, Object> result = new HashMap<String, Object>();
    			// 查询患者信息
    			Map<String, Object> pkMap = getPkPiAndPkPv(patientId, times);
    			if(pkMap!=null){
    				String pkPv = pkMap.get("pkPv").toString();
    				
    				String sql = " select OUT_TRADE_NO from PAY_WECHAT_RECORD where TRADE_STATE='INIT' and PK_PV=? ";
    				List<Map<String, Object>> wpData = DataBaseHelper.queryForList(sql, new Object[]{pkPv});
    				for (Map<String, Object> map : wpData) {
    					Map<String, Object> queryMap = new HashMap<String, Object>();
            			queryMap.put("serialNo", map.get("outTradeNo").toString());
            			queryMap.put("queryTerminal", "YES");
            			
            			String param = JsonUtil.writeValueAsString(queryMap);
            			wxAliPayService.inquiryOrder(param, UserContext.getUser());
					}
    				
    				String infoSql = "select pi.code_ip,pv.bed_no,pv.code_pv,pi.code_pi,ip.ip_times,pv.name_pi,pv.dt_sex,pv.date_begin,pv.date_end,pv.age_pv, pi.birth_date,d.NAME_DEPT " +
    						" from pv_encounter pv left join pv_ip ip on ip.pk_pv = pv.pk_pv left join pi_master pi on pi.pk_pi=pv.pk_pi left join BD_OU_DEPT d ON pv.pk_dept_ns=d.PK_DEPT " +
    						" where pv.pk_pv=?";
    				Map<String, Object> infoMap = DataBaseHelper.queryForMap(infoSql, new Object[]{pkPv});
        			if(infoMap!=null){
        				result.put("namePi", infoMap.get("namePi").toString());
        				result.put("codePi", infoMap.get("codePi").toString());
        				result.put("codeIp", infoMap.get("codeIp").toString());
        				result.put("codePv", infoMap.get("codePv").toString());
        				result.put("ipTimes", infoMap.get("ipTimes").toString());
        				result.put("bedNo", infoMap.get("bedNo").toString());
        				result.put("dateBegin", infoMap.get("dateBegin").toString());
        				result.put("agePv", infoMap.get("agePv").toString());
        				String sex = infoMap.get("dtSex").toString();
        				result.put("dtSex", "02".equals(sex)?"1":"03".equals(sex)?"2":"3");
        				result.put("birthDay", infoMap.get("birthDate").toString());
        				result.put("nameDept", infoMap.get("nameDept").toString());
        				
        				// 查询费用概要
        				StringBuffer feeSql = new StringBuffer("select sum(sum_dep) as sum_dep,sum(sum_gua) as sum_gua,sum(sum_amount) as sum_amount,sum(sum_pi) as sum_pi from (");
        				feeSql.append(" select (case when sum(amount) is null then 0 else sum(amount)*1.0 end ) as sum_dep, 0 as sum_gua, 0 as sum_amount, 0 as sum_pi from bl_deposit where eu_dptype='9' and pk_pv=?");
        				feeSql.append(" UNION select 0 as sum_dep,(case when sum(amt_credit) is null then 0 else sum(amt_credit)*1.0 end ) as sum_gua,0 as sum_amount,0 as sum_pi from pv_ip_acc where nvl(flag_canc,'0') <> '1' and pk_pv=?");
        				feeSql.append(" UNION select 0 as sum_dep,0 as sum_gua,(case when sum(amount) is null then 0 else sum(amount)*1.0 end ) as sum_amount,0 as sum_pi from bl_ip_dt where pk_pv=?");
        				feeSql.append(" UNION select 0 as sum_dep,0 as sum_gua,0 as sum_amount,(case when sum(amount_pi) is null then 0 else sum(amount_pi)*1.0 end ) as sum_pi from bl_ip_dt where pk_pv=? ");
        				feeSql.append(") temp");
        				Map<String, Object> feeMap = DataBaseHelper.queryForMap(feeSql.toString(), new Object[]{pkPv,pkPv,pkPv,pkPv});
        				
            			result.put("sumAmount", new BigDecimal(feeMap.get("sumAmount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP));//总费用
            			result.put("sumDeposit", new BigDecimal(feeMap.get("sumDep").toString()).setScale(2, BigDecimal.ROUND_HALF_UP));//预交金
            			result.put("sumguarantee", new BigDecimal(feeMap.get("sumGua").toString()).setScale(2, BigDecimal.ROUND_HALF_UP));//担保金
            			result.put("sumamountPi", new BigDecimal(feeMap.get("sumPi").toString()).setScale(2, BigDecimal.ROUND_HALF_UP));//自费费用
            			
            			String data = JsonUtil.writeValueAsString(result);
            			jsonData = JsonResult.toJsonObject(new JsonResult(data).success());
        			}else{
        				jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.NOT_EXIST, "未找到患者住院就诊数据: info"));
        			}
    			}else{
    				jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.NOT_EXIST, "未找到患者住院就诊数据：pkPv"));
    			}
    		}else{
    			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.PARAMS_ERROR, "[patientId]、[times]参数不能为空"));
    		}
		} catch (Exception e) {
			e.printStackTrace();
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[getPatInfoAndChargeSummary]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
    }

	/**
	 * 获取指定日期内的总费用
	 * @param requestBody
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getSumAmountByDate")
	public void getSumAmountByDate(@RequestBody String requestBody, 
    		HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		String patientId = paramMap.get("patientId");
    		String times = paramMap.get("times");
    		String endDate = paramMap.get("endDate");
    		if(StringUtils.isNotEmpty(patientId) && StringUtils.isNotEmpty(times) && StringUtils.isNotEmpty(endDate)){
    			Map<String, Object> result = new HashMap<String, Object>();
    			// 查询患者信息
    			Map<String, Object> pkMap = getPkPiAndPkPv(patientId, times);
    			if(pkMap!=null){
    				String amountSql = "SELECT CASE WHEN SUM(amount) IS NULL THEN 0 ELSE SUM(amount) END  AS sum_amount FROM bl_ip_dt  WHERE pk_pv =? and date_cg<=?";
        			String pkPv = pkMap.get("pkPv").toString();
    				Map<String, Object> amountMap = DataBaseHelper.queryForMap(amountSql, new Object[]{pkPv, endDate+" 23:59:59"});
        			if(amountMap!=null){
        				result.put("sumAmount", amountMap.get("sumAmount").toString());
        				
            			String data = JsonUtil.writeValueAsString(result);
            			jsonData = JsonResult.toJsonObject(new JsonResult(data).success());
        			}else{
        				jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.NOT_EXIST, "未找到患者住院费用数据"));
        			}
    			}else{
    				jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.NOT_EXIST, "未找到患者住院就诊数据：pkPv"));
    			}
    		}else{
    			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.PARAMS_ERROR, "[patientId]、[times]、[endDate]参数不能为空"));
    		}
		} catch (Exception e) {
			e.printStackTrace();
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[getSumAmountByDate]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
	/**
	 * 住院一日清单：查询患者费用汇总【实际收费】 - 按账单码显示
	 * @param requestBody
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getBlCgIpSummer")
	public void getBlCgIpSummer(@RequestBody String requestBody, 
			HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		String patientId = paramMap.get("patientId");
    		String times = paramMap.get("times");
    		String queryDate = paramMap.get("queryDate");
    		if(StringUtils.isNotEmpty(patientId) && StringUtils.isNotEmpty(times) && StringUtils.isNotEmpty(queryDate)){
    			// 查询患者信息
    			Map<String, Object> map = getPkPiAndPkPv(patientId, times);
    			if(map!=null){
        			// {pkPvs:患者就诊主键list（必填）,dateBegin：费用开始日期（必填）,dateEnd：费用结束日期（必填）,type：0物品 1诊疗(非必填)}
        			Map<String, Object> queryMap = new HashMap<String, Object>();
        			queryMap.put("pkPvs", new String[]{map.get("pkPv").toString()});//患者就诊主键list（必填）
        			queryMap.put("dateBegin", queryDate+"000000");//费用开始日期（必填）
        			queryMap.put("dateEnd", queryDate+"235959");//费用结束日期（必填）
        			queryMap.put("type", "");//0物品 1诊疗(非必填)
        			
        			String param = JsonUtil.writeValueAsString(queryMap);
        			List<Map<String, Object>> result = cgIpQueryServcie.queryCgIpSummer(param, UserContext.getUser());
    				
        			String data = JsonUtil.writeValueAsString(result);
        			jsonData = JsonResult.toJsonObject(new JsonResult(data).success());
    			}else{
    				jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.NOT_EXIST, "未找到患者住院就诊数据：pkPv"));
    			}
    		}else{
    			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.PARAMS_ERROR, "[patientId]、[times]、[endDate]参数不能为空"));
    		}
		} catch (Exception e) {
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[getBlCgIpSummer]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
	/**
	 *住院一日清单：查询患者费用明细【实际收费】 - 按分类中的项目汇总查询
	 * @param requestBody
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getBlCgIpDetails")
	public void getBlCgIpDetails(@RequestBody String requestBody, 
			HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		String patientId = paramMap.get("patientId");
    		String times = paramMap.get("times");
    		String queryDate = paramMap.get("queryDate");
    		if(StringUtils.isNotEmpty(patientId) && StringUtils.isNotEmpty(times) && StringUtils.isNotEmpty(queryDate)){
    			// 查询患者信息
    			Map<String, Object> map = getPkPiAndPkPv(patientId, times);
    			if(map!=null){
        			// {pkPvs:患者就诊主键list（必填）,dateBegin：费用开始日期（必填）,dateEnd：费用结束日期（必填）,type：0物品 1诊疗(非必填)}
        			Map<String, Object> queryMap = new HashMap<String, Object>();
        			queryMap.put("pkPvs", new String[]{map.get("pkPv").toString()});//患者就诊主键list（必填）
        			queryMap.put("dateBegin", queryDate+"000000");//费用开始日期（必填）
        			queryMap.put("dateEnd", queryDate+"235959");//费用结束日期（必填）
        			queryMap.put("type", "");//0物品 1诊疗(非必填)
        			
        			String param = JsonUtil.writeValueAsString(queryMap);
        			List<Map<String, Object>> result = cgIpQueryServcie.queryCgIpDetails(param, UserContext.getUser());
        			
        			String data = JsonUtil.writeValueAsString(result);
        			jsonData = JsonResult.toJsonObject(new JsonResult(data).success());
    			}else{
    				jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.NOT_EXIST, "未找到患者住院就诊数据：pkPv"));
    			}
    		}else{
    			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.PARAMS_ERROR, "[patientId]、[times]、[queryDate]参数不能为空"));
    		}
		} catch (Exception e) {
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[getBlCgIpDetails]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
	/**
	 * 查询患者住院就诊主键
	 * @param patientId		患者主索引
	 * @param times			住院就诊次数
	 * @return				[pkPi:"患者信息主键",pkPv:"患者就诊主键"]
	 */
	private Map<String, Object> getPkPiAndPkPv(String patientId, String times){
		String mapSql = "select i.PK_PV, n.PK_PI from pv_ip_notice n, PV_ENCOUNTER i  where n.PK_PV_IP=i.PK_PV and n.PK_PV_OP like ? and i.FLAG_CANCEL='0' and i.eu_status<2 ORDER BY n.ts";
		Map<String, Object> data = DataBaseHelper.queryForMap(mapSql, new Object[]{patientId+"-%"});
		if(data==null){
			data = getPiPv(patientId);
		}
		return data;
	}
	
	/**
	 * 查询患者住院就诊主键
	 * @param patientId		患者主索引
	 * @param times			住院就诊次数
	 * @return				[pkPi:"患者信息主键",pkPv:"患者就诊主键"]
	 */
	@RequestMapping("/getPkPv")
	public Map<String, Object> getPkPv(String patientId, String times){
		String mapSql = "select i.PK_PV, n.PK_PI from pv_ip_notice n, PV_ENCOUNTER i  where n.PK_PV_IP=i.PK_PV and n.PK_PV_OP like ? and i.FLAG_CANCEL='0' and i.eu_status<2 ORDER BY n.ts";
		Map<String, Object> data = DataBaseHelper.queryForMap(mapSql, new Object[]{patientId+"-%"});
		if(data==null){
			data = getPiPv(patientId);
		}
		return data;
	}
	
	private Map<String, Object> getPiPv(String patientId){
		StringJoiner sql = new StringJoiner("");
		sql.add(" SELECT pi.PK_PI, pv.PK_PV ");
		sql.add(" FROM PI_MASTER pi INNER JOIN PV_ENCOUNTER pv ON pv.PK_PI=pi.PK_PI");
		sql.add(" WHERE pv.EU_PVTYPE='3' and pv.eu_status<2 and pi.CODE_OP=? ");
		return DataBaseHelper.queryForMap(sql.toString(), new Object[]{patientId});
	}
	
	/**
	 * 查询患者住院基本信息
	 * @param requestBody
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getByCodeIp")
	public void getByCodeIp(@RequestBody String requestBody, 
			HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		String codeIp = paramMap.get("codeIp");
    		if(StringUtils.isNotEmpty(codeIp)){
    			String mapSql = "select * from PI_MASTER where code_ip=?";
    			Map<String, Object> map = DataBaseHelper.queryForMap(mapSql, new Object[]{codeIp});
    			if(map!=null){
        			String data = JsonUtil.writeValueAsString(map);
        			jsonData = JsonResult.toJsonObject(new JsonResult(data).success());
    			}else{
    				jsonData = JsonResult.toJsonObject(new JsonResult().success());
    			}
    		}else{
    			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.PARAMS_ERROR, "[codeIp]参数不能为空"));
    		}
		} catch (Exception e) {
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[getPiMaster]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
	/**
	 * 获取医生工号caid
	 * @param requestBody
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getUserCa")
	public void getUserCa(@RequestBody String requestBody, 
			HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		String codeEmp = paramMap.get("codeEmp");
    		if(StringUtils.isNotEmpty(codeEmp)){
    			// 查询用户CA信息
    			String mapSql = "select CAID from bd_ou_employee where CODE_EMP=?";
    			Map<String, Object> map = DataBaseHelper.queryForMap(mapSql, new Object[]{codeEmp});
    			if(map!=null){
        			String data = JsonUtil.writeValueAsString(map);
        			jsonData = JsonResult.toJsonObject(new JsonResult(data).success());
    			}else{
    				jsonData = JsonResult.toJsonObject(new JsonResult().success());
    			}
    		}else{
    			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.PARAMS_ERROR, "[codeEmp]参数不能为空"));
    		}
		} catch (Exception e) {
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[getUserCa]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}

}
