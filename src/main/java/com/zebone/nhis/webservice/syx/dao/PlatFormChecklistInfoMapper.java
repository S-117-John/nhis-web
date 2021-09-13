package com.zebone.nhis.webservice.syx.dao;

import java.util.List;

import com.zebone.nhis.webservice.syx.vo.platForm.ChecklistInfoReq;
import com.zebone.nhis.webservice.syx.vo.platForm.ChecklistInfoRes;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PlatFormChecklistInfoMapper {

	List<ChecklistInfoRes> getchecklistInfo(ChecklistInfoReq req);

}
