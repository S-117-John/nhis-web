package com.zebone.nhis.emr.rec.rec.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.emr.rec.dict.EmrDoctor;
import com.zebone.nhis.common.module.emr.rec.rec.EmrChargeList;
import com.zebone.nhis.common.module.emr.rec.rec.EmrDeptList;
import com.zebone.nhis.common.module.emr.rec.rec.EmrEmpList;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePage;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageBr;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageCharges;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageDiags;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageOps;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageOr;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageOrDt;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageTrans;
import com.zebone.nhis.common.module.emr.rec.rec.EmrLisResult;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedDoc;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedRec;
import com.zebone.nhis.common.module.emr.rec.rec.EmrOpOrdList;
import com.zebone.nhis.common.module.emr.rec.rec.EmrOpsList;
import com.zebone.nhis.common.module.emr.rec.rec.EmrOrdList;
import com.zebone.nhis.common.module.emr.rec.rec.EmrPatDiags;
import com.zebone.nhis.common.module.emr.rec.rec.EmrPatRec;
import com.zebone.nhis.common.module.emr.rec.rec.EmrPatList;
import com.zebone.nhis.common.module.emr.rec.rec.EmrRisResult;
import com.zebone.nhis.common.module.emr.rec.rec.EmrTransList;
import com.zebone.nhis.common.module.emr.rec.tmp.EmrTemplate;
import com.zebone.nhis.common.module.labor.nis.PvLaborRecDt;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.emr.rec.rec.vo.EmrMedDocVo;
import com.zebone.nhis.emr.rec.rec.vo.EmrVitalSigns;
import com.zebone.nhis.emr.rec.rec.vo.PvDiagVo;
import com.zebone.platform.modules.mybatis.Mapper;

import org.apache.ibatis.annotations.Param;

/**
 * 病历书写-检查检验结果查询-mapper
 * @author chengjia
 */
@Mapper
public interface EmrRsltQryMapper{   
    /**
     * 查询检验申请信息
     * @param codePi
     * @return
     */
    public List<EmrLisResult> queryPatLisReqSyx(Map map); 
    
    /**
     * 查询检验结果信息
     * @param smpNos
     * @return
     */
    public List<EmrLisResult> queryPatLisResultSyx(@Param("smpNos")String smpNos); 
    
    /**
     * 查询检验结果信息
     * @param codes
     * @return
     */
    public List<EmrLisResult> queryPatLisResultSyx2(List<String> codes); 
    
    /**
     * 查询检查结果信息
     * @param codePi
     * @return
     */
    public List<EmrRisResult> queryPatRisResultSyx(Map map);    
    
    /**
     * 查询检查结果信息-超声
     * @param codePi
     * @return
     */
    public List<EmrRisResult> queryPatRisSupperResultSyx(Map map);    
    /**
     * 查询检查结果信息-核医学
     * @param codePi
     * @return
     */
    public List<EmrRisResult> queryPatRisResultSyxHyx(Map map);  
    
    /**
     * 查询检查结果信息-病理
     * @param codePi
     * @return
     */
    public List<EmrRisResult> queryPatRisResultSyxPa(Map map);  
    
    /**
     * 查询患者生命体征数据
     * @param codePv
     * @return
     */
    public List<EmrVitalSigns> queryVitalSignsSyx(@Param("codePv")String codePv);  
    
    /**
     * 查询检验申请信息(boai)
     * @param codePi
     * @return
     */
    public List<EmrLisResult> queryPatLisReqBoai(Map map); 
    /**
     * 查询检验申请信息(boai)
     * @param codePi
     * @return
     */
    public List<EmrLisResult> queryPatLisReqBoaiBaby(Map map); 
    /**
     * 查询检验申请信息(boai)
     * @param codePi
     * @return
     */
    public List<EmrLisResult> queryPatLisReqByObjBoai(Map map); 
    
    /**
     * 查询检查结果信息(boai)
     * @param codePi
     * @return
     */
    public List<EmrRisResult> queryPatRisResultBoai(Map map);   
    /**
     * 查询检查结果信息(心电)(boai)
     * @param codePi
     * @return
     */
    public List<EmrRisResult> queryPatRisnlResultBoai(Map map);   
    /**
     * 查询检查结果信息(boai)
     * @param codePi
     * @return
     */
    public List<EmrRisResult> queryPatRisResultBoaiHomePage(Map map);   
    /**
     * 查询第三方医嘱信息(boai)
     * @param pkPv
     * @return
     */
    public List<EmrOrdList> queryPatOrdResultThird(Map map);
    /**
     * 查询第三方手麻信息(boai)
     * @param map
     * @return
     */
    public List<Map<String, Object>> queryPatOpsResultThird(Map map);
    /**
     * 查询第三方手麻信息(boai)
     * @param map
     * @return
     */
    public List<Map<String, Object>> queryBloodList(Map map);
}
