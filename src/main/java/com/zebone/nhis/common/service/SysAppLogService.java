package com.zebone.nhis.common.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.ou.BdOuOrg;
import com.zebone.nhis.common.module.base.transcode.SysApplog;
import com.zebone.nhis.common.module.base.transcode.SysLogInterface;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * app系统日志
 * @author Administrator
 */
@Service
public class SysAppLogService {

	/**
	 * 写系统应用日志
	 * @param param
	 * @param user
	 */
	public void saveSysAppLog(String param, IUser user){
		SysApplog sysApplog = JsonUtil.readValue(param, SysApplog.class);
		String pkApplog = sysApplog.getPkApplog();
		// 根据主键判断是新增或更新
		if(CommonUtils.isEmptyString(pkApplog)){
			ApplicationUtils.setDefaultValue(sysApplog, true);
			DataBaseHelper.insertBean(sysApplog);
		}else{
			ApplicationUtils.setDefaultValue(sysApplog, false);
			DataBaseHelper.updateBeanByPk(sysApplog, false);
		}
	}
	
	/**
	 * 查询日志
	 * @param param
	 * @param user
	 * @return
	 */
	public List<SysApplog> queryForAppLog(String param, IUser user){
		SysApplog sysParam = JsonUtil.readValue(param, SysApplog.class);
		List<Date> list = new ArrayList<>();
		String querySql = "select * from SYS_APPLOG where 1=1";
		if(!CommonUtils.isEmptyString(sysParam.getPkOrg())){
			querySql += " and pk_org= '" + sysParam.getPkOrg() + "'";
		}
		if(sysParam.getDateStart() != null){
			String startStr = DateUtils.dateToStr("yyyy-MM-dd",sysParam.getDateStart())+" 00:00:00";
			list.add(DateUtils.strToDate(startStr,"yyyy-MM-dd HH:mm:ss"));
			querySql += " and date_op>=?";
		}
		if(sysParam.getDateEnd() != null){
			String endStr = DateUtils.dateToStr("yyyy-MM-dd",sysParam.getDateEnd())+" 23:59:59";
			list.add(DateUtils.strToDate(endStr,"yyyy-MM-dd HH:mm:ss"));
			querySql += " and date_op<=?";
		}
		if(!CommonUtils.isEmptyString(sysParam.getNameEmpOp())){
			querySql += " and name_emp_op like '%" + sysParam.getNameEmpOp() + "%'";
		}
		if(!CommonUtils.isEmptyString(sysParam.getEuOptype())){
			querySql += " and eu_optype= '" + sysParam.getEuOptype() + "'";
		}
		if(!CommonUtils.isEmptyString(sysParam.getEuButype())){
			querySql += " and eu_butype = '" + sysParam.getEuButype() + "'";
		}
		if(!CommonUtils.isEmptyString(sysParam.getContent())){
			querySql += " and content like '%" + sysParam.getContent() + "%'";
		}
		if (!CommonUtils.isEmptyString(sysParam.getObjname())) {
			querySql += " and objname like '%" + sysParam.getObjname() +"%'";
		}
		if (!CommonUtils.isEmptyString(sysParam.getPkObj())) {
			querySql += " and pk_obj = '" + sysParam.getPkObj() + "'";
		}
		querySql += " order by SYS_APPLOG.date_op desc ";
		List<SysApplog> sysAppLogs = null;
		if(list.size() <= 0){
			sysAppLogs = DataBaseHelper.queryForList(querySql,SysApplog.class);
		}else{
			sysAppLogs = DataBaseHelper.queryForList(querySql,SysApplog.class,list.toArray());
		}
		/*
		 * 根据机构主键查询机构名称，更新业务域名称，更新操作类型名称
		 * 		操作类型，枚举：0修改，1删除，99其他 默认为空；
		 * 		业务域，枚举：0基础，1患者，2就诊，99其他，默认为空；
		 */
		for (SysApplog sysApplog : sysAppLogs) {
			//对象名称如果是患者信息表，则根据主键查询患者门诊号，显示在修改日志中(修改的前后都显示) bug7579
			String objName = sysApplog.getObjname();
			String pkObj = sysApplog.getPkObj();
			if(null != objName && objName.toLowerCase().equals("pi_master") && null != pkObj && !pkObj.equals("")) {
				PiMaster piMaster = DataBaseHelper.queryForBean("select * from pi_master where pk_pi =?",PiMaster.class, pkObj);
				if(null != piMaster) {
					String addContent = "[code_op]  "+piMaster.getCodeOp()+",";
					sysApplog.setContentBf(addContent+sysApplog.getContentBf());
					sysApplog.setContent(addContent+sysApplog.getContent());
				}
			}
			BdOuOrg org = DataBaseHelper.queryForBean("select * from bd_ou_org where PK_ORG =? and del_flag= '0'",
					BdOuOrg.class, sysApplog.getPkOrg());
			sysApplog.setPkOrg(org.getNameOrg());
			//查询业务域名称
			String yewuyu = switchYeWuYu(sysApplog.getEuButype());
			sysApplog.setEuButype(yewuyu);
			//操作类型名称
			String euOptype = sysApplog.getEuOptype();
			//sysApplog.setEuOptype(euOptype.equals("0") ? "修改" : (euOptype.equals("2") ? "新增" : ""));
			sysApplog.setEuOptype(euOptype.equals("0") ? "修改" : (euOptype.equals("1") ? "删除" : (euOptype.equals("99") ? "其他":"" )));
		}
		return sysAppLogs;
	}

	/**
	 * 根据参数查询业务域名称
	 * @param euBuType
	 * @return
	 */
	private String switchYeWuYu(String euBuType) {
		String yewuyu = "";
		switch (euBuType) {
		case "0":
			yewuyu = "基础";
			break;
		case "1":
			yewuyu = "患者";
			break;
		case "2":
			yewuyu = "就诊";
			break;
		case "3":
			yewuyu = "药品";
			break;	
		case "4":
			yewuyu = "诊疗";
			break;	
		case "5":
			yewuyu = "医嘱";
			break;	
		case "99":
			yewuyu = "其他";
			break;
		default:
			break;
		}
		return yewuyu;
	}
	
	public void saveSysLogInterfacce(String param,IUser user){
		SysLogInterface syslog=JsonUtil.readValue(param, SysLogInterface.class);
		if(syslog==null)return ;
		ApplicationUtils.setDefaultValue(syslog, true);
		DataBaseHelper.insertBean(syslog);
	}
}
