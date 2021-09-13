package com.zebone.nhis.ma.pub.platform.socket.support;

import com.zebone.nhis.common.support.ApplicationUtils;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.IoServiceStatistics;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


final public class IoStatisticsTools {
    private final static Logger logger = LoggerFactory.getLogger("nhis.lbHl7Log");

    /** 上一次执行时间*/
    private static volatile long preExeTime = System.currentTimeMillis();

    /** 多久让输出一次*/
    private static final int freqTime = 5*60*1000;

    private final static Lock lock = new ReentrantLock();

    /**
     * 获取Ioservice统计信息，不关注是否重连后的Service
     * @param session
     */
    public static void logStatistics(IoSession session){
        if("0".equals(ApplicationUtils.getPropertyValue("msg.send.statistics","0"))){
            return;
        }
        lock.lock();
        try {
            //如果一直有调用，那么最多freqTime让调用输入一次
            if(System.currentTimeMillis() - preExeTime<freqTime){
                return;
            }
        } finally {
            preExeTime = System.currentTimeMillis();
            lock.unlock();
        }
        try{
            if(session!= null){
                //~我们不统计Session的，目前每次重新session，这里统计Connector中的
                IoService ioService =  session.getService();
                if(ioService!= null){
                    StringBuilder sbl = new StringBuilder("IoService信息统计：Service:");
                    sbl.append(ioService.hashCode());
                    //String temp = "IoService信息统计：Service:${sv}\r\n已写入字节数：${wbt}（平均每秒：${wbts}，最大每秒：${wbtsm}）";
                    IoServiceStatistics stc = ioService.getStatistics();

                    //写的简单统计
                    sbl.append("已写字节数：").append(stc.getWrittenBytes())
                            .append(",平均每秒：").append(stc.getWrittenBytesThroughput())
                            .append(",最大每秒：").append(stc.getLargestWrittenBytesThroughput())
                            .append("{").append("已写消息数：").append(stc.getWrittenMessages())
                            .append(",平均每秒：").append(stc.getWrittenMessagesThroughput())
                            .append(",最大每秒：").append(stc.getLargestWrittenMessagesThroughput())
                            .append("}");
                    //读取的简单统计
                    sbl.append("\r\n已读取字节数：").append(stc.getReadBytes())
                            .append(",平均每秒：").append(stc.getReadBytesThroughput())
                            .append(",最大每秒：").append(stc.getLargestReadBytesThroughput())
                            .append("{").append("已读取消息数：").append(stc.getReadMessages())
                            .append(",平均每秒：").append(stc.getReadMessagesThroughput())
                            .append(",最大每秒：").append(stc.getLargestReadMessagesThroughput())
                            .append("}");
                    //会话数
                    sbl.append("\r\n").append("累计会话数（包含已经关闭的）：").append(stc.getCumulativeManagedSessionCount())
                            .append("\t同时管理的最大会话数：").append(stc.getLargestManagedSessionCount());
                    if(logger.isDebugEnabled()){
                        logger.debug(sbl.toString());
                    }else if(logger.isErrorEnabled()){
                        logger.error(sbl.toString());
                    }else{
                        logger.info(sbl.toString());
                    }
                } else{
                    logger.info("ioService is null");
                }
            }
        } catch (Exception e){
            logger.error("统计异常：",e);
        }
    }


}
