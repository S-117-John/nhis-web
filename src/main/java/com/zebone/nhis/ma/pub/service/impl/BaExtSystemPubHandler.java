package com.zebone.nhis.ma.pub.service.impl;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.handler.SystemPubRealizationHandler;
import com.zebone.nhis.ma.pub.service.IExtSystemService;
import com.zebone.nhis.ma.pub.zsba.handler.BaExtOwnBusinessHandler;
import com.zebone.nhis.ma.pub.zsba.handler.BaMedicinePackHandler;
import com.zebone.nhis.ma.pub.zsba.handler.BaPresOutflowHandler;
import com.zebone.nhis.ma.pub.zsrm.handler.ZsrmEhealthCodeHandler;
import com.zebone.nhis.ma.pub.zsrm.handler.ZsrmOpDrugPackPubHandler;
import com.zebone.nhis.ma.pub.zsrm.service.ZsrmOpTransInHospitalHandler;
import com.zebone.nhis.pro.zsba.mz.pub.handler.ZsbaOpWinnoHandler;
import com.zebone.nhis.pro.zsba.mz.pub.service.ZsbaEnoteHandler;
import com.zebone.platform.modules.exception.BusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 中山博爱外部系统对接服务实现（无事物）
 *
 * @author wuqaing
 */
@Service("BaExtSystemPubHandler")
public class BaExtSystemPubHandler implements IExtSystemService {

    @Autowired
    private BaMedicinePackHandler  baMedicinePackHandler;

    @Autowired
    private SystemPubRealizationHandler systemPubRealizationHandler;
    @Autowired
    private BaExtOwnBusinessHandler baExtOwnBusinessHandler;
    @Autowired
    private BaPresOutflowHandler baPresOutflowHandler;
    @Resource
    private ZsrmEhealthCodeHandler baEhealthCodeHandler;
    @Resource
    private ZsrmOpDrugPackPubHandler opDrugPackPubHandler;
    @Resource
    private ZsbaEnoteHandler zsbaEnoteHandler;
    @Resource
    private ZsbaOpWinnoHandler zsbaOpWinnoHandler;
    @Resource
    private ZsrmOpTransInHospitalHandler opTransInHospitalHandler;
    @Override
    public Object processExtSystem(String systemName, String methodName, Object... args) {
        Object result = null;
        if (CommonUtils.isEmptyString(systemName) || CommonUtils.isEmptyString(methodName))
        {throw new BusException("调用外部系统处理接口时，未传入系统名systemName或处理业务的方法名methodName");}
        switch (systemName) {
            case "PackMachine"://包药机
                result=baMedicinePackHandler.invokeMethod(methodName, args);
                break;
                //生成执行单
            case "builtAssistOcc":
                systemPubRealizationHandler.invokeMethod(methodName, args);
                break;
                //检查检验自动执行
            case "handAnesthesiaExeRec":
				baExtOwnBusinessHandler.invokeMethod(methodName, args);
                break;
                //处方流转
            case "preOutflow":
                result=baPresOutflowHandler.invokeMethod(methodName, args);
                break;
            case "EHealthCode"://电子健康码
                result=baEhealthCodeHandler.invokeMethod(methodName, args);
                break;
            case "EBillInfo"://电子票据
                result=zsbaEnoteHandler.invokeMethod(methodName, args);
                break;
            case "PackMachineOp"://发药机
                opDrugPackPubHandler.invokeMethod(methodName, args);
                break;
            case "DrugWinnoRule"://药品分窗规则
                //result=zsbaOpWinnoHandler.invokeMethod(methodName,args);
                break;
            case"OldHis"://与旧系统进行数据交互
                opTransInHospitalHandler.invokeMethod(methodName,args);
                break;
            //对应多个系统，可继续追加
            default:
                break;
        }
        return result;
    }
}
