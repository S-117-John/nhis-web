package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.dao;

import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.pv.PvStaff;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IpNurseOperationMapper {

    List<PvStaff> qryPvStaff(String pkPv);

    List<Map<String,Object>> qryPvInfo(String pkPv);

    List<Map<String, Object>> qryChangeDeptInfo(Map<String, Object> map);

    List<BdOuDept> qryBedInfo(Map<String, Object> paramMap);
}
