package com.zebone.nhis.pro.zsba.nm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;


@Service
public class NmPatientHandler {
	
//	private static Logger log = LoggerFactory.getLogger(NmPatientHandler.class);
	
	/**
	 * 	022003007022 非医疗费用-门诊患者
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMzPatientInfo(String param, IUser user) {
		List<Map<String, Object>> dataAll = new ArrayList<Map<String,Object>>();
		
		param = param.replaceAll("\r|\n", "");
		Map<String,String> params = JsonUtil.readValue(param, Map.class);
		String pkDept = params.get("pkDept");
		String cardType = params.get("cardType");
		String codeIp = params.get("codeIp");
		
		if(StringUtils.isEmpty(cardType)){
			throw new BusException("卡类型不能为空！");
		}
		if(StringUtils.isEmpty(codeIp)){
			throw new BusException("卡号不能为空！");
		}
		//NHIS
		StringBuffer mzSql = new StringBuffer("select ");
		mzSql.append(" pv.PK_ORG, pv.pk_dept pk_dept, pv.pk_pi, pv.pk_pv, pi.CODE_IP, pv.code_pv, op.OP_TIMES as times, od.NAME_DEPT,  ");
		mzSql.append(" '' as name_bed , pv.name_pi, sex.name sex, pv.date_begin date_in, '1' as pv_type,  ");
		mzSql.append(" case when (w.WX_OPEN_ID is null) then '未绑微信' else '已绑微信' end as is_bind  ");
		mzSql.append(" from  ");
		mzSql.append(" pv_encounter pv  ");
		mzSql.append(" LEFT JOIN pv_op op on op.pk_pv = pv.pk_pv ");
		mzSql.append(" LEFT JOIN PI_MASTER pi on pi.PK_PI = pv.PK_PI ");
		mzSql.append(" LEFT JOIN BD_OU_DEPT od on od.PK_DEPT = pv.PK_DEPT ");
		mzSql.append(" LEFT JOIN bd_defdoc sex on sex.code = pv.dt_sex and sex.code_defdoclist = '000000' ");
		mzSql.append(" LEFT JOIN t_patient_wx_mapper w on w.PATIENT_ID=pi.CODE_OP   ");
		mzSql.append(" where  ");
		mzSql.append("  pv.eu_pvtype != '3' ");
		mzSql.append(" and pi.code_ip like '%"+codeIp+"%' ");
		
		List<Map<String, Object>> mzData = null;
		if(StringUtils.isNotEmpty(pkDept)){
			mzSql.append(" and pv.pk_dept = '"+pkDept+"' ");
			mzData = DataBaseHelper.queryForList(mzSql.toString());
		}else{
			mzData = DataBaseHelper.queryForList(mzSql.toString());
		}
		if(!mzData.isEmpty()){
			dataAll.addAll(mzData);
		}
		
		//切换外部数据库，查询没有事务、写入需要事务则需要抽取调用service
		DataSourceRoute.putAppId("HIS_bayy");
		
		StringBuffer mzOldSql = new StringBuffer("select ");
		mzOldSql.append(" '89ace0e12aba439991c0eb001aaf02f7' as PK_ORG,'0' as pk_dept, m.patient_id  as pk_pi,  ");
		mzOldSql.append(" m.patient_id  +'-'+ CONVERT(VARCHAR(4), m.max_times) as pk_pv,  ");
		mzOldSql.append(" m.patient_id  as CODE_IP, m.patient_id  as code_pv,  ");
		mzOldSql.append(" m.max_times as times, '门诊' as NAME_DEPT,'0' as name_bed, m.name  as name_pi, ");
		mzOldSql.append(" case m.sex WHEN '1' then '男' when '2' then '女' else '未知' end as sex,  ");
		mzOldSql.append(" CONVERT(VARCHAR(10),GETDATE(),120) as date_in,'1' as pv_type ,  ");
		mzOldSql.append(" case when (w.wx_open_id is null) then '未绑微信' else '已绑微信' end as is_bind ");
		mzOldSql.append(" from ");
		mzOldSql.append(" dbo.view_mz_patient_mi m ");
		mzOldSql.append(" LEFT JOIN dbo.t_patient_wx_mapper w on w.patient_id=m.patient_id ");
		
		//1患者id、2诊疗卡、3旧社保卡、4三代社保卡、5身份证
		if("1".equals(cardType)){
			mzOldSql.append(" where m.patient_id like '%"+codeIp+"%' ");
		}else if("2".equals(cardType)){
			mzOldSql.append(" where m.p_bar_code like '"+codeIp+"%' ");
		}else if("3".equals(cardType)){
			mzOldSql.append(" where m.addition_no1 like '"+codeIp+"%' ");
		}else if("4".equals(cardType)){
			mzOldSql.append(" where m.new_yb_card like '"+codeIp+"%' ");
		}else if("5".equals(cardType)){
			mzOldSql.append(" where m.social_no like '"+codeIp+"%' ");
		}
		
		List<Map<String, Object>> mzOldData = DataBaseHelper.queryForList(mzOldSql.toString());
		if(!mzOldData.isEmpty()){
			dataAll.addAll(mzOldData);
		}
		return dataAll;
	}

}
