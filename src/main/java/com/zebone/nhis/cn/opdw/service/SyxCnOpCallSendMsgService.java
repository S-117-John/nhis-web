package com.zebone.nhis.cn.opdw.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 门诊叫号推送
 * @author cuijiansheng 2019.8.15
 *
 */
@Service
public class SyxCnOpCallSendMsgService {

	/**
	 * 推送叫号信息服务 ：004003011039
	 * @param param
	 * @param user
	 */
	public void setQueueList(String param, IUser user){
		
		User u = (User)user;
		
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		map.put("userId", u.getId());
		map.put("userName", u.getUserName());
		//前台传入map - type  1:叫号 2：重呼 3：过号
		PlatFormSendUtils.sendCnOpCall(map);
	}
}
