package com.zebone.nhis.webservice.pskq.service;

import com.zebone.nhis.webservice.pskq.model.listener.ResultListener;

public interface EmployeeService {

    void findById(String param, ResultListener listener);

    void findByOrgCode(String param, ResultListener listener);

    void save(String param, ResultListener listener);
}
