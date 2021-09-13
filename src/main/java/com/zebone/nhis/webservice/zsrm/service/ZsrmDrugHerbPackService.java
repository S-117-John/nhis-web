package com.zebone.nhis.webservice.zsrm.service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.zsrm.dao.ZsrmOpHerbPackMapper;
import com.zebone.nhis.webservice.zsrm.vo.pack.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
public class ZsrmDrugHerbPackService {

    @Resource
    private ZsrmOpHerbPackMapper herbPackMapper;

    /**
     * 获取处方
     * @param param
     * @return
     */
    public String getPresHerbInfo(String param){
        String errMsg="<?xml version=\"1.0\" encoding=\"utf-8\" ? ><root><msg>%s</msg></root>";
        try {
            if(CommonUtils.isNull(param)){
                return String.format(errMsg, "未获取请求参数！");
            }
            ZsrmHerbPresRequest presRoot= (ZsrmHerbPresRequest) XmlUtil.XmlToBean(param,ZsrmHerbPresRequest.class);
            if(presRoot==null ||CommonUtils.isNull(presRoot.getDateStart())||CommonUtils.isNull(presRoot.getDateEnd())){
                return String.format(errMsg, "请求参数转化失败！");
            }

            Map<String,Object> paramMap=new HashMap<>();
            paramMap.put("dateStart",presRoot.getDateStart());
            paramMap.put("dateEnd",presRoot.getDateEnd());
            List<ZsrmHerbPresVo> herbPresVoList=herbPackMapper.qryHerbPresInfo(paramMap);
            if(herbPresVoList==null || herbPresVoList.size()==0){
                return String.format(errMsg, "未查询到草药处方相关信息！");
            }
            ZsrmHerbPresRoot root=new ZsrmHerbPresRoot();
            root.setPresVoList(herbPresVoList);
            String resxml=XmlUtil.beanToXml(root,ZsrmHerbPresRoot.class);
            return resxml;
        }catch (Exception e){
            e.printStackTrace();
            return String.format(errMsg, e.getMessage());
        }
    }

    /**
     * 获取处方明细数据
     * @param param
     * @return
     */
    public String getPresHerbDtInfo(String param){
        String errMsg="<?xml version=\"1.0\" encoding=\"utf-8\" ? ><root><msg>%s</msg></root>";
        try {
            if(CommonUtils.isNull(param)){
                return String.format(errMsg, "未获取请求参数！");
            }
            ZsrmHerbPresRequest presRoot= (ZsrmHerbPresRequest) XmlUtil.XmlToBean(param,ZsrmHerbPresRequest.class);
            if(presRoot==null ||CommonUtils.isNull(presRoot.getPresNo())){
                return String.format(errMsg, "请求参数转化失败！");
            }
            List<String> presList= Arrays.asList(presRoot.getPresNo().split(","));
            if(presList==null || presList.size()==0){
                return String.format(errMsg, "未获取处方号！");
            }
            List<ZsrmHerbPresDtVo> herbPresVoDtList=herbPackMapper.qryHerbPresDtInfo(presList);
            if(herbPresVoDtList==null || herbPresVoDtList.size()==0){
                return String.format(errMsg, "未查询到草药处方明细相关信息！");
            }
            ZsrmHerbPresDtRoot root=new ZsrmHerbPresDtRoot();
            root.setPresDtVoList(herbPresVoDtList);
            String resxml=XmlUtil.beanToXml(root,ZsrmHerbPresDtRoot.class);
            return resxml;
        }catch (Exception e){
            return String.format(errMsg, e.getMessage());
        }

    }
}
