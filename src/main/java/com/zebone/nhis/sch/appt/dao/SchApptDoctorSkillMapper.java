package com.zebone.nhis.sch.appt.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.sch.appt.vo.CheckApplyVo;
import com.zebone.nhis.sch.plan.vo.SchSchVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SchApptDoctorSkillMapper {

	public CheckApplyVo getCheckApplyByPk(@Param("pk")String pk);

	public List<SchSchVo> getSkillSchInfo(Map<String, String> params);
}
