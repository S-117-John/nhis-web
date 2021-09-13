package com.zebone.nhis.webservice.pskq.service;

import com.zebone.nhis.webservice.pskq.model.listener.ResultListener;

public interface AdvancePaymentService {

    void save(String param, ResultListener resultListener);
}
