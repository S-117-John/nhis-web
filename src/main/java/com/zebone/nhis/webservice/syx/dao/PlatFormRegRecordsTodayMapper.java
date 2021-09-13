package com.zebone.nhis.webservice.syx.dao;

import java.util.List;

import com.zebone.nhis.webservice.syx.vo.platForm.RegRecordsTodayReq;
import com.zebone.nhis.webservice.syx.vo.platForm.RegRecordsTodayRes;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PlatFormRegRecordsTodayMapper {

	List<RegRecordsTodayRes> getRegRecordsToday(RegRecordsTodayReq req);

}
