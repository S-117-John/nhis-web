package com.zebone.platform.web.service.impl;

import com.zebone.nhis.common.support.RedisUtils;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.service.WebService;
import com.zebone.platform.web.support.DataOption;
import com.zebone.platform.web.support.ResponseJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class WebServiceImpl implements WebService {
	
	
	@Autowired
	public ServiceLocator serviceLocator;
	
	@PostConstruct
	public void initWebCach(){
		/**
		 * Web应用重启时清除缓存
		 */
		RedisUtils.delCache("sys:trancode");
		
		RedisUtils.delCache("bd:datasource");
		
		this.deleteCacheUser();
	}
	
	long logExceTime = 0;

	@SuppressWarnings("rawtypes")
	@Override
	public Map getTrancode(String code) {
		
		Map map = RedisUtils.getCacheHashObj("sys:trancode", code,Map.class);
		
		if(map == null){
			map= DataBaseHelper.queryForMap("select * from SYS_SERVICE_REGISTER where trans_code = ?",  code);
			RedisUtils.setCacheHashObj("sys:trancode", code, map);
		}

		return map;
	}

	@Override
	@Transactional
	public void saveTrancodeLog(Map transmap , ResponseJson rjson, User user, String param, DataOption actionoption, long exectime) {
	
		if(logExceTime == 0){
			logExceTime = Long.valueOf(Application.getApplicationSettings().getProperty("log.read.exec.time", "1000"));
		}
		
		if(transmap.get("rpcRetrun") != null){
			try{
				rjson = JsonUtil.readValue(transmap.get("rpcRetrun").toString() , ResponseJson.class);
			}catch(Exception e){
				rjson.setDesc("rpc调用 日志格式化输出参数异常，具体结果 ， 请看输出参数");
				rjson.setStatus(-10000);
			}
		}

		//如果关于查询的服务，如果执行时间小于 规定时间不记录日志
		
		if(rjson.getStatus() >= 0 ){
			if(actionoption.getType().equals("sql") && actionoption.getSql().trim().toLowerCase().startsWith("select") ){
				if(exectime < logExceTime){  
					return;
				}
			}else if(actionoption.getType().equals("java") && actionoption.getSql().trim().toUpperCase().startsWith("R") ){
				if(exectime < logExceTime){  
					return;
				}
			}
		}
			
		String inparam = getData(param);
		int outDatasize = 0;
		if(rjson.getData()!=null){
			try {
				outDatasize = JsonUtil.writeValueAsString(rjson.getData()).getBytes("GBK").length/1024;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		String outparam = getData(JsonUtil.writeValueAsString(rjson));
		String log = getData(rjson.getDesc());
	
		String type = "1";
		if(rjson.getStatus() == -1){
			type = "2";
		}else if(rjson.getStatus() == -50){
			type = "4";
		}else if(rjson.getStatus() == -10000){
			type = "9";
			outparam = getData( transmap.get("rpcRetrun").toString());
		}else if(rjson.getStatus() < 0){
			type = "3";
		}

		Object transCode = transmap.get("transCode");
		Object transName = transmap.get("name");
		
		String logSql = "insert into sys_log (pk_log, trans_code, type,log, in_param, out_param, creator, create_time, exec_time,TRANS_NAME,result_size) values (?,?,?,?,?,?,?,?,?,?,?)";
      
		DataBaseHelper.update(logSql, NHISUUID.getKeyId(),transCode , type ,log , inparam , outparam , user.getPkEmp(),new Date(),exectime,transName,outDatasize);
	}
	
	
	private String getData(String str){
		if(str == null){
			return "";
		}
		
		int m = 0 , count = 0;  
        char arr[] = str.toCharArray();  
        int len  = arr.length;
        for(int i=0;i<len;i++)  
        {  
            char c = arr[i];  
            if((c >= 0x0391 && c <= 0xFFE5))  //中文字符  
            {
                m = m + 3;
            }  
            else{  
                m = m + 1;  
            }
            if(m > 3500 ){
            	break;
            }
            count++;
        } 
        
        if(m > 3500){
        	return str.substring(0, count );
        }else{
        	return str;
        }
	
	}
	/**
	 * 删除缓存中的用户信息
	 */
	private void deleteCacheUser(){
		String keyUserPrefix = "redis_user:";
		Set<String> users = RedisUtils.getRedisTemplate().keys(RedisUtils.getRedisPreName() + keyUserPrefix + "*");
        if(users!=null&&users.size()>0){
        	List<String> list = new ArrayList<>(users);
        	for(String user:list){
        		RedisUtils.getRedisTemplate().delete(user);
        	}
        }
		 
	}
 
}
