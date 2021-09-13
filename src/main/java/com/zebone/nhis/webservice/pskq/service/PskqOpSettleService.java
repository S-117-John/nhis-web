package com.zebone.nhis.webservice.pskq.service;

import com.zebone.nhis.webservice.pskq.model.listener.ResultListener;

public interface PskqOpSettleService {


    void findByPkPiTime(String param, ResultListener listener);

}
