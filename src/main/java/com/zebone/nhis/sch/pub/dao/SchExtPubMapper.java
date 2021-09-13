package com.zebone.nhis.sch.pub.dao;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.sch.pub.vo.SchForExtVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SchExtPubMapper {
	/**
	 * 查询门诊临床科室列表
	 * @param paramMap{pkOrg}
	 * @return
	 */
	public List<Map<String,Object>> queryDeptList(Map<String,Object> paramMap);
	/**
	 * 根据科室查询医生列表
	 * @param paramMap{codeDept}
	 * @return
	 */
	public List<Map<String,Object>> queryDoctorList(Map<String,Object> paramMap);
	/**
	 * 查询排班信息
	 * @param params
	 * @return
	 */
	public List<SchForExtVo> getSchInfo(Map<String,String> params);
	
	/**
	 * 查询排版信息(微信)
	 * @param params
	 * @return
	 */
    public List<Map<String,Object>> getSchInfoForSelf(Map<String,String> params);
    
    /**
     * 查询患者未就诊预约挂号信息(自助机)
     * @param params
     * @return
     */
    public List<Map<String,Object>> getPiAppointment(Map<String,String> params);
}
