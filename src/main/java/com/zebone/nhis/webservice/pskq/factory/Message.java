package com.zebone.nhis.webservice.pskq.factory;

import com.zebone.nhis.webservice.pskq.model.message.RequestBody;
import com.zebone.nhis.webservice.pskq.model.message.ResponseBody;

import java.util.Map;

public interface Message {

    RequestBody getRequestBody(Map<String,Object> param) throws IllegalAccessException;

    ResponseBody getResponseBody(Map<String,Object> param) throws IllegalAccessException;
}
