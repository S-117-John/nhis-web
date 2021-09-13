package com.zebone.nhis.task.sms.executor;


import com.zebone.nhis.task.sms.service.MsgService;
import org.slf4j.Logger;


public class DefaultMessageExecutor  implements MessageExecutor{

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(DefaultMessageExecutor.class);
    @Override
    public void execute(MsgService service, final Object data) {
        try{
            if(service !=null && data != null){
                service.send(data);
            }
        } catch (Exception e){
            logger.error("MessageExecutorError:",e);
        }
    }

}
