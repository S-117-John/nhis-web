package com.zebone.nhis.pro.zsba.hisser.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Classname BaPubMapper
 * @Description 博爱项目个性化数据库层
 * @Date 2020-05-08 10:30
 * @Created by wuqiang
 */

@Mapper
public interface BaPubMapper {


    List<Map<String, Object>> qryBloodLis(Map<String, Object> params);
}
