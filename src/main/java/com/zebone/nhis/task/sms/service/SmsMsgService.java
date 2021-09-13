package com.zebone.nhis.task.sms.service;

import com.zebone.nhis.ma.lb.service.SmsService;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.utils.JsonUtil;
import org.slf4j.Logger;

import java.util.List;

public class SmsMsgService implements MsgService {

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(SmsMsgService.class);

    @Override
    public void send(final Object data) {
        try{
            if(data != null){
                final SmsService service = ServiceLocator.getInstance().getBean(SmsService.class);
                final User user = UserContext.getUser();

                if(data != null && data instanceof List){
                    //由于发送短信的定时分组不会太多（时间点分组，不是内容分组），这里异步去调用业务层即可
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            UserContext.setUser(user);
                            service.smsSend(JsonUtil.writeValueAsString(data), user);
                        }
                    }).start();
                }
            }
        } catch (Exception e){
            logger.error("MessageExecutorError:",e);
        }
    }

}
