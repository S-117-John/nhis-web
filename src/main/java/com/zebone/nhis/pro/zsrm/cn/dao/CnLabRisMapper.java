package com.zebone.nhis.pro.zsrm.cn.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CnLabRisMapper {
    //查询门诊全院检查项目树
    List<Map<String, Object>> queryRisLabTreeList(Map<String,Object>map);

    //获取当前用户的常用检治项目
    List<Map<String,Object>> qryPreferOrders(Map<String,Object>map);
}
