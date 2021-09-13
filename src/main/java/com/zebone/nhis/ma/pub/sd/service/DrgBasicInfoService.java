package com.zebone.nhis.ma.pub.sd.service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ma.pub.sd.dao.DrgInfoMapper;
import com.zebone.nhis.common.support.HttpClientUtil;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * DRG系统业务处理服务(有事物)
 * @author yangxue
 *
 */
@Service
public class DrgBasicInfoService {
	private static Logger log = LoggerFactory.getLogger("nhis.SdDrgPlatFormLog");
	@Autowired
	private DrgInfoMapper drgInfoMapper;
	
	public String url=ApplicationUtils.getPropertyValue("DRG.HOST_URL", "");
	/**
	 * 上传医院信息
	 * @param args
	 * @return
	 */
	public Object hospitalInfoUpload(Object...args){
		try {
			log.info("****************开始调用上传医院信息*****************");
			Map<String,String> reqParam = new HashMap<>();
			String appId=ApplicationUtils.getPropertyValue("DRG.APP_ID", "");
			String orgCode=ApplicationUtils.getPropertyValue("DRG.ORG_CODE", "");
			List<Map<String, Object>> orglist=drgInfoMapper.queryOrgList(null);
			reqParam.put("APP_ID", appId);
			reqParam.put("ORG_CODE", orgCode);
			reqParam.put("HOSPITALS", JsonUtil.writeValueAsString(orglist));
			//String req = JsonUtil.writeValueAsString(reqParam);
			log.info("调用上传医院信息入参为:"+reqParam);
			//String result = HttpClientUtil.sendHttpPostJson(url+ApplicationUtils.getPropertyValue("DRG.HOSPITALINFO.URL", ""), req);
			String result = HttpClientUtil.sendHttpPost(url+ApplicationUtils.getPropertyValue("DRG.HOSPITALINFO.URL", ""), reqParam);
			log.info("调用上传医院信息返回结果为:"+result);
			log.info("****************结束调用上传医院信息*****************");
			return result;
		} catch (Exception e) {
			log.info("上传医院信息接口异常"+e.getMessage());
			throw new BusException("上传医院信息接口异常"+e.getMessage());
		}
	}
	/**
	 * 上传医院信息修改
	 * @param args
	 * @return
	 */
	public Object hospitalInfoUpdUpload(Object...args){
		try {
			log.info("****************开始调用上传医院信息修改*****************");
			Map<String,String> reqParam = new HashMap<>();
			String appId=ApplicationUtils.getPropertyValue("DRG.APP_ID", "");
			String orgCode=ApplicationUtils.getPropertyValue("DRG.ORG_CODE", "");
			List<Map<String, Object>> orglist=drgInfoMapper.queryOrgList(null);
			reqParam.put("APP_ID", appId);
			reqParam.put("ORG_CODE", orgCode);
			reqParam.put("HOSPITALS", JsonUtil.writeValueAsString(orglist));
			//String req = JsonUtil.writeValueAsString(reqParam);
			log.info("调用上传医院信息修改入参为:"+reqParam);
			//String result = HttpClientUtil.sendHttpPostJson(url+ApplicationUtils.getPropertyValue("DRG.HOSPITALINFOUPD.URL", ""), req);
			String result = HttpClientUtil.sendHttpPost(url+ApplicationUtils.getPropertyValue("DRG.HOSPITALINFOUPD.URL", ""), reqParam);
			log.info("调用上传医院信息修改返回结果为:"+result);
			log.info("****************结束调用上传医院信息修改*****************");
			return result;
		} catch (Exception e) {
			log.info("上传医院信息修改接口异常"+e.getMessage());
			throw new BusException("上传医院信息修改接口异常"+e.getMessage());
		}
	}
	/**
	 * 上传标准科室查询
	 * @param args
	 * @return
	 */
	public Object deptSearchUpload(Object...args){
		try {
			log.info("****************开始调用上传标准科室查询*****************");
			Map<String,String> reqParam = new HashMap<>();
			String appId=ApplicationUtils.getPropertyValue("DRG.APP_ID", "");
			String orgCode=ApplicationUtils.getPropertyValue("DRG.ORG_CODE", "");
			reqParam.put("APP_ID", appId);
			reqParam.put("ORG_CODE", orgCode);
			reqParam.put("PARAM", "");//20200617增加
			
			//String req = JsonUtil.writeValueAsString(reqParam);
			log.info("调用上传标准科室查询入参为:"+reqParam);
			//String result = HttpClientUtil.sendHttpPostJson(url+ApplicationUtils.getPropertyValue("DRG.DEPTSEARCH.URL", ""), req);
			String result = HttpClientUtil.sendHttpPost(url+ApplicationUtils.getPropertyValue("DRG.DEPTSEARCH.URL", ""), reqParam);
			log.info("调用上传标准科室查询返回结果为:"+result);
			log.info("****************结束调用上传标准科室查询*****************");
			return result;
		} catch (Exception e) {
			log.info("标准科室查询异常"+e.getMessage());
			throw new BusException("标准科室查询异常"+e.getMessage());
		}
	}
	
