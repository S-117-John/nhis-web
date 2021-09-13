package com.zebone.nhis.webservice.syx.dao;

import java.util.List;

import com.zebone.nhis.webservice.syx.vo.platForm.PayDetailInfoReq;
import com.zebone.nhis.webservice.syx.vo.platForm.PayDetailInfoRes;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PlatFormPayDetailInfoMapper {

	List<PayDetailInfoRes> getPayDetailInfo(PayDetailInfoReq req);

}
