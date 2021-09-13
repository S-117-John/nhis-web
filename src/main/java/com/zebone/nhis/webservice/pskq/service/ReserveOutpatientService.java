package com.zebone.nhis.webservice.pskq.service;

import com.zebone.nhis.webservice.pskq.model.listener.ResultListener;
import com.zebone.nhis.webservice.pskq.model.message.RequestBody;

/**
 * 预约服务
 */
public interface ReserveOutpatientService {

    void save(RequestBody requestBody, ResultListener listener);

    void edit(RequestBody requestBody, ResultListener listener);


}
