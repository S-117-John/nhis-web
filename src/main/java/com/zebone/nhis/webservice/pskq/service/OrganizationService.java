package com.zebone.nhis.webservice.pskq.service;

import com.zebone.nhis.webservice.pskq.model.listener.ResultListener;

public interface OrganizationService {

    void findByOrgCode(String param, ResultListener listener);
}
