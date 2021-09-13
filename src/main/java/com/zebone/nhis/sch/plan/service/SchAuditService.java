package com.zebone.nhis.sch.plan.service;

import ca.uhn.hl7v2.util.StringUtil;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.common.module.sch.pub.SchModlog;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.sch.plan.dao.SchMapper;
import com.zebone.nhis.sch.pub.service.SchHandlerService;
import com.zebone.nhis.sch.pub.support.SchEuStatus;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zebone.nhis.sch.plan.service.SchService;


import java.util.*;

/**
 * 排班审核相关
 * 
 * @author alvin
 *
 */
@Service
public class SchAuditService {

	@Autowired
	private SchMapper schMapper;
	@Autowired
	private SchHandlerService handlerService;

	/** 退回 */
	final static String PROC_EU_STATUS_BACK = "-1";
	/** 取消审核 */
	final static String PROC_EU_STATUS_CANCLE = "-8";

	/**
	 * 排班-诊疗排班审核/取消审核/退回<br>
	 * 交易号：009002002016
	 * @param param
	 *            {"pkSchs":["1","2"],"euStatus":"提交：1、退回：-1；审核：8，取消审核：-8",
	 *            "auditInfo":{"pkEmpChk":"", "nameEmpChk":"","dateChk":""}}
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public int saveAudit(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		User u =(User)user;
		String euStatus = MapUtils.getString(paramMap, "euStatus");
		List<String> pkSchs = (List<String>) paramMap.get("pkSchs");
		Map<String, Object> auditInfo = MapUtils.getMap(paramMap, "auditInfo");
		if (MapUtils.isEmpty(paramMap) || StringUtils.isEmpty(euStatus) || CollectionUtils.isEmpty(pkSchs)) {
			throw new BusException("未传入参数！");
		}
		if(SchEuStatus.AUDIT == SchEuStatus.getEuStatuValue(euStatus) && MapUtils.isEmpty(auditInfo)) {
			throw new BusException("未传入审核信息！");
		}
		if (!Arrays.asList(PROC_EU_STATUS_BACK, PROC_EU_STATUS_CANCLE, SchEuStatus.INIT.getStatus(),
				SchEuStatus.REFER.getStatus(), SchEuStatus.AUDIT.getStatus()).contains(euStatus)) {
			throw new BusException("传入的状态不正确！");
		}
		SchSch sch = new SchSch();
		SchEuStatus originalStatus = null;
		String euStatusName ="";
		if(SchEuStatus.REFER == SchEuStatus.getEuStatuValue(euStatus)) {
			euStatusName = "提交";
			//originalStatus = SchEuStatus.INIT; 提交暂时不判断原始状态，因为遗留数据有null
		} else if (SchEuStatus.AUDIT == SchEuStatus.getEuStatuValue(euStatus)) {
			euStatusName = "发布";
			sch.setPkEmpChk(MapUtils.getString(auditInfo, "pkEmpChk"));
			sch.setNameEmpChk(MapUtils.getString(auditInfo, "nameEmpChk"));
			sch.setDateChk(DateUtils.strToDate(MapUtils.getString(auditInfo, "dateChk"), "yyyyMMddHHmmss"));
			String IsReferStatus = MapUtils.getString(paramMap, "isReferStatus");
			originalStatus = StringUtil.isBlank(IsReferStatus) || StringUtils.isEmpty(IsReferStatus)
					? SchEuStatus.REFER : SchEuStatus.INIT;
			sch.setFlagModi("0");//还原修改状态为0，为了使发布后的修改记录能变成红色显示 yangxue 2020/12/12
		} else if (PROC_EU_STATUS_BACK.equals(euStatus)) {
			euStatusName = "退回";
			originalStatus = SchEuStatus.REFER;
			euStatus = SchEuStatus.INIT.getStatus();
		} else if (PROC_EU_STATUS_CANCLE.equals(euStatus)) {
			euStatusName = "取消审核";
			sch.setPkEmpChk(null);
			sch.setNameEmpChk(null);
			sch.setDateChk(null);
			originalStatus = SchEuStatus.AUDIT;
			euStatus = SchEuStatus.REFER.getStatus();
		}else if(SchEuStatus.INIT==SchEuStatus.getEuStatuValue(euStatus)){
			euStatusName = "取消发布";
			//lb-排班审核状态更改排班状态时，查询判断该排班号源是否被使用
			for (int i = pkSchs.size(); i >0; i--) {
				if(0<schMapper.getSchTicketSum(pkSchs.get(i-1))){
					//已有号源进行删除，不进行后续修改
					pkSchs.remove(i-1);
				}
			}
		}
		sch.setEuStatus(euStatus);
		sch.setModityTime(new Date());
		sch.setModifier(u.getPkEmp());
		sch.setTs(new Date());
		int rs = 0;
		if(pkSchs.size()>0){
			rs = schMapper.updateEuStatus(sch, originalStatus != null ? originalStatus.getStatus() : null, pkSchs);
			//保存排版操作日志
			List<SchModlog> schModlogs = new ArrayList<>();
			for(String str:pkSchs){
				SchModlog schModlog = new SchModlog();
				schModlog.setPkSchlog(NHISUUID.getKeyId());
				schModlog.setPkSch(str);
				schModlog.setEuType("99");//其他
				schModlog.setReasons(euStatusName);
				schModlogs.add(schModlog);
			}
			handlerService.saveSchModLogBatch(schModlogs);
			paramMap.put("saveAudit", "");
			PlatFormSendUtils.sendSchInfo(paramMap);
		}
		return rs;
	}

}
