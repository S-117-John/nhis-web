package com.zebone.nhis.ma.pub.platform.send.impl.syx.service;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.ma.pub.platform.emr.vo.receivevo.ResponseEmrAckn;
import com.zebone.nhis.ma.pub.platform.emr.vo.sendvo.*;
import com.zebone.nhis.ma.pub.platform.syx.support.HIPMessageServerUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**发送emr领域下的类容
 * create by: gao shiheng
 *
 * @Param: null
 * @return
 */
@Service
public class SyxPlaFormSendEmrHandler {
    private Logger logger = LoggerFactory.getLogger("syx.hrSyxConsis");


    /**向集成平台上传病历质控信息
     * create by: gao shiheng
     *
     * @Param: null
     * @return
     */
    public void sendEmrPiMessage(Map<String, Object> paramMap){
        long timeStart = new Date().getTime();    //记录一个开始上传时间
        if (paramMap == null) return;

        HIPMessageServerUtils hipUtils = new HIPMessageServerUtils();
        RequestEmrVo requestEmrVo = new RequestEmrVo();
        ReqEmrPv reqEmrPv = (ReqEmrPv) paramMap.get("REV");
        ReqEmrSender reqEmrSender = new ReqEmrSender();
        ReqEmrSubject reqEmrSubject = new ReqEmrSubject();
        ReqEmrPvList reqEmrPvList = new ReqEmrPvList();
        User user= UserContext.getUser();
        String reqXml = "";
        String resXml = "";

        try {
            logger.info("\n\n\n*******************************************************************************************************************************************");
            logger.info("==================== HIP平台电子病历指控上传数据接口开始==============================================================");
            /*拼装传输的xml*/
            requestEmrVo.setiTSVersion("1.0");
            requestEmrVo.setActionId("EMRcatalogService");
            requestEmrVo.setId(NHISUUID.getKeyId());
            requestEmrVo.setCreateTime(DateUtils.getDateTimeStr(new Date()));
            requestEmrVo.setActionName("EMR归档服务");
            /*拼sender*/
            reqEmrSender.setSystemId("EMR");
            reqEmrSender.setSystemName("电子病历");
            reqEmrSender.setSenderId(user.getId());
            reqEmrSender.setSenderName(user.getUserName());
            requestEmrVo.setReqEmrSender(reqEmrSender);
            /*拼subject*/
            reqEmrSubject.setEuPvtype("3");
            reqEmrPvList.setReqEmrPv(reqEmrPv);
            reqEmrSubject.setReqEmrPvList(reqEmrPvList);
            requestEmrVo.setReqEmrSubject(reqEmrSubject);

            reqXml = XmlUtil.beanToXml(requestEmrVo, RequestEmrVo.class);

            resXml = hipUtils.sendPrivateHIPService("EMRcatalogService", reqXml);
            /*截取xml*/
            resXml = XmlUtil.interceptionXml(resXml, "<acknowledgement typeCode=\"AA\">", "</MCCI_IN000002UV01>");
            ResponseEmrAckn emrAID = (ResponseEmrAckn)XmlUtil.XmlToBean(resXml, ResponseEmrAckn.class);


            if (!"请求成功".equals(emrAID.getResponseEmrAID().getResponseEmrText().getValue())){
                logger.info("====================HIP平台EMR归档请求接口：状态【失败】，HIS业务处理失败,住院号："+reqEmrPv.getCodeIp());
            }
        }catch (Exception e){
            logger.info("====================HIP平台EMR归档请求接口：状态【失败】，HIS业务处理失败，异常原因：【"+e.getMessage()+"】,住院号："+reqEmrPv.getCodeIp());
        }finally {
            long timeEnd=new Date().getTime();
            logger.info("====================请求的消息：\n"+reqXml);
            logger.info("====================响应的消息：\n"+resXml);
            logger.info("====================HIP平台数据上传EMR归档请求接口结束：共耗时："+(timeEnd-timeStart)+"ms==============================================================");
        }

    }
}
