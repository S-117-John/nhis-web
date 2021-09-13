package com.zebone.nhis.ma.pub.zsrm.handler;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.zsrm.annotation.EventMethod;
import com.zebone.nhis.ma.pub.zsrm.service.ZsrmHrpPostService;
import com.zebone.nhis.ma.pub.zsrm.support.ZsrmHrpResult;
import com.zebone.nhis.ma.pub.zsrm.vo.*;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.*;

import static com.zebone.nhis.pro.zsba.common.support.HttpClientUtils.createIgnoreVerifySSL;

/**
 * HRP 接口handler 数据处理
 * 此接口对接收并解析参数做必要数据验证，并且构建User信息
 * 业务逻辑不在此类实现
 */
@Service
public class ZsrmHrpPostHandler {
    private static Logger logger = LoggerFactory.getLogger("nhis.hrp");

    @Resource
    private ZsrmHrpPostService zsrmHrpPostService;

    public static ZsrmHrpPostHandler zsrmHrpPostHandler;
    @PostConstruct
    public void init(){
        zsrmHrpPostHandler = this;
    }

    public Object invokeMethod(String methodName,Object...args) {
        if (CommonUtils.isNull(methodName) || args==null || args.length==0) return null;
        Object obj = null;
        switch (methodName) {
            case "sendAllocationRequest":
                sendAllocationRequest((String)args[0]);
                break;
            case "sendReturnBackDrug":
                sendReturnBackDrug((String)args[0]);
                break;
            case "getHrpStock":
                obj=getHrpStock((String)args[0]);
                break;
            case "hrpOutStorePd":
               hrpOutStorePd((String)args[0]);
                break;
            case "qryHisStock":
                obj = qryHisStock((String) args[0]);
                break;
        }
        return obj;
    }

    /**
     * 药房退药 HRP => HIS
     * @return
     */
    @EventMethod("HRPYFTY")
    public String sureRetunBackDrug(String param){
        String result = zsrmHrpPostHandler.zsrmHrpPostService.saveDrugStorageConfirmReturnBackDrug(param);
        return JSONObject.toJSONString(ZsrmHrpResult.successMessage(new Object[] {}));
    }

    /**
     * 药库出库HRP->HIS
     * @return
     */
    @EventMethod("HISYFCK")
    public String hrpOutStorePd(String param){
        HrpPubReceiveVo<List<HrpAllocaReqVo>> receiveVo=JsonUtil.readValue(param, new TypeReference< HrpPubReceiveVo<List<HrpAllocaReqVo>>>(){});
        try {
            zsrmHrpPostHandler.zsrmHrpPostService.saveHrpOutStorePd(receiveVo);
        } catch (ParseException e) {
            throw new BusException("时间日期格式错误! yyyyMMdd" + e.getMessage());
        }
        return JSONObject.toJSONString(ZsrmHrpResult.successMessage(new Object[]{}));
    }

    /**
     * 药品调价单HRP->HIS
     * @param param
     * @return
     */
    @EventMethod("HISYFTJ")
    public String doPriceHisStore(String param){
        HrpPubReceiveVo<List<HrpAllocaReqVo>> receiveVo = JsonUtil.readValue(param, new TypeReference<HrpPubReceiveVo<List<HrpAllocaReqVo>>>() {});
        Object result = zsrmHrpPostHandler.zsrmHrpPostService.savePriceHisStore(receiveVo);
        return JSONObject.toJSONString(ZsrmHrpResult.successMessage(new Object []{}));
    }

    /**
     * 药库库存HRP->HIS
     * @param param
     * @return
     */
    @EventMethod("HRPYFKCL")
    public String qryHisStock(String param){
        ZsrmHrpStock zsrmHrpStock = JsonUtil.readValue(param, new TypeReference<ZsrmHrpStock>() {});
        if(zsrmHrpStock.getMedicineCode().size() == 0 && zsrmHrpStock.getLocationCode().size() == 0) throw new BusException("没有任何数据!");
        List<ApplyVo> applyVoList = (List<ApplyVo>)zsrmHrpPostHandler.zsrmHrpPostService.qryHisStock(zsrmHrpStock);
        return JSONObject.toJSONString(ZsrmHrpResult.successMessage(applyVoList));
    }

    /**
     * http请求
     * @param param
     * @param url
     * @return
     */
    public Object getHttpRequest(Object param, String url) {
        CloseableHttpClient client = null;
        try {
            SSLContext sslcontext = createIgnoreVerifySSL();
            // 设置协议http和https对应的处理socket链接工厂的对象
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new SSLConnectionSocketFactory(sslcontext))
                    .build();
            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            //创建自定义的httpclient对象
            client = HttpClients.custom().setConnectionManager(connManager).build();
            HttpPost post = new HttpPost(url);
            CloseableHttpResponse res = null;
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            ContentType contentType=ContentType.create("text/plain",Charset.forName("UTF-8"));
            String jsonString = JSONObject.toJSONString(param);
            logger.info("入参-----------》   " + jsonString);
            multipartEntityBuilder.addPart("data",new StringBody(jsonString,contentType));
            post.setEntity(multipartEntityBuilder.build());
            res = client.execute(post);
            HttpEntity entity = res.getEntity();
            if(entity != null){
                String result = EntityUtils.toString(entity, "utf-8");
                logger.info("外部接口返回参数:----> " + result);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 提交发送调拨申请
     * @param pkPdplan
     * @return
     */
    public Object sendAllocationRequest(String pkPdplan){
        HrpPubReceiveVo result = zsrmHrpPostService.sendAllocaMethod(pkPdplan);
        Map<String, Object> param = JSON.parseObject(JSONObject.toJSONString(result), Map.class);
        if(param.get("projectNo") == null|| param.get("remarks") == null){
            param.put("projectNo","");
            param.put("remarks","");
        }
        //TODO 转化实体=》json 调用第三方接口
        Object obj = getHttpRequest(param, ApplicationUtils.getPropertyValue("msg.hrp.send", ""));
        return obj;
    }

    /**
     * 发送药房退回药库申请
     * @param param
     * @return ZsrmHrpResult
     */
    public String sendReturnBackDrug(String param){
        HrpPubReceiveVo result = zsrmHrpPostHandler.zsrmHrpPostService.sureReturnBackDrug(param);
        Object obj = getHttpRequest(result, ApplicationUtils.getPropertyValue("msg.hrp.send", ""));
        return JSONObject.toJSONString(ZsrmHrpResult.successMessage(new Object[]{}));
    }

    /**
     * HIS->HRP
     * 获取HRP 库存数据
     * @return
     */
    @EventMethod("HISYKKCL")
    public Object getHrpStock(String param){
        if(param == null || param == ""){
            throw new BusException("没有任何数据！");
        }
        Map<String, Object> paramMap = JSONObject.parseObject(param, Map.class);
        String httpResult = getHttpRequest(paramMap,ApplicationUtils.getPropertyValue("msg.hrp.send","")).toString();
        Map<String, Object> outResult = JSONObject.parseObject(httpResult, Map.class);
        List dataList = (List) outResult.get("data");
        String json = JSONObject.toJSONString(dataList);
        return json;
    }
}
