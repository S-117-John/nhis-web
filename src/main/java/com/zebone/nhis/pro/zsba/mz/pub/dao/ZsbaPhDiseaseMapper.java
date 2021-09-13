package com.zebone.nhis.pro.zsba.mz.pub.dao;

import com.zebone.nhis.common.module.pro.lb.PhDisease;
import com.zebone.nhis.common.module.pro.lb.PhDiseaseSample;
import com.zebone.nhis.pro.zsba.mz.pub.vo.PhDiseaseFoodVo;
import com.zebone.nhis.pro.zsba.mz.pub.vo.PhDiseaseHarm;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ZsbaPhDiseaseMapper {

    List<PhDiseaseSample> qrySample(List<String> pkFoodbornes);
    List<PhDiseaseFoodVo> qryFoodborne(List<String> pkPhdise);
    
    //查询食源性列表数据
    List<Map<String,Object>> qrySearchData(Map<String,Object> paramMap);
    List<Map<String,Object>> qrySearchDataPkPv(Map<String,Object> paramMap);
     /**
      * @Description 查询全国伤害报卡列表
      * @auther wuqiang
      * @Date 2021-04-12
      * @Param [serParam]
      * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
      */
    List<PhDiseaseHarm> getPhDiseaseHaramList(Map<String, Object> serParam);

    List<PhDisease> getPhDiseaseList(Map<String, Object> serParam);

    List<Map<String,Object>> getPhDiseaseHarams(Map<String,Object> paramMap);

}
