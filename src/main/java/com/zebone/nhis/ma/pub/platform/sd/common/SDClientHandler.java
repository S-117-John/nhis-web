package com.zebone.nhis.ma.pub.platform.sd.common;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端Handler(灵璧复制版本)
 * @author chengjia
 *
 */
public class SDClientHandler extends IoHandlerAdapter {
	
	private static Logger loger = LoggerFactory.getLogger("nhis.lbHl7Log");
    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        // TODO Auto-generated method stub
        //super.messageReceived(session, message);
        System.out.println("message :"+message);
        loger.info("SDClientHandler-messageReceived:"+message);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        // TODO Auto-generated method stub
        super.sessionClosed(session);
        loger.info("SDClientHandler-sessionClosed:连接关闭");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        // TODO Auto-generated method stub
        super.sessionIdle(session, status);
        loger.info("SDClientHandler-sessionIdle");
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
 
        System.out.println("发送的消息是："+message.toString());        
        //super.messageSent(session, message);
        loger.info("SDClientHandler-messageSent:"+message.toString());
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        
        super.sessionCreated(session);
        loger.info("SDClientHandler-sessionCreated 创建连接");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
        loger.info("SDClientHandler-sessionOpened 打开连接");
    }  
    
}