package com.zebone.nhis.webservice.syx.dao;

import java.util.List;
import com.zebone.nhis.webservice.syx.vo.platForm.PayInfoReq;
import com.zebone.nhis.webservice.syx.vo.platForm.PayInfoRes;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PlatFormPayInfoMapper {

	List<PayInfoRes> getPayInfo(PayInfoReq req);

}
