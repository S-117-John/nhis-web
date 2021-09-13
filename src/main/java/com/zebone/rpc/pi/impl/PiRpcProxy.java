package com.zebone.rpc.pi.impl;


import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.DataOption;
import com.zebone.platform.web.support.DataSupport;
import com.zebone.rpc.pi.RPi15Rpc;
import com.zebone.rpc.pi.WPi15Rpc;
import com.zebone.rpc.pi.WPi60Rpc;


public class PiRpcProxy implements RPi15Rpc, WPi15Rpc,WPi60Rpc{

	@Override
	public String handel(DataOption option, String param, IUser user) {
	    UserContext.setUser(user);
		return JsonUtil.writeValueAsString(DataSupport.doJava(option, param, user));
	}

}
