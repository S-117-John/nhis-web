package com.zebone.nhis.pro.zsba.bl.dao;

import com.zebone.nhis.pro.zsba.bl.vo.DrugPresPdUsecateVo;
import com.zebone.nhis.pro.zsba.bl.vo.WinoConfVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ZsbaWinnoDrugMapper {

    List<DrugPresPdUsecateVo> getPresPdUsecate(List<String> pkPresList);

    List<WinoConfVo> getWino(Map<String,Object> paraMap);
}
