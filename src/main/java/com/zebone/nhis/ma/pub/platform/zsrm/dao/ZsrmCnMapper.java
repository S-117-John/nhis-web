package com.zebone.nhis.ma.pub.platform.zsrm.dao;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bl.OutFeedeTail;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bl.Outfee;
import com.zebone.nhis.ma.pub.platform.zsrm.vo.MzRecordInfoVo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ZsrmCnMapper {
    //查询门诊处方药品信息
    List<Map<String,Object>> getOpCnOrderBdPd(Map<String,Object> param);
    //查询门诊处方医嘱项目信息
    List<Map<String,Object>> getOpCnOrderBdOrd(Map<String,Object> param);
    //查询发票信息
    List<Map<String,Object>> getEBillByDate(Map<String,Object> paramMap);

    /**
     * 查询主表费用
     * @param paramMap
     * @return
     */
    List<Outfee> queryOutpfeeMasterInfo(Map<String,Object> paramMap);

    /**
     * 3.44.获取门诊费用明细服务
     * @param paramMap
     * @return
     */
    List<OutFeedeTail> queryoutpfeedetailinfo(Map<String,Object> paramMap);

    /**
     * @Description 查询门诊病历信息
     */
    List<MzRecordInfoVo> queryMzRecordInfo(Map<String,Object> paramMap);
}
