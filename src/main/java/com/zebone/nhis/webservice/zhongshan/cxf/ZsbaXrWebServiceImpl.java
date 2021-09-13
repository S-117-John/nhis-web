package com.zebone.nhis.webservice.zhongshan.cxf;

import com.zebone.nhis.webservice.support.RespJson;
import com.zebone.nhis.webservice.zhongshan.service.ZsbaOutpatientOpChargeService;
import com.zebone.nhis.webservice.zhongshan.service.ZsbaOutpatientOrderService;
import com.zebone.nhis.webservice.zhongshan.service.ZsbaPhysicalExaminationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Classname ZsbaXrWebServiceImpl
 * @Description 博爱三方对外接口
 * @Date 2021-03-04 17:35
 * @Created by wuqiang
 */
public class ZsbaXrWebServiceImpl implements IZsbaXrWebService {
    private Logger logger = LoggerFactory.getLogger("nis.zsbaWebSrv");

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ZsbaOutpatientOpChargeService zsbaOutpatientOpChargeService;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private  ZsbaPhysicalExaminationService zsbaPhysicalExaminationService;
    
    @Autowired
    private ZsbaOutpatientOrderService zsbaOutpatientOrderService;
    @Override
    public String InterfaceWS(String inf, String function) {
        if (StringUtils.isWhitespace(function)){
           logger.error(" InterfaceWS | -|参数功能号 function不允许为空 "+"\n"+inf);
            return new RespJson("-|参数功能号 function不允许为空", false).toString();
        }
        String retuxml="";
        switch (function){
            // 三方门诊记费
            case "01":
                retuxml=zsbaOutpatientOpChargeService.opCharge(inf);
                break;
            case "02":
                retuxml=zsbaOutpatientOpChargeService.saveCnOrder(inf);
                break;
            case "03":
                retuxml=zsbaOutpatientOpChargeService.cancelCnOrd(inf);
                break;
            case "04":
                retuxml=zsbaOutpatientOpChargeService.exeCnOrd(inf);
                break;
            case "05":
                retuxml=zsbaOutpatientOpChargeService.cancelExeCnord(inf);
                break;
            case "06":
                retuxml=zsbaOutpatientOpChargeService.IpCharge(inf);
                break;
            case "07":
                retuxml=zsbaOutpatientOpChargeService.cancelIpCharge(inf);
                break;
            case "08":
                retuxml=zsbaOutpatientOpChargeService.login(inf);
                break;
            case "09":
                retuxml=zsbaOutpatientOpChargeService.getPvInfo(inf);
                break;
            case "10":
                retuxml=zsbaOutpatientOpChargeService.deleteOpCharge(inf);
                break;
            case "11"://2.1.5.获取门诊和住院患者的检验医嘱
                retuxml=zsbaOutpatientOrderService.getCnLabApply(inf);
                break;
            case "12"://2.4.1.条码打印/取消条码打印接口
                retuxml=zsbaOutpatientOrderService.updaCnLabApplySampNo(inf);
                break;
            case "13"://2.4.2.LIS状态回写接口
                retuxml=zsbaOutpatientOrderService.updateListEuStatus(inf);
                break;
            case "14":
                retuxml=zsbaPhysicalExaminationService.generaPvOpRec(inf);
                break;
            case "15":
                retuxml=zsbaPhysicalExaminationService.deletePvOpRec(inf);
                break;
            case "16":
                retuxml=zsbaPhysicalExaminationService.saveOpCnOrder(inf);
                break;
            case "17":
                retuxml=zsbaPhysicalExaminationService.deleteOpCnOrder(inf);
                break;
            case "18"://2.1.12.检验申请录入
                retuxml=zsbaOutpatientOrderService.saveLisApplyList(inf);
                break;
            case "19":
                // 作废住院检验申请
                retuxml=zsbaOutpatientOpChargeService.invalidSaveLisApplyList(inf);
                break;
            default:
                break;

        }

        return retuxml;

    }

}
