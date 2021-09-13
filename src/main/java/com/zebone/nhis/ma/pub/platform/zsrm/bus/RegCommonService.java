package com.zebone.nhis.ma.pub.platform.zsrm.bus;

import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.sch.appt.SchAppt;
import com.zebone.nhis.ma.pub.platform.zsrm.vo.PiMasterRegVo;

/**
 * 抽取公用患者就诊方法，不那么通用的不放入
 */
public interface RegCommonService {
    /***
     * 保存就诊信息
     * @param regVo
     * @return
     */
    PvEncounter savePvEncounter(PiMasterRegVo regVo);
    /***
     * 保存就诊属性信息 pv_op 、pv_er
     * @param regVo
     * @return
     */
    void savePvPt(PiMasterRegVo regVo);

    /**
     * 保存预约信息
     * @param piMasterRegVo
     */
    void saveAppt(PiMasterRegVo piMasterRegVo);

    /**
     * 取消预约信息
     * @param appt
     */
    void cancelAppt(SchAppt appt);

}
