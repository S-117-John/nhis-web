package com.zebone.nhis.pro.zsba.mz.ins.zsba.service.pay;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.HttpClientUtil;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import net.sf.json.JSONObject;

@Service
public class ZsbaPayService {
	private Logger logger = LoggerFactory.getLogger("nhis.ZsrmPayLog");
	
	private String URL = "http://192.168.0.22:5566/zsdzybpz/";
	
	/*@SuppressWarnings("unchecked")
	public String ybQrCodeQuery(String params, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		String rs = "";

		logger.info("入参参数：" + params);
		try {
			rs = HttpClientUtils.sendHttpPost(URL + "dzpzQrCodeQuery", params);
			logger.info("出参参数：" + rs);
			JSONObject obj = JSONObject.fromObject(rs);
			rs = obj.toString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("医保接口调用失败 ");
			throw new BusException("医保返回失败" + e.getMessage());
		}
		logger.info("出参参数：" + rs);

		return rs;
	}*/
	
	/**
	 * 电子凭证验证
	 * 交易号：015001013023->022003027016
	 * @param user
	 * @throws ParseException 
	 */
	public String ybEcTokenCheck(String params, IUser user) {
		String rs = "";
		logger.info("验码入参：" + params);
		try {
			String result = HttpClientUtil.sendHttpPostJson(URL + "dzpzQrCodeQuery", params);
			logger.info("验码入参：" + result);
			JSONObject obj = JSONObject.fromObject(result);
			if(obj.getInt("code") == 0) {
				JSONObject data = obj.getJSONObject("data");
				JSONObject json = new JSONObject();
				json.put("ecToken", data.getString("ecToken"));
				json.put("idType", data.getString("idType"));
				json.put("idNo", data.getString("idNo"));
				json.put("userName", data.getString("userName"));
				rs = HttpClientUtil.sendHttpPostJson(URL + "dzpzEcTokenCheck", json.toString());
				json = JSONObject.fromObject(rs);
				JSONObject temp = json.getJSONObject("data");
				rs = temp.toString();
				logger.info("验码入参2：" + rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("验码调用失败 ");
			throw new BusException("验码失败" + e.getMessage());
		}
		return rs;
	}
	
	/**
	 * 结算回流
	 * 交易号：015001013024->022003027017
	 * @param user
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	public String ybSettleAccounts(String params, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		
		Map<String, Object> reqMap=new HashMap<String, Object>();
		reqMap.put("appId","1");
		reqMap.put("data",paramMap);
		
		String reqStr=JsonUtil.writeValueAsString(reqMap);
		logger.info("调用结算回流入参：" + reqStr);
		try {
			String rs = HttpClientUtil.sendHttpPostJson(URL + "jshl", reqStr);
			logger.info("调用结算回流出参：" + rs);
			JSONObject obj = JSONObject.fromObject(rs);
			return obj.toString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("调用失败 "+e.getMessage());
			throw new BusException("医保返回失败" + e.getMessage());
		}
	}
}
