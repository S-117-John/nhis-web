package com.zebone.nhis.ma.pub.service;

import com.zebone.nhis.common.module.ma.msg.SysMsgRec;

/**
 * 消息重发具体处理器接口，可有可无（可以按照发送到平台，发送其他三方区分实现）
 */
public interface MsgRetryProcessor {

    String send(SysMsgRec msgRec);
    /**
     * 设置对应的sysCode
     * @return
     */
    String getSysCode();
}
