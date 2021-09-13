package com.zebone.rpc.base.impl;


import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.DataOption;
import com.zebone.platform.web.support.DataSupport;
import com.zebone.rpc.base.RBase15Rpc;
import com.zebone.rpc.base.WBase15Rpc;
import com.zebone.rpc.base.WBase60Rpc;


public class BaseRpcProxy implements RBase15Rpc, WBase15Rpc, WBase60Rpc {

	@Override
	public String handel(DataOption option, String param, IUser user) {
	    UserContext.setUser(user);
		return JsonUtil.writeValueAsString(DataSupport.doJava(option, param, user));
	}

}
