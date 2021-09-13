package com.zebone.nhis.ma.pub.platform.zsrm.service;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Entry;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Response;
import com.zebone.nhis.ma.pub.platform.zsrm.model.listener.ResultListener;

import java.util.List;

/**
 * 费用相关接口
 */
public interface ZsrmBlService {
    //保存费用补录接口--手麻补费服务-支持门诊、住院--待测试
    Response saveSupplementPrice(String param);

    //查询电子票据
    List<Entry> getEBillByDate(String param);

    //更新电子票据打印次数
    void updateEBillPrintTimes(String param);

    /**
     * 3.43.获取门诊费用主表服务
     * @param param
     * @return
     */
    List<Entry> queryOutpfeeMasterInfo(String param);

    /**
     * 3.44.获取门诊费用明细服务
     * @param param
     * @return
     */
    List<Entry> queryoutpfeedetailinfo(String param);
}
