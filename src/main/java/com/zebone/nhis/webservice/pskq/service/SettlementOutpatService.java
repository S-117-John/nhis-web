package com.zebone.nhis.webservice.pskq.service;

import com.zebone.nhis.webservice.pskq.model.listener.ResultListener;
import com.zebone.nhis.webservice.pskq.model.message.RequestBody;

/**
 * 门急诊结算服务
 */
public interface SettlementOutpatService {

    /**
     * 新增收费服务
     * @param param
     * @param listener
     */
    void save(RequestBody requestBody, ResultListener listener);

    /**
     * 退费服务
     * @param param
     * @param listener
     暂定无线上退费
    void refund(String param, ResultListener listener);

     */
}
