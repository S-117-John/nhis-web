package com.zebone.nhis.webservice.pskq.dao;

import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;

@Mapper
public interface HospitalizedCostDao {

    /**
     * 获取患者基本信息
     * @param codePi
     * @return
     */
    PiMaster selectPiMaster(String codePi);

    PvEncounter selectPvEncounter(String pkPi);

    BdItem selectBdItem(String code);
}
