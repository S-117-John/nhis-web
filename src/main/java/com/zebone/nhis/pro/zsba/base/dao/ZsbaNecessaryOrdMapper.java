package com.zebone.nhis.pro.zsba.base.dao;

import com.zebone.nhis.pro.zsba.base.vo.DeptNecOrdDtVo;
import com.zebone.nhis.pro.zsba.base.vo.DeptNecOrdGroupVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ZsbaNecessaryOrdMapper {

    List<DeptNecOrdGroupVo> getDeptNecOrdGroupInfo(Map<String, Object> param);

    List<DeptNecOrdDtVo> getDeptNecOrdDtInfo(Map<String, Object> param);
}
