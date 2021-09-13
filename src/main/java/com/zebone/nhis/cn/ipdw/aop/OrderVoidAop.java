package com.zebone.nhis.cn.ipdw.aop;

import com.zebone.nhis.cn.ipdw.vo.CnSignCaParam;
import com.zebone.nhis.common.module.cn.ipdw.CnConsultApply;
import com.zebone.nhis.common.module.cn.ipdw.CnNotice;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.List;

@Component
@Aspect
public class OrderVoidAop {

    @AfterReturning( value="@annotation(com.zebone.nhis.cn.ipdw.aop.OrderVoid)")
    public void doAfter(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method proxyMethod = methodSignature.getMethod();
        try {
            Method targetMethod = joinPoint.getTarget().getClass().getMethod(proxyMethod.getName(), proxyMethod.getParameterTypes());
            OrderVoid surgeryVoid = targetMethod.getAnnotation(OrderVoid.class);
            //参数名
            String param = surgeryVoid.param();

            String type = surgeryVoid.type();
            Object[] args = joinPoint.getArgs();
            String[] parameterNames = methodSignature.getParameterNames();
            int paramIndex = ArrayUtils.indexOf(parameterNames, param);
            String jsonParam = String.valueOf(args[paramIndex]);

            if(!StringUtils.isEmpty(type)&&"会诊".equals(type)){
                CnNotice cnNoticeDO = new CnNotice();
                CnConsultApply cnConsultApply=JsonUtil.readValue(jsonParam, CnConsultApply.class);
                String pkCnord=cnConsultApply.getPkCnord();
                List<CnOrder> cnOrds = DataBaseHelper.queryForList("select name_ord,pk_pv,pk_dept_ns,pk_cnord,ordsn from cn_order where pk_cnord=?",
                        CnOrder.class, new Object[]{pkCnord});
                if(cnOrds.size()>0){
                    cnNoticeDO.setPkDeptRecp(cnOrds.get(0).getPkDeptNs());
                    cnNoticeDO.setEuStatus("0");
                    cnNoticeDO.setDelFlag("0");
                    cnNoticeDO.setCntVoid(1);
                    DataBaseHelper.insertBean(cnNoticeDO);
                }
                return;
            }


            CnSignCaParam cnSignCaParam = JsonUtil.readValue(jsonParam, CnSignCaParam.class);
            List<String> pkOpList = cnSignCaParam.getPkCnList();
            CnNotice cnNoticeDO = new CnNotice();
            if(pkOpList.size()>0){
                List<CnOrder> cnOrds = DataBaseHelper.queryForList("select name_ord,pk_pv,pk_dept_ns,pk_cnord,ordsn from cn_order where pk_cnord=?",
                        CnOrder.class, new Object[]{pkOpList.get(0)});
                if(cnOrds.size()>0){
                    cnNoticeDO.setPkDeptRecp(cnOrds.get(0).getPkDeptNs());
                    cnNoticeDO.setEuStatus("0");
                    cnNoticeDO.setDelFlag("0");
                    cnNoticeDO.setCntVoid(pkOpList.size());
                    DataBaseHelper.insertBean(cnNoticeDO);
                }
            }
        } catch (NoSuchMethodException e) {

        }
    }
}
