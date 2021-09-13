package com.zebone.nhis.webservice.pskq.dao;

import com.zebone.nhis.webservice.pskq.model.LabResult;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;

@Mapper
public interface ExLabOccMessageMapper {

    List<LabResult> findByCodeApply(String codeApply);
}
