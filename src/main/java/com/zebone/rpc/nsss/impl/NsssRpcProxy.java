package com.zebone.rpc.nsss.impl;



import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.DataOption;
import com.zebone.platform.web.support.DataSupport;
import com.zebone.rpc.nsss.RNsss15Rpc;
import com.zebone.rpc.nsss.WNsss15Rpc;
import com.zebone.rpc.nsss.WNsss60Rpc;


public class NsssRpcProxy implements RNsss15Rpc, WNsss15Rpc, WNsss60Rpc {

	@Override
	public String handel(DataOption option, String param, IUser user) {
	    UserContext.setUser(user);
		return JsonUtil.writeValueAsString(DataSupport.doJava(option, param, user));
	}

}
