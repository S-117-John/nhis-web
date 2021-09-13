package com.zebone.nhis.task.arch.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.arch.service.ArchOuterService;
import com.zebone.nhis.common.arch.vo.ArchLogPushVo;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.quartz.modle.QrtzJobCfg;

@Service
public class ArchPushMessageService {

	@Resource
	private ArchOuterService archOuterService;
	
	public Map executeTask(QrtzJobCfg cfg){
		Map<String,String> result = new HashMap<String,String>();
		String pkOrg = cfg.getJgs();
		pkOrg="~";
		if(CommonUtils.isEmptyString(pkOrg))
			throw new BusException("未获取到机构信息！");
//		String mes = executePushMessage();
//		result.put("Mes", mes);
		return result;
		
		//2018-04-09 调试demo
//		String dateTxt = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
//		String test = "type=5&patientId=000693943600&patientName=曹昌平&dateTime="+dateTxt+"&first=曹昌平@@000693943600@@检查@@性激素2项&keyword=性激素2项@@"+dateTxt+"&uniqueValue=000693943600@@性激素2项@@"+dateTxt+"@@急诊科@@检查";
//		String output = pushMesByUrl(URL,test);
//		result.put("Mes", output);
//		return result;
	}
	
	public String executePushMessage() {
		//1.查询当前符合条件的报告记录(pv_arch_log中推送标志为0的)
		Date dateOpe = new Date();
		String dateBegTxt =DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss",dateOpe);
		List<ArchLogPushVo> logs = DataBaseHelper.queryForList("select top(5) * from dbo.Wechat_Push_Report where flag_push <> '1' and date_upload >= ? ", ArchLogPushVo.class,dateBegTxt);

		//2.调用 http://192.168.200.32:8087/zswebinterface/itf/push/pushWechatMessage 接口，组织数据
		if(logs == null || logs.size() < 1) return "无可推送消息！";
		
		StringBuffer strbuf = null;
		String output = "";
		
		List<String> mes = new ArrayList<String>();
		for(ArchLogPushVo log :logs){
			strbuf = new StringBuffer();
			//2.1组织数据格式调用ws接口
			String opeDate = null != log.getDateUpload() ? DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss",log.getDateUpload()) : "";
			strbuf.append("type=5");
			strbuf.append("&patientId=" + log.getPatientId());
			strbuf.append("&patientName=" + log.getPatientName());
			strbuf.append("&dateTime=" + opeDate);
			strbuf.append("&first=" + log.getPatientName() +"@@"+ log.getPatientId() +"@@"+ log.getDocType()+"@@"+ log.getItemName());
			strbuf.append("&keyword=" + log.getItemName()+"@@"+ opeDate);
			strbuf.append("&uniqueValue="+log.getPatientId() +"@@"+ log.getItemName()+"@@"+ opeDate +"@@"+ log.getApplyDept() +"@@"+ log.getDocType());
			output = archOuterService.pushMesByUrl(strbuf.toString());//调用url推送
			
			//2.2.接口返回的status为1，证明成功，将本条记录的推送标志置为1
			Map<String,String> map = JsonUtil.readValue(output, Map.class);
			if(map != null && null != map.get("status") 
					&& !CommonUtils.isEmptyString(map.get("status").toString()) 
					&&"1".equals(map.get("status").toString())){
				mes.add(log.getPkLog());
				updateFlagPushByPk(log.getPkLog());
			}
		}
		return mes.size() > 0 ? mes.toString() : "";
	}
	
	/**
	 * 更新归档日志表中的 推送字段【flag_push = 1】
	 * @param pklog
	 * @return
	 */
	public int updateFlagPushByPk(String pklog){
		int rs = 0;
		if(CommonUtils.isEmptyString(pklog))
			return rs;
		rs = DataBaseHelper.update("update pv_arch_log set flag_push = '1' where pk_log = ?", new Object[]{pklog});
		return rs;
	}
	
	 
}
