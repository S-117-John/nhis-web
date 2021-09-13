package com.zebone.nhis.pro.zsba.mz.pub.dao;

import com.zebone.nhis.ma.pub.zsrm.vo.DrugPresUsecateVo;
import com.zebone.nhis.ma.pub.zsrm.vo.PressAttInfo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface ZsbaOpWinnoMapper {

    public List<DrugPresUsecateVo> getPresUsecate(List<String> pkPresList);

    /**
     * 根据开立科室确定执行科室信息
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> getDeptExByUsecate(Map<String,Object> paramMap);

    /**
     * 查询处方属性
     * @param pkPreses
     * @return
     */
    public List<PressAttInfo> getPresAtt(List<String> pkPreses);

    /**
     * 查询药品收费明细处方属性
     * @param pkPreses
     * @return
     */
    public List<PressAttInfo> getOpCgdtAtt(List<String> pkPreses);
}
