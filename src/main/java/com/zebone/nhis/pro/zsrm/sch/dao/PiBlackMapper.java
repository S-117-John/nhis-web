package com.zebone.nhis.pro.zsrm.sch.dao;

import com.zebone.nhis.pro.zsrm.sch.vo.PiBlackVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PiBlackMapper {

    /**
     * 预约失约记录
     * @param paramMap
     * @return
     */
    List<PiBlackVo> getPiOfAppt(Map<String,Object> paramMap);

    /**
     * 已就诊但是未缴纳诊金 患者信息
     * @param paramMap
     * @return
     */
    List<PiBlackVo> getPiOfNoPayRegfee(Map<String,Object> paramMap);

    /**
     * 存在医生开立的已确认执行的医嘱，但是患者未缴费 患者信息
     * @param paramMap
     * @return
     */
    List<PiBlackVo> getPiOfNoPayExeFee(Map<String,Object> paramMap);

    List<PiBlackVo> getPiLockApptOfRecord(Map<String,Object> paramMap);
    List<PiBlackVo> getPiLockFeeOfRecord(Map<String,Object> paramMap);
}
