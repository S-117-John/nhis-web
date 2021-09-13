package com.zebone.nhis.ex.pub.dao;

import com.zebone.nhis.common.module.pv.PvIp;
import com.zebone.nhis.ex.pub.vo.PvEncounterVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.Map;

@Mapper
public interface PvInfantPubMapper {
    /**
     * 查询婴儿母亲信息
     * @param paramMap{pkPv}
     * @return
     */
	public PvEncounterVo queryInfantMother(Map<String,Object> paramMap);


    void  saveBean (PvIp pvIp);
}
