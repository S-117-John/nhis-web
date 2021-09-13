package com.zebone.nhis.webservice.cxf.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.apache.shiro.codec.Base64;
import org.springframework.beans.factory.annotation.Autowired;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.cxf.INHISGZWebService;
import com.zebone.nhis.webservice.service.EmrPubRecService;
import com.zebone.nhis.webservice.support.RespJson;
import com.zebone.nhis.webservice.syx.service.CnCreateOrdService;
import com.zebone.nhis.webservice.syx.service.ExternalInterfaceService;
import com.zebone.nhis.webservice.syx.service.PiAddrKMService;
import com.zebone.nhis.webservice.syx.service.SyxBlWsService;
import com.zebone.nhis.webservice.syx.vo.ExtRequest;
import com.zebone.nhis.webservice.syx.vo.ExtReturn;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.utils.JsonUtil;

@WebService
@SOAPBinding(style = Style.RPC)
public class NHISGZWebServiceImpl implements INHISGZWebService {

	@Autowired
	private PiAddrKMService piAddrKMService;
	
	@Resource
	private EmrPubRecService emrRecService;//病历服务
	
	@Resource
	private SyxBlWsService syxBlWsService;//住院记费服务
	
	@Resource 
	private CnCreateOrdService cnCreateOrdService;
	
	
	@Resource
	private ExternalInterfaceService externalInterfaceService;
	
	/**
	 * 患者地址注册
	 */
	@SuppressWarnings("static-access")
	@Override
	public String acceptUserAddrInfo(String strBase64) {
		
		//解密得到xml
		Base64 base64 = new Base64();
		String requestXML = base64.decodeToString(strBase64.getBytes());
		
		//数据处理
		String responseXML= piAddrKMService.registerAddr(requestXML);
		
		//加密返回
		return base64.encodeToString(responseXML.getBytes());
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public String NHISInterface(String input_info) {
		System.out.print("传入参数:" + input_info +"\n");//测试使用
		String result = CommonUtils.getString(new RespJson("99|调用成功！|", true));
		String funcId = "";
		String param = "";
		try {
			Map<String, Object> paramMap = JsonUtil.readValue(input_info,Map.class);
			param = paramMap.toString();
			funcId = CommonUtils.getString(paramMap.get("func_id"));
			if(CommonUtils.isEmptyString(funcId)){
				return CommonUtils.getString(new RespJson("99|未获取到接口调用的功能号func_id！", false));
			}

			switch(funcId) {  
			case "EMR01": //患者列表
				result = emrRecService.queryPatList(paramMap);
				//System.out.println("result emr1:"+result);
				break; 
			case "EMR02": //病历文档记录
				result = emrRecService.queryPatMedRecList(paramMap);
				//System.out.println("result emr2:"+result);
				break; 
			case "EMR03": //医嘱记录
				result = emrRecService.queryOrdList(paramMap);
				//System.out.println("result emr3:"+result);
				break; 
			case "EMR04": //登录接口
				result = emrRecService.userLogin(paramMap);
				//System.out.println("result emr4:"+result);
				break;
			case "CHARGE01": 
				result = syxBlWsService.chargeIpBatch(input_info);
				break; 
			case "CNBloodOrd": 
				result = cnCreateOrdService.CnOrderBlood(input_info);
				break; 
			default: 
				result = "99|未找到功能号"+funcId+"对应的业务接口！";
				return CommonUtils.getString(new RespJson(result, false));
			} 
		} catch (Exception e) {
			e.printStackTrace();
			//wslogService.InsertWsLogs(funcId, CommonUtils.isEmptyString(param) ? input_info : param,"99|" + e.toString(),"0");//添加失败日志
			return CommonUtils.getString(new RespJson("99|" + e.toString().split(":")[1], false));
		} finally {
			//DataSourceRoute.putAppId("default");
		}
		return result;
	}

	//住院记费接口、医嘱生成接口
	@Override
	public String NHISInterfaceStr(String func_id, String pk_org, String content) {
		if(CommonUtils.isEmptyString(func_id)){
			return "未获取到接口调用的功能号func_id！";
		}		
		String result = func_id+"接口调用成功！";
		try {
			switch(func_id) {  
	         
			 //医嘱生成
	         case "CNORDER01": 
					result = cnCreateOrdService.analysisXml(content);
					break; 
			 //记费
	         case "CHARGE01": 
					result = syxBlWsService.analysisXml(content);
					break; 
	         default: 
	            result = "未找到功能号"+func_id+"对应的业务接口！";
	            break;  
	        } 
		} catch (Exception e) {
			e.printStackTrace();
			return "调用功能号"+func_id+"异常，"+e.toString();
		} finally {
			DataSourceRoute.putAppId("default");
		}
		return result;
	}

	@Override
	public String NHISInterfaceExt(String request) {
		
		try {
			ExtRequest req = (ExtRequest)XmlUtil.XmlToBean(request, ExtRequest.class);
			if(req == null)
				throw new Exception();
			return XmlUtil.beanToXml(externalInterfaceService.extManageMethod(req), ExtReturn.class);
		} catch (Exception e) {
			e.printStackTrace();
			ExtReturn returnErr = new ExtReturn();
			returnErr.setCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			returnErr.getResult().setText("请求失败！");
			returnErr.getResult().setId("AE");
			return  XmlUtil.beanToXml(returnErr, ExtReturn.class);
		}
	}
	
}
