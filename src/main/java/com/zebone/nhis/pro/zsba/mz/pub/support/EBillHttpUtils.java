package com.zebone.nhis.pro.zsba.mz.pub.support;

import java.io.UnsupportedEncodingException;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.HttpClientUtil;
import com.zebone.nhis.ma.pub.sd.vo.EnoteReqVo;
import com.zebone.nhis.ma.pub.sd.vo.EnoteResDataVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

public class EBillHttpUtils {
	
	private static Logger settelApiLog = LoggerFactory.getLogger("nhis.SettelApiLog");
	
	/**
	 * 组织主参数，调用http服务
	 * @param dataJson  数据参数
	 * @param version	服务版本
	 * @param serviceName	服务名称
	 */
	public static EnoteResDataVo enoteRts(String dataJson,String version,String serviceName){
		settelApiLog.info("--------------------------------调用HTTP服务开始--------------------------------");
		String dataBs64Str = "";
		try {
			dataBs64Str= Base64.encodeToString(dataJson.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		//组织sign参数
		StringBuffer signSbf = new StringBuffer("");
		String noiseStr = NHISUUID.getKeyId();
		
		signSbf.append(String.format("appid=%s", ApplicationUtils.getPropertyValue("eNote.appId", "")));//appid
		signSbf.append(String.format("&data=%s", dataBs64Str));//data
		signSbf.append(String.format("&noise=%s", noiseStr));//noise UUID
		signSbf.append(String.format("&key=%s", ApplicationUtils.getPropertyValue("eNote.key", "")));//key
		signSbf.append(String.format("&version=%s", version));//version
		
		String signMd5 = new SimpleHash("md5",signSbf.toString()).toHex().toUpperCase();
		
		//组织主参数
		EnoteReqVo reqVo = new EnoteReqVo();
		reqVo.setAppid(ApplicationUtils.getPropertyValue("eNote.appId", ""));
		reqVo.setData(dataBs64Str);
		reqVo.setNoise(noiseStr);
		reqVo.setVersion(version);
		reqVo.setSign(signMd5);
		
		//请求参数转为json格式
		String reqJson = JsonUtil.writeValueAsString(reqVo);
		/**调用服务接口*/
		StringBuffer urlStr = new StringBuffer(String.format("%s%s", ApplicationUtils.getPropertyValue("eNote.url", ""), serviceName));
		settelApiLog.info("调用服务地址{}",urlStr.toString());
		settelApiLog.info("入参{}",dataJson);
		String resJson = HttpClientUtil.sendHttpPostJson(urlStr.toString(), reqJson);
		settelApiLog.info("出参结果：{}",resJson);
		//解析响应参数
		EnoteReqVo enoteResVo = JsonUtil.readValue(resJson, EnoteReqVo.class);

		//对返回参数data做base64解码处理
		String datajson = Base64.decodeToString(enoteResVo.getData());
		//转换为data实体类
		EnoteResDataVo dataVo = JsonUtil.readValue(datajson, EnoteResDataVo.class);
		
		String jsonStr = base64ToStr(dataVo.getMessage());
		dataVo.setMessage(jsonStr);
		
		settelApiLog.info("出参转换出的Message：{}",jsonStr);
		settelApiLog.info("--------------------------------调用HTTP服务结束--------------------------------");
		return dataVo;
	}
	
	private static String base64ToStr(String base64Str){
		try {
			return Base64.decodeToString(base64Str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			settelApiLog.error("编码异常",e);
			throw new BusException("编码异常");
		}
	}
}
