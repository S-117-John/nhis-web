package com.zebone.nhis.ma.pub.syx.service;


import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.common.support.HttpClientUtil;
import com.zebone.nhis.ma.pub.syx.dao.SyxPaAppWebMapper;
import com.zebone.nhis.ma.pub.syx.vo.SettleUpReq;
import com.zebone.nhis.ma.pub.syx.vo.SettleUpVo;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * his调用平安的webService服务
 *
 * @author jd
 */
@Service
public class SyxPaAppWebHandler {

    @Resource
    private SyxPaAppWebMapper syxPaAppWebMapper;

    private Logger logger = LoggerFactory.getLogger("ma.syxInterface");

    @Value("#{applicationProperties['ext.pa.webservice.address']}")
    private String paWsUrl;

    public void invokeMethod(String methodName, Object... args) {
        switch (methodName) {
            case "noticeSettle":
                this.noticeSettle(args);
                break;
            case "noticeOutHosp":
                this.noticeOutHosp(args);
                break;
            case "noticeCancelOutHosp":
                this.noticeCancelOutHosp(args);
                break;
            case "noticeCancelInHosp":
                this.noticeCancelInHosp(args);
                break;
            case "noticeInHospExamine":
                this.noticeInHospExamine(args);
                break;
            case "verifyAccounting4Pa":
                this.verifyAccounting4Pa(args);
                break;
            default:
                break;
        }
    }

