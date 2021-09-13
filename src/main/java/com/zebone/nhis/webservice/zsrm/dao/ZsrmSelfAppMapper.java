package com.zebone.nhis.webservice.zsrm.dao;

import com.zebone.nhis.webservice.zsrm.vo.self.ResponseQryOutpfeeDetailVo;
import com.zebone.nhis.webservice.zsrm.vo.self.ResponseQryOutpfeeMasterVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

@Mapper
public interface ZsrmSelfAppMapper {

    /**
     * 查询就诊信息
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> qryPvdataByPi(Map<String,Object> paramMap);

    /**
     * 获取门诊费用主表服务
     * @param patientid 门诊号
     * @param indate 结算开始时间
     * @param outdate 结算结束时间
     * @return
     */
	public List<ResponseQryOutpfeeMasterVo> qryOutpfeeMasterInfo(@Param("patientid")String patientid, @Param("indate")String indate, @Param("outdate")String outdate);

	/**
	 * 获取门诊费用明细服务
	 * @param patientid 门诊号
	 * @param codeSt 结算号
	 * @return
	 */
	public List<ResponseQryOutpfeeDetailVo> qryOutpfeeDetailInfo(@Param("patientid")String patientid, @Param("codeSt")String codeSt);

    //public List<Map<String,Object>> qryPvinfoByCodePv(Map<>);
}
