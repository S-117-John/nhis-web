package com.zebone.nhis.webservice.syx.dao;

import java.util.List;
import java.util.Map;
import com.zebone.nhis.webservice.syx.vo.platForm.CreateACardReq;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PlatFormCreateACardMapper {

	List<Map<String, Object>> createACard(CreateACardReq req);

}
