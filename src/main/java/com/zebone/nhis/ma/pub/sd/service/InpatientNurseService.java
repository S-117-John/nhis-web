package com.zebone.nhis.ma.pub.sd.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 住院护士服务类
 * @author maijiaxing
 *
 */
@Service
public class InpatientNurseService {
	
	/**
     * 交易码：005002004013
     * 催缴预交金发消息（短信，微信信息）--深大项目
     * @param param
     * @param user
     * @return
     */
    public boolean sendMsg(String param, IUser user){
    	boolean rs = false;
    	if(null!=param&&!"".equals(param)){
    		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    		PlatFormSendUtils.sendCallPayMsg(map);
    		rs = true;
    	}
    	return rs;
    }
	

}
