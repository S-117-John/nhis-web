package com.zebone.nhis.webservice.cxf.impl;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.webservice.cxf.INHISBaPivasWebService;
import com.zebone.nhis.webservice.service.BaPivasService;
import com.zebone.nhis.webservice.service.LbPivasService;
import com.zebone.nhis.webservice.vo.bapivas.*;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class NHISBaPivasWebServiceImpl implements INHISBaPivasWebService {

    @Resource
    private BaPivasService baPivasService;
    @Override
    public String sendPivasDrug(String param) {
        //TODO 1.接口入参解析
        BaPivasCommonReq<List<BaPivasSendDrugReq>> request= JsonUtil.readValue(param, new TypeReference<BaPivasCommonReq<List<BaPivasSendDrugReq>>>() {});
        BaPivasCommonRes<Object> response=null;
        try {

            //TODO 2.接口参数数据校验 ，发药科室,请领明细主键校验
            if (request == null || request.getData()==null) {
                response=new BaPivasCommonRes("0", "解析请求参数失败，请核对！", "", null);
                return JsonUtil.writeValueAsString(response);
            }
            List<BaPivasSendDrugReq> dataList=request.getData();

            Map<String,Object> paramMap=new HashMap<>();
            for (BaPivasSendDrugReq data: dataList) {
                if(CommonUtils.isNull(data.getApplyNo()) ||CommonUtils.isNull(data.getCodeDept())){
                    response=new BaPivasCommonRes("0", "关键数据[applyNo]或[codeDept]为空，请检查！", "", null);
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
            String result = baPivasService.saveIpPivasDrug(paramMap,dataList.get(0).getAsOpertor());
            if(result.equals("2")){
                response = new BaPivasCommonRes("2", "成功（二次调用）！", request.getReqId(), null);
                return JsonUtil.writeValueAsString(response);
            }
            //TODO 4.根据web发药服务返回接口构建消息返回体
            response=new BaPivasCommonRes("1", "发药成功", request.getReqId(), null);
            return JsonUtil.writeValueAsString(response);
        }catch (Exception ex){
            response=new BaPivasCommonRes("0", ex.getMessage(), request.getReqId(), null);
            return JsonUtil.writeValueAsString(response);
        }
    }

    @Override
    public String sendRetPivasDrug(String param) {
        //TODO 1.接口入参解析
        BaPivasCommonReq<List<BaPivasSendDrugReq>> request= JsonUtil.readValue(param, new TypeReference<BaPivasCommonReq<List<BaPivasSendDrugReq>>>() {});
        BaPivasCommonRes<Object> response=null;
        try {

            //TODO 2.接口参数数据校验 ，退药科室,请领明细主键校验
            if (request == null || request.getData()==null) {
                response=new BaPivasCommonRes("0", "解析请求参数失败，请核对！", "", null);
                return JsonUtil.writeValueAsString(response);
            }
            List<BaPivasSendDrugReq> dataList=request.getData();

            Map<String,Object> paramMap=new HashMap<>();
            for (BaPivasSendDrugReq data: dataList) {
                if (CommonUtils.isNull(data.getApplyNo()) || CommonUtils.isNull(data.getCodeDept())) {
                    response = new BaPivasCommonRes("0", "关键数据[applyNo]或[codeDept]为空，请检查！", "", null);
                    return JsonUtil.writeValueAsString(response);
                }
                Set<String> pkPdapdts = new HashSet<>();
                if (paramMap.containsKey(data.getCodeDept())) {
                    Set<String> param_temp = (Set<String>) paramMap.get(data.getCodeDept());
                    param_temp.add(data.getApplyNo());
                } else {
                    pkPdapdts.add(data.getApplyNo());
                    paramMap.put(data.getCodeDept(), pkPdapdts);
                }
            }

            //TODO 3.调用web退药服务方法
            String result = baPivasService.saveIpRetPivasDrug(paramMap,dataList.get(0).getAsOpertor());
            if(result.equals("2")){
                response=new BaPivasCommonRes("2", "成功（二次调用）！", request.getReqId(), null);
                return JsonUtil.writeValueAsString(response);
            }else if(result.equals("0")){
                response=new BaPivasCommonRes("0", "配置不能申请退费", request.getReqId(), null);
                return JsonUtil.writeValueAsString(response);
            }
            //TODO 4.根据web退药服务返回接口构建消息返回体
            response=new BaPivasCommonRes("1", "退费成功", request.getReqId(), null);
            return JsonUtil.writeValueAsString(response);
        }catch (Exception ex){
            response=new BaPivasCommonRes("0", ex.getMessage(), request.getReqId(), null);
            return JsonUtil.writeValueAsString(response);
        }
    }

    @Override
    public String sendPivasAddCgdt(String param) {
        //TODO 1.接口入参解析
        BaPivasCommonReq<List<BlCgVo>> request= JsonUtil.readValue(param, new TypeReference<BaPivasCommonReq<List<BlCgVo>>>() {});
        BaPivasCommonRes<Object> response=null;
        try {
            //TODO 2.接口参数数据校验
            if (request == null || request.getData()==null) {
                response=new BaPivasCommonRes("0", "解析请求参数失败，请核对！", "", null);
                return JsonUtil.writeValueAsString(response);
            }
            List<BlCgVo> dataList=request.getData();

            baPivasService.chargeIpBatch(dataList);
            //TODO 3.构建消息返回体
            response=new BaPivasCommonRes("1", "记费成功", request.getReqId(), null);
            return JsonUtil.writeValueAsString(response);
        }catch (Exception ex){
            response=new BaPivasCommonRes("0", ex.getMessage(), request.getReqId(), null);
            return JsonUtil.writeValueAsString(response);
        }
    }

    @Override
    public String updatePivasStatus(String param) {
        //TODO 1.接口入参解析
        BaPivasCommonReq<List<BaPivasUpdateVo>> request= JsonUtil.readValue(param, new TypeReference<BaPivasCommonReq<List<BaPivasUpdateVo>>>() {});
        BaPivasCommonRes<Object> response=null;
        try {
            //TODO 2.接口参数数据校验
            if (request == null || request.getData()==null) {
                response=new BaPivasCommonRes("0", "解析请求参数失败，请核对！", "", null);
                return JsonUtil.writeValueAsString(response);
            }
            List<BaPivasUpdateVo> dataList=request.getData();

            baPivasService.updatePivasStatus(dataList);
            //TODO 3.构建消息返回体
            response=new BaPivasCommonRes("1", "修改配置方式成功", request.getReqId(), null);
            return JsonUtil.writeValueAsString(response);
        }catch (Exception ex){
            response=new BaPivasCommonRes("0", ex.getMessage(), request.getReqId(), null);
            return JsonUtil.writeValueAsString(response);
        }
    }
}
