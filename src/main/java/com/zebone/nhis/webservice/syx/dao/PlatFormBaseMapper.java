package com.zebone.nhis.webservice.syx.dao;

import com.zebone.nhis.webservice.syx.vo.platForm.*;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PlatFormBaseMapper {

	public List<DoctorInfo> getDoctorInfo(Map<String, Object> paramMap);

	public List<Map<String, Object>> getRegInfo(Map<String, Object> paramMap);

	public List<TimeRegInfo> getTimeRegInfo(Map<String, Object> paramMap);

	public List<DeptRegInfo> getDeptRegInfo(Map<String, Object> paramMap);

	public List<UserInfo> getUserInfo(Map<String, Object> paramMap);

	/**
	 * 查询患者常用地址
	 * @param userCardType
	 * @param userCardId
	 * @return
	 */
    public List<PiAddrInfo> getPatientAddress(@Param("userCardType") String userCardType, @Param("userCardId") String userCardId);
}
