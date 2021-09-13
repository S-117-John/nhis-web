package com.zebone.nhis.ma.pub.zsrm.dao;

import com.zebone.nhis.ma.pub.zsrm.vo.AtfYpxxDetailVo;
import com.zebone.nhis.scm.pub.vo.PdDeDrugVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

/**
 *
 **/
@Mapper
public interface AtfYpxxDetailMapper {
    List<AtfYpxxDetailVo> getPackagePlant(PdDeDrugVo pdDeDrugVo);
    List<Map<String,Object>> getSumQuanOcc(PdDeDrugVo pdDeDrugVo);
    List<PdDeDrugVo> getPdDeDrugVo(String codeDe);
}
