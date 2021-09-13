package com.zebone.nhis.webservice.pskq.dao;


import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.platform.modules.mybatis.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper()
@Component(value = "pskqSysMsgRecDao")
public interface SysMsgRecDao {

    List<SysMsgRec> selectMessage(Map<String,Object> param);
}