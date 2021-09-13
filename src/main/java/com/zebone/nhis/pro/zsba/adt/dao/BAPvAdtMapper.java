package com.zebone.nhis.pro.zsba.adt.dao;

import com.zebone.nhis.pv.pub.vo.PageQryPvParam;
import com.zebone.nhis.pv.pub.vo.PvEncounterListVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;

/**
 * @Classname BAPvAdtMapper
 * @Description TODO
 * @Date 2020-06-22 10:27
 * @Created by wuqiang
 */
@Mapper
public interface BAPvAdtMapper {
    List<PvEncounterListVo> getPvEncounterVoList(PageQryPvParam qryparam);
}
