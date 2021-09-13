package com.zebone.nhis.webservice.pskq.service;

import com.zebone.nhis.webservice.pskq.model.listener.ResultListener;
import com.zebone.nhis.webservice.pskq.model.message.RequestBody;

public interface PatientService {

    /**
     * 查询个人信息
     * @param param
     * @param listener
     */
    void findByEmpiId(String param, ResultListener listener);

    /**
     * 患者注册
     * @param
     * @param listener
     */
    void save(RequestBody requestBody, ResultListener listener);

    /**
     * 患者合并
     * @param requestBody
     * @param listener
     */
    void merge(RequestBody requestBody, ResultListener listener);
}
