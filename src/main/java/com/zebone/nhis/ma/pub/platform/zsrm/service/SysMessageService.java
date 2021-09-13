package com.zebone.nhis.ma.pub.platform.zsrm.service;


public interface SysMessageService {

    void saveReceiveMessage(String id,String msgType,String param,String errMsg);
}