    /**
     * 入院审核通知接口
     *
     * @param args
     */
    private String noticeInHospExamine(Object... args) {

        try {
            //获取参数数据，并且构建请求参数
            String param = args[0].toString();
            String pkPv = JsonUtil.getFieldValue(param, "pkpv");
            SettleUpReq reqVo = syxPaAppWebMapper.getInHospExamine(pkPv);

            SettleUpVo upVo = new SettleUpVo();
            upVo.setReqType("noticeInHospExamine");
            upVo.setSettleUpReq(reqVo);

            String message = XmlUtil.beanToXml(upVo, SettleUpVo.class);
            logger.info("\nHIS请求XML：" + message);
            //TODO 调用web服务，响应参数及地址待处理，请求后的业务暂未处理
            String res = HttpClientUtil.sendHttpPostXml(paWsUrl, message);
            logger.info(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 3.18 取消入院通知（新增）
     *
     * @param args
     */
    private String noticeCancelInHosp(Object... args) {
        try {
            //获取参数数据，并且构建请求参数
            String param = args[0].toString();
            PvEncounter pv = JsonUtil.readValue(param, PvEncounter.class);
            SettleUpReq reqVo = syxPaAppWebMapper.getPiCancelIn(pv);

            SettleUpVo upVo = new SettleUpVo();
            upVo.setReqType("noticeCancelInHosp");
            upVo.setSettleUpReq(reqVo);

            String message = XmlUtil.beanToXml(upVo, SettleUpVo.class);
            logger.info("\nHIS请求XML：" + message);
            //TODO 调用web服务，响应参数及地址待处理，请求后的业务暂未处理
            String res = HttpClientUtil.sendHttpPostXml(paWsUrl, message);
            logger.info(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 3.21 取消出院办理通知（新增）
     *
     * @param args
     * @return
     */
    private String noticeCancelOutHosp(Object... args) {

        try {
            //获取参数数据，并且构建请求参数
            String param = args[0].toString();
            SettleUpReq reqVo = JsonUtil.readValue(param, SettleUpReq.class);
            reqVo = syxPaAppWebMapper.getPiOutInfo(reqVo);
            SettleUpVo upVo = new SettleUpVo();
            upVo.setReqType("noticeCancelOutHosp");
            upVo.setSettleUpReq(reqVo);

            String message = XmlUtil.beanToXml(upVo, SettleUpVo.class);
            logger.info("\nHIS请求XML：" + message);
            //TODO 调用web服务，响应参数及地址待处理，请求后的业务暂未处理
            String res = HttpClientUtil.sendHttpPostXml(paWsUrl, message);
            logger.info(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 通知结算接口—平安
     *
     * @param param
     * @return
     */
    public String noticeSettle(Object... args) {
        try {
            //获取参数数据，并且构建请求参数
            String param = args[0].toString();
            SettleUpReq reqVo = JsonUtil.readValue(param, SettleUpReq.class);
            List<PiMaster> piMasters = DataBaseHelper.queryForList("select * from pi_master where code_ip=?", PiMaster.class, reqVo.getCodeIp());
            Map<String, Object> pvMap = DataBaseHelper.queryForMap("select pv.*,bp.name as hp_name from pv_encounter pv inner join bd_hp bp on bp.pk_hp=pv.pk_insu where pv.pk_pv=?", new Object[]{reqVo.getPkPv()});
            reqVo.setNameInsu(pvMap.get("hpName")+"");
            if (piMasters != null && piMasters.size() > 0) {
                reqVo.setUserCardId(piMasters.get(0).getIdNo());
            }
            reqVo.setDateBegin(DateUtils.parseDateStr(DateUtils.strToDate(reqVo.getDateBegin())));
            reqVo.setDateEnd(DateUtils.parseDateStr(DateUtils.strToDate(reqVo.getDateEnd())));
            reqVo.setSettleStatus("2");
            SettleUpVo upVo = new SettleUpVo();
            upVo.setReqType("noticeSettle");
            upVo.setSettleUpReq(reqVo);

            String message = XmlUtil.beanToXml(upVo, SettleUpVo.class);
            logger.info("\nHIS请求XML：" + message);
            //TODO 调用web服务，响应参数及地址待处理，请求后的业务暂未处理
            String res = HttpClientUtil.sendHttpPostXml(paWsUrl, message);
            logger.info(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 病房出院接口—平安
     *
     * @param args
     * @return
     */
    public String noticeOutHosp(Object... args) {
        try {
            //获取参数数据，并且构建请求参数
            String param = args[0].toString();
            SettleUpReq reqVo = JsonUtil.readValue(param, SettleUpReq.class);
            reqVo = syxPaAppWebMapper.getPiOutInfo(reqVo);
            SettleUpVo upVo = new SettleUpVo();
            upVo.setReqType("noticeOutHosp");
            upVo.setSettleUpReq(reqVo);

            String message = XmlUtil.beanToXml(upVo, SettleUpVo.class);
            logger.info("\nHIS请求XML：" + message);
            //TODO 调用web服务，响应参数及地址待处理，请求后的业务暂未处理
            String res = HttpClientUtil.sendHttpPostXml(paWsUrl, message);
            logger.info(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String verifyAccounting4Pa(Object... args) {
        try {
            //获取参数数据，并且构建请求参数
            String param = args[0].toString();
            SettleUpReq reqVo = JsonUtil.readValue(param, SettleUpReq.class);
//            reqVo = syxPaAppWebMapper.getPiOutInfo(reqVo);
//            SettleUpVo upVo = new SettleUpVo();
//            upVo.setId(NHISUUID.getKeyId());
//            upVo.setCreateTime(DateUtils.getDate("yyyyMMddHHmmss"));
//            upVo.setActionId("noticeSettle");
//            PlatFormReqSender sender = new PlatFormReqSender();
//            sender.setSenderId(UserContext.getUser().getCodeEmp());
//            sender.setSendername(UserContext.getUser().getNameEmp());
//            sender.setSystemId("HIS");
//            sender.setSystemName("HIS");
//            upVo.setSender(sender);

//            SettleUpSub subject = new SettleUpSub();
//            subject.setReqVo(reqVo);
//            String message = XmlUtil.beanToXml(upVo, SettleUpVo.class);
//            String servicePath = ApplicationUtils.getPropertyValue("ext.pa.webservice.address", "");
//            //TODO 调用web服务，响应参数及地址待处理，请求后的业务暂未处理
//            WSUtil.invoke(servicePath, "noticeOutHosp", message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
