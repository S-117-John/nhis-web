package com.zebone.rpc.ma.impl;


import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.DataOption;
import com.zebone.platform.web.support.DataSupport;
import com.zebone.rpc.ma.RMa15Rpc;
import com.zebone.rpc.ma.WMa15Rpc;
import com.zebone.rpc.ma.WMa60Rpc;


public class MaRpcProxy implements RMa15Rpc, WMa15Rpc,WMa60Rpc{

	@Override
	public String handel(DataOption option, String param, IUser user) {
	    UserContext.setUser(user);
		return JsonUtil.writeValueAsString(DataSupport.doJava(option, param, user));
	}

}
