package com.zebone.rpc.arch.impl;

import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.DataOption;
import com.zebone.platform.web.support.DataSupport;
import com.zebone.rpc.arch.RArch15Rpc;
import com.zebone.rpc.arch.WArch15Rpc;
import com.zebone.rpc.arch.WArch60Rpc;

public class ArchRpcProxy implements RArch15Rpc, WArch15Rpc, WArch60Rpc {

	@Override
	public String handel(DataOption option, String param, IUser user) {

		UserContext.setUser(user);
		return JsonUtil.writeValueAsString(DataSupport.doJava(option, param, user));
	}

}
