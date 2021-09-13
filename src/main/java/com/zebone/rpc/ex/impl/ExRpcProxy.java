package com.zebone.rpc.ex.impl;


import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.DataOption;
import com.zebone.platform.web.support.DataSupport;
import com.zebone.rpc.ex.REx15Rpc;
import com.zebone.rpc.ex.WEx15Rpc;
import com.zebone.rpc.ex.WEx60Rpc;


public class ExRpcProxy implements REx15Rpc, WEx15Rpc, WEx60Rpc {

	@Override
	public String handel(DataOption option, String param, IUser user) {
	    UserContext.setUser(user);
		return JsonUtil.writeValueAsString(DataSupport.doJava(option, param, user));
	}

}
