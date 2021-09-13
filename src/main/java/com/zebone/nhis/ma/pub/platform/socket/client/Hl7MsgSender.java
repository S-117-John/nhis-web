package com.zebone.nhis.ma.pub.platform.socket.client;

import com.zebone.nhis.ma.pub.platform.zb.comm.Hl7CodeFactory;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import org.apache.mina.filter.codec.ProtocolCodecFilter;

import java.nio.charset.Charset;

public class Hl7MsgSender extends AbstractClientExecutor {

    private Hl7MsgSender(ClientConfig clientConfig) {
        super(clientConfig);
    }

    /**
     * 延时加载其他配置化信息
     */
    private static class Hl7MsgSenderHolder{
        private static AbstractClientExecutor instance;
        static{
            ClientConfig clientConfig = ServiceLocator.getInstance().getBean(ClientConfig.class);
            clientConfig.getProtocolCodecFilterList().add(new ProtocolCodecFilter(new Hl7CodeFactory(Charset.forName("GBK"))));
            instance = new Hl7MsgSender(clientConfig);
        }
    }

    public static AbstractClientExecutor getInstance(){
        return Hl7MsgSenderHolder.instance;
    }


    public void send(MsgBounder msgBounder){
        super.sendMsg(msgBounder);
    }

}
