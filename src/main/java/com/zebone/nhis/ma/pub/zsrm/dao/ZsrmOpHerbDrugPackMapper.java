package com.zebone.nhis.ma.pub.zsrm.dao;

import com.zebone.nhis.ma.pub.zsrm.vo.ZsrmHerbPresDtVo;
import com.zebone.nhis.ma.pub.zsrm.vo.ZsrmHerbPresVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ZsrmOpHerbDrugPackMapper {

    /**
     * 查询草药处方
     * @param presList
     * @return
     */
    public List<ZsrmHerbPresVo> qryHerbPresInfo(List<Map<String,Object>> presList);

    /**
     * 查询草药处方明细
     * @param presList
     * @return
     */
    public List<ZsrmHerbPresDtVo> qryHerbPresDtInfo(List<Map<String,Object>> presList);
}
