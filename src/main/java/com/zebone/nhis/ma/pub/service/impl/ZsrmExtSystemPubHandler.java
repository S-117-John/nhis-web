package com.zebone.nhis.ma.pub.service.impl;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.sd.service.EnoteHandler;
import com.zebone.nhis.ma.pub.service.IExtSystemService;
import com.zebone.nhis.ma.pub.zsba.handler.BaPresOutflowHandler;
import com.zebone.nhis.ma.pub.zsrm.handler.*;
import com.zebone.nhis.ma.pub.zsrm.service.ZsrmOpTransInHospitalHandler;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 中山人民医院外部系统对接服务实现（无事物）
 */
@Service("ZsrmExtSystemPubHandler")
public class ZsrmExtSystemPubHandler implements IExtSystemService {

    @Resource
    private ZsrmOpDrugPackPubHandler opDrugPackPubHandler;
    @Resource
    private EnoteHandler enoteHandler;
    @Resource
    private ZsrmEhealthCodeHandler zsrmEhealthCodeHandler;
    @Resource
    private ZsrmOpWinnoHandler zsrmOpWinnoHandler;
    @Resource
    private ZsrmOpTransInHospitalHandler opTransInHospitalHandler;
    @Resource
    private ZsrmHighConsumHandler zsrmHighConsumHandler;

    @Resource
    private ZsrmIpAutoPackagePlantHandler zsrmIpAutoPackagePlantHandler;
    //引用处方外流处理接口
    @Resource
    private BaPresOutflowHandler baPresOutflowHandler;

    @Resource
    private ZsrmHrpPostHandler zsrmHrpPostHandler;

    @Override
    public Object processExtSystem(String systemName, String methodName, Object... args) {
        Object result = null;
        if (CommonUtils.isEmptyString(systemName) || CommonUtils.isEmptyString(methodName))
            throw new BusException("调用外部系统处理接口时，未传入系统名systemName或处理业务的方法名methodName");
        switch (systemName) {
            case "PackMachineOp"://发药机
                opDrugPackPubHandler.invokeMethod(methodName, args);
                break;
            case "EBillInfo"://电子票据
                result=enoteHandler.invokeMethod(methodName, args);
                break;
            case "DrugWinnoRule"://药品分窗规则
                result=zsrmOpWinnoHandler.invokeMethod(methodName,args);
                break;
            case "EHealthCode"://电子健康码
                result=zsrmEhealthCodeHandler.invokeMethod(methodName, args);
                break;
            case"OldHis"://与旧系统进行数据交互
                opTransInHospitalHandler.invokeMethod(methodName,args);
                break;
            case "CONSUMABLE"://高值耗材
                result=zsrmHighConsumHandler.invokeMethod(methodName,args);
                break;
            //处方流转
            case "preOutflow":
                result=baPresOutflowHandler.invokeMethod(methodName, args);
                break;
            case "HRPSERVICE"://hrp 服务接口
                result=zsrmHrpPostHandler.invokeMethod(methodName,args);
                break;
            case "PackMachine":
                result=zsrmIpAutoPackagePlantHandler.invokeMethod(methodName,args);
                break;
            default:
                break;
        }
        return result;
    }
}
