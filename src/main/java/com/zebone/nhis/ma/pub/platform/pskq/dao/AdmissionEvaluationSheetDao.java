package com.zebone.nhis.ma.pub.platform.pskq.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author 卡卡西
 */
@Mapper
public interface AdmissionEvaluationSheetDao {

    List<Map<String,Object>> admissionEvaluationSheet(String pkPv);

    List<Map<String,Object>> singleRiskAssessment(String pkPv);
}
