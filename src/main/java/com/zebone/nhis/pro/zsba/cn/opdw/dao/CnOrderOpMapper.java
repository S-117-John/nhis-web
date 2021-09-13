package com.zebone.nhis.pro.zsba.cn.opdw.dao;

import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CnOrderOpMapper {
    //获得患者处方明细列表
    List<Map<String,Object>> getPrescriptionDetail(@Param(value="pkPres")String pkPres, @Param(value="pkDeptExec")String pkDeptExec, @Param(value="pkDeptExChineseDrug")String pkDeptExChineseDrug);
    //患者历史就诊信息
    List<Map<String,Object>> qryHistoryOrders(Map<String,Object> map);
    //查询模板非药品医嘱
    List<Map<String,Object>> qryBdOrd(Map<String,Object> map);
    //查询模板非药品医嘱
    List<Map<String,Object>> qryBdOrdDrug(Map<String,Object> map);

    //查询检查信息
    List<Map<String,Object>> qryRisApplyInfo(@Param(value="pkCnords")List<String> pkCnords);

    //查询检验信息
    List<Map<String,Object>> qryLabApplyInfo(@Param(value="pkCnords")List<String> pkCnords);

    List<BlOpDt>  queryBlOpDts(String pkPv);
    //根据pkPv查询费用信息
    List<Map<String,Object>> queryPkPvBlOpDtPts(Map<String,Object> map);
    //根据pkPv查询医嘱信息
    List<Map<String,Object>> queryPkCnOrderInfro(Map<String,Object> map);
    
    void  deleteExAssistOcc(String pkCnord);

    void  deleteExAssistOccDt(String pkCnord);
}
