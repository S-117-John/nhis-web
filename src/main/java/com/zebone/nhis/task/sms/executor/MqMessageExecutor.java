package com.zebone.nhis.task.sms.executor;


import com.zebone.nhis.task.sms.service.MsgService;
import com.zebone.platform.modules.exception.BusException;

public class MqMessageExecutor implements MessageExecutor {

    @Override
    public void execute(MsgService service, Object data) {
        throw new BusException("尚未实现！");
    }
}
