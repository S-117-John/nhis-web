package com.zebone.rpc.pro.impl;



import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.DataOption;
import com.zebone.platform.web.support.DataSupport;
import com.zebone.rpc.pro.RPro15Rpc;
import com.zebone.rpc.pro.WPro15Rpc;
import com.zebone.rpc.pro.WPro60Rpc;


public class ProRpcProxy implements RPro15Rpc, WPro15Rpc, WPro60Rpc {

	@Override
	public String handel(DataOption option, String param, IUser user) {
	    UserContext.setUser(user);
		return JsonUtil.writeValueAsString(DataSupport.doJava(option, param, user));
	}

}
