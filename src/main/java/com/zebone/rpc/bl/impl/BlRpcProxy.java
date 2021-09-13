package com.zebone.rpc.bl.impl;


import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.DataOption;
import com.zebone.platform.web.support.DataSupport;
import com.zebone.rpc.bl.RBl15Rpc;
import com.zebone.rpc.bl.WBl15Rpc;
import com.zebone.rpc.bl.WBl60Rpc;


public class BlRpcProxy implements RBl15Rpc,WBl15Rpc, WBl60Rpc {

	@Override
	public String handel(DataOption option, String param, IUser user) {
	    UserContext.setUser(user);
		return JsonUtil.writeValueAsString(DataSupport.doJava(option, param, user));
	}

}
