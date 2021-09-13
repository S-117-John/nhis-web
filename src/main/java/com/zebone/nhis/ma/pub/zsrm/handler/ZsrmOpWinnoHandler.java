package com.zebone.nhis.ma.pub.zsrm.handler;

import com.zebone.nhis.bl.pub.vo.BlPatiCgInfoNotSettleVO;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.ex.nis.ns.ExPresOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExPresOccDt;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.zsrm.service.ZsrmOpWinnoService;
import com.zebone.platform.common.support.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * HIS门诊分窗接口服务
 * 需求来源中山人民医院项目
 */
@Service
public class ZsrmOpWinnoHandler {

    @Resource
    private ZsrmOpWinnoService zsrmOpWinnoService;

    private Logger logger = LoggerFactory.getLogger("nhis.scm");
    /**
     * 分窗接口入口
     * @param methodName
     * @param args
     * @return
     */
    public Object invokeMethod(String methodName,Object...args) {
        long startTime = System.currentTimeMillis();
        if (CommonUtils.isNull(methodName) || args==null || args.length==0) return null;
        Object obj = null;
        switch (methodName) {
            case "getDrugDeptEx":
                obj=zsrmOpWinnoService.geDeptExOpDrug((Map<String,List<BlOpDt>>)args[0]);
                break;
            case "getDrugWinno":
                zsrmOpWinnoService.getWinnoForAllRolue(args);
                break;
            case "getNotSettlePresInfo":
                zsrmOpWinnoService.getNotSettlePresInfo((List<BlPatiCgInfoNotSettleVO>)args[0]);
                break;
        }
        long  exectime  = System.currentTimeMillis() - startTime;
        logger.info("分窗接口调用：【"+methodName+"】,执行耗时："+exectime+"毫秒！");
        return obj;
    }
}
