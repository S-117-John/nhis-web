package com.zebone.nhis.pro.zsba.mz.pub.api;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zebone.nhis.bl.pub.vo.OpCgTransforVo;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.sd.vo.EnoteResDataVo;
import com.zebone.nhis.pro.zsba.common.support.HttpClientUtils;
import com.zebone.nhis.pro.zsba.common.support.JsonResult;
import com.zebone.nhis.pro.zsba.common.support.StringTools;
import com.zebone.nhis.pro.zsba.mz.opcg.service.ZsbaOpCgSettlementService;
import com.zebone.nhis.pro.zsba.mz.pub.service.ZsbaBlInvEBillService;
import com.zebone.nhis.pro.zsba.mz.pub.support.EBillHttpUtils;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

import net.sf.json.JSONObject;

/**
 * 门诊结算-电子票据开立接口服务
 * @author lipz 
 *
 */
@Controller
@RequestMapping("/static/mz/st")
public class ZsbaSettelApiController {
	
	@Autowired ZsbaOpCgSettlementService settleService; 
	@Autowired ZsbaBlInvEBillService eBillService;
	
	/**
	 * 门诊预结算
	 * @param request
	 * @param response
	 */
	@RequestMapping("/opcgPreSett")
	public void opcgPreSett(@RequestBody String requestBody, 
			HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		
    		JSONObject queryMap = new JSONObject();
			queryMap.put("pkPi", paramMap.get("pkPi"));
			queryMap.put("pkPv", paramMap.get("pkPv"));
			if(paramMap.containsKey("codeEmp")){
				queryMap.put("machineName", paramMap.get("codeEmp"));//本地计算机名称，自助机、公众号传终端号
			}
			if(paramMap.containsKey("totalAmt")){
				queryMap.put("aggregateAmount", paramMap.get("totalAmt"));
			}
			if(paramMap.containsKey("ybZfAmt")){
				queryMap.put("amtInsuThird", paramMap.get("ybZfAmt"));
			}
			if(paramMap.containsKey("grZfAmt")){
				queryMap.put("xjzf", paramMap.get("grZfAmt"));
			}
    		
			//预结算
			OpCgTransforVo resultVo = settleService.countOpcgAccountingSettlement(queryMap.toString(), UserContext.getUser());
			jsonData = JsonResult.toJsonObject(new JsonResult(resultVo).success());
		} catch (Exception e) {
			e.printStackTrace();
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[opcgPreSett]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
	/**
	 * 门诊结算
	 * @param request
	 * @param response
	 */
	@RequestMapping("/opcgSett")
	public void opcgSett(@RequestBody String requestBody, 
			HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		
    		JSONObject queryMap = new JSONObject();
			queryMap.put("pkPi", paramMap.get("pkPi"));
			queryMap.put("pkPv", paramMap.get("pkPv"));
			queryMap.put("machineName", paramMap.get("codeEmp"));//本地计算机名称，自助机、公众号传终端号
			queryMap.put("invStatus", "-2");//不校验发票信息
			queryMap.put("flagPrint", "1");//打印发票
			if(paramMap.containsKey("totalAmt")) {
				queryMap.put("aggregateAmount", paramMap.get("totalAmt"));//结算总金额
			}
			queryMap.put("xjzf", paramMap.get("ybgzAmt"));//外部医保个帐金额
			queryMap.put("amtInsuThird", paramMap.get("amtInsuThird"));//外部医保支付金额
			queryMap.put("setlId", paramMap.get("ybPkSettle"));//外部医保结算主键
			queryMap.put("pkExtPay", paramMap.get("pkExtPay"));//已付款的第三方交易主键

			//结算
			OpCgTransforVo resultVo = settleService.mergeOpcgAccountedSettlement(queryMap.toString(), UserContext.getUser());
			if(resultVo!=null && StringUtils.isNotEmpty(resultVo.getPkSettle())){
				//票据开立
				openMzEleBill(resultVo.getPkPi(), resultVo.getPkPv(), resultVo.getPkSettle(), resultVo.getMachineName());
				
				jsonData = JsonResult.toJsonObject(new JsonResult(resultVo).success());
			}else{
				jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "结算失败"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			PrintStream pout = new PrintStream(out);
			e.printStackTrace(pout);
			String ret = new String(out.toByteArray());
			
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[opcgSett]系统异常："+e.getMessage()+"; "+ret));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
	/**
	 * 获取收费指引单信息
	 * @param requestBody
	 * @param request
	 * @param response
	 */
	@RequestMapping("/guideInfo")
	public void guideInfo(@RequestBody String requestBody, 
			HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		
    		JSONObject queryMap = new JSONObject();
    		queryMap.put("pkPv", paramMap.get("pkPv"));
			queryMap.put("pkSettle", paramMap.get("pkSettle"));
    		
			Map<String, Object> returnMap = settleService.getVisitSettleGuideInfo(queryMap.toString(), UserContext.getUser());
			jsonData = JsonResult.toJsonObject(new JsonResult(returnMap).success());
		} catch (Exception e) {
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[guideInfo]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
	/*
	 * 开立电子票据，开启线程处理不等待结果
	 */
	private void openMzEleBill(String pkPi, String pkPv, String pkSettle, String machineName){
		try {
			JSONObject queryMap = new JSONObject();
			queryMap.put("pkPi", pkPi);
			queryMap.put("pkPv", pkPv);
			queryMap.put("pkSettle", pkSettle);
			queryMap.put("machineName", machineName);//本地计算机名称，自助机、公众号传终端号
			
			eBillService.eBillMzOutpatient(queryMap.toString(), UserContext.getUser());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 开立电子票据
	 * 先查数据库，如果存在直接返回，不存在则进行开立后返回
	 * @param pkPi
	 * @param pkPv
	 * @param pkSettle
	 * @param machineName
	 * @param request
	 * @param response
	 */
	@RequestMapping("/openMzEleBill")
	public void openMzEleBill(String pkPi, String pkPv, String pkSettle, String machineName,
			HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
		try {
			String urlEbill = "";
			String sql = "SELECT i.URL_EBILL FROM BL_ST_INV si LEFT JOIN BL_INVOICE i on i.PK_INVOICE = si.PK_INVOICE WHERE si.PK_SETTLE=?";
			Map<String, Object> dataMap = DataBaseHelper.queryForMap(sql, pkSettle);
			if(dataMap!=null && dataMap.containsKey("urlEbill") && dataMap.get("urlEbill")!=null){
				urlEbill = dataMap.get("urlEbill").toString();
			}else{
				JSONObject queryMap = new JSONObject();
				queryMap.put("pkPi", pkPi);
				queryMap.put("pkPv", pkPv);
				queryMap.put("pkSettle", pkSettle);
				queryMap.put("machineName", machineName);//本地计算机名称，自助机、公众号传终端号
				
				OpCgTransforVo octv = eBillService.eBillMzOutpatient(queryMap.toString(), UserContext.getUser());
				if(octv!=null && octv.getBlInvoices()!=null && octv.getBlInvoices().size()>0){
					urlEbill = octv.getBlInvoices().get(0).getUrlEbill();
				}
			}
			jsonData = JsonResult.toJsonObject(new JsonResult(urlEbill).success());
		} catch (Exception e) {
			e.printStackTrace();
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[openMzEleBill]系统异常："+e.getMessage()));
		}finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
	/**
	 * 冲红电子票据(仅用于电子票据冲红使用，不做NHIS数据交互)
	 * http://127.0.0.1:80/HISGL/static/mz/st/writeOffEbill?billBatchCode=44060120&billNo=0083613149&reason=业务退费&operator=公众号-98&placeCode=99998
	 */
	@RequestMapping("/writeOffEbill")
	public void writeOffEbill(String billBatchCode, String billNo, String reason, String operator, 
			String placeCode, HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
			Map<String,Object> reqMap = new HashMap<>(16);
			//电子票据代码
			reqMap.put("billBatchCode", billBatchCode);
			//电子票据号码
			reqMap.put("billNo", billNo);
			//冲红原因
			reqMap.put("reason", reason);
			//经办人
			reqMap.put("operator", operator);
			//业务发生时间
			reqMap.put("busDateTime", DateUtils.getDate("yyyyMMddHHmmssSSS"));
			//开票点编码
			reqMap.put("placeCode", placeCode);
			//将Data内容转换为json格式再base64编码,编码字符集UTF-8
			String dataJson = JsonUtil.writeValueAsString(reqMap);
			//发送请求
			EnoteResDataVo dataVo = EBillHttpUtils.enoteRts(dataJson,"1.0","writeOffEBill");
			jsonData = JsonResult.toJsonObject(new JsonResult(dataVo).success());
    	} catch (Exception e) {
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[opcgPreSett]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
	/**
	 * 人工推送待缴费通知到患者微信-主要在需要通知患者重新补缴费用时使用
	 * http://127.0.0.1:80/HISGL/static/mz/st/pushWechatMessageToPatient?name=雷钰欣&codeOp=000649465100&openId=oAdlHtwKgufEi5Fp0rFJpiayn8Pg&amount=537.67&datePv=2021-07-11 23:42:37&dateSt=2021-07-12 00:04:17&dateRefund=2021-07-12 00:06:32
	 */
	@RequestMapping("/pushWechatMessageToPatient")
	public void pushWechatMessageToPatient(String name, String codeOp, String openId, String amount, String datePv, String dateSt, String dateRefund, HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		if(StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(codeOp) && StringUtils.isNotEmpty(openId) 
				&& StringUtils.isNotEmpty(amount) && StringUtils.isNotEmpty(datePv) && StringUtils.isNotEmpty(dateSt) 
				&& StringUtils.isNotEmpty(dateRefund)) {
    			String itemSql = "select TOP 1 SHORTNAME, NAME from BD_DEFDOC where CODE_DEFDOCLIST=? and CODE=?";
    			Map<String, Object> dictMap = DataBaseHelper.queryForMap(itemSql, "BAXTJKQZ", "NHIS_API_URI");
    			if(dictMap!=null && dictMap.containsKey("name")){
    	    		String url = dictMap.get("name").toString() + "/nhis_api/wechat/push/openPush";
    				Map<String, Object> param = new HashMap<String, Object>();
    				param.put("type", "1");
    				param.put("codeOp", codeOp);
    				param.put("first", "【姓名："+name+"，门诊ID："+codeOp+"】您在中山市博爱医院于"+datePv+"就诊并在"+dateSt+"缴费"+amount+"元，由于系统故障于"+dateRefund+"已自动原路退回；为了不影响您后续在我院就诊麻烦您在【中山市博爱医院】微信公众号补缴本次费用，给您带来困扰万分抱歉，谢谢！");
    				param.put("keyword", amount+"@-@"+dateSt);
    				param.put("remark", "点击【查看详情】浏览待缴费信息");
    				param.put("openId", openId);
    				HttpClientUtils.getMap(url, param);
    				jsonData = JsonResult.toJsonObject(new JsonResult("微信通知已推送").success());
    			}else{
    				jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "微信通知推送接口地址获取失败"));
    			}
    		}else{
    			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "微信通知推送参数错误"));
    		}
    	} catch (Exception e) {
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
}
