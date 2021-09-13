package com.zebone.nhis.webservice.pskq.dao;


import com.zebone.nhis.webservice.pskq.model.PatientInfo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PskqWebservicePatientDao {

    PatientInfo findByEmpiId(String id);
}
