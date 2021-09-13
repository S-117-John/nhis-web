package com.zebone.rpc.sch.impl;


import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.DataOption;
import com.zebone.platform.web.support.DataSupport;
import com.zebone.rpc.sch.RSch15Rpc;
import com.zebone.rpc.sch.WSch15Rpc;
import com.zebone.rpc.sch.WSch60Rpc;


public class SchRpcProxy implements RSch15Rpc, WSch15Rpc, WSch60Rpc {

	@Override
	public String handel(DataOption option, String param, IUser user) {
	    UserContext.setUser(user);
		return JsonUtil.writeValueAsString(DataSupport.doJava(option, param, user));
	}

}
