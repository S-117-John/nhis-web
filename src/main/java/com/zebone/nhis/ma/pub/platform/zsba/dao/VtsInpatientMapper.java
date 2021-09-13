package com.zebone.nhis.ma.pub.platform.zsba.dao;


import java.util.List;
import java.util.Map;

import com.zebone.nhis.ma.pub.platform.zsba.vo.Employee;
import com.zebone.nhis.ma.pub.platform.zsba.vo.Inpatient;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface VtsInpatientMapper {
    /**
     * 查询患者信息
     * @param paramMap
     * @return
     */
    List<Inpatient> selectInpatients(Map<String,Object> paramMap);

    /**
     * 获取护士信息
     * @param paramMap
     * @return
     */
    List<Employee> selectEmployees(Map<String,Object> paramMap);
}