package com.zebone.nhis.webservice.pskq.dao;

import com.zebone.nhis.webservice.pskq.model.LabApply;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface LabApplyDao {

    int updateLabApply(LabApply labApply);
}
