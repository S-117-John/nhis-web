package com.zebone.nhis.webservice.pskq.dao;


import com.zebone.nhis.webservice.pskq.model.Organization;
import com.zebone.nhis.webservice.pskq.model.PatientInfo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PskqWebserviceOrganizationDao {

    Organization findOrgInfoByOrgCode(String id);
}