	/**
	 * 科室匹配
	 * @param args
	 * @return
	 */
	public Object deptMateUpload(Object...args){
		try {
			log.info("****************开始调用科室匹配*****************");
			Map<String,String> reqParam = new HashMap<>();
			String appId=ApplicationUtils.getPropertyValue("DRG.APP_ID", "");
			String orgCode=ApplicationUtils.getPropertyValue("DRG.ORG_CODE", "");
			List<Map<String, Object>> deptlist=drgInfoMapper.queryDeptList(null);
			reqParam.put("APP_ID", appId);
			reqParam.put("ORG_CODE", orgCode);
			reqParam.put("DEPT_MATCHS", JsonUtil.writeValueAsString(deptlist));
			//String req = JsonUtil.writeValueAsString(reqParam);
			log.info("调用上传科室匹配入参为:"+reqParam);
			//String result = HttpClientUtil.sendHttpPostJson(url+ApplicationUtils.getPropertyValue("DRG.DEPTMATE.URL", ""), req);
			String result = HttpClientUtil.sendHttpPost(url+ApplicationUtils.getPropertyValue("DRG.DEPTMATE.URL", ""), reqParam);
			log.info("调用上传科室匹配返回结果为:"+result);
			log.info("****************结束调用科室匹配*****************");
			return result;
		} catch (Exception e) {
			log.info("科室匹配接口异常"+e.getMessage());
			throw new BusException("科室匹配接口异常"+e.getMessage());
		}
	}
	/**
	 * 科室匹配修改
	 * @param args
	 * @return
	 */
	public Object deptMateUpdUpload(Object...args){
		try {
			log.info("****************开始调用科室匹配修改*****************");
			Map<String,String> reqParam = new HashMap<>();
			String appId=ApplicationUtils.getPropertyValue("DRG.APP_ID", "");
			String orgCode=ApplicationUtils.getPropertyValue("DRG.ORG_CODE", "");
			List<Map<String, Object>> deptlist=drgInfoMapper.queryDeptList(null);
			reqParam.put("APP_ID", appId);
			reqParam.put("ORG_CODE", orgCode);
			reqParam.put("DEPT_MATCHS", JsonUtil.writeValueAsString(deptlist));
			//String req = JsonUtil.writeValueAsString(reqParam);
			log.info("调用上传科室匹配修改入参为:"+reqParam);
			//String result = HttpClientUtil.sendHttpPostJson(url+ApplicationUtils.getPropertyValue("DRG.DEPTMATEUPD.URL", ""), req);
			String result = HttpClientUtil.sendHttpPost(url+ApplicationUtils.getPropertyValue("DRG.DEPTMATEUPD.URL", ""), reqParam);
			log.info("调用上传科室匹配修改返回结果为:"+result);
			log.info("****************结束调用科室匹配修改*****************");
			return result;
		} catch (Exception e) {
			log.info("科室匹配修改接口异常"+e.getMessage());
			throw new BusException("科室匹配修改接口异常"+e.getMessage());
		}
	}
	/**
	 * 上传医生信息
	 * @param args
	 * @return
	 */
	public Object doctorInfoUpload(Object...args){
		try {
			log.info("****************开始调用上传医生信息*****************");
			Map<String,String> reqParam = new HashMap<>();
			String appId=ApplicationUtils.getPropertyValue("DRG.APP_ID", "");
			String orgCode=ApplicationUtils.getPropertyValue("DRG.ORG_CODE", "");
			List<Map<String, Object>> doctorlist=drgInfoMapper.queryDoctorList(null);
			reqParam.put("APP_ID", appId);
			reqParam.put("ORG_CODE", orgCode);
			reqParam.put("DOCTORS", JsonUtil.writeValueAsString(doctorlist));
			//String req = JsonUtil.writeValueAsString(reqParam);
			log.info("调用上传医生信息入参为:"+reqParam);
			//String result = HttpClientUtil.sendHttpPostJson(url+ApplicationUtils.getPropertyValue("DRG.DOCTORINFO.URL", ""), req);
			String result = HttpClientUtil.sendHttpPost(url+ApplicationUtils.getPropertyValue("DRG.DOCTORINFO.URL", ""), reqParam);
			log.info("调用上传医生信息返回结果为:"+result);
			log.info("****************结束调用医生信息*****************");
			return result;
		} catch (Exception e) {
			log.info("医生信息接口异常"+e.getMessage());
			throw new BusException("医生信息接口异常"+e.getMessage());
		}
	}
}
