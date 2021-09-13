package com.zebone.rpc.nd.impl;



import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.DataOption;
import com.zebone.platform.web.support.DataSupport;
import com.zebone.rpc.nd.RNd15Rpc;
import com.zebone.rpc.nd.WNd15Rpc;
import com.zebone.rpc.nd.WNd60Rpc;


public class NdRpcProxy implements RNd15Rpc,WNd15Rpc,WNd60Rpc{

	@Override
	public String handel(DataOption option, String param, IUser user) {
	    UserContext.setUser(user);
		return JsonUtil.writeValueAsString(DataSupport.doJava(option, param, user));
	}

}
