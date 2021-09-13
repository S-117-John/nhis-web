package com.zebone.nhis.cn.ipdw.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.cn.cp.BdCpAction;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CpActionMapper {
	public List<BdCpAction> getDictAction_1(String pkAction);
	public List<BdCpAction> getDictAction(Map<String,Object> param);
	public void saveDictAction(BdCpAction cpaction);
}
