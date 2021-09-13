package com.zebone.nhis.ma.pub.platform.pskq.service;

import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.platform.framework.support.IUser;

import java.util.List;
import java.util.Map;

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
}
