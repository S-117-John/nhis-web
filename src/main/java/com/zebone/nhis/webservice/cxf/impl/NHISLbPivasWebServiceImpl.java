package com.zebone.nhis.webservice.cxf.impl;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.webservice.cxf.INHISLbPivasWebService;
import com.zebone.nhis.webservice.service.LbPivasService;
import com.zebone.nhis.webservice.vo.lbpivas.BlCgVo;
import com.zebone.nhis.webservice.vo.lbpivas.LbPivasCommonReq;
import com.zebone.nhis.webservice.vo.lbpivas.LbPivasCommonRes;
import com.zebone.nhis.webservice.vo.lbpivas.LbPivasSendDrugReq;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 灵璧静配web服务实现接口
 */
@Service
public class NHISLbPivasWebServiceImpl implements INHISLbPivasWebService {

    @Resource
    private LbPivasService lbPivasService;

    @Override
    public String sendPivasDrug(String param) {
        //TODO 1.接口入参解析
        LbPivasCommonReq<List<LbPivasSendDrugReq>> request= JsonUtil.readValue(param, new TypeReference<LbPivasCommonReq<List<LbPivasSendDrugReq>>>() {});
        LbPivasCommonRes<Object> response=null;
        try {

            //TODO 2.接口参数数据校验 ，发药科室,请领明细主键校验
            if (request == null || request.getData()==null) {
                response=new LbPivasCommonRes("0", "解析请求参数失败，请核对！", "", null);
                return JsonUtil.writeValueAsString(response);
            }
            List<LbPivasSendDrugReq> dataList=request.getData();

            Map<String,Object> paramMap=new HashMap<>();
            for (LbPivasSendDrugReq data: dataList) {
                if(CommonUtils.isNull(data.getApplyNo()) ||CommonUtils.isNull(data.getCodeDept())){
                    response=new LbPivasCommonRes("0", "关键数据[applyNo]或[codeDept]为空，请检查！", "", null);
                    return JsonUtil.writeValueAsString(response);
                }
                List<String> pkPdapdts=new ArrayList<>();
                if(paramMap.containsKey(data.getCodeDept())){
                    List<String> param_temp= (List<String>) paramMap.get(data.getCodeDept());
                    param_temp.add(data.getApplyNo());
                }else{
                    pkPdapdts.add(data.getApplyNo());
                    paramMap.put(data.getCodeDept(),pkPdapdts);
                }
            }

            //TODO 3.调用web发药服务方法
            lbPivasService.saveIpPivasDrug(paramMap,dataList.get(0).getAsOpertor());
            //TODO 4.根据web发药服务返回接口构建消息返回体
            response=new LbPivasCommonRes("1", "发药成功", request.getReqId(), null);
            return JsonUtil.writeValueAsString(response);
        }catch (Exception ex){
            response=new LbPivasCommonRes("0", ex.getMessage(), request.getReqId(), null);
            return JsonUtil.writeValueAsString(response);
        }
    }

    @Override
    public String sendRetPivasDrug(String param) {
        //TODO 1.接口入参解析
        LbPivasCommonReq<List<LbPivasSendDrugReq>> request= JsonUtil.readValue(param, new TypeReference<LbPivasCommonReq<List<LbPivasSendDrugReq>>>() {});
        LbPivasCommonRes<Object> response=null;
        try {

            //TODO 2.接口参数数据校验 ，退药科室,请领明细主键校验
            if (request == null || request.getData()==null) {
                response=new LbPivasCommonRes("0", "解析请求参数失败，请核对！", "", null);
                return JsonUtil.writeValueAsString(response);
            }
            List<LbPivasSendDrugReq> dataList=request.getData();

            Map<String,Object> paramMap=new HashMap<>();
            for (LbPivasSendDrugReq data: dataList) {
                if(CommonUtils.isNull(data.getApplyNo()) ||CommonUtils.isNull(data.getCodeDept())){
                    response=new LbPivasCommonRes("0", "关键数据[applyNo]或[codeDept]为空，请检查！", "", null);
                    return JsonUtil.writeValueAsString(response);
                }
                Set<String> pkPdapdts=new HashSet<>();
                if(paramMap.containsKey(data.getCodeDept())){
                    Set<String> param_temp= (Set<String>) paramMap.get(data.getCodeDept());
                    param_temp.add(data.getApplyNo());
                }else{
                    pkPdapdts.add(data.getApplyNo());
                    paramMap.put(data.getCodeDept(),pkPdapdts);
                }
            }

            //TODO 3.调用web退药服务方法
            lbPivasService.saveIpRetPivasDrug(paramMap,dataList.get(0).getAsOpertor());
            //TODO 4.根据web退药服务返回接口构建消息返回体
            response=new LbPivasCommonRes("1", "退药成功", request.getReqId(), null);
            return JsonUtil.writeValueAsString(response);
        }catch (Exception ex){
            response=new LbPivasCommonRes("0", ex.getMessage(), request.getReqId(), null);
            return JsonUtil.writeValueAsString(response);
        }
    }

    @Override
    public String sendPivasAddCgdt(String param) {
        //TODO 1.接口入参解析
        LbPivasCommonReq<List<BlCgVo>> request= JsonUtil.readValue(param, new TypeReference<LbPivasCommonReq<List<BlCgVo>>>() {});
        LbPivasCommonRes<Object> response=null;
        try {
            //TODO 2.接口参数数据校验
            if (request == null || request.getData()==null) {
                response=new LbPivasCommonRes("0", "解析请求参数失败，请核对！", "", null);
                return JsonUtil.writeValueAsString(response);
            }
            List<BlCgVo> dataList=request.getData();

            lbPivasService.chargeIpBatch(dataList);
            //TODO 3.构建消息返回体
            response=new LbPivasCommonRes("1", "退药成功", request.getReqId(), null);
            return JsonUtil.writeValueAsString(response);
        }catch (Exception ex){
            response=new LbPivasCommonRes("0", ex.getMessage(), request.getReqId(), null);
            return JsonUtil.writeValueAsString(response);
        }
    }
}
