package com.zebone.nhis.webservice.cxf.impl;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.cxf.INHISSUGHWebService;
import com.zebone.nhis.webservice.service.SughIronDrugService;
import com.zebone.nhis.webservice.vo.sdyy.iron.IronReqBody;
import com.zebone.nhis.webservice.vo.sdyy.iron.IronRequest;

/**
 * 深大webService服务
 * @author jd_em
 *
 */
@WebService
@SOAPBinding(style = Style.RPC)
public class NHISSUGHWebService implements INHISSUGHWebService {
	private static Logger log = LoggerFactory.getLogger("nhis.iron");
	@Resource
	private SughIronDrugService ironDrugService;
	@Override
	public String getDispensingStatus(String param) {
		log.info("门诊发药机配药接口：请求参数\n"+param);
		String result="<response><status>%s</status><msg>%s</msg></response>";
		if(CommonUtils.isNotNull(param)){
			IronRequest request=(IronRequest) XmlUtil.XmlToBean(param, IronRequest.class);
			if(request!=null){
			    IronReqBody body=	request.getReqBody();
			    if(body!=null){
			    	String presNo= body.getOrderNumber();
			    	ironDrugService.updatePresPrepStatus(presNo);
			    	result=String.format(result, new Object[]{"1","调用成功"});
			    }else{
			    	result=String.format(result, new Object[]{"0","调用失败，<Body>信息为空"});
			    }
			}else{
				result=String.format(result, new Object[]{"0","调用失败，未传入请求参数"});
			}
		}
		log.info("门诊发药机配药接口：响应参数\n"+result);
		return result;
	}

}
