package com.zebone.nhis.sch.appt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.sch.appt.vo.InquireChargesVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface InquireChargesVoMapper {

	
	List<InquireChargesVo> getInquireChargesVo(String spcode);
	

}
