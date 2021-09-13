package com.zebone.nhis.webservice.pskq.dao;


import com.zebone.nhis.webservice.pskq.model.OpSettlement;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.Map;

@Mapper
public interface PskqOpSettleDao {

    OpSettlement getListOpSettlement(Map<String,Object> map);

}
