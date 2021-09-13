package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.sch;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.PhResource;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.patient.Patient;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.Appointment;

import java.util.List;

/**
 * 微信模板 Resource节点
 */
public class WeChatResource extends PhResource {

    private DataJson dataJson;

    public DataJson getDataJson() {
        return dataJson;
    }

    public void setDataJson(DataJson dataJson) {
        this.dataJson = dataJson;
    }
}
