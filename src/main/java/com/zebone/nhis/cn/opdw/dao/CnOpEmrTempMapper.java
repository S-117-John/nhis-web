package com.zebone.nhis.cn.opdw.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.cn.opdw.vo.bdOpEmrVo;
import com.zebone.nhis.common.module.cn.opdw.BdOpEmrTemp;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CnOpEmrTempMapper {

	public List<bdOpEmrVo> queryListNow(Map<String, String> params);
}
