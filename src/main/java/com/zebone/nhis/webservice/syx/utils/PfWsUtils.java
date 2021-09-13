package com.zebone.nhis.webservice.syx.utils;

import com.google.common.collect.Lists;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.webservice.syx.vo.platForm.*;
import com.zebone.platform.common.support.NHISUUID;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

public class PfWsUtils {
    /**
     * 组装请求XML接收Bean
     *
     * @param data
     * @return
     */
    public static PlatFormReq constructReqBean(HospitalInfo data) {
//        PlatFormReq platFormReq = new PlatFormReq();
//        PlatFormReqSender platFormSender = new PlatFormReqSender();
//        PlatFormReqSubject platFormSubject = new PlatFormReqSubject();
//
//        platFormSubject.setReq(data);
//        platFormReq.setSender(platFormSender);
//        platFormReq.setSubject(platFormSubject);
//        return platFormReq;
        return null;
    }

    /**
     * 组装响应实体
     *
     * @return
     */
    public static PlatFormRes constructResBean(PlatFormReq platFormReq, PlatFormRes platFormRes) {
        //response
        platFormRes.setActionId(platFormReq.getActionId());
        platFormRes.setActionName(platFormReq.getActionName());
        platFormRes.setCreateTime(DateUtils.getDateTimeStr(new Date()));
        platFormRes.setId(NHISUUID.getKeyId());

        return platFormRes;
    }

    /**
     * 请求失败返回消息
     *
     * @param content
     * @param reason
     * @return
     */
    public static PlatFormResErr constructErrResBean(String content, String reason) {

        //res
        PlatFormResSubjectErr platFormResSubjectSucc = new PlatFormResSubjectErr();
        platFormResSubjectSucc.setResultCode("1");
        platFormResSubjectSucc.setResultDesc(reason);
        List<PlatFormResSubjectErr> platFormResSubjectSuccs = Lists.newArrayList();
        platFormResSubjectSuccs.add(platFormResSubjectSucc);

        //result
        PlatFormResResultErr platFormResResult = new PlatFormResResultErr();
        platFormResResult.setId("AE");
        platFormResResult.setRequestId(StringUtils.substringBetween(content, "<id>", "</id>"));
        platFormResResult.setText("处理失败！");
        platFormResResult.setRequestTime(DateUtils.getDateTimeStr(new Date()));
        platFormResResult.setText(reason);
        platFormResResult.setSubject(platFormResSubjectSuccs);

        //response
        PlatFormResErr platFormResErr = new PlatFormResErr();
        platFormResErr.setActionId(StringUtils.substringBetween(content, "<actionId>", "</actionId>"));
        platFormResErr.setActionName(StringUtils.substringBetween(content, "<actionName>", "</actionName>"));
        platFormResErr.setCreateTime(DateUtils.getDateTimeStr(new Date()));
        platFormResErr.setId(NHISUUID.getKeyId());

        platFormResErr.setResult(platFormResResult);

        return platFormResErr;
    }

    /**
     * 业务处理成功返回消息
     *
     * @param platFormReq
     * @param desc
     * @param resCode
     * @return
     */
    public static PlatFormResSucc constructSuccResBean(PlatFormReq<?> platFormReq, String desc, String resCode) {
        //res
        PlatFormResSubjectErr platFormResSubjectSucc = new PlatFormResSubjectErr();
        platFormResSubjectSucc.setResultCode(resCode);
        platFormResSubjectSucc.setResultDesc(desc);
        List<PlatFormResSubjectErr> platFormResSubjectSuccs = Lists.newArrayList();
        platFormResSubjectSuccs.add(platFormResSubjectSucc);

        //result
        PlatFormResResultSucc platFormResResultSucc = new PlatFormResResultSucc();
        platFormResResultSucc.setId("AA");
        platFormResResultSucc.setText("处理成功!");
        platFormResResultSucc.setRequestId(platFormReq.getId());
        platFormResResultSucc.setRequestTime(platFormReq.getCreateTime());
        platFormResResultSucc.setSubject(platFormResSubjectSuccs);

        //response
        PlatFormResSucc platFormResSucc = new PlatFormResSucc();
        platFormResSucc.setActionId(platFormReq.getActionId());
        platFormResSucc.setActionName(platFormReq.getActionName());
        platFormResSucc.setId(NHISUUID.getKeyId());
        platFormResSucc.setResult(platFormResResultSucc);
        return platFormResSucc;
    }

    /**
     * @param resultCode 处理结果代码
     * @param resultDesc 处理结果描述
     * @param resXml     待处理XML节点
     */
    public static String setResStatus(String resultCode, String resultDesc, String resXml) {
        String xml = "";
        if (resultCode != null && resultDesc != null && resXml != null) {
            if (StringUtils.contains(resXml, "<res>")) {
                String xmlNode = "<res>\n\t\t\t<resultCode>" + resultCode + "</resultCode>\n\t\t\t<resultDesc>" + resultDesc + "</resultDesc>";
                xml = StringUtils.replaceOnce(resXml, "<res>", xmlNode);
            } else if (StringUtils.contains(resXml, "<res/>")) {
                String xmlNode = "<res>\n\t\t\t<resultCode>0</resultCode>\n\t\t\t<resultDesc>未查询到数据！</resultDesc>\n\t\t</res>";
                xml = StringUtils.replaceOnce(resXml, "<res/>", xmlNode);
            }
        }
        return xml;
    }
}
