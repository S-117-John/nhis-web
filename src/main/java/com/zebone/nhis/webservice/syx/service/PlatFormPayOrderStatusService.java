package com.zebone.nhis.webservice.syx.service;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.syx.dao.PlatFormPayOrderStatusMapper;
import com.zebone.nhis.webservice.syx.utils.PfWsUtils;
import com.zebone.nhis.webservice.syx.vo.platForm.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 用户缴费记录状态查询接口
 *
 * @author cuijiansheng 2019.7.18
 */
@Service
public class PlatFormPayOrderStatusService {

    @Autowired
    private PlatFormPayOrderStatusMapper mapper;

    public String getPayOrderStatus(String content) throws Exception {

        PayOrderStatusReqSubject req = (PayOrderStatusReqSubject) XmlUtil.XmlToBean(content, PayOrderStatusReqSubject.class);
        if (!StringUtils.isEmpty(req.getSubject().get(0).getStartDate()))
            req.getSubject().get(0).setStartDate(req.getSubject().get(0).getStartDate() + " 00:00:00");
        if (!StringUtils.isEmpty(req.getSubject().get(0).getEndDate()))
            req.getSubject().get(0).setEndDate(req.getSubject().get(0).getEndDate() + " 23:59:59");

        PayOrderStatusRes res = mapper.getPayOrderStatus(req.getSubject().get(0));

        //subject
        PayOrderStatusResSubject subject = new PayOrderStatusResSubject();
        subject.setRes(res);

        //result
        PayOrderStatusResResult result = new PayOrderStatusResResult();
        result.setId("AA");
        result.setRequestTime(DateUtils.getDateTimeStr(new Date()));
        result.setRequestId(req.getId());
        result.setText("处理成功!");
        result.setSubject(subject);

        //response
        PayOrderStatusResExd exd = new PayOrderStatusResExd();
        exd.setResult(result);

        String hospXml = XmlUtil.beanToXml(PfWsUtils.constructResBean(req, exd), PayOrderStatusResExd.class);
        
    	if(res != null){
        	return PfWsUtils.setResStatus("0","处理成功！",hospXml);
        }else{
        	return PfWsUtils.setResStatus("0","未查询到数据!",hospXml);
        }	
        
       
    }
}
