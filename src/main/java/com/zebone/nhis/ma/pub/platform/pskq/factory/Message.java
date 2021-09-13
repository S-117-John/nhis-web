package com.zebone.nhis.ma.pub.platform.pskq.factory;

import com.zebone.nhis.ma.pub.platform.pskq.model.message.RequestBody;

import java.util.Map;

public interface Message {

    RequestBody getRequestBody(Map<String,Object> param) throws IllegalAccessException;
}
