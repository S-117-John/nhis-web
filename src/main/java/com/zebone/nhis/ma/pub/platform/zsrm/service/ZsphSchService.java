package com.zebone.nhis.ma.pub.platform.zsrm.service;


import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Entry;
import com.zebone.nhis.ma.pub.platform.zsrm.model.listener.ResultListener;

import java.util.List;

public interface ZsphSchService {

    void getSch(String param, ResultListener listener);

    /**获取可预约排班科室*/
    void getSchApptDept(String param, ResultListener listener);

    /**获取可预约医生信息*/
    List<Entry> getSchApptDoc(String param);
    /**获取可预约时段信息*/
    List<Entry> getSchApptTime(String param);

    //根据预约主键信息，更新sch_appt.pk_pi
    void updateSchApptByPkSchApp(String param);
}
