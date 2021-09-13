package com.zebone.nhis.ma.pub.platform.socket.client;

import com.zebone.nhis.ma.pub.platform.socket.support.IoStatisticsTools;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mina io 事件处理器<br>
 *     屏蔽io事件细节，避免不正确的关闭和session处理等。
 *   <br/>客户化实现不用实现IoHandlerAdapter或者继承此类，必要时实现CustomerHandler接口或者集成CustomerHandlerAdapter即可
 * @author alvin
 */
public final class ClientHandler extends IoHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger("nhis.lbHl7Log");

    /**客户化处理器*/
    private CustomerHandler clientHandler;

    public ClientHandler(){

    }
    public ClientHandler(CustomerHandler clientHandler){
        this.clientHandler = clientHandler;
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        logger.info("线程名：{}，session:{},发送的消息:{}",Thread.currentThread().getName(),session,message);
        if(clientHandler!= null){
            MsgBounder msgBounder = (MsgBounder) session.getAttribute(getBoundKey(session));
            if(msgBounder!=null && msgBounder.getMsg() == null){
                msgBounder.setMsg(message);
            }
            clientHandler.completeSend(msgBounder);
        }

    }

    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        logger.info("线程名：{},session:{},收到响应消息:{}",Thread.currentThread().getName(),session,message);
        try{
            if(clientHandler!=null){
                clientHandler.afterReceived((MsgBounder) session.getAttribute(getBoundKey(session)), message);
            }
        } catch (Exception e){
          logger.error("线程名：{},收到响应，业务逻辑处理异常：{}",Thread.currentThread().getName(),e.getLocalizedMessage());
        } finally {
            closeSession(session);
        }

    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
    	
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        /**
         * 如果连接建立了消息在规定idleTime时间内未写完成、或者未得到响应消息，就直接关闭。
         * 避免堆积消息过多,资源无法及时释放
         */
        logger.info("线程名：{},当前连接读写空闲上限，session关闭{}",Thread.currentThread().getName(),session);
        closeSession(session);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        closeSession(session);
        //super.exceptionCaught(session, cause);
        logger.info("线程名：{},发生异常,连接关闭{}",Thread.currentThread().getName(),cause.getMessage());
    }

    /**
     * 得到绑定的Key，不能返回null
     * @param session
     * @return
     */
    private String getBoundKey(IoSession session){
        if(session == null)
            return "";
        return session.getId()+ClientConfig.BOUND_KEY_SUFFIX;
    }

    private void closeSession(IoSession session){
        if(session!=null){
            session.removeAttribute(getBoundKey(session));
            session.closeNow();
            logger.info("线程名：{}，session会话已经关闭{}\n",Thread.currentThread().getName(),session);
            IoStatisticsTools.logStatistics(session);
        }
    }
}