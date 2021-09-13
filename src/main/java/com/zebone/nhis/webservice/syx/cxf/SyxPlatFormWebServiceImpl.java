package com.zebone.nhis.webservice.syx.cxf;

import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.syx.service.*;
import com.zebone.nhis.webservice.syx.utils.PfWsUtils;
import com.zebone.nhis.webservice.syx.vo.platForm.PlatFormResErr;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC)
public class SyxPlatFormWebServiceImpl implements ISyxPlatFormWebService {

    @Resource
    private PlatFormOrderService platFormOrderService;

    @Resource
    private PlatFormStopRegService platFormStopRegService;

    @Resource
    private PlatFormStopplanRegisterInfoService platFormStopplanRegisterInfoService;

    @Resource
    private PlatFormChecklistInfoService platFormChecklistInfoService;

    @Resource
    private PlatFormPayInfoService platFormPayInfoService;

    @Resource
    private PlatFormPayDetailInfoService platFormPayDetailInfoService;

    @Resource
    private PlatFormPayOrderStatusService platFormPayOrderStatusService;

    @Resource
    private PlatFormBaseService platFormBaseService;

    @Resource
    private PlatFormPayOrderDetailService platFormPayOrderDetailService;

    @Resource
    private PlatFormPayOrderService platFormPayOrderService;

    @Resource
    private PlatFormRegisterInfoService platFormRegisterInfoService;

    @Resource
    private PlatFormRegRecordsTodayService platFormRegRecordsTodayService;

    @Resource
    private PlatFormOnlineService platFormOnlineService;

    @Resource
    private PlatFormOpvisitService platFormOpvisitService;

    @Resource
    private PlatFormRoomListService platFormRoomListService;

    @Resource
    private PlatFormDoctorPlanListService platFormDoctorPlanListService;

    @Resource
    private PlatFormDoctorListService platFormDoctorListService;

    @Resource
    private PlatFormQueueListService platFormQueueListService;

    @Resource
    private PlatFormSetQueueListService platFormSetQueueListService;

    @Resource
    private PlatFormCreateACardService platFormCreateACardService;

    @Resource
    private PlatformAppWebService platformAppWebService;
    
    @Resource
    private SelfmacService selfmacService;

    private Logger logger = LoggerFactory.getLogger("ma.syxInterface");

    @Override
    public String SyxPlatFormNhisInterface(@WebParam(name = "content") String content) {
        System.out.println("请求XML：" + content);
        String funId = StringUtils.substringBetween(content, "<actionId>", "</actionId>");
        if (StringUtils.isEmpty(funId)) {
            String resErrXml = XmlUtil.beanToXml(PfWsUtils.constructErrResBean(content, "未获取到功能号！"), PlatFormResErr.class);
            System.out.println("请求失败响应：" + resErrXml);
            return resErrXml;
        }

        String resXml = "";
        try {
            switch (funId) {
                case "getHospitalInfo":
                    resXml = platFormOrderService.getHospitalInfo(content);
                    break;
                case "stopReg":
                    resXml = platFormStopRegService.stopReg(content);
                    break;
                case "lockReg":
                    resXml = platFormOrderService.lockReg(content);
                    break;
                case "getstopplanRegisterInfo":
                    resXml = platFormStopplanRegisterInfoService.getstopplanRegisterInfo(content);
                    break;
                case "getchecklistInfo":
                    resXml = platFormChecklistInfoService.getchecklistInfo(content);
                    break;
                case "unlockReg":
                    resXml = platFormOrderService.unlockReg(content);
                    break;
                case "getPayInfo":
                    resXml = platFormPayInfoService.getPayInfo(content);
                    break;
                case "getPayDetailInfo":
                    resXml = platFormPayDetailInfoService.getPayDetailInfo(content);
                    break;
                case "addOrder":
                    resXml = platFormOrderService.addOrder(content);
                    break;
                case "addOrderByLock":
                    resXml = platFormOrderService.addOrder(content);
                    break;
                case "getDeptInfo":
                    resXml = platFormOrderService.getDeptInfo(content);
                    break;
                case "getPayOrderStatus":
                    resXml = platFormPayOrderStatusService.getPayOrderStatus(content);
                    break;
                case "getDoctorInfo":
                    resXml = platFormBaseService.getDoctorInfo(content);
                    break;
                case "getPayOrderDetail":
                    resXml = platFormPayOrderDetailService.getPayOrderDetail(content);
                    break;
                case "payOrder":
                    resXml = platFormPayOrderService.payOrderInfo(content);
                    break;
                case "returnPay":
                    resXml = platFormPayOrderService.rtnPayInfo(content);
                    break;
                case "cancelOrder":
                    resXml = platFormOrderService.cancelOrder(content);
                    break;
                case "infoOrder":
                    resXml = platFormOrderService.infoOrder(content);
                    break;
                case "notifyPatient":
                    resXml = platFormOrderService.notifyPatient(content);
                    break;
                case "getRegInfo":
                    resXml = platFormBaseService.getRegInfo(content);
                    break;
                case "getPatientAddress":
                    resXml = platFormBaseService.getPatientAddress(content);
                    break;
                case "setPatientAddress":
                    resXml = platFormBaseService.setPatientAddress(content);
                    break;
                case "getTimeRegInfo":
                    resXml = platFormBaseService.getTimeRegInfo(content);
                    break;
                case "getDeptRegInfo":
                    resXml = platFormBaseService.getDeptRegInfo(content);
                    break;
                case "getUserInfo":
                    resXml = platFormBaseService.getUserInfo(content);
                    break;
                case "getRegisterInfo":
                    resXml = platFormRegisterInfoService.getRegisterInfo(content);
                    break;
                case "getRegRecordsToday":
                    resXml = platFormRegRecordsTodayService.getRegRecordsToday(content);
                    break;
                case "getDetailslist":
                    resXml = platFormOnlineService.getDetailslist(content);
                    break;
                case "getTrialSettlementInfo":
                    resXml = platFormOnlineService.getTrialSettlementInfo(content);
                    break;
                case "getSettlementInfo":
                    resXml = platFormOnlineService.getSettlementInfo(content);
                    break;
                case "getHisSettlementInfo":
                    resXml = platFormOnlineService.getHisSettlementInfo(content);
                    break;
                case "cancelSettlement":
                    resXml = platFormOnlineService.cancelSettlement(content);
                    break;
                case "getRoomList":
                    resXml = platFormRoomListService.getRoomList(content);
                    break;
                case "getDoctorPlanList":
                    resXml = platFormDoctorPlanListService.getDoctorPlanList(content);
                    break;
                case "getDoctorList":
                    resXml = platFormDoctorListService.getDoctorList(content);
                    break;
                case "getQueueList":
                    resXml = platFormQueueListService.getQueueList(content);
                    break;
                case "setQueueList":
                    resXml = platFormSetQueueListService.setQueueList(content);
                    break;
                case "payBigOrder":
                    resXml = platFormPayOrderService.payBigOrder(content);
                    break;
                case "createACard":
                    resXml = platFormCreateACardService.createACard(content);
                    break;
                case "RequestreturnPay":
                    resXml = platFormPayOrderService.requestreturnPay(content);
                    break;
                case "getRegRecords":
                    resXml = platFormOrderService.getRegRecords(content);
                    break;
                case "getSettleInfo":
                	resXml = selfmacService.qrySettleInfo(content);
                	break;
                case "getSettleDetail":
                	resXml = selfmacService.qrySettleDetail(content);
                	break;
                case "updatePrintList":
                	resXml = selfmacService.updatePrintList(content);
                	break;
                default:
                    break;
            }
        } catch (BusException e) {
            e.printStackTrace();
            resXml = XmlUtil.beanToXml(PfWsUtils.constructErrResBean(content, e.getMessage()), PlatFormResErr.class);
            System.out.println("请求失败响应：" + resXml);
            return resXml;
        } catch (Exception e1) {
            e1.printStackTrace();
            resXml = XmlUtil.beanToXml(PfWsUtils.constructErrResBean(content, "His系统报错！"), PlatFormResErr.class);
            System.out.println("请求失败响应：" + resXml);
            return resXml;
        }
        System.out.println("响应XML：" + resXml);
        return resXml;
    }

