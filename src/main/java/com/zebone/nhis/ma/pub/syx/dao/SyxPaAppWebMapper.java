package com.zebone.nhis.ma.pub.syx.dao;

import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.ma.pub.syx.vo.SettleUpReq;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SyxPaAppWebMapper {

    /**
     * 查询出院患者信息
     * @param reqVo
     * @return
     */
    public SettleUpReq getPiOutInfo(SettleUpReq reqVo);

    /**
     * 查询取消入院患者信息
     * @param pv
     * @return
     */
    public SettleUpReq getPiCancelIn(PvEncounter pv);

    /**
     *  查询待审核入院患者
     * @param pv
     * @param pkPv
     * @return
     */
    SettleUpReq getInHospExamine(String pkPv);
}
