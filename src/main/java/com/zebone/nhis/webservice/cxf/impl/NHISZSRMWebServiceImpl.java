package com.zebone.nhis.webservice.cxf.impl;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.webservice.cxf.INHISZSRMWebService;
import com.zebone.nhis.webservice.zhongshan.service.NhisWSLogService;
import com.zebone.nhis.webservice.zsrm.service.ZsrmArchOuterHandler;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;

@WebService
@SOAPBinding(style = Style.RPC)
public class NHISZSRMWebServiceImpl implements INHISZSRMWebService {

	private Logger logger = LoggerFactory.getLogger("nhis.zsbaWebSrv");//日志
	
	@Resource
	private ZsrmArchOuterHandler zsrmArchOuterHandler;//归档服务
	
	@Resource
	private NhisWSLogService wslogService;//日志记录数据库
	
	@Override
	public String NHISInterface(String Input_info) {
		// TODO Auto-generated method stub
		return null;
	}

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
				result = zsrmArchOuterHandler.uploadFile(content);
				break;
			case "ARCH_02"://文档查询接口
				result = zsrmArchOuterHandler.qryFile(content);
				break;
			case "ARCH_03"://文档下载
				result = zsrmArchOuterHandler.downloadFile(content);
				break;
			case "ARCH_04"://解除归档
				result = zsrmArchOuterHandler.cancelArch(content);
				break;
			case "ARCH_05"://查询归档状态
				result = zsrmArchOuterHandler.queryFileStatus(content);
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
