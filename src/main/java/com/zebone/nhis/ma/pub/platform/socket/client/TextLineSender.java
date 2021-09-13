package com.zebone.nhis.ma.pub.platform.socket.client;

import com.zebone.platform.modules.core.spring.ServiceLocator;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;

import java.nio.charset.Charset;

public class TextLineSender extends AbstractClientExecutor {

    public TextLineSender(ClientConfig clientConfig) {
        super(clientConfig);
    }

    private static class TextLineSenderHolder{
        private static AbstractClientExecutor instance;
        static{
            ClientConfig clientConfig = ServiceLocator.getInstance().getBean(ClientConfig.class);
            clientConfig.getProtocolCodecFilterList().add(new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("utf-8"))));
            instance = new TextLineSender(clientConfig);
        }
    }

    public static AbstractClientExecutor getInstance(){
        return TextLineSenderHolder.instance;
    }

    public void send(MsgBounder msgBounder){
        super.sendMsg(msgBounder);
    }

}
