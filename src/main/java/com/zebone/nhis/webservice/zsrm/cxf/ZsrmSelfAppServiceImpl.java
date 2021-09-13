package com.zebone.nhis.webservice.zsrm.cxf;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.webservice.zsrm.service.ZsrmSelfAppService;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.utils.JsonUtil;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class ZsrmSelfAppServiceImpl implements IZsrmSelfAppService {

	private static Logger log = LoggerFactory.getLogger("nhis.ZsrmWeChatZzjLog");
	
    @Resource
    private ZsrmSelfAppService zsrmSelfAppService;

    
    @Override
    public String queryPatiCgInfoNotSettle(String param) {
    	log.info("queryPatiCgInfoNotSettle查询待缴费接口入参："+param);
        JsonNode jsonParam = JsonUtil.getJsonNode(param, "entry");
        List<Map<String,Object>> resqo=JsonUtil.readValue(jsonParam, new TypeReference<List<Map<String,Object>>>() {});
        String resJson="";
        if(resqo!=null && resqo.size()>1){
            Map<String,Object> resqos=(Map<String, Object>) resqo.get(1).get("resource");
            String paramInp=JsonUtil.writeValueAsString(resqos);
            resJson = zsrmSelfAppService.queryPatiCgInfoNotSettle(paramInp);
        }
        log.info("queryPatiCgInfoNotSettle查询待缴费接口出参："+resJson);
        return resJson;
    }

    @Override
    public String accountedSettlement(String param) {
    	log.info("accountedSettlement结算接口入参："+param);
        JsonNode jsonParam = JsonUtil.getJsonNode(param, "entry");
        List<Map<String,Object>> resqo=JsonUtil.readValue(jsonParam, new TypeReference<List<Map<String,Object>>>() {});
        String resJson="";
        if(resqo!=null && resqo.size()>1){
            Map<String,Object> resqos=(Map<String, Object>) resqo.get(1).get("resource");
            String paramInp=JsonUtil.writeValueAsString(resqos);
            resJson = zsrmSelfAppService.saveSettleForSelf(paramInp);
        }
        log.info("accountedSettlement结算接口出参："+resJson);
        return resJson;
    }

    @Override
    public String accountedPreSettlement(String param) {
    	log.info("accountedSettlement医保预结算接口入参："+param);
        JsonNode jsonParam = JsonUtil.getJsonNode(param, "entry");
        List<Map<String,Object>> resqo=JsonUtil.readValue(jsonParam, new TypeReference<List<Map<String,Object>>>() {});
        String resJson="";
        if(resqo!=null && resqo.size()>1){
            Map<String,Object> resqos=(Map<String, Object>) resqo.get(1).get("resource");
            String paramInp=JsonUtil.writeValueAsString(resqos);

            resJson = zsrmSelfAppService.getYbWillSettle(paramInp);
        }
        log.info("accountedSettlement医保预结算接口出参："+resJson);
        return resJson;
    }
    

	@Override
	public String Covid19(String param) {
		log.info("Covid19预约核酸检测接口入参："+param);
        JsonNode jsonParam = JsonUtil.getJsonNode(param, "entry");
        List<Map<String,Object>> resqo=JsonUtil.readValue(jsonParam, new TypeReference<List<Map<String,Object>>>() {});
        String resJson="";
        if(resqo!=null && resqo.size()>1){
            Map<String,Object> resqos=(Map<String, Object>) resqo.get(1).get("resource");
            String paramInp=JsonUtil.writeValueAsString(resqos);
            resJson = zsrmSelfAppService.appointNuc(paramInp);
        }
        log.info("Covid19预约核酸检测接口出参："+resJson);
        return resJson;
	}
	
	@Override
	public String queryOutpfeeMasterInfo(String param) {
		log.info("queryOutpfeeMasterInfo获取门诊费用主表接口入参："+param);
        JsonNode jsonParam = JsonUtil.getJsonNode(param, "entry");
        List<Map<String,Object>> resqo=JsonUtil.readValue(jsonParam, new TypeReference<List<Map<String,Object>>>() {});
        String resJson="";
        if(resqo!=null && resqo.size()>1){
            Map<String,Object> resqos=(Map<String, Object>) resqo.get(1).get("resource");
            String paramInp=JsonUtil.writeValueAsString(resqos);
            resJson = zsrmSelfAppService.queryOutpfeeMasterInfo(paramInp);
        }
        log.info("queryOutpfeeMasterInfo获取门诊费用主表接口出参："+resJson);
        return resJson;
	}

	@Override
	public String queryOutpfeeDetailInfo(String param) {
		log.info("queryOutpfeeDetailInfo获取门诊费用明细接口入参："+param);
        JsonNode jsonParam = JsonUtil.getJsonNode(param, "entry");
        List<Map<String,Object>> resqo=JsonUtil.readValue(jsonParam, new TypeReference<List<Map<String,Object>>>() {});
        String resJson="";
        if(resqo!=null && resqo.size()>1){
            Map<String,Object> resqos=(Map<String, Object>) resqo.get(1).get("resource");
            String paramInp=JsonUtil.writeValueAsString(resqos);
            resJson = zsrmSelfAppService.queryOutpfeeDetailInfo(paramInp);
        }
        log.info("queryOutpfeeDetailInfo获取门诊费用明细接口出参："+resJson);
        return resJson;
	}
    
    /**
     * 失败消息分装
     * @param message
     * @return
     */
    private String errorJson(String message){
        String result="{\"resourceType\":\"Bundle\",\"id\":\"%s\",\"type\":\"message\",\"timestamp\":\"%s\",\"entry\":[{\"response\":{\"staus\":\"AE\",\"outcome\":{\"resourceType\":\"OperationOutcome\",\"issue\":[{\"severity\":\"error\",\"code\":\"informational\",\"diagnostics\":\"%s\"}]}}}]}";
        result=String.format(result,NHISUUID.getKeyId(),DateUtils.getDate("yyyy-MM-dd HH:mm:ss"),message);
        return result;
    }

}
