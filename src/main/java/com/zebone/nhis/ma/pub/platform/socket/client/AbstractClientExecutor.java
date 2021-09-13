package com.zebone.nhis.ma.pub.platform.socket.client;

import org.apache.commons.collections.CollectionUtils;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Mina消息发送的基类。
 * <br>1.同步连接，异步发送
 * <br>2.只有调用时才进行连接初始化，且初始化一次
 * <br>3.需要对不通消息类型进行去扩展，主要是处理编解码规则,继承并改造即可
 * <br>4.此类中包含心跳检测的方法，使用的聋子型心跳机制（客户端发送心跳，但不关心返回值，为了检测连接可用即可）
 * <br>5.心跳是在请求--响应IdleStatus.BOTH_IDLE空闲时间期间，如果开启心跳session的sessionIdle就不会触发，此时在心跳检测中实现<b>超时断线<b/><br/>
 */
public abstract class AbstractClientExecutor {
	private static Logger logger = LoggerFactory.getLogger("nhis.lbHl7Log");

    public AbstractClientExecutor(ClientConfig clientConfig){
        this.clientConfig = clientConfig;
        initConnector();
    }

    /** 如果服务器连接一直不可用多久之后，就不在做断线重连（秒），减少资源消耗*/
    private static final long MAX_RECONNECT_DURATION = 60;
    /** 心跳包内容 */
    private static final String HEART_BEAT_REQUEST = "#$#";
    //private static final String HEART_BEAT_RESPONSE = "$#$";

