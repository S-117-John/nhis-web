package com.zebone.nhis.ma.pub.service;

import com.zebone.nhis.common.module.ma.msg.SysMsgRec;

/**
 * 消息重发处理接口，各项目可自行实现逻辑
 */
public interface IMsgRetryHandler {

    void send(SysMsgRec sysMsgRec);
}
