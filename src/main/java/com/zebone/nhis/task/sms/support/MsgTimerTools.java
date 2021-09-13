package com.zebone.nhis.task.sms.support;

import com.zebone.nhis.task.sms.executor.MessageExecutor;
import com.zebone.nhis.task.sms.service.MsgService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


final public class MsgTimerTools {
    private final static Logger logger = LoggerFactory.getLogger(MsgTimerTools.class);

    /**符合条件的内容列表*/
    private static Map<String, List<Map<String,Object>>> waitQueue = new ConcurrentHashMap<>();

    public static void addQueue(String timeSend,List<Map<String,Object>> list){
        if(StringUtils.isNotBlank(timeSend) && CollectionUtils.isNotEmpty(list)){
            waitQueue.put(timeSend, list);
        }
    }

    public static int getQueueSize(){
        return waitQueue.size();
    }

    /**
     * 从列表中获取符合条件的数据，并调用执行接口
     * @param service
     * @param messageExecutor
     */
    public static boolean exeByQueue(MsgService service, final MessageExecutor messageExecutor){
        try{
            if(waitQueue.size() >0 && messageExecutor !=null){
                String now = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(new Date());
                Set<Map.Entry<String, List<Map<String, Object>>>> entrySet = waitQueue.entrySet();
                Iterator<Map.Entry<String, List<Map<String, Object>>>> it = entrySet.iterator();
                while(it.hasNext()){
                    Map.Entry<String, List<Map<String, Object>>> entry = it.next();
                    String formatDate = entry.getKey();
                    if(formatDate.compareTo(now)<=0){
                        logger.info("sms调用了业务方法:"+formatDate);
                        try{
                            messageExecutor.execute(service,entry.getValue());
                        } finally {
                            //符合条件执行完，从迭代器移除掉
                            it.remove();
                        }
                    }
                }
                return true;
            }
        } catch(Exception e){
            logger.error("invokeEveryTime:",e);
        }
        return false;
    }

    public static List<Map<String,Object>> getListOfQueue(String timeSend) {
        return waitQueue.get(timeSend);
    }

}
