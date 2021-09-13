package com.zebone.nhis.ma.pub.arch.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.arch.vo.ArchReqParam;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class FileArchService {

	private Logger log = LoggerFactory.getLogger("ZsrmArchService");//日志
	
	@Resource
	private FileArchHandler fileArchHandler;//归档服务
	
	@Resource
	private FileArchLogService wslogService;//日志记录数据库
	
	public String archive(String param) throws JsonParseException, JsonMappingException, IOException {
		Calendar nowDate=Calendar.getInstance();
		nowDate.setTime(new Date());//设置为当前系统时间
		Long timeNow=nowDate.getTimeInMillis();
		//param = StringEscapeUtils.unescapeJava(param);
		//param=param.replaceAll("\\", "\\\\");
        ArchReqParam archParam=JsonUtil.readValue(param, ArchReqParam.class);
//        Calendar nowDate2=Calendar.getInstance();
//        nowDate2.setTime(new Date());//设置为当前系统时间
//        Long timeNow2=nowDate2.getTimeInMillis();
//        Long time = (timeNow2-timeNow);
//        logger.info("time:"+time);
//        
		String funcId=archParam.getFuncId();
		String pkOrg = archParam.getPkOrg();
		
		if(CommonUtils.isEmptyString(funcId)){
			return "未获取到接口调用的功能号funcId！";
		}		
		String result = funcId+"接口调用成功！";
		try {
			switch(funcId) {  
			case "ARCH_01"://业务系统文件上传接口
				result = fileArchHandler.uploadFile(param,"1");
				break;
			case "ARCH_02"://文档查询接口 
				result = fileArchHandler.qryFile(param);
				break;
			case "ARCH_03"://文档下载
				result = fileArchHandler.downloadFile(param);
				break;
			case "ARCH_04"://解除归档
				result = fileArchHandler.cancelArch(param);
				break;
			case "ARCH_05"://查询归档状态
				result = fileArchHandler.queryFileStatus(param);
				break;
//			default: 
//				result = "未找到功能号"+func_id+"对应的业务接口！";
//				break;  
			} 
		} catch (Exception e) {
			e.printStackTrace();
			return "调用功能号"+funcId+"异常，"+e.toString();
		} finally {
			DataSourceRoute.putAppId("default");
		}
		log.info("\n出参:" + result +"\n");//测试使用
		return result;
	}

	
}
