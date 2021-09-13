package com.zebone.nhis.cn.ipdw.dao;

import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CnEntryCchiMapper {


    //查询CCHI
    public List<Map<String, Object>> queryPvCchi(Map<String, Object> paramMap);

    //返回最大序号
    public List<Integer> getSortno(@Param(value = "pkPv") String pkPv);

    //查询参照 部位、属性、修饰符
    public List<Map<String, Object>> searchDefdoc(Map<String, Object> paramMap);


    //查询科室模板
    public List<Map<String, Object>> searchTemp(Map<String, Object> paramMap);

    //查询医嘱模板
    public List<Map<String, Object>> searchOrd(Map<String, Object> paramMap);
    //数据校验
    public List<Map<String,Object>> check(Map<String, Object> paramMap);
    //查询性别
    public List<Map<String,Object>> sex(Map<String, Object> paramMap);
    //查询登录用户关联Emp
    public List<Map<String,Object>> emp(@Param(value = "pkEmp") String pkEmp);

}
