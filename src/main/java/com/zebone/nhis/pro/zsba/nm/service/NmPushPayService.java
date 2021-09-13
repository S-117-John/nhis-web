package com.zebone.nhis.pro.zsba.nm.service;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.pro.zsba.common.support.HttpUtil;
import com.zebone.nhis.pro.zsba.common.utils.PayConfig;
import com.zebone.nhis.pro.zsba.nm.dao.NmCiStMapper;
import com.zebone.nhis.pro.zsba.nm.vo.NmCiSt;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 非医疗费用-微信通知付款
 * @author lipz
 *
 */
@Service
public class NmPushPayService {
	
	private static Logger log = LoggerFactory.getLogger(NmPushPayService.class);

	@Autowired private NmCiStMapper stMapper;
	
	/**
	 * 发送通知
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public void sendPush(String param , IUser user){
		param = param.replaceAll("\r|\n", "");
		Map<String,String> params = JsonUtil.readValue(param, Map.class);
		String pkCiSt = params.get("pkCiSt");
		if(StringUtils.isEmpty(pkCiSt)){
			throw new BusException("结算主键不能为空！");
		}
		NmCiSt st = stMapper.getById(pkCiSt);
		if(st==null){
			throw new BusException("未找到结算记录！结算主键："+pkCiSt);
		}
		if(!"0".equals(st.getIsPay())){
			throw new BusException("结算记录已付款, 不需要再次通知！");
		}
		
		// 组装数据
		String title = "新住院生活用品计费";
		String content = st.getNamePi()+"["+st.getCodePv()+"]["+st.getTimes()+"],\n就诊期间的【生活用品】费用共计应缴："+st.getAmount().toString()+"元。";
		String remarks = "点击进入查看明细 和 缴纳费用。";
		String detailUriParams = "/lift/nmIndex?settId="+st.getPkCiSt();//公众号控制器的方法路径及参数
		if("2".equals(st.getPvType())){//住院患者
			String patientId = findPkPvOp(st.getPkPv());
			if(StringUtils.isNotEmpty(patientId)){
				String mzPateientId = patientId.split("-")[0];
				if(sendPushWx(title, content, remarks, detailUriParams, mzPateientId)){//调用推送中转接口
					// 更新推送标记
					st.setIsPush("1");
					st.setModityTime(new Date());
					DataBaseHelper.updateBeanByPk(st);
				}else{
					patientId = findCodeOp(st.getPkPv());
					if(StringUtils.isNotEmpty(patientId)){
						if(sendPushWx(title, content, remarks, detailUriParams, patientId)){//调用推送中转接口
							// 更新推送标记
							st.setIsPush("1");
							st.setModityTime(new Date());
							DataBaseHelper.updateBeanByPk(st);
						}else{
							throw new BusException("推送失败，请稍候再试！");
						}
					}else{
						throw new BusException("推送失败，请稍候再试！");
					}
				}
			}else{
				patientId = findCodeOp(st.getPkPv());
				if(StringUtils.isNotEmpty(patientId)){
					if(sendPushWx(title, content, remarks, detailUriParams, patientId)){//调用推送中转接口
						// 更新推送标记
						st.setIsPush("1");
						st.setModityTime(new Date());
						DataBaseHelper.updateBeanByPk(st);
					}else{
						throw new BusException("推送失败，请稍候再试！");
					}
				}else{
					throw new BusException("未找到患者门诊主键，不能进行微信通知！");
				}
			}
		}else{//门诊患者
			if(sendPushWx(title, content, remarks, detailUriParams, st.getCodePv())){//调用推送中转接口
				// 更新推送标记
				st.setIsPush("1");
				st.setModityTime(new Date());
				DataBaseHelper.updateBeanByPk(st);
			}else{
				throw new BusException("推送失败，请稍候再试！");
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Boolean sendPushWx(String title, String content, String remarks, String detailUriParams, String patientId){
		// 调用NHIS的接口服务查询
		SortedMap<Object, Object> params = new TreeMap<Object, Object>();
		params.put("title", title);
		params.put("content", content);
		params.put("remarks", remarks);
		params.put("detailUriParams", detailUriParams);
		params.put("patientId", patientId);
		String jsonParams = JsonUtil.writeValueAsString(params);
		
		//发送http请求通过中转服务器请求微信推送接口
		String queryUrl = ApplicationUtils.getPropertyValue("PAY_SERVER_URL", "") + PayConfig.wx_push_pay_url;
		String result = HttpUtil.httpPost(queryUrl, jsonParams);
		log.info("非医疗费用[微信通知]返回结果:" + result);
		
		Boolean flag =false;
		Map<String, Object> map = JsonUtil.readValue(result, Map.class);
		if(map!=null && map.containsKey("common_return")){
			flag = (Boolean)map.get("common_return");
		}
		return flag;
	}

	/**
	 * 从患者基本信息中获取
	 * 根据就诊主键获取患者门诊主索引
	 * @param pkPv
	 * @return
	 */
	private String findCodeOp(String pkPv){
		String result = "";
		//新
		String sqlNew = "select CODE_OP from PI_MASTER where PK_PI in (select PK_PI from PV_ENCOUNTER where PK_PV=?)";
		Map<String, Object> dataNew = DataBaseHelper.queryForMap(sqlNew, new Object[]{pkPv});
		if(dataNew!=null && dataNew.containsKey("codeOp") && dataNew.get("codeOp")!=null){
			result = dataNew.get("codeOp").toString();
		}
		return result;
	}
	
	/**
	 * 从入院通知记录中获取
	 * 根据就诊主键获取患者门诊主索引
	 * @param pkPv
	 * @return
	 */
	private String findPkPvOp(String pkPv){
		String result = "";
		//旧
		String sql = "select n.PK_PV_OP from pv_ip_notice n, PV_IP i where n.PK_IN_NOTICE=i.PK_IP_NOTICE and i.PK_PV=?";
		Map<String, Object> data = DataBaseHelper.queryForMap(sql, new Object[]{pkPv});
		if(data!=null && data.containsKey("pkPvOp") && data.get("pkPvOp")!=null){
			result = data.get("pkPvOp").toString();
		}
		return result;
	}
	
}
