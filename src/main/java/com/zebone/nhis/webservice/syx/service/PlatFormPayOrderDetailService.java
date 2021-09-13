package com.zebone.nhis.webservice.syx.service;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.syx.dao.PlatFormPayOrderDetailMapper;
import com.zebone.nhis.webservice.syx.utils.PfWsUtils;
import com.zebone.nhis.webservice.syx.vo.platForm.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户已缴费记录查询接口
 *
 * @author cuijiansheng 2019.7.18
 */
@Service
public class PlatFormPayOrderDetailService {

    @Autowired
    private PlatFormPayOrderDetailMapper mapper;

    public String getPayOrderDetail(String content) throws Exception {

        PayOrderDetailReqSubject req = (PayOrderDetailReqSubject) XmlUtil.XmlToBean(content, PayOrderDetailReqSubject.class);

        List<PayOrderDetailRes> info = null;
        int payAmout = 0;
        String userName = "";
        if (StringUtils.isNotBlank(req.getSubject().get(0).getOrderIdHis())) {

            List<Map<String, Object>> list = mapper.getPayOrderDetail(req.getSubject().get(0));
            if (list != null && list.size() > 0) {
                info = new ArrayList<>();

                for (Map<String, Object> map : list) {
                    userName = map.get("userName") == null ? "" : map.get("userName").toString();
                    int count = map.get("payAmout") == null ? 0 : Integer.parseInt(map.get("payAmout").toString());
                    payAmout += count;

                    PayOrderDetailRes item = new PayOrderDetailRes();
                    item.setDetailFee(map.get("detailFee") == null ? null : map.get("detailFee").toString());
                    item.setDetailId(map.get("detailId") == null ? null : map.get("detailId").toString());
                    item.setDetailName(map.get("detailName") == null ? null : map.get("detailName").toString());
                    item.setDetailCount(map.get("detailCount") == null ? null : map.get("detailCount").toString());
                    item.setDetailUnit(map.get("detailUnit") == null ? null : map.get("detailUnit").toString());
                    item.setDetailAmout(map.get("detailAmout") == null ? null : map.get("detailAmout").toString());

                    info.add(item);
                }
            }
        }

        //res
        PayOrderDetailResSubjectRes res = new PayOrderDetailResSubjectRes();
        res.setOrderDetailInfo(info);
        if(info != null && info.size() != 0 ){
        	res.setResultCode("0");
        	res.setResultDesc("处理成功！");
        }else{
        	res.setResultCode("0");
        	res.setResultDesc("未查询到数据！");
        }
        res.setUserName(userName);
        res.setPayAmout(payAmout + "");

        //subject
        PayOrderDetailResSubject subject = new PayOrderDetailResSubject();
        subject.setRes(res);

        //result
        PayOrderDetailResResult result = new PayOrderDetailResResult();
        result.setId("AA");
        result.setRequestTime(DateUtils.getDateTimeStr(new Date()));
        result.setRequestId(req.getId());
        result.setText("处理成功!");
        result.setSubject(subject);

        //response
        PayOrderDetailResExd exd = new PayOrderDetailResExd();
        exd.setResult(result);

        String xml = XmlUtil.beanToXml(PfWsUtils.constructResBean(req, exd), PayOrderDetailResExd.class);
        return xml;
    }
}
