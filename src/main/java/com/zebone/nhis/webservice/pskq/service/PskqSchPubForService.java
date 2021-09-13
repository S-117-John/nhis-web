package com.zebone.nhis.webservice.pskq.service;

import com.zebone.nhis.webservice.pskq.model.listener.ResultListener;

public interface PskqSchPubForService {


    void findOrderByPkPi(String param, ResultListener listener);

}
