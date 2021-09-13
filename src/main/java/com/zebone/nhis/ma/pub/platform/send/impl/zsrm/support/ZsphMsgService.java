package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support;

import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.ma.pub.platform.zb.service.SysMsgService;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;

@Service
public class ZsphMsgService {

    @Autowired
    private SysMsgService sysMsgService;

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void addMsg(RequestData requestData,String status,String error){
        if(StringUtils.length(error)>3000){
            try{
                byte [] b = error.getBytes("GBK");
                error = new String(Arrays.copyOf(b,3000),"GBK");
            } catch (Exception e){}
        }
        SysMsgRec rec = new SysMsgRec();
        rec.setTransType("send");
        rec.setMsgType(requestData.getRemoteMethod());
        rec.setMsgId(requestData.getId());
        rec.setTransDate(new Date());
        rec.setMsgContent(requestData.getData());
        rec.setSysCode("NHIS");
        rec.setMsgStatus(status);
        rec.setErrTxt(error);
        rec.setRemark(requestData.getDirectUrl());
        MsgIndexData indexData = requestData.getMsgIndexData();
        if(indexData != null){
            rec.setCodePi(indexData.getCodePi());
            rec.setCodeOp(indexData.getCodeOp());
            rec.setCodePv(indexData.getCodePv());
            rec.setCodeIp(indexData.getCodeIp());
            rec.setCodeOther(indexData.getCodeOther());
            rec.setEuEme(indexData.getEuEme());
        }
        sysMsgService.saveSysMsgRec(rec);
    }

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void updateStatus(String pkMsg,String msgStatus,String errMsg){
        DataBaseHelper.update("update sys_msg_rec set msg_status = ?,err_txt=? where PK_MSG = ?",
                new Object[] { msgStatus,errMsg,pkMsg});
    }
}
