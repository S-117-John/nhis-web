package com.zebone.nhis.ma.pub.service;

import com.google.common.collect.Lists;
import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.ma.pub.platform.zb.comm.Hl7MsgHander;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 消息重发统一入口，前台、任务等都可以调用<br>
 *     不区分具体项目,按配置执行
 */
@Service
public class MsgRetryService {

    private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");

    @Value("#{applicationProperties['msg.retryHandler.class']}")
    private String msgRetryHandlerClass;

    @Autowired
    private Hl7MsgHander hl7MsgHander;

    private IMsgRetryHandler msgRetryHandler;

    @PostConstruct
    public void init(){
        msgRetryHandler = StringUtils.isBlank(msgRetryHandlerClass)?hl7MsgHander:ServiceLocator.getInstance().getBean(msgRetryHandlerClass,IMsgRetryHandler.class);
    }

    /**
     * 支持传入map，传入List
     * @param param
     * @param user
     */
    public void reSend(String param, IUser user){
        //重发同步或者异步、修改状态、是否生成新消息等逻辑，各个底层按要求自行处理。
        List<SysMsgRec> list = null;
        if(StringUtils.isNotBlank(param)) {
            list = param.startsWith("{")? Lists.newArrayList(JsonUtil.readValue(param, new TypeReference<SysMsgRec>() {}))
                    :JsonUtil.readValue(param, new TypeReference<List<SysMsgRec>>() {});
        }
        if(CollectionUtils.isNotEmpty(list)){
            list.stream().forEach(sysMsgRec -> reSend(sysMsgRec));
        }
    }


    public void reSend(SysMsgRec sysMsgRec){
        msgRetryHandler.send(sysMsgRec);
        log.info("消息重发完成：{}",sysMsgRec.getMsgId());
    }
}
