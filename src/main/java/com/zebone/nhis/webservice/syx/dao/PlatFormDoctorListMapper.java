package com.zebone.nhis.webservice.syx.dao;

import java.util.List;
import com.zebone.nhis.webservice.syx.vo.platForm.DoctorListReq;
import com.zebone.nhis.webservice.syx.vo.platForm.DoctorListRes;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PlatFormDoctorListMapper {

	List<DoctorListRes> getDoctorList(DoctorListReq req);

}
