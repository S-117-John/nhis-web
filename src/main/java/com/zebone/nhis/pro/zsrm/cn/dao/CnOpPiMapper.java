package com.zebone.nhis.pro.zsrm.cn.dao;

import com.zebone.nhis.pro.zsrm.cn.vo.PiZsVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CnOpPiMapper {

    /**
     * 根据挂号信息更新患者基本信息,只更新界面录入的证件信息
     * @param regvo
     */
    void updatePiMaster(PiZsVo regvo);

    /**
     * 查询患者
     * @param param
     * @return
     */
    List<Map<String,Object>> searchPv(Map<String,Object> param);

    /**
     * 患者医保信息
     * @param param
     * @return
     */
    List<Map<String,Object>> searchPvDeti(Map<String,Object> param);

    /**
     * 查询统计数据
     * @param param
     * @return
     */
    Map<String,Object> searchPvCount(Map<String,Object> param);

    /**
     * 查询患者结算数据
     * @param param
     * @return
     */
    List<Map<String,Object>> searchPvSettle(Map<String,Object> param);
}
