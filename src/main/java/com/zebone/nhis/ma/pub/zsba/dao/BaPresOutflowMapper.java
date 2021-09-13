package com.zebone.nhis.ma.pub.zsba.dao;

import com.zebone.nhis.ma.pub.zsba.vo.outflow.PresDetail;
import com.zebone.nhis.ma.pub.zsba.vo.outflow.PresInfo;
import com.zebone.nhis.ma.pub.zsba.vo.outflow.VisitInfo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaPresOutflowMapper {

    VisitInfo getPatient(String pkPv);

    List<PresInfo> getPresInfo(List<String> listPkPres);

    List<PresDetail> getPresDetail(Map<String,Object> paramMap);
}
