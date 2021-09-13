package com.zebone.nhis.pro.sd.cn.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

/***
 *
 */
@Mapper
public interface CnPaitQryMapper {
    //检查检验列表
    List<Map<String,Object>> qryLabRis(Map<String,Object> paramMap);
    //检查信息
    List<Map<String,Object>> qryLab(Map<String,Object> paramMap);
    //检验信息
    List<Map<String,Object>> qryRis(Map<String,Object> paramMap);
    //微生物信息
    List<Map<String,Object>> qryBact(Map<String,Object> paramMap);
    //入院通知单
    List<Map<String,Object>> qryAdmNotice(Map<String,Object> paramMap);
    //会诊申请信息--列表
    List<Map<String,Object>> qryConsultApp(Map<String,Object> paramMap);
    //申请单详细信息
    List<Map<String,Object>> qryConsultAppDetailed(Map<String,Object> paramMap);
    //受邀科室信息
    List<Map<String,Object>> qryBeInvited(Map<String,Object> paramMap);
    //会诊应答列表
    List<Map<String,Object>> qryConsultAnswer(Map<String,Object> paramMap);
    //应答明细
    List<Map<String,Object>> qryResponseDetails(Map<String,Object> paramMap);

}
