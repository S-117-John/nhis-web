package com.zebone.nhis.ma.pub.sd.dao;

import com.zebone.nhis.ma.pub.sd.vo.FollowUpAdviceDoctorInfoVo;
import com.zebone.nhis.ma.pub.sd.vo.FollowUpClinicAdviceItemVo;
import com.zebone.nhis.ma.pub.sd.vo.FollowUpPiInfoVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FollowUpMapper {

    public List<FollowUpPiInfoVo> queryPvInfo(String pkPv);

    public List<FollowUpClinicAdviceItemVo> queryOrdInfo(String pkPv);

    public List<Map<String, Object>> queryDiagInfo(String pkPv);

    public List<FollowUpAdviceDoctorInfoVo> queryDocinfo(String pkPv);
}
