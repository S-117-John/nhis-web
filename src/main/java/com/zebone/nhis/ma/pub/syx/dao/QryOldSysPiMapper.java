package com.zebone.nhis.ma.pub.syx.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.ma.pub.syx.vo.OldPiInfo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface QryOldSysPiMapper {

	public List<OldPiInfo> queryPiInfo(Map<String, Object> qryMap);

	public Integer qrypiCount(Map<String, Object> qryMap);

	public PiMaster cancelInhospital(String pkPv);

}
