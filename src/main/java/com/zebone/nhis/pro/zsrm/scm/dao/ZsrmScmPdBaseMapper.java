package com.zebone.nhis.pro.zsrm.scm.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.Map;

@Mapper
public interface ZsrmScmPdBaseMapper {

    /**
     * 获取药品字典中某字段得最大值
     * @param paramMap
     * @return
     */
    public Map<String,Object> getBdPdCodeMax(Map<String,Object> paramMap);
}
