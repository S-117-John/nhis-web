package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.dao;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.model.PvEncounterVo;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.model.SchApptVo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ZsrmCnSendMapper {

    /**
     * 查询检验信息
     * @param pkCnord
     * @return
     */
    List<Map<String,Object>> getLisInfo(List<String> pkCnord);

    /**
     * 查询检查申请
     * @param pkCnord
     * @return
     */
    List<Map<String,Object>> getRisInfo(List<String> pkCnord);

    /**
     * 查询手术申请信息
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> qryOpApplyInfo(Map<String,Object> paramMap);

    /**
     * 查询治疗申请
     * @param pkCnord
     * @return
     */
    List<Map<String,Object>> getTreatmentInfo(List<String> pkCnord);

    /**
     * 查询处方信息
     * @param pkCnord
     * @return
     */
    List<Map<String,Object>> getPtPresInfo(List<String> pkPresocces);

	List<Map<String, Object>> getChargeInfo(@Param("pkCnords")String pkCnords, @Param("pkCgops")String pkCgops);

    /**
     * 查询医嘱信息
     * @param pkCnOrds
     * @return
     */
    List<Map<String,Object>> getCnOrderInfo(List<String> pkCnOrds);

}
