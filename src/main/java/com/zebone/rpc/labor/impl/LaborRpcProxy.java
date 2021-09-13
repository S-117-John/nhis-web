package com.zebone.rpc.labor.impl;



import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.DataOption;
import com.zebone.platform.web.support.DataSupport;
import com.zebone.rpc.labor.RLabor15Rpc;
import com.zebone.rpc.labor.WLabor15Rpc;
import com.zebone.rpc.labor.WLabor60Rpc;


public class LaborRpcProxy implements RLabor15Rpc, WLabor15Rpc, WLabor60Rpc {

	@Override
	public String handel(DataOption option, String param, IUser user) {
	    UserContext.setUser(user);
		return JsonUtil.writeValueAsString(DataSupport.doJava(option, param, user));
	}

}
