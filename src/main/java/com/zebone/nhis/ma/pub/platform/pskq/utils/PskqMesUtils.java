package com.zebone.nhis.ma.pub.platform.pskq.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.ma.pub.platform.pskq.factory.MessageFactory;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.MessageLog;
import com.zebone.nhis.ma.pub.platform.pskq.service.SysMessageService;
import com.zebone.nhis.ma.pub.platform.pskq.service.impl.RestSysMessageServiceImpl;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.platform.modules.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class PskqMesUtils {

    public static SimpleDateFormat simDatFor = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    public static SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
    private static final Logger logger = LoggerFactory.getLogger("nhis.lbWebServiceLog");

    //院部代码_
    public static final String pskqHisCode = "10_";

    //系统代码_
    public static final String pskqSysCode = "2000_";

    //坪山口腔数据日志记录
    private static final RestSysMessageServiceImpl sysMessageService = new RestSysMessageServiceImpl();


    //发送消息日志记录
    public static void recordLogMessage(String request , String responseBody,String status){
        logger.info("发送诊断信息成功，请求和响应结果分别为："+request+"--------------------"+responseBody);
        MessageLog messageLog = MessageFactory.getMessageLog(request, MessageFactory.MessageType.SEND,status);
        //修改为手动事物 , 关闭事务自动提交
        JdbcTemplate jdbcTemplate = SpringContextHolder.getApplicationContext().getBean(JdbcTemplate.class);
        DefaultTransactionDefinition transDefinition = new DefaultTransactionDefinition();
        transDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
        DataSourceTransactionManager dataSourceTransactionManager = SpringContextHolder.getApplicationContext().getBean(DataSourceTransactionManager.class);
        TransactionStatus transStatus = dataSourceTransactionManager.getTransaction(transDefinition);

        sysMessageService.save(JSON.toJSONString(messageLog));
        dataSourceTransactionManager.commit(transStatus);
    }


    /**
     * 时间转换加  T
     * @param time
     * @return
     */
    public static String getTimeAddT(String time){


        return "";
    }


    public static String getPvTypeToPvTypeName(String pvcode){
       String pvName = "其他";
        switch(pvcode){
            case "1" ://门诊
                pvName = "门诊";
                break;
            case "2" ://急诊
                pvName = "急诊";
                break;
            case "3" ://住院
                pvName = "住院";
                break;
            case "9" ://其他
                pvName = "其他";
                break;
        }
        return pvName;
    }
    
    public static List<Map<String, Object>> lisBToLisMap(List<Object> list) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        for(Object lis:list){
            String stringBean = JsonUtil.writeValueAsString(lis);
            listMap.add((Map<String, Object>)JsonUtil.readValue(stringBean, Map.class));
        }

        return listMap;
    }
    /**
     * 取文本内容
     * @param map
     * @return
     */
    public static String getPropValueStr(Map<String, Object> map,String key) {
        String value="" ;
        if(key==null||"".equals(key)||map==null||map.size()<=0){
            return "";
        }
        if(map.containsKey(key)){
            Object obj=map.get(key);
            value=obj==null?"":obj.toString();
        }
        return value;
    }

}
