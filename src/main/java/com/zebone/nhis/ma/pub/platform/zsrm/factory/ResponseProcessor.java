package com.zebone.nhis.ma.pub.platform.zsrm.factory;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Response;
import com.zebone.nhis.ma.pub.platform.zsrm.model.message.ResponseBody;

public interface ResponseProcessor {

    ResponseBody create(Response response);
}
