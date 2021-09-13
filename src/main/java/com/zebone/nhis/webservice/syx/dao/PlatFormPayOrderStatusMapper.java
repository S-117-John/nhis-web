package com.zebone.nhis.webservice.syx.dao;

import com.zebone.nhis.webservice.syx.vo.platForm.PayOrderStatusReq;
import com.zebone.nhis.webservice.syx.vo.platForm.PayOrderStatusRes;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PlatFormPayOrderStatusMapper {

	PayOrderStatusRes getPayOrderStatus(PayOrderStatusReq req);

}
