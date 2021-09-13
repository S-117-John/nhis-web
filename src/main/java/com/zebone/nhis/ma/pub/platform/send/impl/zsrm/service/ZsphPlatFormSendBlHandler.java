package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.service;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MapUtils;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.build.RequestBuild;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.dao.ZsrmBlSendMapper;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.MsgIndexData;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.RequestData;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.SendAndResolve;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.BusinessBase;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.sch.SchShortMsgResource;
import com.zebone.platform.common.support.NHISUUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ZsphPlatFormSendBlHandler {
    private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");

    @Resource
    private ZsrmBlSendMapper zsrmBlSendMapper;

    public void sendOpFeeReminderMsg(Map<String,Object> paramMap){
        //查询bl_op_dt中记费日志为当天未交费患者姓名、联系方式
        List<Map<String,Object>> arreaList = zsrmBlSendMapper.getOpArrearsLis(DateUtils.dateToStr("yyyyMMdd",new Date()));
        if(arreaList.size()>0){
            for(Map<String,Object> arrea : arreaList){
                String telNo = "";
                if (!CommonUtils.isEmptyString(MapUtils.getString(arrea,"telNo",""))) {
                    telNo = MapUtils.getString(arrea,"telNo");
                } else if(!CommonUtils.isEmptyString(MapUtils.getString(arrea,"mobile",""))){
                    telNo = MapUtils.getString(arrea,"mobile");
                }
                if (CommonUtils.isEmptyString(telNo)) {
                    continue;
                }
                String message = MapUtils.getString(arrea,"namePi","")+"先生/女士，您好！ 您今天在中山市人民医院门诊产生了新的费用，为避免后续无法就诊，请您及时缴费。可以选择关注中山市人民医院官方微信或到医院门诊自助机、人工窗口进行缴费";
                SchShortMsgResource shorMsgvo = new SchShortMsgResource(telNo, message);
                shorMsgvo.setImplicitRules("sendSmsNow");
                shorMsgvo.setServiceDomain("interfaceCenterTwo");
                BusinessBase businessBase = RequestBuild.create(shorMsgvo);
                RequestData requestData = RequestData.newBuilder()
                        .id(businessBase.getId())
                        .data(JSON.toJSONString(businessBase))
                        .msgIndexData(MsgIndexData.newBuilder().codeOp(MapUtils.getString(arrea,"codeOp"))
                                .codePi(MapUtils.getString(arrea,"codePi"))
                                .codeIp(MapUtils.getString(arrea,"codeIp")).build())
                        .remoteMethod("sendSmsNow").build();
                log.info("门诊排班调整发送短信至患者构造完成id:{},data:{},remoteMethod:{},implicitRules:{}", requestData.getId(), requestData.getData(), requestData.getRemoteMethod(), shorMsgvo.getImplicitRules());

                SendAndResolve.getInstance().send(HttpMethod.POST, requestData);
            }
        }
    }
}
