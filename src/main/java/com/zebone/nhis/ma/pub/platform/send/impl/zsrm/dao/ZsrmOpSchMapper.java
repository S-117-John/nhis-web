package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.dao;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.model.SchApptVo;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.sch.ShortMsgSchApptInfoVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;

/**
 * 排班
 */
@Mapper
public interface ZsrmOpSchMapper {

    /**
     * 获取预约未就诊，未取消的预约登记信息
     * @param pkSchs
     * @return
     */
    List<ShortMsgSchApptInfoVo> getSchApptInfoList(List<String> pkSchs);

    /**
     * 获取排班停止影响的患者的 预约信息
     * @param pkSchs
     * @return
     */
    List<SchApptVo> getSchApptStopList(List<String> pkSchs);
}
