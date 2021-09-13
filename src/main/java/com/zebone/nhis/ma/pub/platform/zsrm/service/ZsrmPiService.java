package com.zebone.nhis.ma.pub.platform.zsrm.service;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;

import java.util.List;

/**
 * 患者相关信息查询
 */
public interface ZsrmPiService {
    //查询患者基本信息
    List<Entry> getpiMasterZsInfo(String param);
    //根据就诊流水号查询患者就诊诊断
    List<Entry> getPvDiagZsInfo(String param);

    //保存患者注册
    List<Entry> savePiMaster(String param);

    //empi诙谐
    List<Entry> getPiMasterEmpiInfo(String param);

}
