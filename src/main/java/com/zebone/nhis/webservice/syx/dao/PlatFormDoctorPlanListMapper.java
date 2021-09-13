package com.zebone.nhis.webservice.syx.dao;

import java.util.List;
import com.zebone.nhis.webservice.syx.vo.platForm.DoctorPlanListReq;
import com.zebone.nhis.webservice.syx.vo.platForm.DoctorPlanListRes;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PlatFormDoctorPlanListMapper {

	List<DoctorPlanListRes> getDoctorPlanList(DoctorPlanListReq req);

}
