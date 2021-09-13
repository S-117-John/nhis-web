package com.zebone.rpc.move.impl;


import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.DataOption;
import com.zebone.platform.web.support.DataSupport;
import com.zebone.rpc.move.RMove15Rpc;
import com.zebone.rpc.move.WMove15Rpc;
import com.zebone.rpc.move.WMove60Rpc;


public class MoveRpcProxy implements RMove15Rpc, WMove15Rpc,WMove60Rpc{

	@Override
	public String handel(DataOption option, String param, IUser user) {
	    UserContext.setUser(user);
		return JsonUtil.writeValueAsString(DataSupport.doJava(option, param, user));
	}

}