    private ClientConfig clientConfig;
    private NioSocketConnector connector;
    private volatile long reconStartTime = 0L;
	/**
     * 初始化连接,连接到目标最大等待TimeOut时长
     */
    private void initConnector(){
        connector = new NioSocketConnector(clientConfig.getProcessCount());
        connector.setHandler(new ClientHandler(clientConfig.getClientHandler()));
        connector.setConnectTimeoutMillis(clientConfig.getTimeOut());
        connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, clientConfig.getIdleTime());
        connector.getSessionConfig().setWriteTimeout(clientConfig.getIdleTime());
        connector.setDefaultRemoteAddress(new InetSocketAddress(clientConfig.getHost(), clientConfig.getPort()));
        addFilters();
    }

    private void addFilters(){
        List<IoFilter> list = clientConfig.getProtocolCodecFilterList();
        if(clientConfig != null && connector!=null && CollectionUtils.isNotEmpty(list)){
            for (IoFilter filter:list) {
                connector.getFilterChain().addLast(filter.getClass().getSimpleName(),filter);
            }
        }
        if(clientConfig.isOpenHeartBeat()){
            //设置心跳检测Filter---目的是保持客户端和服务器端连接活性，有时候服务器端发现一直有“空连接”存在，就会干掉它。
            KeepAliveMessageFactory heartBeatFactory = new KeepAliveMessageFactoryImpl();
            KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory, IdleStatus.BOTH_IDLE, KeepAliveRequestTimeoutHandler.DEAF_SPEAKER,clientConfig.getHeartBeat(),80);
            heartBeat.setForwardEvent(false);
            connector.getFilterChain().addLast("heartbeat", heartBeat);
        }
        //日志Filter
        connector.getFilterChain().addLast("logging", new LoggingFilter());
    }



    public void sendMsg(final MsgBounder msgBounder){
        if(connector.isDisposed()){
            logger.warn("消息发送失败，连接不可用！");
            reCreate();
        }
        ConnectFuture connectFuture = connector.connect();
        connectFuture.awaitUninterruptibly();
        IoSession session = null;
        Throwable throwable = connectFuture.getException();
        if(throwable!= null){
            logger.warn("连接服务器失败,异常内容：{}",throwable.getMessage());
            session = reConnect(connectFuture);
            if(session == null || !session.isConnected()){
                return;
            }
        }
        if(session == null){
            session = connectFuture.getSession();
            throwable = connectFuture.getException();
            if(throwable!= null) {
                logger.warn("获取Session异常,异常内容：{}", throwable.getMessage());
                return;
            }
        }

        if(session != null){
            reconStartTime = 0L;
            //这里session中不在存储msg，而是在Handler的messageSent中设置。receive中不依赖于msg，而是serviceData
            Object msg = null;
            if(msgBounder!=null){
                msg = msgBounder.getMsg();
                msgBounder.setMsg(null);
            }
            session.setAttribute(session.getId()+ClientConfig.BOUND_KEY_SUFFIX, msgBounder);
            session.write(msg);
        }else{
            logger.warn("session为空");
        }

    }
    
    /**
     * 集成消息同步发送
     * @param msgBounder
     */
    public void sendMsgFor(final MsgBounder msgBounder){
    	if(connector.isDisposed()){
            logger.warn("消息发送失败，连接不可用！");
            reCreate();
        }
        ConnectFuture connectFuture = connector.connect();
        connectFuture.awaitUninterruptibly();
        IoSession session = null;
        try {
			
			Throwable throwable = connectFuture.getException();
			if(throwable!= null){
			    logger.warn("连接服务器失败,异常内容：{}",throwable.getMessage());
			    session = reConnect(connectFuture);
			    if(session == null || !session.isConnected()){
			        return;
			    }
			}
			if(session == null){
			    session = connectFuture.getSession();
			    throwable = connectFuture.getException();
			    if(throwable!= null) {
			        logger.warn("获取Session异常,异常内容：{}", throwable.getMessage());
			        return;
			    }
			}

			if(session != null){
			    reconStartTime = 0L;
			    //这里session中不在存储msg，而是在Handler的messageSent中设置。receive中不依赖于msg，而是serviceData
			    Object msg = null;
			    if(msgBounder!=null){
			        msg = msgBounder.getMsg();
			        msgBounder.setMsg(null);
			    }
			    session.setAttribute(session.getId()+ClientConfig.BOUND_KEY_SUFFIX, msgBounder);
			    session.write(msg).await(10000L);
			}else{
			    logger.warn("session为空");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

    }
    
    

    /**
     * 用来isDisposed()被执行后，重新初始化Connector
     */
    private synchronized void reCreate(){
        initConnector();
    }


    /**
     * 断线重连.如果一直连不上，就不在重连。直到服务器可用时，又可以接受断线重连
     * @param connectFuture
     * @return
     */
    private IoSession reConnect(ConnectFuture connectFuture){
        IoSession session = null;
        try{
            synchronized (this){
                if(reconStartTime ==0){
                    reconStartTime = System.currentTimeMillis();
                } else if(System.currentTimeMillis() - reconStartTime >MAX_RECONNECT_DURATION*1000){
                    logger.warn("重连次数上限，暂时不再尝试！");
                    connector.dispose();
                    reconStartTime = Long.MAX_VALUE;
                    return null;
                } else if(reconStartTime == Long.MAX_VALUE){
                    return null;
                }
            }

            connectFuture = connector.connect();
            if(connectFuture.awaitUninterruptibly(5, TimeUnit.SECONDS)){
                session = connectFuture.getSession();// 获取会话
                if(session == null){
                    logger.warn("重连失败,5S未连接到服务！");
                    return null;
                }
                if(session.isConnected()){
                    logger.info("重连成功host:{},port:{}",connector.getDefaultRemoteAddress().getHostName() ,connector.getDefaultRemoteAddress().getPort());
                }
            }
        }catch(Exception ex){
            logger.error("重连失败:", ex);
        }
        return session;
    }
    /**
     * 内部类，实现KeepAliveMessageFactory（心跳工厂）
     */
    private static class KeepAliveMessageFactoryImpl implements KeepAliveMessageFactory {

        @Override
        public boolean isRequest(IoSession session, Object message) {
            return message.equals(HEART_BEAT_REQUEST);
        }

        @Override
        public boolean isResponse(IoSession session, Object message) {
            return false;
        }

        @Override
        public Object getRequest(IoSession session) {
            if(session == null || !session.isActive() || session.isClosing())
                return null;
            //如果读的时间超时，就关闭此连接.主要是那种发出去后一直没有响应的连接
            if(session!=null &&
                    System.currentTimeMillis()-session.getLastReadTime() > session.getConfig().getWriteTimeoutInMillis()){
                session.closeNow();
                return null;
            }
            if(logger.isDebugEnabled()){
                logger.debug("tick tock...session:{},maxTime:{}",session);
            }
            return HEART_BEAT_REQUEST;
        }

        /* 服务器不会给客户端发送心跳请求，客户端当然也不用反馈，该方法返回 null  */
        @Override
        public Object getResponse(IoSession session, Object request) {
            return null;
        }
    }
}
