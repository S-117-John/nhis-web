package com.zebone.rpc.cn.impl;


import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.DataOption;
import com.zebone.platform.web.support.DataSupport;
import com.zebone.rpc.cn.RCn15Rpc;
import com.zebone.rpc.cn.WCn15Rpc;
import com.zebone.rpc.cn.WCn60Rpc;


public class CnRpcProxy implements RCn15Rpc,WCn15Rpc, WCn60Rpc {

	@Override
	public String handel(DataOption option, String param, IUser user) {
	    UserContext.setUser(user);
		return JsonUtil.writeValueAsString(DataSupport.doJava(option, param, user));
	}

}
