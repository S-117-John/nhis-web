package com.zebone.nhis.bl.pub.support;

import com.zebone.nhis.bl.pub.service.ICgService;
import com.zebone.nhis.bl.pub.service.IWinnoDrugService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.modules.core.spring.ServiceLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 结算调用个性化服务工具类
 */
public class SettlePubUtils {
    /**
     * 处理门诊结算分窗服务
     */
    public static Object getDrugWinno(Object... obj){
        //获取是否启用自定义记费服务标志 对应配置文件cg.properties配置文件
        Object result = new HashMap<String,Object>();
        if ("true".equals(ApplicationUtils.getPropertyValue("cg.op.enable", ""))) {
            //获取自定义记费服务service，对应配置文件cg.properties配置文件
            String processClass = ApplicationUtils.getPropertyValue("cg.winno.processClass", "defaultWinnoService");
            Object bean = ServiceLocator.getInstance().getBean(processClass);
            if (bean != null) {
                IWinnoDrugService handler = (IWinnoDrugService) bean;
                result = handler.getDrugWinno(obj);
            }
        }
        return result;
    }
}
