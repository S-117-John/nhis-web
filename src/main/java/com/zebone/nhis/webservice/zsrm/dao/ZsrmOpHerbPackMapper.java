package com.zebone.nhis.webservice.zsrm.dao;


import com.zebone.nhis.webservice.zsrm.vo.pack.ZsrmHerbPresDtVo;
import com.zebone.nhis.webservice.zsrm.vo.pack.ZsrmHerbPresRequest;
import com.zebone.nhis.webservice.zsrm.vo.pack.ZsrmHerbPresVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ZsrmOpHerbPackMapper {

    /**
     * 查询草药处方
     * @param presList
     * @return
     */
    public List<ZsrmHerbPresVo> qryHerbPresInfo(Map<String,Object> reqvo);

    /**
     * 查询草药处方明细
     * @param presList
     * @return
     */
    public List<ZsrmHerbPresDtVo> qryHerbPresDtInfo(List<String> presList);
}
