package com.zebone.nhis.cn.ipdw.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;
import com.zebone.nhis.common.module.cn.cp.BdCpExp;

@Mapper
public interface CpExpMapper {
	public List<BdCpExp> getDictCpExp(Map<String,Object> param);
	public void saveDictCpExp(BdCpExp cpexp);
}
