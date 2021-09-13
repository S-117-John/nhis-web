package com.zebone.rpc.scm.impl;


import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.DataOption;
import com.zebone.platform.web.support.DataSupport;
import com.zebone.rpc.scm.RScm15Rpc;
import com.zebone.rpc.scm.WScm15Rpc;
import com.zebone.rpc.scm.WScm60Rpc;


public class ScmRpcProxy implements RScm15Rpc, WScm15Rpc, WScm60Rpc {

	@Override
	public String handel(DataOption option, String param, IUser user) {
	    UserContext.setUser(user);
		return JsonUtil.writeValueAsString(DataSupport.doJava(option, param, user));
	}

}
