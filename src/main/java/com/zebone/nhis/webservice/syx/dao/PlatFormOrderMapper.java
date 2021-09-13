package com.zebone.nhis.webservice.syx.dao;

import com.zebone.nhis.common.module.sch.plan.SchTicket;
import com.zebone.nhis.webservice.syx.vo.platForm.DeptInfo;
import com.zebone.nhis.webservice.syx.vo.platForm.DistrictInfo;
import com.zebone.nhis.webservice.syx.vo.platForm.RegListInfo;
import com.zebone.nhis.webservice.syx.vo.platForm.RegLockVo;
import com.zebone.platform.modules.mybatis.Mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PlatFormOrderMapper {

    public List<DistrictInfo> getAreaInfo(String hospitalName);

    public Integer qryDateLot(Map<String, Object> dateParm);

    public List<SchTicket> qryIsApp(Map<String, Object> qryParm);
    
    public List<DeptInfo> getDeptInfo(Map<String, Object> paramMap);

    public List<SchTicket> qryTicket(Map<String, Object> qryParm);

    public Map<String, Object> qryApptInfo(@Param("orderType") String orderType, @Param("orderId") String orderId);

    public List<SchTicket> qryRegPam(RegLockVo regLockVo);

	public List<RegListInfo> getRegListInfo(Map<String, Object> paramMap);
}
