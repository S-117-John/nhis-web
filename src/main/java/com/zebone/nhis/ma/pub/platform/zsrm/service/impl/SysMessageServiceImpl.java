package com.zebone.nhis.ma.pub.platform.zsrm.service.impl;

import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.ma.pub.platform.zsrm.service.SysMessageService;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

@Service("zsrmSysMsgService")
public class SysMessageServiceImpl implements SysMessageService {


    /**
     * 保存平台发来的参数
     */
    @Override
    public void saveReceiveMessage(String id,String msgType,String param,String errMsg) {
        if(StringUtils.length(errMsg)>3000){
            try{
                byte [] b = errMsg.getBytes("GBK");
                errMsg = new String(Arrays.copyOf(b,3000),"GBK");
            } catch (Exception e){
            }
        }
        SysMsgRec rec = new SysMsgRec();
        rec.setTransType("receive");
        rec.setMsgType(msgType);
        rec.setMsgId(id);
        rec.setTransDate(new Date());
        rec.setMsgContent(param);
        rec.setSysCode("ESB");
        rec.setMsgStatus("SAVE");
        rec.setErrTxt(errMsg);
        DataBaseHelper.insertBean(rec);
    }
}
