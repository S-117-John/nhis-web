package com.zebone.nhis.common.support.aop;

import com.zebone.nhis.common.module.base.transcode.SysSameUrl;
import com.zebone.nhis.common.support.anno.SameUrlData;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;


/**
 * 拦截SameUrlData注解
 */
@Component
@Aspect
//@Configuration
//@EnableAspectJAutoProxy
//@ComponentScan("com.zebone.nhis.common.support.aop")
public class SameUrlDataAop {

    @Autowired
    private DataSourceTransactionManager manager;

    @Before(value="@annotation(com.zebone.nhis.common.support.anno.SameUrlData)")
    public void doBefore(JoinPoint jp) throws Exception {
        try {
            //获取SameUrlData注解
            Method proxyMethod = ((MethodSignature) jp.getSignature()).getMethod();
            Method targetMethod = jp.getTarget().getClass().getMethod(proxyMethod.getName(), proxyMethod.getParameterTypes());
            SameUrlData sameUrlData = targetMethod.getAnnotation(SameUrlData.class);
//            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//            HttpServletRequest request = attributes.getRequest();
            //处理注解逻辑
            String clientId = sameUrlData.clientId();
            int minutes = sameUrlData.minutes();
            if(StringUtils.isNotEmpty(clientId)){
                //查询数据库中的clientId，如果不存在直接插入
                StringBuilder sql=new StringBuilder();
                sql.append("select * from SYS_SAME_URL where USER_AGENT = '"+clientId+"'");
                List<SysSameUrl> mapList= DataBaseHelper.queryForList(sql.toString(),SysSameUrl.class);
                TransactionStatus status = manager.getTransaction(new DefaultTransactionDefinition());
                if(mapList!=null&&mapList.size()>0){
                    //如果已经存在，判断创建时间与当前时间，大于10分钟则允许通过，否则不执行
                    for (int i = 0; i < mapList.size(); i++) {
                        if(mapList.get(i)!=null){
                            Date lastDate = mapList.get(i).getCreateTime();
                            //计算时间差值
                            Interval interval = new Interval(lastDate.getTime(),new Date().getTime());
                            Period period = interval.toPeriod();
                            int resultMinutes = period.getMinutes();
                            int resultHours = period.getHours();
                            int resultDay = period.getDays();
                            if(resultMinutes>minutes||resultHours>1||resultDay>1){
                                //如果相差时间大于10分钟，允许执行，并记录当前时间
                                mapList.get(i).setCreateTime(new Date());
                                DataBaseHelper.update(DataBaseHelper.getUpdateSql(SysSameUrl.class),mapList.get(i));
                                manager.commit(status);
                            }else {
                                //间隔时间小于10分钟，不允许提交
                                throw new BusException("重复提交");
                            }
                        }
                    }
                }else{
                    SysSameUrl sysSameUrl = new SysSameUrl();
                    sysSameUrl.setApiName(targetMethod.getName());
                    StringBuilder sb = new StringBuilder();
                    //获取所有的参数
                    Object[] args = jp.getArgs();
                    for (int k = 0; k < args.length; k++) {
                        Object arg = args[k];
                        // 获取对象类型
                        String typeName = arg.getClass().getName();
                        if ("java.lang.String".equals(typeName)) {
                            sb.append(arg);
                        }
                    }
                    sysSameUrl.setParams(sb.toString());

//                    if(request!=null){
//                        sysSameUrl.setMethod(request.getMethod());
//                        if(request.getRequestURL()!=null){
//                            sysSameUrl.setRequestUri(request.getRequestURL().toString());
//                        }
//                    }
                    InetAddress address = null;
                    try {
                        address = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {

                    }
                    if(address!=null){
                        sysSameUrl.setRemoteAddr(address.getHostAddress());
                    }
                    sysSameUrl.setUserAgent(clientId);
                    sysSameUrl.setCreateTime(new Date());
                    DataBaseHelper.insertBean(sysSameUrl);
                    manager.commit(status);
                }




            }
        } catch (Throwable e) {
            //处理异常e
            throw e;
        }
    }
}
