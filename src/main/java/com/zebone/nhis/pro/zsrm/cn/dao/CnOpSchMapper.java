package com.zebone.nhis.pro.zsrm.cn.dao;

import com.zebone.nhis.pro.zsrm.cn.vo.SchOrPv;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CnOpSchMapper {
    /**
     * 查询医生出诊信息
     * @param paramMap
     * @return
     */
    List<Map<String, Object>> qryClinicSchList(Map<String, Object> paramMap);

    /**
     * 查询患者预约记录
     * @param schPv
     * @return
     */
    List<Map<String, Object>> qrySchpv(SchOrPv schPv);

    /**
     * 查询患者就诊信息
     * @param schPv
     * @return
     */
    List<Map<String, Object>> qryPv(SchOrPv schPv);

    /**
     *
     * @param paramMap
     * @return
     */
    List<Map<String, Object>> qryPimaster(Map<String, Object> paramMap);

    /** 根据患者主键获取最大的就诊次数(门诊) */
    public Integer getMaxOpTimes(String pkPi);

    //查询就诊记录的待遇类型
    public List<Map<String,Object>> qryPvMode(Map<String,Object> map);

    List<Map<String, Object>>  qryPvSql(SchOrPv schPv);

    /**
     * 查询当前科室医生接诊资源
     * @param schPv
     * @return
     */
    List<Map<String, Object>> qryResSch(SchOrPv schPv);

    List<Map<String, Object>> qryPvSeeOrd(Map<String,Object> map);

    List<Map<String, Object>> qryPvSeeBl(Map<String,Object> map);

    Integer getMaxOpTimesFromPiMaster(String pkPi);
}
