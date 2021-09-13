package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.service;

import com.google.common.collect.Sets;
import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.EnumUrlType;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.*;
import com.zebone.nhis.ma.pub.service.MsgRetryProcessor;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 人民医院-》平台接口重发处理器
 */
@Service
public class ZsrmPlatFormRetryProcessor implements MsgRetryProcessor {

    private ResponseResolver responseResolver;

    public ZsrmPlatFormRetryProcessor(){
        responseResolver = new ResponseResolver();
    }

    @Autowired
    private SendDirectHandler sendDirectHandler;

    @Autowired
    private ZsphMsgService zsphMsgService;
    @Autowired
    private RequestTemplate requestTemplate;

    @Override
    public String send(SysMsgRec msgRec) {
        //使用传入的消息，未改变id直接发送。若需要变更id后在发送，直接改id节点即可，
        // 若要同时再次记录发送日志，直接调用SendAndResolve.getInstance().send 即可
        RequestData requestData = RequestData.newBuilder()
                .id(msgRec.getMsgId())
                .data(msgRec.getMsgContent())
                .remoteMethod(msgRec.getMsgType())
                .msgIndexData(MsgIndexData.newBuilder().codePi(msgRec.getCodePi())
                        .codeOp(msgRec.getCodeOp())
                        .codeIp(msgRec.getCodeIp())
                        .codeOther(msgRec.getCodeOther())
                        .build())
                .urlType(getUrlType(msgRec.getMsgType()))
                .build();
        //发送的时候，即使是1：N，每一个消息都会记录一条日志，同时记录直连url到remark字段，所以这里直接拿去。也就是传入的也是具体到某个url
        List<String> urls = DataBaseHelper.getJdbcTemplate().queryForList("select remark from SYS_MSG_REC where PK_MSG=?", String.class, msgRec.getPkMsg());
        if(urls.size() == 0){
            throw new BusException("消息记录中存储的url为空，发送失败");
        }
        requestData.setUrlType(EnumUrlType.DIRECT);
        requestData.setDirectUrl(urls.get(0));
        String result = null;
        ResponseData responseData = requestTemplate.request(HttpMethod.POST, requestData);
        if (responseData.isSuccess()) {
            Object obj = responseResolver.deserialize(responseData);
            result = obj == null ? null : obj.toString();
            //只有成功了才调用修改，失败时异常抛到上层去了
            zsphMsgService.updateStatus(msgRec.getPkMsg(),"ACK",responseData.getData());
        }
        return result;
    }

    @Override
    public String getSysCode() {
        return "NHIS";
    }

    public EnumUrlType getUrlType(String msgType){
        //以业务remoteMethod来区分是基础数据还是业务，如果还不行，改造这里载依据 remoteMethod + implicitRules 来区分
        return BASE_MSGTYPE.contains(msgType)?EnumUrlType.BASE:EnumUrlType.BUSSINESS;
    }

    /** 走baseUrl的接口类型*/
    public static final Set<String> BASE_MSGTYPE
            = Sets.newHashSet("Organization", "Location", "Practitioner", "Basic", "ChargeItem", "Substance", "Medication");
}
