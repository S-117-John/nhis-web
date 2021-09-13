package com.zebone.nhis.pro.zsrm.cn.dao;

import com.zebone.nhis.cn.pub.vo.OrdBlVo;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.pro.zsrm.cn.vo.BlCnOpDt;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CnOpOrdMapper {
    //查询医嘱的收费主键--一代卡
    List<BlOpDt> qryBlOrd(Map<String,Object> param);
    //查询医嘱的收费主键
    List<BlCnOpDt> qryBlSettle(Map<String,Object> param);

    List<CnOrder> qryOrdBySett(Map<String,Object> param);
    //查询当前患者医嘱
    List<OrdBlVo> qryOrd(Map<String,Object> param);
    //查询当前患者医嘱
    List<OrdBlVo> qryOrdHis(Map<String,Object> param);
    //查询本次收费信息
    List<OrdBlVo> qryBlByPv(Map<String,Object> param);
    //查询本次收费信息--执行状态
    List<OrdBlVo> qryBlByOrd(Map<String,Object> param);
    //查询本医嘱执行信息
    List<OrdBlVo> qryOrdEx(Map<String,Object> param);
    //查询患者抗癌药品
    List<Map<String,Object>> qryPdKA(Map<String,Object> param);
    //查询医嘱详细费用信息--逐条显示
    List<Map<String,Object>> qryBlOrdDetil(Map<String,Object> param);
    //查询患者本次就诊未结算总费用
    Map<String,Object> qryBlSumPv(Map<String,Object> param);

    //查询模板非药品医嘱
    List<Map<String,Object>> qryBdOrd(Map<String,Object> map);
    //查询模板药品医嘱
    List<Map<String,Object>> qryBdOrdDrug(Map<String,Object> map);
}
