package com.zebone.nhis.ma.pub.zsrm.dao;

import com.zebone.nhis.ma.pub.zsrm.vo.ZsrmOpTransInHospitalVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ZsrmOpTransInHospitalMapper {

    /**
     * 获取门诊转入院申请信息
     * @param paramMap
     * @return
     */
    List<ZsrmOpTransInHospitalVo> getOpTransInHospInfo(Map<String,Object> paramMap);

    /**
     * 获取老系统数据
     * @param paramMap
     * @return
     */
    List<ZsrmOpTransInHospitalVo> getOldHisInfo(Map<String,Object> paramMap);


    /**
     * 获取诊断信息
     * @param pkdiag
     * @return
     */
    List<Map<String,Object>> getDiagInfo(String pkdiag);
}
