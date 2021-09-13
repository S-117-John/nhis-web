package com.zebone.nhis.ma.pub.zsba.dao;

import com.zebone.nhis.ma.pub.zsba.vo.PvFunctionVo;
import com.zebone.nhis.ma.pub.zsba.vo.PvInfantLab;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaPatiInfoMapper {
    /**
     * 查询患者信息
     * @param pkPv
     * @return
     */
    public PvInfantLab quePvInfantLab(String pkPv);

    /**
     *查询医生站操作记录
     * @param paramMap
     * @return
     */
    public List<PvFunctionVo> queryPvFunctionList(Map<String, Object> paramMap);
}
