package com.zebone.nhis.pro.zsba.cn.opdw.dao;

import com.zebone.nhis.cn.pub.vo.OrdBlVo;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.pi.pub.vo.PiMasterVo;
import com.zebone.nhis.pro.zsba.cn.opdw.vo.BaOrdBlVo;
import com.zebone.nhis.pro.zsba.cn.opdw.vo.BlConsultationFree;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface CnPiMapper {
    //查询当前患者医嘱
    List<OrdBlVo> qryOrd(Map<String,Object> param);
    //查询本次收费信息
    List<OrdBlVo> qryBlByPv(Map<String,Object> param);
    //查询本次收费信息--执行状态
    List<OrdBlVo> qryBlByOrd(Map<String,Object> param);
    //查询本医嘱执行信息
    List<OrdBlVo> qryOrdEx(Map<String,Object> param);
    //查询医嘱的收费主键
    List<BaOrdBlVo> qryBlSettle(Map<String,Object> param);

    //查询欠费就诊记录
    List<Map<String,Object>> qryHistoryNoSettle(Map<String,Object> map);


     List<BlConsultationFree>  qryBlCon(String pkOrg);

    List<Map<String, Object>> qryCnInf(String pkPv);

    //查询患者信息
    List<Map<String,Object>> searchPv(Map<String,Object> map);

    List<PiMasterVo> qryPimaster(Map<String,Object> map);

    public Map<String,Object> queryPati(Map<String,Object> map) ;

    //查询欠费就诊记录
    List<Map<String,Object>> qryPhycalData(Map<String,Object> map);
    
    //查询入院通知单集合
    public List<Map<String,Object>> qryPvIpNoticeList(Map<String,Object> map);
}
