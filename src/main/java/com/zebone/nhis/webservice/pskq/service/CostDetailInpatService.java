package com.zebone.nhis.webservice.pskq.service;

import com.zebone.nhis.webservice.pskq.model.listener.ResultListener;
import com.zebone.nhis.webservice.pskq.model.message.RequestBody;

public interface CostDetailInpatService {

    void save(String param, ResultListener listener);

    void cancel(String param, ResultListener listener);

    void billing(String param,ResultListener listener) throws IllegalAccessException;
}
