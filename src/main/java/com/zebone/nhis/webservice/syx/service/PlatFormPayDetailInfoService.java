package com.zebone.nhis.webservice.syx.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.syx.dao.PlatFormPayDetailInfoMapper;
import com.zebone.nhis.webservice.syx.utils.PfWsUtils;
import com.zebone.nhis.webservice.syx.vo.platForm.PayDetailInfoReqSubject;
import com.zebone.nhis.webservice.syx.vo.platForm.PayDetailInfoRes;
import com.zebone.nhis.webservice.syx.vo.platForm.PayDetailInfoResExd;
import com.zebone.nhis.webservice.syx.vo.platForm.PayDetailInfoResResult;
import com.zebone.nhis.webservice.syx.vo.platForm.PayDetailInfoResSubject;
import com.zebone.nhis.webservice.syx.vo.platForm.PayDetailInfoResSubjectRes;

/**
 * 用户待缴费记录查询接口
 * @author cuijiansheng 2019.7.18
 */
@Service
public class PlatFormPayDetailInfoService {

	@Autowired
	private PlatFormPayDetailInfoMapper mapper;
	
	public String getPayDetailInfo(String content) throws Exception {
		
		PayDetailInfoReqSubject req= (PayDetailInfoReqSubject)XmlUtil.XmlToBean(content, PayDetailInfoReqSubject.class);
		
		List<PayDetailInfoRes> list = mapper.getPayDetailInfo(req.getSubject().get(0));
		
		//res
		PayDetailInfoResSubjectRes res = new PayDetailInfoResSubjectRes();
		if(list.size() != 0){
			res.setResultCode("0");
			res.setResultDesc("处理成功！");
		}else{
			res.setResultCode("0");
			res.setResultDesc("未查询到数据！");
		}
		res.setUserName(req.getSender().getSendername());
		res.setOrderDetailInfo(list);
		res.setInfoSeq(req.getSubject().get(0).getInfoSeq());
		res.setAccountAmount("0.00");
		res.setPayAmout("0.00");
		res.setSelfPayAmout("0.00");
		
		//subject
		PayDetailInfoResSubject subject = new PayDetailInfoResSubject();
		subject.setRes(res);
		
		//result
		PayDetailInfoResResult result = new PayDetailInfoResResult();
		result.setId("AA");
		result.setRequestTime(DateUtils.getDateTimeStr(new Date()));
		result.setRequestId(req.getId());
		result.setText("处理成功!");
		result.setSubject(subject);
		
		//response
		PayDetailInfoResExd exd = new PayDetailInfoResExd();
		exd.setResult(result);
		
		String xml = XmlUtil.beanToXml(PfWsUtils.constructResBean(req, exd), PayDetailInfoResExd.class);
        return xml;
	}
}
