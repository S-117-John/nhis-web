package com.zebone.nhis.pro.zsrm.cn.dao;

import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.nhis.pro.zsrm.cn.vo.HerbVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CnOpHerbMapper {

    List<Map<String,Object>> qryHerPd(HerbVo herb);

    /**
     * 获取草药医嘱的明细
     * @return
     */
    List<Map<String,Object>> getHerbItems(Map<String,Object> paramMap);

    List<BdDefdoc> qryBdDefdoc(Map<String,Object> paramMap);
}
