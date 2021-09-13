package com.zebone.nhis.cn.ipdw.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.cn.ipdw.SchIpDt;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CnIpScheduleMapper {
	
	//住院医生排班预览
	List<Map<String, Object>> qrySchPreviewList(@Param("pkDept")String pkDept,@Param("monthSch")String monthSch);
	
	//查询科室下的医生列表
	List<Map<String, Object>> qryDoctorList(@Param("date")String date,@Param("pkDept")String pkDept);
	
	//查询复制排班的年份
	List<Map<String, Object>> qrySchCopyYear(@Param("pkDept")String pkDept);
	
	//查询复制排班的月份
	List<Map<String, Object>> qrySchCopyMonth(@Param("pkDept")String pkDept,@Param("yearSch")String yearSch);
	
	//查询复制排班的人员
	List<Map<String, Object>> qrySchCopyEmployee(@Param("pkDept")String pkDept,@Param("monthSch")String monthSch);

	//查询排班复制的列表
	List<SchIpDt> qrySchCopyList(Map<String, Object> map);

	//根据日期查询排班记录
	SchIpDt qrySchIpDtByDate(@Param("dateBegin")String dateBegin,@Param("dateEnd")String dateEnd);

}
