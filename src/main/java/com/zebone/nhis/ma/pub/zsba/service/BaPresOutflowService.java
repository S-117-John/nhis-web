package com.zebone.nhis.ma.pub.zsba.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zebone.nhis.common.module.cn.opdw.OutpresDrug;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.HttpClientUtil;
import com.zebone.nhis.ma.pub.zsba.support.PresOutflowProcessor;
import com.zebone.nhis.ma.pub.zsba.support.PresOutflowTool;
import com.zebone.nhis.ma.pub.zsba.vo.outflow.*;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.http.client.methods.HttpGet;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BaPresOutflowService {

    @Resource
    private PresOutflowProcessor presOutflowProcessor;

    /**
     * 2118 获取对照药品信息
     * http://hcnmindoc.bsoft.com.cn/docs/uop/outCompare
     * @return
     */
    public List<DrugVo> getOutCompare(Object... args){
        String path = "/open/prescription/drug/contrast/"+presOutflowProcessor.getHospitalOutId();
        HttpGet httpGet = new HttpGet(presOutflowProcessor.getBaseUrl()+path);
        Map<String, String> header = PresOutflowTool.buildHeader(presOutflowProcessor.buildReq(path, null));
        header.forEach(httpGet::addHeader);
        String rs = HttpClientUtil.sendHttpGet(httpGet);
        if(StringUtils.isBlank(rs)){
            throw new BusException("获取对照药品信息-返回结果为空");
        }
        OutResponse<List<DrugVo>> response = JsonUtil.readValue(rs, new TypeReference<OutResponse<List<DrugVo>>>(){});
        return response.isSus()?response.getData():null;
    }


    /**
     * 2108 按药品ID筛选药店
     * http://hcnmindoc.bsoft.com.cn/docs/uop/aypidsxyd
     * @param args
     * @return
     */
    public List<StoreVo> getStoreAndDrugByDrugId(Object... args){
        if(args ==null || !(args[0] instanceof String) || !(args[1] instanceof List)){
            return null;
        }
        //21:西药 22:成药 23:中草药 24:其他
        String drugType = String.valueOf(args[0]);
        //pkPd,quan,quanf
        List<Map<String,Object>> listDrug = (List)args[1];
        if(CollectionUtils.isEmpty(listDrug)){
            return null;
        }
        Map<String,Object> mapData = new HashMap<>();
        mapData.put("longitude","113.39265062963865");
        mapData.put("latitude","22.495654138474816");
        //每次肯定是一种类型的处方提交，所以这里暂放一个
        Map<String,Object> mapPresType = new HashMap<>();
        mapPresType.put("hospitalIdOutter",presOutflowProcessor.getHospitalOutId());
        mapPresType.put("drugType",drugType);

        List<Map<String,Object>> listDetail = new ArrayList<>();
        mapPresType.put("detailInfo",listDetail);
        mapData.put("drugInfo", Lists.newArrayList(mapPresType));

        //构造处方-药品信息
        Set<String> pkPds = listDrug.parallelStream().map(v -> MapUtils.getString(v, "pkPd")).collect(Collectors.toSet());
        List<OutpresDrug> list = DataBaseHelper.queryForList("select drug_id,pk_pd from OUTPRES_DRUG pd where PK_PD in(" + CommonUtils.convertSetToSqlInPart(pkPds, "PK_PD") + ")",OutpresDrug.class);
        Map<String,String> mapPd = list.stream().collect(Collectors.toMap(OutpresDrug::getPkPd, vo -> vo.getDrugId(), (k1, k2) -> k1));
        for (Map<String, Object> map : listDrug) {
            String pkPd = MapUtils.getString(map, "pkPd");
            Map<String,Object> mapDetail = new HashMap<>();
            mapDetail.put("drugId", mapPd.get(pkPd));
            mapDetail.put("medicineCount",MapUtils.getIntValue(map,"quanf",1));//中药剂数	Integer	11	否	中草药：剂数
            mapDetail.put("drugDemand", MapUtils.getIntValue(map,"quan",1));//药品需求量	double	(8,2)	是	西药：外包装数量 中草药：每剂的总用量
            listDetail.add(mapDetail);
        }

        String path = "/open/prescription/recipeOrder/pharmacyList";
        String data = JSON.toJSONString(mapData);
        String rs = HttpClientUtil.sendHttpPost(presOutflowProcessor.getBaseUrl() + path, data, PresOutflowTool.buildHeader(presOutflowProcessor.buildReq(path, data)));
        OutResponse<List<StoreVo>> response = JsonUtil.readValue(rs, new TypeReference<OutResponse<List<StoreVo>>>(){});
        if(!response.isSus()){
            throw new BusException(response.getMsg());
        }
        return response.isSus()?response.getBody():null;
    }


    /**
     * 2104 处方上传
     * http://hcnmindoc.bsoft.com.cn/docs/uop/cfsc
     * @param args
     */
    public List<PresUpResponse> uploadPreInfo(Object... args){
        if(args ==null || !(args[0] instanceof String) || !(args[1] instanceof List)){
            return null;
        }
        String patientName=null,phone=null;
        if(args[2] instanceof Map){
            patientName = MapUtils.getString((Map) args[2],"patientName");
            phone = MapUtils.getString((Map) args[2],"phone");
        }
        PresUpRequest request = presOutflowProcessor.buildPresUpRequest(String.valueOf(args[0]), (List<String>) args[1]);
        if(StringUtils.isNotBlank(patientName)){
            request.getVisitInfo().setPatientName(patientName);
        }
        if(StringUtils.isNotBlank(phone)){
            request.getVisitInfo().setPatientPhone(phone);
        }
        String path = "/open/prescription/recipe/upload";
        String data = JSON.toJSONString(request);
        String rs = HttpClientUtil.sendHttpPost(presOutflowProcessor.getBaseUrl() + path, data, PresOutflowTool.buildHeader((presOutflowProcessor.buildReq(path, data))));
        OutResponse<List<PresUpResponse>> response = JSONObject.parseObject(rs, OutResponse.class);
        if(!response.isSus()){
            throw new BusException(response.getMsg());
        }
        return JSONObject.parseArray(JSONObject.toJSONString(response.getBody()),  PresUpResponse.class);
    }

    /**
     * 2106 处方撤回
     * http://hcnmindoc.bsoft.com.cn/docs/uop/cfch
     * @param args
     */
    public void cancelUploadPres(Object... args){
        String path = "/open/prescription/recipe/revoke";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("recipeIdOutter",String.valueOf(args[0]));//互联网医院的处方id	对应我们的 处方号
        jsonObject.put("operateDt", FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String data = jsonObject.toJSONString();
        String rs = HttpClientUtil.sendHttpPost(presOutflowProcessor.getBaseUrl() + path, data, PresOutflowTool.buildHeader((presOutflowProcessor.buildReq(path, data))));
        OutResponse<String> response = JsonUtil.readValue(rs, new TypeReference<OutResponse<String>>(){});
        if(!response.isSus()) {
            throw new BusException("外购处方撤回失败");
        }
    }

    /**
     * 2119 院内缴费信息更新
     * http://hcnmindoc.bsoft.com.cn/docs/uop/payInfoUpdate
     * @param args
     */
    public void updatePayInfo(Object... args){
        String path = "/open/prescription/recipe/orgPayInfoUpdate";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("visitIdOutter",String.valueOf(args[0]));//codePv
        jsonObject.put("visitAmt", args[1]);//诊查费？-金额
        jsonObject.put("bizType", 1);
        jsonObject.put("payStatus",1);//0=未支付，1=已支付/无需支付
        jsonObject.put("payTime", FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String data = jsonObject.toJSONString();
        String rs = HttpClientUtil.sendHttpPost(presOutflowProcessor.getBaseUrl() + path, data, PresOutflowTool.buildHeader((presOutflowProcessor.buildReq(path, data))));
        OutResponse<CommonResponse> response = JsonUtil.readValue(rs, new TypeReference<OutResponse<CommonResponse>>(){});
        if(!response.isSus() || response.getBody()==null || !"success".equalsIgnoreCase(String.valueOf(response.getBody().getResult()))){
            throw new BusException("更新失败");
        }
    }

}
