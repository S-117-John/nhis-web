package com.zebone.nhis.webservice.zsrm.cxf;

import com.zebone.nhis.webservice.zsrm.service.ZsrmDrugHerbPackService;
import com.zebone.nhis.webservice.zsrm.service.ZsrmWesDrugPackService;
import com.zebone.nhis.webservice.zsrm.utils.InputParseTool;
import com.zebone.nhis.webservice.zsrm.vo.pack.MachInParam;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;

public class ZsrmDrugPackServiceImpl implements IZsrmDrugPackService {

    private static Logger log = LoggerFactory.getLogger("nhis.drug.pack");

    @Resource
    private ZsrmDrugHerbPackService herbPackService;
    @Resource
    private ZsrmWesDrugPackService zsrmWesDrugPackService;

    @Override
    public String GetAllPurPoseData(String param) {
        log.info("包药机请求入参：{}",param);
        try {
            MachInParam.MachInputVoHeader machInput = InputParseTool.getMachInput(param);
            String remoteMethod = null;
            if(machInput == null || CollectionUtils.isEmpty(machInput.getItems())){
                throw new BusException("未解析到完整入参信息");
            }
            if((remoteMethod = InputParseTool.getRemoteMethod(machInput))==null){
                throw new BusException("未匹配到正确的本地方法");
            }
            List<MachInParam> inputVos = machInput.getItems();
            MachInParam inputVo = inputVos.get(0);
            String orderId = inputVo==null?null:inputVo.getOrderId();
            switch (remoteMethod){
                case "GetOrder":
                    return zsrmWesDrugPackService.getOrders(orderId);
                case "GetItems":
                    return zsrmWesDrugPackService.getItems(orderId);
                case "UpdateOrderStatus":
                    return zsrmWesDrugPackService.saveSendDrugStatus(inputVo);
                case "GetDrugList":
                    return zsrmWesDrugPackService.getBdInfoList();
            }
        } catch (Exception e){
            log.error("接受摆药机数据处理异常：",e);
            if(ExceptionUtils.getRootCause(e) instanceof SQLException){
                return "SQL执行异常，请查看日志";
            }
            return e.getMessage();
        } finally {
            UserContext.cleanUser();
        }
        return "other";
    }

    @Override
    public String getHerbPresInfo(String param) {
        String resXml=  herbPackService.getPresHerbInfo(param);
        return resXml;
    }

    @Override
    public String getHerbPresDtInfo(String param) {
        String resXml=  herbPackService.getPresHerbDtInfo(param);
        return resXml;
    }
}
