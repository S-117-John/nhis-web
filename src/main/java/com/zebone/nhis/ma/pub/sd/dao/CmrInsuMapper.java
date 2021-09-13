package com.zebone.nhis.ma.pub.sd.dao;

import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybDictMap;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybStCity;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybStDiff;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsTkybSt;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvIp;
import com.zebone.nhis.ma.pub.sd.vo.cmrInsu.CiIcdVo;
import com.zebone.nhis.ma.pub.sd.vo.cmrInsu.CiInvoiceFee;
import com.zebone.nhis.ma.pub.sd.vo.cmrInsu.CiMedFeeVo;
import com.zebone.nhis.ma.pub.sd.vo.cmrInsu.CiUploadDtl;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CmrInsuMapper {

    /**查询患者基本信息*/
    List<PiMaster> getPimasterInfo(Map<String,Object> paramMap);

    /**获取对照信息*/
    List<InsSzybDictMap> getDictMapInfo(Map<String,Object> paramMap);

    /**查询患者就诊信息*/
    List<Map<String,Object>> qryPvInfo(Map<String,Object> paramMap);

    /**查询患者诊断信息*/
    List<CiIcdVo> qryDiagInfo(Map<String,Object> paramMap);

    /**查询患者诊断信息*/
    List<CiIcdVo> qrycnDiagInfo(Map<String,Object> paramMap);

    /**查询上传商保费用明细*/
    List<CiUploadDtl> qryUpLoadDtList(Map<String,Object> paramMap);

    /**查询患者就诊信息*/
    PvEncounter qryPvinfo(Map<String,Object> paramMap);

    /**根据pkPv查询异地医保患者结算数据*/
    InsSzybStDiff qryStdiffByPkPv(Map<String,Object> paramMap);

    /**根据pkPv查询医保患者结算数据*/
    InsSzybStCity qryStcityByPkPv(Map<String,Object> paramMap);

    /**查询发票信息费用*/
    List<CiInvoiceFee> qryInvDtList(Map<String,Object> paramMap);

    /**查询商保结算信息*/
    InsTkybSt qryTkStInfo(Map<String,Object> paramMap);

    /**根据pkpv查询医保信息*/
    Map<String,Object> qryHpInfoByPkpv(Map<String,Object> paramMap);

    /**根据pkpv查询住院属性信息*/
    PvIp qryPvIpinfo(Map<String,Object> paramMap);

    /**获取病历上传所需数据*/
    List<Map<String, Object>> qryPvMedicalRecord(Map<String, Object> paramMap);

    /**查询深圳医保报销明细集合*/
    List<CiMedFeeVo> qryMedFeeList(Map<String, Object> paramMap);
}
