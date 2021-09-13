package com.zebone.nhis.webservice.pskq.service;

import java.util.List;

import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.platform.framework.support.IUser;

public interface SysMessageService {

    /**
     * 保存消息
     * @param param
     */
    void save(String param);

    /**
     * 查需消息
     * @param
     */
    List<SysMsgRec> getMessageList(String param, IUser user);

    /**
     * 消息重发
     * @param param
     */
    void resend(String param);

    void saveReceiveMessage(String param);
}
