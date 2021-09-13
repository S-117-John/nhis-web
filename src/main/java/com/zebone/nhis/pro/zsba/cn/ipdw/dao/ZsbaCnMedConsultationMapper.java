package com.zebone.nhis.pro.zsba.cn.ipdw.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Classname ZsbaCnMedConsultationMapper
 * @Description TODO
 * @Date 2021-01-19 10:06
 * @Created by wuqiang
 */
@Mapper
public interface ZsbaCnMedConsultationMapper {

    List<Map<String, Object>>  getCnOrdAntiExpre();

    List<Map<String, Object>>  getCnOrdAntiApply(Map<String, Object> paramMap);
}
