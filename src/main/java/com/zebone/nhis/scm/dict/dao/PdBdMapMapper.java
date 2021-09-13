package com.zebone.nhis.scm.dict.dao;

import com.zebone.nhis.scm.dict.vo.BdPdMapVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;

@Mapper
public interface PdBdMapMapper {
    public List<BdPdMapVo> qryHerbPdRateList();
}
