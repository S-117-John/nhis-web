package com.zebone.rpc.pv.impl;


import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.DataOption;
import com.zebone.platform.web.support.DataSupport;
import com.zebone.rpc.pv.RPv15Rpc;
import com.zebone.rpc.pv.WPv15Rpc;
import com.zebone.rpc.pv.WPv60Rpc;


public class PvRpcProxy implements RPv15Rpc,WPv15Rpc, WPv60Rpc {

	@Override
	public String handel(DataOption option, String param, IUser user) {
	    UserContext.setUser(user);
		return JsonUtil.writeValueAsString(DataSupport.doJava(option, param, user));
	}

}
