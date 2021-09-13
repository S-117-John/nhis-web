package com.zebone.nhis.ma.pub.platform.pskq.aop;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@Aspect
public class MethodAspect {

    @Before(value="@annotation(com.zebone.nhis.ma.pub.platform.pskq.annotation.MethodLog)")
    public void before(JoinPoint joinPoint){
        DefaultTransactionDefinition transDefinition = new DefaultTransactionDefinition();
        transDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
        DataSourceTransactionManager dataSourceTransactionManager = SpringContextHolder.getApplicationContext().getBean(DataSourceTransactionManager.class);
        TransactionStatus transStatus = dataSourceTransactionManager.getTransaction(transDefinition);
        try{
            Object[] args = joinPoint.getArgs();

            List<Object> objectList  = (List<Object>) args[0];

            String param = JSON.toJSONString(objectList);

            String sql = "insert into PSKQ_MSG_LOG(ID, PARAM, METHOD, CREAT_TIME) values (?,?,?,?)";
            String id = UUID.randomUUID().toString().replace("-","");
            String method = joinPoint.getSignature().getName();
            Date date = new Date();

            DataBaseHelper.update(sql,new Object[]{id,param,method,date});
            dataSourceTransactionManager.commit(transStatus);

        }catch (Exception exception){
            dataSourceTransactionManager.rollback(transStatus);
        }

    }
}
