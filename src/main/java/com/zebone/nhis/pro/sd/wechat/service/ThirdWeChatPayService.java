package com.zebone.nhis.pro.sd.wechat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.ma.pub.support.WSUtil;
import com.zebone.nhis.pro.sd.wechat.dao.ThirdWeChatPayMapper;
import com.zebone.nhis.pro.sd.wechat.vo.WechatReqBodyVo;
import com.zebone.nhis.pro.sd.wechat.vo.WechatReqHeadvo;
import com.zebone.nhis.pro.sd.wechat.vo.WechatRequest;
import com.zebone.nhis.pro.sd.wechat.vo.WechatResBillVo;
import com.zebone.nhis.pro.sd.wechat.vo.WechatResponse;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 深大第三方微信退费服务 仅限处理160平台数据，与HIS无任何业务数据交互
 * 
 * @author jd_em
 *
 */
@Service
public class ThirdWeChatPayService {

	@Resource
	private ThirdWeChatPayMapper weChatPayMapper;
	private Logger logger = LoggerFactory.getLogger("com.zebone");

	/**
	 * 通过pkPi查询患者就诊记录
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getPatientPvList(String param,
			IUser user) {
		String pkPi = JsonUtil.getFieldValue("pkPi", param);
		List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
		if (CommonUtils.isEmptyString(pkPi))
			return resList;
		resList = weChatPayMapper.getPatientPvList(pkPi);
		return resList;
	}

	public List<Map<String, Object>> getPvSettleInfo(String param, IUser user) {
		List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();

		return resList;
	}

	/**
	 * 获取HIS 微信支付的结算记录
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> qryHisSettle(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<Map<String, Object>> resList = weChatPayMapper.qryHisSettle(paramMap);
		return resList;
	}

	public List<WechatResBillVo> getWechatSettle(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		WechatRequest request = new WechatRequest();
		WechatReqHeadvo reqhead = new WechatReqHeadvo();
		WechatReqBodyVo reqbody = new WechatReqBodyVo();

		reqhead.setKey("getDailyBill");
		reqhead.setChannelId("his");
		reqhead.setToken("");
		// reqhead.setTime(DateUtils.getDate("yyyyMMddHHmmss"));

		reqbody.setPay_method(CommonUtils.getString(paramMap.get("paytype")));
		reqbody.setBeginDate(CommonUtils.getString(paramMap.get("dateFree")));
		reqbody.setEndDate(CommonUtils.getString(paramMap.get("dateFree")));
		reqbody.setOrderType(CommonUtils.getString(paramMap.get("ordertype")));
		reqbody.setBranchCode("");
		reqbody.setChannel("JY160");

		request.setBody(reqbody);
		request.setHead(reqhead);

		String reqXml = XmlUtil.beanToXml(request, WechatRequest.class);

		String servicePath = ApplicationUtils.getPropertyValue("JY160.address","");
		String resXml = postHttp(servicePath, "requestWS", reqXml);

		WechatResponse response = (WechatResponse) XmlUtil.XmlToBean(resXml,
				WechatResponse.class);

		if (response == null || response.getHead() == null
				|| response.getHead().getResult() == null
				|| "1".equals(response.getHead().getResult())
				|| response.getBody() == null
				|| response.getBody().getBill() == null) {
			return null;
		}

		return response.getBody().getBill();

	}

	private String postHttp(String url, String method, String reqXml) {
		String resXml = "";
		Object[] obj = null;
		try {

			obj = (Object[]) WSUtil.invoke(url, method, reqXml);
			resXml = (String) obj[0];
			logger.info("调用YJ160requestWS成功");
		} catch (Exception e) {
			logger.error("调用YJ160requestWS异常：" + e.getMessage());
			e.printStackTrace();
		}
		return resXml;
	}
}
