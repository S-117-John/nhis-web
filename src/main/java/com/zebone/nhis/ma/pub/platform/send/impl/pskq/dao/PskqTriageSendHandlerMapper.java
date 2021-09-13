package com.zebone.nhis.ma.pub.platform.send.impl.pskq.dao;

import com.zebone.nhis.ma.pub.platform.send.impl.pskq.vo.TriageRegVo;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.vo.TriageUserVo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


@Mapper
public interface PskqTriageSendHandlerMapper {

    List<TriageUserVo> getUserInfo(Map<String,Object> param);
    List<Map<String,Object>> getUserDept(@Param("pkUser") String pkUser);
    List<TriageRegVo> getRegInfo(String pkPv);
}
