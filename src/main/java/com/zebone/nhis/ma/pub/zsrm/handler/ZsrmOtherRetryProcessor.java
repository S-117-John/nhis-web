package com.zebone.nhis.ma.pub.zsrm.handler;

import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.ma.pub.service.MsgRetryProcessor;
import org.springframework.stereotype.Service;

/**
 * 人民医院--》其他三方接口重发处理器
 */
@Service
public class ZsrmOtherRetryProcessor implements MsgRetryProcessor {

    @Override
    public String send(SysMsgRec msgRec) {
        return null;
    }

    /**
     * 设置对应的sysCode
     *
     * @return
     */
    @Override
    public String getSysCode() {
        return null;
    }
}
