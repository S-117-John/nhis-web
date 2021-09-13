package com.zebone.nhis.webservice.syx.utils;

import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.syx.vo.EmrAdmitRecRtn;
import com.zebone.nhis.webservice.syx.vo.EmrPatInfoRtn;
import com.zebone.nhis.webservice.syx.vo.EmrRespData;
import com.zebone.nhis.webservice.syx.vo.EmrResponse;


/**
 * @author chengjia
 *
 */
public class EmrWsUtil {

    
    public static String getEmrResponse(String rtnCode,String rtnMsg,EmrAdmitRecRtn respRec) {
    	EmrResponse resp = new EmrResponse();
		EmrRespData respData=new EmrRespData();
		respData.setRtnCode(rtnCode);
		respData.setRtnMsg(rtnMsg);
		respData.setResponseData(respRec);
		resp.setRespData(respData);

		String result = XmlUtil.beanToXml(resp, resp.getClass());
		
		return result;
    }
    
    public static String getEmrResponsePatInfo(String rtnCode,String rtnMsg,EmrPatInfoRtn respRec) {
    	EmrResponse resp = new EmrResponse();
		EmrRespData respData=new EmrRespData();
		respData.setRtnCode(rtnCode);
		respData.setRtnMsg(rtnMsg);
		respData.setResponseDataPatInfo(respRec);
		resp.setRespData(respData);

		String result = XmlUtil.beanToXml(resp, resp.getClass());
		
		return result;
    }
    
    public static String getEmrResponseStr(String rtnCode,String rtnMsg) {
    	EmrResponse resp = new EmrResponse();
		EmrRespData respData=new EmrRespData();
		respData.setRtnCode(rtnCode);
		respData.setRtnMsg(rtnMsg);
		respData.setResponseDataPatInfo(null);
		resp.setRespData(respData);

		String result = XmlUtil.beanToXml(resp, resp.getClass());
		
		return result;
    }
} 
