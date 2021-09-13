package com.zebone.nhis.webservice.syx.dao;

import java.util.List;

import com.zebone.nhis.webservice.syx.vo.platForm.StopRegReq;
import com.zebone.nhis.webservice.syx.vo.platForm.StopRegRes;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PlatFormStopRegMapper {

	List<StopRegRes> stopReg(StopRegReq stopRegReq);

}
