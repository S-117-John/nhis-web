package com.zebone.nhis.bl.pub.dao;

import com.zebone.nhis.bl.pub.vo.CnOrderVo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OpDrugsExecuteMapper {

    /**
     * 根据患者查询医嘱信息
     *
     * @param qryMap
     * @return
     */
    public List<Map<String, Object>> qryOrdInfoByPv(Map<String, Object> qryMap);

    /**
     * 根据医嘱查询附加项目
     *
     * @param pkCnord
     * @return
     */
    public List<Map<String, Object>> qryAdditItem(String pkCnord);

    /**
     * 查询执行记录
     *
     * @param pkCnord
     */
    public List<Map<String, Object>> qryExeRecord(String pkCnord);

    public List<Map<String, Object>> qryPerformRecord(Map paramMap);

    public void updateInfusionOcc(Map occMap);

    public void updateOrd(String pkCnord);

    /**
     * 查询执行记录
     *
     * @param pkCnords
     */
    public List<Map<String, Object>> qryExeRecords(@Param("list") List<Map<String, Object>> pkCnords);

    /**
     * 查询打印信息
     *
     * @param cnOrderVos
     * @return
     */
    public List<CnOrderVo> qryPrintInfo(List<CnOrderVo> cnOrderVos);

    /**
     * 查询原液皮试
     *
     * @param liquidTestOrds
     * @return
     */
    public List<Map<String, Object>> qryLiquidSt(List<String> liquidTestOrds);

    /**
     * 查询皮试剂皮试
     *
     * @param pkPv
     * @return
     */
    public List<Map<String, Object>> qryDoseSt(String pkPv);
}
