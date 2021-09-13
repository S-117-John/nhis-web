package com.zebone.rpc.emr.impl;


import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.DataOption;
import com.zebone.platform.web.support.DataSupport;
import com.zebone.rpc.emr.REmr15Rpc;
import com.zebone.rpc.emr.WEmr15Rpc;
import com.zebone.rpc.emr.WEmr60Rpc;


public class EmrRpcProxy implements REmr15Rpc,WEmr15Rpc,WEmr60Rpc{

	@Override
	public String handel(DataOption option, String param, IUser user) {
	    UserContext.setUser(user);
		return JsonUtil.writeValueAsString(DataSupport.doJava(option, param, user));
	}

}
