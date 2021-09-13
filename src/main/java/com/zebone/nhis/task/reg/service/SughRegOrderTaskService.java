package com.zebone.nhis.task.reg.service;

import com.zebone.nhis.common.module.base.transcode.SysLogInterface;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.task.reg.dao.SughRegOrderMapper;
import com.zebone.nhis.task.reg.vo.SughRegIpInputOrderReqVo;
import com.zebone.nhis.task.reg.vo.SughRegIpOutputOrderReqVo;
import com.zebone.nhis.task.reg.vo.SughRegRequest;
import com.zebone.nhis.task.reg.vo.SughRegResponse;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.quartz.modle.QrtzJobCfg;
import org.apache.http.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 深大第三方订单数据推送
 *
 * @author jd_em
 */
@Service
public class SughRegOrderTaskService {

    @Resource
    private SughRegOrderMapper regOrderMapper;

    private final static Logger logger = LoggerFactory.getLogger("nhis.quartz");

    /**
     * 深大第三方订单数据推送
     *
     * @param cfg
     */
    public void getRegOrderInfoTask(QrtzJobCfg cfg) {

        getRegOrderInfo(cfg);
        getRegRetOrderInfo(cfg);
        sendIpOrder();
        sendIpoutOrderInfo();
    }

    /**
     * 挂号订单推送
     *
     * @param cfg
     */
    private void getRegOrderInfo(QrtzJobCfg cfg) {
        //获取14天之前的日期
        Map<String, Object> paramMap = new HashMap<>();
        String dateStart = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        String dateEnd = DateUtils.addDate(new Date(), -1, 4, "yyyyMMddHHmmss");
        paramMap.put("dateStart", dateStart);
        paramMap.put("dateEnd", dateEnd);
        List<SughRegRequest> pvList = regOrderMapper.qryPvInfoList(paramMap);
        if (pvList == null) return;

        String openUrl = ApplicationUtils.getPropertyValue("msg.third.order.openurl", "");
        SughRegResponse responOpen = JsonUtil.readValue(post(openUrl, ""), SughRegResponse.class);
        logger.info("连通性测试地址+++++++++++++++++++++++++" + openUrl + "\n连通性测试地址返回++++++++++++++" + JsonUtil.writeValueAsString(responOpen));

        if (!"0".equals(responOpen.getCode())) {

            return;
        }
        String tokenUrl = ApplicationUtils.getPropertyValue("msg.third.order.tokenurl", "");
        SughRegResponse responToken = JsonUtil.readValue(post(tokenUrl, ""), SughRegResponse.class);
        logger.info("获取访问令牌地址+++++++++++++++++++++++++" + tokenUrl + "\n获取访问令牌地址返回+++++++++++++++++++++++++" + JsonUtil.writeValueAsString(responToken));

        if (!"0".equals(responToken.getCode())) {
            return;
        }
        String tokenKey = responToken.getAccess_token();

        String url = ApplicationUtils.getPropertyValue("msg.third.order.dataurl", "");
        String postUrl = String.format(url, "PD001", tokenKey);
        String orguuid = ApplicationUtils.getPropertyValue("msg.third.order.orguuid", "");

        List<SysLogInterface> logList = new ArrayList<SysLogInterface>();
        for (SughRegRequest reqvo : pvList) {
            reqvo.setOrgUuid("MB2C1321X");
            reqvo.setPoolOrgUuid(orguuid);
            reqvo.setOrgName("深圳大学总医院");
            String jsonData = "[" + JsonUtil.writeValueAsString(reqvo) + "]";
            String resMsg = "";
            int i = 0;
            do {
                resMsg = post(postUrl, jsonData);
                i++;
            } while (("002".equals(resMsg) || !"0".equals(JsonUtil.getFieldValue(resMsg, "code"))) && i <= 3);
            logger.info("推送接口数据地址+++++++++++++++++++++++++" + postUrl + "\n推送接口JSON数据+++++++++++++++++++++++++" + jsonData + "\n推送数据接口返回+++++++++++++++++++++++++" + resMsg);

            SysLogInterface syslog = new SysLogInterface();
            syslog.setPkLogIntf(NHISUUID.getKeyId());
            syslog.setCreateTime(new Date());
            syslog.setCreator("定时");
            syslog.setTs(new Date());
            //ApplicationUtils.setDefaultValue(syslog, true);
            syslog.setInParam(jsonData);
            syslog.setNameIntf("PD001");
            syslog.setOutParam(resMsg);
            if ("002".equals(resMsg)) {
                syslog.setLog("挂号订单推送失败,程序异常");
                syslog.setType("3");
                logList.add(syslog);
                continue;
            }

            SughRegResponse resData = JsonUtil.readValue(resMsg, SughRegResponse.class);
            if (!"0".equals(resData.getCode())) {
                syslog.setLog("挂号订单推送失败");
                syslog.setType("2");
            } else {
                syslog.setLog("挂号订单推送成功");
                syslog.setType("1");//成功
            }

            logList.add(syslog);
        }
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(SysLogInterface.class), logList);

    }

    /**
     * 退号订单推送
     *
     * @param cfg
     */
    private void getRegRetOrderInfo(QrtzJobCfg cfg) {
        //获取14天之前的日期
        Map<String, Object> paramMap = new HashMap<>();
        String dateStart = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        String dateEnd = DateUtils.addDate(new Date(), -1, 4, "yyyyMMddHHmmss");
        paramMap.put("dateStart", dateStart);
        paramMap.put("dateEnd", dateEnd);
        List<SughRegRequest> pvList = regOrderMapper.qryPvRetInfoList(paramMap);
        if (pvList == null) return;

        String openUrl = ApplicationUtils.getPropertyValue("msg.third.order.openurl", "");
        SughRegResponse responOpen = JsonUtil.readValue(post(openUrl, ""), SughRegResponse.class);
        logger.info("连通性测试地址+++++++++++++++++++++++++" + openUrl + "\n连通性测试地址返回++++++++++++++" + JsonUtil.writeValueAsString(responOpen));

        if (!"0".equals(responOpen.getCode())) {

            return;
        }
        String tokenUrl = ApplicationUtils.getPropertyValue("msg.third.order.tokenurl", "");
        SughRegResponse responToken = JsonUtil.readValue(post(tokenUrl, ""), SughRegResponse.class);
        logger.info("获取访问令牌地址+++++++++++++++++++++++++" + tokenUrl + "\n获取访问令牌地址返回+++++++++++++++++++++++++" + JsonUtil.writeValueAsString(responToken));

        if (!"0".equals(responToken.getCode())) {
            return;
        }
        String tokenKey = responToken.getAccess_token();

        String url = ApplicationUtils.getPropertyValue("msg.third.order.dataurl", "");
        String postUrl = String.format(url, "PD001", tokenKey);
        String orguuid = ApplicationUtils.getPropertyValue("msg.third.order.orguuid", "");

        List<SysLogInterface> logList = new ArrayList<SysLogInterface>();
        for (SughRegRequest reqvo : pvList) {
            reqvo.setOrgUuid("MB2C1321X");
            reqvo.setPoolOrgUuid(orguuid);
            reqvo.setOrgName("深圳大学总医院");
            String jsonData = "[" + JsonUtil.writeValueAsString(reqvo) + "]";

            String resMsg = "";
            int i = 0;
            do {
                resMsg = post(postUrl, jsonData);
                String code = JsonUtil.getFieldValue(resMsg, "code");
                i++;
            } while (("002".equals(resMsg) || !"0".equals(JsonUtil.getFieldValue(resMsg, "code"))) && i <= 3);
            logger.info("推送接口数据地址+++++++++++++++++++++++++" + postUrl + "\n推送接口JSON数据+++++++++++++++++++++++++" + jsonData + "\n推送数据接口返回+++++++++++++++++++++++++" + resMsg);

            SysLogInterface syslog = new SysLogInterface();
            syslog.setPkLogIntf(NHISUUID.getKeyId());
            syslog.setCreateTime(new Date());
            syslog.setCreator("定时");
            syslog.setTs(new Date());
            //ApplicationUtils.setDefaultValue(syslog, true);
            syslog.setInParam(jsonData);
            syslog.setNameIntf("PD001");
            syslog.setOutParam(resMsg);

            if ("002".equals(resMsg)) {
                syslog.setLog("退号订单推送失败,程序异常");
                syslog.setType("3");
                logList.add(syslog);
                continue;
            }

            SughRegResponse resData = JsonUtil.readValue(resMsg, SughRegResponse.class);
            if (!"0".equals(resData.getCode())) {
                syslog.setLog("退号订单推送失败");
                syslog.setType("2");
            } else {
                syslog.setLog("退号订单推送成功");
                syslog.setType("1");//成功
            }
            logList.add(syslog);
        }
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(SysLogInterface.class), logList);

    }

    /**
     * 发送入院订单数据
     */
    private void sendIpOrder() {
        //获取14天之前的日期
        Map<String, Object> paramMap = new HashMap<>();
        String dateStart = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        String dateEnd = DateUtils.addDate(new Date(), -1, 4, "yyyyMMddHHmmss");
        paramMap.put("dateStart", dateStart);
        paramMap.put("dateEnd", dateEnd);
        List<SughRegIpInputOrderReqVo> pvList = regOrderMapper.qryPvIpInfoList(paramMap);
        if (pvList == null) return;

        String openUrl = ApplicationUtils.getPropertyValue("msg.third.order.openurl", "");
        SughRegResponse responOpen = JsonUtil.readValue(post(openUrl, ""), SughRegResponse.class);
        logger.info("连通性测试地址+++++++++++++++++++++++++" + openUrl + "\n连通性测试地址返回++++++++++++++" + JsonUtil.writeValueAsString(responOpen));

        if (!"0".equals(responOpen.getCode())) {
            return;
        }
        String tokenUrl = ApplicationUtils.getPropertyValue("msg.third.order.tokenurl", "");
        SughRegResponse responToken = JsonUtil.readValue(post(tokenUrl, ""), SughRegResponse.class);
        logger.info("获取访问令牌地址+++++++++++++++++++++++++" + tokenUrl + "\n获取访问令牌地址返回+++++++++++++++++++++++++" + JsonUtil.writeValueAsString(responToken));

        if (!"0".equals(responToken.getCode())) {
            return;
        }
        String tokenKey = responToken.getAccess_token();

        String url = ApplicationUtils.getPropertyValue("msg.third.order.dataurl", "");
        String postUrl = String.format(url, "PD003", tokenKey);

        List<SysLogInterface> logList = new ArrayList<SysLogInterface>();
        for (SughRegIpInputOrderReqVo reqvo : pvList) {
            reqvo.setOrgUuid("MB2C1321X");
            reqvo.setOrgName("深圳大学总医院");
            String jsonData = "[" + JsonUtil.writeValueAsString(reqvo) + "]";

            String resMsg = "";
            int i = 0;
            do {
                resMsg = post(postUrl, jsonData);
                i++;
            } while (("002".equals(resMsg) || !"0".equals(JsonUtil.getFieldValue(resMsg, "code"))) && i <= 3);
            logger.info("推送接口数据地址+++++++++++++++++++++++++" + postUrl + "\n推送接口JSON数据+++++++++++++++++++++++++" + jsonData + "\n推送数据接口返回+++++++++++++++++++++++++" + resMsg);

            SysLogInterface syslog = new SysLogInterface();
            syslog.setPkLogIntf(NHISUUID.getKeyId());
            syslog.setCreateTime(new Date());
            syslog.setCreator("定时");
            syslog.setTs(new Date());
            //ApplicationUtils.setDefaultValue(syslog, true);
            syslog.setInParam(jsonData);
            syslog.setNameIntf("PD003");
            syslog.setOutParam(resMsg);

            if ("002".equals(resMsg)) {
                syslog.setLog("入院订单推送失败,程序异常");
                syslog.setType("3");
                logList.add(syslog);
                continue;
            }

            SughRegResponse resData = JsonUtil.readValue(resMsg, SughRegResponse.class);
            if (!"0".equals(resData.getCode())) {
                syslog.setLog("入院订单推送失败");
                syslog.setType("2");
            } else {
                syslog.setLog("入院订单推送成功");
                syslog.setType("1");//成功
            }
            logList.add(syslog);
        }
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(SysLogInterface.class), logList);
    }

    /**
     * 发送出院订单数据
     */
    private void sendIpoutOrderInfo() {
        //获取14天之前的日期
        Map<String, Object> paramMap = new HashMap<>();
        String dateStart = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        String dateEnd = DateUtils.addDate(new Date(), -1, 4, "yyyyMMddHHmmss");
        paramMap.put("dateStart", dateStart);
        paramMap.put("dateEnd", dateEnd);
        List<SughRegIpOutputOrderReqVo> pvList = regOrderMapper.qryPvIpOutInfoList(paramMap);
        if (pvList == null) return;

        String openUrl = ApplicationUtils.getPropertyValue("msg.third.order.openurl", "");
        SughRegResponse responOpen = JsonUtil.readValue(post(openUrl, ""), SughRegResponse.class);
        logger.info("连通性测试地址+++++++++++++++++++++++++" + openUrl + "\n连通性测试地址返回++++++++++++++" + JsonUtil.writeValueAsString(responOpen));

        if (!"0".equals(responOpen.getCode())) {

            return;
        }
        String tokenUrl = ApplicationUtils.getPropertyValue("msg.third.order.tokenurl", "");
        SughRegResponse responToken = JsonUtil.readValue(post(tokenUrl, ""), SughRegResponse.class);
        logger.info("获取访问令牌地址+++++++++++++++++++++++++" + tokenUrl + "\n获取访问令牌地址返回+++++++++++++++++++++++++" + JsonUtil.writeValueAsString(responToken));
        if (!"0".equals(responToken.getCode())) {
            return;
        }
        String tokenKey = responToken.getAccess_token();

        String url = ApplicationUtils.getPropertyValue("msg.third.order.dataurl", "");
        String postUrl = String.format(url, "PD004", tokenKey);
        List<SysLogInterface> logList = new ArrayList<SysLogInterface>();
        for (SughRegIpOutputOrderReqVo reqvo : pvList) {
            reqvo.setOrgUuid("MB2C1321X");
            String jsonData = "[" + JsonUtil.writeValueAsString(reqvo) + "]";

            String resMsg = "";
            int i = 0;
            do {
                resMsg = post(postUrl, jsonData);
                i++;
            } while (("002".equals(resMsg) || !"0".equals(JsonUtil.getFieldValue(resMsg, "code"))) && i <= 3);
            logger.info("推送接口数据地址+++++++++++++++++++++++++" + postUrl + "\n推送接口JSON数据+++++++++++++++++++++++++" + jsonData + "\n推送数据接口返回+++++++++++++++++++++++++" + resMsg);

            SysLogInterface syslog = new SysLogInterface();
            syslog.setPkLogIntf(NHISUUID.getKeyId());
            syslog.setCreateTime(new Date());
            syslog.setCreator("定时");
            syslog.setTs(new Date());
            //ApplicationUtils.setDefaultValue(syslog, true);
            syslog.setInParam(jsonData);
            syslog.setNameIntf("PD004");
            syslog.setOutParam(resMsg);

            if ("002".equals(resMsg)) {
                syslog.setLog("出院订单推送失败,程序异常");
                syslog.setType("3");
                logList.add(syslog);
                continue;
            }
            SughRegResponse resData = JsonUtil.readValue(resMsg, SughRegResponse.class);
            if (!"0".equals(resData.getCode())) {
                syslog.setLog("出院订单推送失败");
                syslog.setType("2");
            } else {
                syslog.setLog("出院订单推送成功");
                syslog.setType("1");//成功
            }
            logList.add(syslog);
        }
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(SysLogInterface.class), logList);
    }

    /**
     * 第三方地址推送
     *
     * @param url
     * @param json
     * @return
     */
    public String post(String url, String json) {
        String result = "";
        HttpPost post = new HttpPost(url);
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            post.setHeader("Content-Type", "application/json;charset=utf-8");
            StringEntity postingString = new StringEntity(json, "utf-8");
            post.setEntity(postingString);
            HttpResponse response = httpClient.execute(post);

            InputStream in = response.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
            StringBuilder strber = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                strber.append(line + '\n');
            }
            br.close();
            in.close();
            result = strber.toString();
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                result = "002";
            }
        } catch (Exception e) {
            System.out.println("请求异常");
            result = "002";
        } finally {
            post.abort();
        }
        return result;
    }

}