    @Override
    public String PrivateHIPMessageServer(String action, String message) {
        logger.info("\n------------------------------平安请求Action：:"+action+"\n请求XML："+message);
//        System.out.println("请求XML：" + message);
        String resXml = "";

        try {
            switch (action) {
                case "opvisitinfoQuery":
                    resXml = platFormOpvisitService.opvisitinfoQuery(message);
                    break;
                case "opdiaginfoQuery":
                    resXml = platFormOpvisitService.opdiaginfoQuery(message);
                    break;
                case "getInPatientInfo":
                    resXml = platformAppWebService.qryPiInfoForIn(message);
                    break;
                case "getInPatientFeeInfo":
                    resXml = platformAppWebService.getInPatientFeeInfo(message);
                    break;
                case "getInPatientForegiftInfo":
                    resXml = platformAppWebService.getInPatientForegiftInfo(message);
                    break;
                case "PayForegiftOrder":
                    resXml = platformAppWebService.payForegiftOrder(message);
                    break;
                case "getInPatientListOfDayInfo":
                    resXml = platformAppWebService.getInPatientListOfDayInfo(message);
                    break;
                case "getInPatientListOfDayItemInfo":
                    resXml = platformAppWebService.getInPatientListOfDayItemInfo(message);
                    break;
                case "getPatiInfo":
                    resXml = platformAppWebService.getPatiInfo(message);
                    break;
                case "regiAdmission":
                    resXml = platformAppWebService.regiAdmission(message);
                    break;
                case "cancelRegiAdmission":
                    resXml = platformAppWebService.cancelRegiAdmission(message);
                    break;
                case "getDictInfo":
                    resXml = platformAppWebService.getDictInfo(message);
                    break;
                case "noticeSettle":
                    resXml = platformAppWebService.noticeSettle(message);
                    break;
                case "getinpatientchecklistInfo":
                    resXml = platformAppWebService.getinpatientchecklistInfo(message);
                    break;
                case "querySettleAccounts":
                    resXml = platformAppWebService.querySettleAccounts(message);
                    break;
                case "Updatebind":
                    resXml = platformAppWebService.Updatebind(message);
                    break;
                default:

                    break;
            }
        } catch (BusException e) {
            e.printStackTrace();
            resXml = XmlUtil.beanToXml(PfWsUtils.constructErrResBean(resXml, e.getMessage()), PlatFormResErr.class);
            logger.info("\n------------------------------返回给平安的XML------------------------------\n：:"+resXml);
            return resXml;
        } catch (Exception e1) {
            e1.printStackTrace();
            resXml = XmlUtil.beanToXml(PfWsUtils.constructErrResBean(resXml, e1.getMessage()), PlatFormResErr.class);
            logger.info("\n------------------------------返回给平安的XML------------------------------\n：:"+resXml);
            return resXml;
        }
        logger.info("\n------------------------------返回给平安的XML------------------------------\n：:"+resXml);
        System.out.println(resXml);
        return resXml;
    }
}
