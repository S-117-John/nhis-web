package com.zebone.nhis.webservice.syx.service;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.syx.dao.PlatFormPayInfoMapper;
import com.zebone.nhis.webservice.syx.utils.PfWsUtils;
import com.zebone.nhis.webservice.syx.vo.platForm.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 用户就诊记录查询接口
 *
 * @author cuijiansheng 2019.7.18
 */
@Service
public class PlatFormPayInfoService {

    @Autowired
    private PlatFormPayInfoMapper mapper;

    public String getPayInfo(String content) throws Exception {

        PayInfoReqSubject req = (PayInfoReqSubject) XmlUtil.XmlToBean(content, PayInfoReqSubject.class);

        if (!StringUtils.isEmpty(req.getSubject().get(0).getStartDate()))
            req.getSubject().get(0).setStartDate(req.getSubject().get(0).getStartDate() + " 00:00:00");
        if (!StringUtils.isEmpty(req.getSubject().get(0).getEndDate()))
            req.getSubject().get(0).setEndDate(req.getSubject().get(0).getEndDate() + " 23:59:59");

        List<PayInfoRes> list = mapper.getPayInfo(req.getSubject().get(0));

        //res
        PayInfoResSubjectRes res = new PayInfoResSubjectRes();
        if(list.size() != 0){
        	res.setResultCode("0");
        	res.setResultDesc("处理成功！");
        }else{
        	res.setResultCode("0");
        	res.setResultDesc("未查询到数据！");
        }
        res.setPayListInfo(list);
        res.setUserName(req.getSender().getSendername());
        //res.setCanPay("");

        //subject
        PayInfoResSubject subject = new PayInfoResSubject();
        subject.setRes(res);

        //result
        PayInfoResResult result = new PayInfoResResult();
        result.setId("AA");
        result.setRequestTime(DateUtils.getDateTimeStr(new Date()));
        result.setRequestId(req.getId());
        result.setText("处理成功!");
        result.setSubject(subject);

        //response
        PayInfoResExd exd = new PayInfoResExd();
        exd.setResult(result);

        String xml = XmlUtil.beanToXml(PfWsUtils.constructResBean(req, exd), PayInfoResExd.class);
        return xml;
    }
}
