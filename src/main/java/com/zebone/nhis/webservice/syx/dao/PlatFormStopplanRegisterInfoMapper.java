package com.zebone.nhis.webservice.syx.dao;

import java.util.List;
import com.zebone.nhis.webservice.syx.vo.platForm.StopplanRegisterInfoReq;
import com.zebone.nhis.webservice.syx.vo.platForm.StopplanRegisterInfoRes;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PlatFormStopplanRegisterInfoMapper {

	List<StopplanRegisterInfoRes> getstopplanRegisterInfo(StopplanRegisterInfoReq req);

}
