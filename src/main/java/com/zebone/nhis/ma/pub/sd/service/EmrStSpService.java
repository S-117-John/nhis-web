package com.zebone.nhis.ma.pub.sd.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.HttpClientUtil;
import com.zebone.nhis.ma.pub.sd.vo.EmrOpcgEuSettleVo;
import com.zebone.nhis.ma.pub.support.WSUtil;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 急诊结算策略服务
 * 
 * @author Administrator
 *
 */
@Service
public class EmrStSpService {
	private Logger logger = LoggerFactory.getLogger("com.zebone");
	
	/**
	 * HIS收费系统对从急诊写入的收费信息进行收费、删除等操作后，通过该接口传递数据，急诊系统写入收费状态
	 * 
	 * @param args
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveOrUpdateCharges(Object[] args) {
		
		/**
		 * 入参 List<String> pkList 收费项目主键集合 String euSettle 收费状态 0：删除，1：已收费，2退费
		 */
		try{
			Map<String,Object> paramMap = (Map<String,Object>)args[0];
		
			List<String> pkList = new ArrayList<>((HashSet<String>) paramMap.get("pkList"));
		
			String euSettle = CommonUtils.getString(paramMap.get("euSettle"));
			
			List<EmrOpcgEuSettleVo> cgList = new ArrayList<>();
			
			if(pkList!=null && pkList.size()>0){
				//校验是否急诊患者，如果不是急诊患者则不需要调用接口
				PvEncounter pvInfo = DataBaseHelper.queryForBean(
						"select pv.* from PV_ENCOUNTER pv inner join bl_op_dt dt on dt.pk_pv = pv.pk_pv where dt.pk_cgop = ?",
						PvEncounter.class,
						new Object[]{pkList.get(0)}
				);

				if(pvInfo==null || !EnumerateParameter.TWO.equals(pvInfo.getEuPvtype())){
					return;
				}

				for(String pkCgop : pkList){
					EmrOpcgEuSettleVo cgVo = new EmrOpcgEuSettleVo();
					cgVo.setChargeId(pkCgop);
					cgVo.setEuSettle(euSettle);
					
					cgList.add(cgVo);
				}
			}
			
			if(cgList!=null && cgList.size()>0){
				Map<String,Object> reqMap = new HashMap<String, Object>(16);
				reqMap.put("charges", cgList);
				
				// 将Data内容转换为json格式
				String dataJson = JsonUtil.writeValueAsString(reqMap);
				logger.info("调用急诊saveOrUpdateCharges开始："+dataJson);
				try {

					String servicePath = ApplicationUtils.getPropertyValue("emerOw.address", "");
					WSUtil.invoke(servicePath,"saveOrUpdateCharges", dataJson);
					logger.info("调用急诊saveOrUpdateCharges成功");
				} catch (Exception e) {
					logger.error("调用急诊saveOrUpdateCharges异常：" + e.getMessage());
					e.printStackTrace();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 组织主参数，调用http服务
	 * 
	 * @param dataJson
	 *            数据参数
	 * @param version
	 *            服务版本
	 * @param serviceName
	 *            服务名称
	 */
	private String sendHttpPostJson(String dataJson,String serviceName){
		/** 调用服务接口 */
		StringBuffer urlStr = new StringBuffer(String.format("%s%s%s",
				"http://10.0.108.143:8080/nhis/static/webservice/emswebservice/", serviceName, "?wsdl"));
		String resJson = HttpClientUtil.sendHttpPostJson(urlStr.toString(), dataJson);
		//String resJson = HttpClientUtil.sendHttpPostXml(urlStr.toString(), dataJson);

		return resJson;
	}
}
