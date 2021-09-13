package com.zebone.nhis.webservice.pskq.service;

import com.zebone.nhis.webservice.pskq.model.listener.ResultListener;

public interface MaterialItemService {

    void findById(String param, ResultListener listener);

    void findByOrgCode(String param, ResultListener listener);

    void save(String param, ResultListener listener);
}
