package com.zebone.rpc.compay.impl;


import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.DataOption;
import com.zebone.platform.web.support.DataSupport;
import com.zebone.rpc.compay.RCompay15Rpc;
import com.zebone.rpc.compay.WCompay15Rpc;
import com.zebone.rpc.compay.WCompay60Rpc;


public class CompayRpcProxy implements RCompay15Rpc, WCompay15Rpc, WCompay60Rpc {

	@Override
	public String handel(DataOption option, String param, IUser user) {
	    UserContext.setUser(user);
		return JsonUtil.writeValueAsString(DataSupport.doJava(option, param, user));
	}

}
