package com.zebone.nhis.webservice.cxf.impl;


import java.util.Map;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.webservice.cxf.INHISZSBAWebService;
import com.zebone.nhis.webservice.support.RespJson;
import com.zebone.nhis.webservice.zhongshan.service.ArchOuterHandler;
import com.zebone.nhis.webservice.zhongshan.service.CriticalService;
import com.zebone.nhis.webservice.zhongshan.service.ECGHandler;
import com.zebone.nhis.webservice.zhongshan.service.LISHandler;
import com.zebone.nhis.webservice.zhongshan.service.NhisCgHandler;
import com.zebone.nhis.webservice.zhongshan.service.NhisWSLogService;
import com.zebone.nhis.webservice.zhongshan.service.PiMasterHandler;
import com.zebone.nhis.webservice.zhongshan.service.RISHandler;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.utils.JsonUtil;

@WebService
@SOAPBinding(style = Style.RPC)
public class NHISZSBAWebServiceImpl implements INHISZSBAWebService {
	
	private Logger logger = LoggerFactory.getLogger("nhis.zsbaWebSrv");//日志
	
	@Resource
	private ECGHandler ecgHandler;//心电服务
	
	@Resource
	private LISHandler lisHandler;//Lis服务
	
	@Resource
	private ArchOuterHandler archOuterHandler;//归档服务
	
	@Resource
	private RISHandler risHandler;//ris服务
	
	@Resource
	private NhisCgHandler nhisCgHandler;//记费服务
	
	@Resource
	private PiMasterHandler piMasterHandler;//人工办卡服务
	
	@Resource
	private CriticalService criticalService;//危急值
	
	@Resource
	private NhisWSLogService wslogService;//日志记录数据库
	
	/**
	 * Json格式接口实现方法
	 */
	@Override
	@SuppressWarnings("unchecked")
	public String NHISInterface(String input_info) {
		logger.debug("\n入参:" + input_info +"\n");//测试使用
		String result = CommonUtils.getString(new RespJson("99|调用成功！|", true));
		String funcId = "";
		String param = "";
		try {
			Map<String, Object> paramMap = JsonUtil.readValue(input_info,Map.class);
			param = paramMap.toString();
			funcId = CommonUtils.getString(paramMap.get("func_id"));
			if(CommonUtils.isEmptyString(funcId)){
				wslogService.InsertWsLogs(funcId, param, "99|未获取到接口调用的功能号func_id！","0");//添加失败日志
				return CommonUtils.getString(new RespJson("99|未获取到接口调用的功能号func_id！", false));
			}

			switch(funcId) {  
			case "ECG01": //中山心电系统 - ECG01：获取心电检查申请单
				result = ecgHandler.getXDAppList(paramMap);
				break;  
			case "ECG02": //中山心电系统 - ECG02：更新心电检查申请单
				result = ecgHandler.updateXDAppStatus(paramMap);
				break;  
			case "LIS01": //中山lis系统 - LIS01：条码打印与试管费用加收
				result = lisHandler.updateLabApplySampNo(input_info);
				break; 
			case "LIS02": //中山lis系统  - LIS02：更新标本状态
				result = lisHandler.updateLabApplyStatus(input_info);
				break; 
			case "LABOR01": //中山妇幼系统与产房接口  - LABOR01：同步更新NHIS系统的孕妇档案信息
				result = CommonUtils.getString(new RespJson("99|妇幼接口暂未实现！|", false));
				break;		 
			case "RIS01": //中山RIS系统 - RIS01：获取全部检查申请单（心电除外）
				result = risHandler.getRisAppList(paramMap);
				break; 
			case "RIS02": //中山RIS系统 - RIS02：更新检查申请单状态
				result = risHandler.updateRisApp(paramMap);
				break; 
			case "NHISCG01": //按记费项目记费
				result = nhisCgHandler.BlCg(paramMap);
				break; 
			case "NHISCG02": //按记费项目退费
				result = nhisCgHandler.BlCgRtn(paramMap);
				break;
			case "PiMaster01": //人工办卡
				result = piMasterHandler.qryOrSavePiInfo(input_info);
				break;
			default: 
				result = "99|未找到功能号"+funcId+"对应的业务接口！";
				wslogService.InsertWsLogs(funcId, param, result,"0");//添加失败日志
				return CommonUtils.getString(new RespJson(result, false));
			} 
		} catch (Exception e) {
			e.printStackTrace();
			wslogService.InsertWsLogs(funcId, CommonUtils.isEmptyString(param) ? input_info : param,"99|" + e.toString(),"0");//添加失败日志
			return CommonUtils.getString(new RespJson("99|" + e.toString().split(":")[1], false));
		} finally {
			DataSourceRoute.putAppId("default");
		}
		logger.debug("\n出参:" + result +"\n");//测试使用
		wslogService.InsertWsLogs(funcId, param,result,"1");//添加失败日志
		return result;
	}
    /**
     * Xml格式接口实现方法
     * @throws JAXBException 
     */
	@Override
	public String NHISInterfaceStr(String func_id, String pk_org, String content) {	
		String[] txt = content.split("<body>");
		logger.debug("\n入参:" + txt[0] +"\n");//测试使用
		if(CommonUtils.isEmptyString(func_id)){
			return "未获取到接口调用的功能号func_id！";
		}		
		String result = func_id+"接口调用成功！";
		try {
			switch(func_id) {  
			case "ARCH_01"://业务系统文件上传接口
				result = archOuterHandler.uploadFile(content);
				break;
			case "ARCH_02"://文档查询接口
				result = archOuterHandler.qryFile(content);
				break;
			case "ARCH_03"://文档下载
				result = archOuterHandler.downloadFile(content);
				break;
			case "ARCH_04"://解除归档
				result = archOuterHandler.cancelArch(content);
				break;
			case "ARCH_05"://查询归档状态
				result = archOuterHandler.queryFileStatus(content);
				break;
			case "CRITICAL_01":
				result = criticalService.addMsg(content);
				break;
			default: 
				result = "未找到功能号"+func_id+"对应的业务接口！";
				break;  
			} 
		} catch (Exception e) {
			e.printStackTrace();
			wslogService.InsertWsLogs(func_id, content,"99|" + e.toString(),"0");//添加失败日志
			return "调用功能号"+func_id+"异常，"+e.toString();
		} finally {
			DataSourceRoute.putAppId("default");
		}
		logger.debug("\n出参:" + result +"\n");//测试使用
		wslogService.InsertWsLogs(func_id, content,result,"1");//添加失败日志
		return result;
	}

	


	
}
