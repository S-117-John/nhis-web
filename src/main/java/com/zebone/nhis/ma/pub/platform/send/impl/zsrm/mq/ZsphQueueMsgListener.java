package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.mq;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.service.ZsphPlatFormSendPubHandler;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;

public class ZsphQueueMsgListener implements MessageListener {

    private static Logger loger = org.slf4j.LoggerFactory.getLogger("nhis.lbHl7Log");

    @Autowired
    private ZsphPlatFormSendPubHandler sendPubHandler;

    @Override
    public void onMessage(Message message) {

    }
}
