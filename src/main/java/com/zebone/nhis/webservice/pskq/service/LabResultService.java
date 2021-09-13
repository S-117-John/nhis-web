package com.zebone.nhis.webservice.pskq.service;

import com.zebone.nhis.webservice.pskq.model.listener.ResultListener;

public interface LabResultService {

    /**
     * 检验结果新增服务
     */
    void save(String param, ResultListener listener);
}
