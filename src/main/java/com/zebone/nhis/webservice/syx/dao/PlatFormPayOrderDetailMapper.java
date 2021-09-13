package com.zebone.nhis.webservice.syx.dao;

import java.util.List;
import java.util.Map;
import com.zebone.nhis.webservice.syx.vo.platForm.PayOrderDetailReq;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PlatFormPayOrderDetailMapper {

	List<Map<String, Object>> getPayOrderDetail(PayOrderDetailReq req);

}
