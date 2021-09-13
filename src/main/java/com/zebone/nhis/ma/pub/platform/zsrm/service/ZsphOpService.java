package com.zebone.nhis.ma.pub.platform.zsrm.service;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Entry;
import com.zebone.nhis.ma.pub.platform.zsrm.model.listener.ResultListener;

import java.util.List;

public interface ZsphOpService {
    /***
     *  查询门诊就诊信息
     * @param param
     * @param listener
     */
    void getPvOpInfo(String param, ResultListener listener);

    /**
     * 患者预约接口
     * @param param
     */
    void saveAppt(String param);
    /**
     * 3.13.患者预约信息查询
     * @param param
     */
    List<Entry> getApptInfo(String param);

    /**
     * 回传分诊签到
     * @param param
     */
    void triageSign(String param);

    /***
     * 更新申请单状态
     * @param param
     */
    void modApplyStatus(String param);

    /**
     * 3.81.3.81.体检-保存患者门诊费用信息接口
     * @param param
     */
    List<Entry> saveOpcg(String param);

    /***
     * 删除体检费用
     * @param param
     */
    void deleteOpcgpushStatus(String param);
}
