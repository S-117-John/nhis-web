package com.zebone.nhis.pro.zsba.death.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ZsbaDeathCertificateMapper {

/***
 * @Description 查询死亡登记患者信息
 * @auther wuqiang
 * @Date 2020-08-18
 * @Param [pkPi]患者主键
 * @return java.util.Map<java.lang.String,java.lang.Object>
 */
 Map<String, Object> getPiInformation(String pkPi);
/**
 * @Description 查询角色下所有用户
 * @auther wuqiang
 * @Date 2020-08-19
 * @Param [pkRole]
 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
 */
    List<Map<String, Object>> GetUserByPkRole(String pkRole);
}
