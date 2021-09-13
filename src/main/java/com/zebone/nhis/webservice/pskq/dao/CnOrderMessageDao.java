package com.zebone.nhis.webservice.pskq.dao;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CnOrderMessageDao {

    List<String> findByIdIn(List<String> list);
}