package com.zebone.nhis.ma.kangMei.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.module.ex.nis.ns.ExPresOcc;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.kangMei.service.SendPrescriptionService;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class SendKmMsgHandler {
	
	@Autowired
	SendPrescriptionService sendPrescriptionService;

	/**
	 * 选择商家代煎的草药处方  调用康美接口发药
	 * @param exPdAppDetails
	 * @throws Exception
	 */
	public void sendMedInfo(List<ExPdApplyDetail> exPdAppDetails,String param) throws Exception {
		String KangMeiSendSwitch = ApplicationUtils.getPropertyValue("KangMeiSendSwitch","0");
		if("1".equals(KangMeiSendSwitch)){
			//前台传过来的发药明细
			List<Map<String,Object>> exPdAppInfos = JsonUtil.readValue(param, new TypeReference<List<Map<String,Object>>>() {
			});
			
			//查询该科室所属院区
			Map<String,Object> codeArea = DataBaseHelper.queryForMap("select code_area from bd_ou_org_area where pk_orgarea in (select pk_orgarea from bd_ou_dept where pk_dept=?)", new Object[]{UserContext.getUser().getPkDept()});
			if (codeArea == null)
				return;

			//根据处方单号发药,获取处方单号
			Set<String> applyDtSet = Sets.newHashSet();
			for (ExPdApplyDetail temp : exPdAppDetails) {
				applyDtSet.add(temp.getPkPres());
			}
			String sql="select pres_no from CN_PRESCRIPTION where pk_pres in ("+CommonUtils.convertSetToSqlInPart(applyDtSet, "pk_pres")+")";
			List<Map<String, Object>> presNoMap = DataBaseHelper.queryForList(sql, new Object[]{});
			Set<String> presNoSet = Sets.newHashSet();
			for(Map<String, Object> presNo : presNoMap){//处方单号去重
				presNoSet.add(presNo.get("presNo").toString());
			}
			Map<String,Object> msgParam;
			for(String presNo : presNoSet){
				//遍历集合传入患者地址
				msgParam = Maps.newHashMap();
				for(Map<String,Object> temp : exPdAppInfos){
					if(temp.containsValue(presNo)) msgParam.put("piAddress",temp.get("piAddress"));
				}
				msgParam.put("presNo", presNo);
				msgParam.put("codeArea", codeArea.get("codeArea"));
				sendPrescriptionService.sendPresData(msgParam);
			}
		}
		
	}
	
	/**
	 * 门诊选择商家代煎的草药处方  调用康美接口发药
	 * @param exPdAppDetails
	 * @param param
	 * @throws Exception
	 */
	public void sendOpMedInfo(ExPresOcc exPres ,Map<String,Object> paramMap) throws Exception {
		String KangMeiSendSwitch = ApplicationUtils.getPropertyValue("KangMeiSendSwitch","0");
		if("1".equals(KangMeiSendSwitch)){
			//查询该科室所属院区
			Map<String,Object> codeArea = DataBaseHelper.queryForMap("select code_area from bd_ou_org_area where pk_orgarea in (select pk_orgarea from bd_ou_dept where pk_dept=?)", new Object[]{UserContext.getUser().getPkDept()});
			if (codeArea == null)
				return;
			Map<String,Object> msgParam=new HashMap<String, Object>();
		    msgParam.put("presNo", exPres.getPresNo());
			msgParam.put("codeArea", codeArea.get("codeArea"));
			msgParam.putAll(paramMap);
			msgParam.put("pkPv", exPres.getPkPv());
			sendPrescriptionService.sendOpPresData(msgParam);
		}
		
	}

}
