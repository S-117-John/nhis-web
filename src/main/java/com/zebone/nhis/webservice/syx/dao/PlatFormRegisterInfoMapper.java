package com.zebone.nhis.webservice.syx.dao;

import java.util.List;
import com.zebone.nhis.webservice.syx.vo.platForm.RegisterInfoReq;
import com.zebone.nhis.webservice.syx.vo.platForm.RegisterInfoRes;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PlatFormRegisterInfoMapper {

	List<RegisterInfoRes> getRegisterInfo(RegisterInfoReq req);

}
