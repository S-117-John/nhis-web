package com.zebone.nhis.ma.pub.zsba.support;

import com.zebone.nhis.ma.pub.zsba.dao.BaPresOutflowMapper;
import com.zebone.nhis.ma.pub.zsba.vo.outflow.*;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PresOutflowProcessor {

    @Value("#{applicationProperties['cn.outflow.url']}")
    private String baseUrl;
    @Value("#{applicationProperties['cn.outflow.appId']}")
    private String appId;
    @Value("#{applicationProperties['cn.outflow.hospitalOutId']}")
    private String hospitalOutId;
    @Value("#{applicationProperties['cn.outflow.publicKey']}")
    private String publicKey;
    @Value("#{applicationProperties['cn.outflow.privateKey']}")
    private String privateKey;

    @Resource
    private BaPresOutflowMapper baPresOutflowMapper;

    public VisitInfo getPatient(String pkPv){
        VisitInfo visitInfo=baPresOutflowMapper.getPatient(pkPv);
        visitInfo.setOrgIdOutter(hospitalOutId);
        return visitInfo;
    }

    public List<Diagnoses> getDiagnoses(String pkPv){
        return DataBaseHelper.queryForList("select FLAG_MAJ master_Flag,(case when flag_maj_chn='1' then '02' else '01' end) icd_Type, CODE_ICD icd_Code,NAME_DIAG icd_Name from PV_DIAG where PK_PV=? order by SORT_NO"
                ,Diagnoses.class,new Object[]{pkPv});
    }

    public List<PresInfo> getPresInfo(List<String> listPkPres){
        return baPresOutflowMapper.getPresInfo(listPkPres);
    }

    public List<PresDetail> getPresDetail(String pkPv,List<String> pkPresList){
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("pkPv",pkPv);
        paramMap.put("pkPresList" ,pkPresList);
        return baPresOutflowMapper.getPresDetail(paramMap);
    }

    public PresUpRequest buildPresUpRequest(String pkPv,List<String> pkPresList){
        VisitInfo patient = getPatient(pkPv);
        if(patient == null){
            throw new BusException("未获取到患者信息");
        }
        List<Diagnoses> diagnosesList = getDiagnoses(pkPv);
        if(CollectionUtils.isEmpty(diagnosesList)){
            throw new BusException("未获取到诊断信息");
        }
        List<PresInfo> presInfoList = getPresInfo(pkPresList);
        if(CollectionUtils.isEmpty(presInfoList)){
            throw new BusException("未获取到处方信息");
        }
        List<PresDetail> presDetailList = getPresDetail(pkPv, pkPresList);
        if(CollectionUtils.isEmpty(presDetailList)){
            throw new BusException("未获取到处方明细信息");
        }
        //构造数据
        Map<String, List<PresDetail>> detailGroup = presDetailList.parallelStream().collect(Collectors.groupingBy(PresDetail::getPresNo));
        presInfoList.parallelStream().forEach(vo -> vo.setDetails(detailGroup.get(vo.getRecipeIdOutter())));
        PresUpRequest request = new PresUpRequest();
        patient.setDiagnoses(diagnosesList);
        request.setVisitInfo(patient);
        request.setRecipeInfo(presInfoList);
        return request;
    }

    public SignCheckReq buildReq(String path,String data){
        SignCheckReq signCheckReq = new SignCheckReq();
        signCheckReq.setAppId(appId);
        signCheckReq.setPublicKey(publicKey);
        signCheckReq.setPath(path);
        signCheckReq.setBody(data);
        signCheckReq.setMultipart(false);
        return signCheckReq;
    }


    public String getBaseUrl() {
        return baseUrl;
    }

    public String getHospitalOutId() {
        return hospitalOutId;
    }
}
