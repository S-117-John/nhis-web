package com.zebone.nhis.ma.pub.zsrm.handler;


import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.ma.pub.zsrm.dao.ZsrmOpDrugPackPubMapper;
import com.zebone.nhis.ma.pub.zsrm.dao.ZsrmOpHerbDrugPackMapper;
import com.zebone.nhis.ma.pub.zsrm.support.ZsrmHttpForThirdUtils;
import com.zebone.nhis.ma.pub.zsrm.vo.ZsrmHerbPresDtVo;
import com.zebone.nhis.ma.pub.zsrm.vo.ZsrmHerbPresVo;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 中山人民医院草药包药机接口服务 （包含三九和康仁堂）
 * 无事务
 */
@Service
public class ZsrmOpHerbDrugPackHandler {

    private static Logger log = LoggerFactory.getLogger("nhis.drug.pack");
    @Resource
    private ZsrmOpHerbDrugPackMapper opHerbDrugPackMapper;

    @Resource
    private ZsrmOpDrugPackPubMapper opDrugPackPubMapper;



    public void sendHerbPresInfo(Map<String, Object> paramMap) {
        try {
            if (!"1".equals(MapUtils.getString(paramMap, "IsPackMachine"))) return;
            List<String> pkPresocces = (List<String>) paramMap.get("pkPresocces");
            List<Map<String, Object>> presList = getPresTypeByPk(pkPresocces);
            if (presList == null) return;
            List<Map<String, Object>> presTypeMap = presList.stream().filter(m -> "02".equals(m.get("dtPrestype"))).collect(Collectors.toList());

            List<ZsrmHerbPresVo> herbPresVoList = opHerbDrugPackMapper.qryHerbPresInfo(presTypeMap);
            if (herbPresVoList == null) return;
            List<ZsrmHerbPresDtVo> dtVoList = opHerbDrugPackMapper.qryHerbPresDtInfo(presTypeMap);
            if (dtVoList == null) return;

            for (ZsrmHerbPresVo herbPres : herbPresVoList) {
                double amount = Double.valueOf(herbPres.getPriceTotal());
                double ords = Double.valueOf(herbPres.getOrdsDay());
                herbPres.setPrice(MathUtils.div(amount, ords, 2));
                List<ZsrmHerbPresDtVo> herbDtTempList = dtVoList.stream().filter(m -> herbPres.getPresNo().equals(m.getPresNo())).collect(Collectors.toList());
                herbPres.setDtVoList(herbDtTempList);
            }
            String urlParam = ApplicationUtils.getPropertyValue("scm.opdt.herbpack.webservice.url", "");
            Map<String, Object> urlMap = JsonUtil.readValue(urlParam, Map.class);
            for (ZsrmHerbPresVo herbPres : herbPresVoList) {
                String url = MapUtils.getString(urlMap, herbPres.getCodeStore());
                if (CommonUtils.isNull(url)) continue;
                String codeStore = herbPres.getCodeStore();
                if ("1104010040003".equals(codeStore)) {
                    herbPres.setNoteOrd("一日两次， 一次一盒，用200ml开水冲服");
                }
                String reqXml = XmlUtil.beanToXml(herbPres, ZsrmHerbPresVo.class, false);
                //reqXml=reqXml.replaceAll(" ","");
                reqXml = reqXml.replaceAll("\n", "");

                log.info("草药包药机-请求消息：" + herbPres.getCodeStore() + ":" + reqXml);
                String resXml = postHttp(url, "DataReceive", reqXml);
                log.info("草药包药机-接收消息：" + herbPres.getCodeStore() + ":" + resXml);
            }
        } catch (Exception ex) {
            log.info("草药包药机-处理异常：" + ex.getMessage());
        }
    }

    /**
     * 获取结算处方类型
     * @param pkPresocces
     * @return
     */
    public List<Map<String,Object>> getPresTypeByPk(List<String> pkPresocces){
        if(pkPresocces==null ||pkPresocces.size()==0)return null;

        return opDrugPackPubMapper.getPresTypeByPk(pkPresocces);
    }

    private String postHttp(String url,String method,String message) throws Exception {
        String responseMsg = "";
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setContentCharset("utf-8");
        PostMethod postMethod = new PostMethod(url+"/"+method);
        postMethod.addParameter("xml", message);
        postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        try {
            httpClient.executeMethod(postMethod);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = postMethod.getResponseBodyAsStream();
            int len = 0;
            byte[] buf = new byte[1024];
            while((len=in.read(buf))!=-1){
                out.write(buf, 0, len);
            }
            responseMsg = out.toString("UTF-8");
        } finally {
            postMethod.releaseConnection();
        }
        return responseMsg;
    }
}
