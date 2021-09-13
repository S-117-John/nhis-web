package com.zebone.nhis.compay.ins.lb.dao.nhyb;

import java.util.Map;

import com.zebone.nhis.common.module.compay.ins.lb.nhyb.InsSuzhounhydPi;
import com.zebone.nhis.common.module.compay.ins.lb.nhyb.InsSuzhounhydReginfo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface InsSuZhouNHYDMapper {

	//查询医保登记信息
	public InsSuzhounhydReginfo queryYbRegitInfo(Map<String, Object> paramMap);

	//查询患者信息
	public InsSuzhounhydPi queryPatiInfo(Map<String, Object> paramMap);

}
