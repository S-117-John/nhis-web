package com.zebone.nhis.emr.rec.rec.service;

/**
 * @author liH
 * @version 1.0
 * @date 2020/11/12 13:34
 * @description
 * @currentMinute zebone_CZ
 */

import ca.uhn.hl7v2.util.StringUtil;
import com.zebone.nhis.common.module.emr.rec.rec.EmrAuthApply;
import com.zebone.nhis.emr.rec.rec.vo.EmrAuthApplyVo;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 病历书写-病历书写
 *
 * @author chengjia
 *
 */
@Service
public class MedRecopService {
	public List<EmrAuthApplyVo> queryEmrAuthList(String param, IUser user){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map paramMap= JsonUtil.readValue(param, Map.class);
		String sql = "SELECT pm.NAME_PI,pe.DT_SEX,pm.CODE_PI,pe.DATE_BEGIN,pd.CREATE_TIME Doc_Date,bpt.NAME DOC_NAME,pe.PK_DEPT,pd.creator Doc_Creator,po.op_times Times,eaa.pk_apply,"
				+"eaa.pk_org,eaa.pk_pv,eaa.pk_pi,eaa.eu_pvtyp,eaa.eu_type,eaa.pk_rec,eaa.pk_emp_apply,eaa.name_emp_apply,eaa.pk_dept_apply,eaa.app_date,"
				+"eaa.app_txt,eaa.eu_status,eaa.pk_emp_approve,eaa.pk_dept_approve,eaa.approve_date,eaa.approve_txt,eaa.del_flag,eaa.remark,eaa.creator,eaa.create_time,eaa.ts "
				+" FROM PI_MASTER pm INNER JOIN PV_ENCOUNTER pe ON pe.PK_PI = pm.PK_PI INNER JOIN pv_doc pd ON pd.PK_PI = pe.PK_PI AND pd.pk_pv = pe.pk_pv INNER JOIN emr_auth_apply eaa ON eaa.pk_rec = pd.PK_PVDOC left join pv_op po on po.pk_pv = pe.pk_pv LEFT JOIN bd_pvdoc_temp bpt ON bpt.PK_PVDOCTEMP = pd.PK_PVDOCTEMP "
				+" WHERE pm.DEL_FLAG != '1' AND pe.DEL_FLAG != '1' AND pd.DEL_FLAG != '1'";
		if (!StringUtil.isBlank((String)paramMap.get("euType"))){
			sql+=" and eaa.EU_TYPE = :euType";
		}
		if (!StringUtil.isBlank((String)paramMap.get("euStatus"))){
			sql+=" and eaa.eu_status = :euStatus";
		}
		if (!StringUtil.isBlank((String)paramMap.get("beginDateTime"))){
			sql+=" and eaa.app_date>=to_date(:beginDateTime,'yyyy-MM-dd HH24:mi:ss')";
		}
		if (!StringUtil.isBlank((String)paramMap.get("endDateTime"))){
			sql+=" and eaa.app_date<=to_date(:endDateTime,'yyyy-MM-dd HH24:mi:ss')";
		}
		if (!StringUtil.isBlank((String)paramMap.get("pkDeptApply"))){
			sql+=" and eaa.pk_dept_apply=:pkDeptApply";
		}
		if (!StringUtil.isBlank((String)paramMap.get("name"))){
			sql+=" and pm.NAME_PI=:name";
		}
		if (!StringUtil.isBlank((String)paramMap.get("codePi"))){
			sql+=" and pm.CODE_PI=:codePi";
		}
		if (!StringUtil.isBlank((String)paramMap.get("pkEmpApply"))){
			sql+=" and eaa.pk_emp_apply=:pkEmpApply";
		}
		if (!StringUtil.isBlank((String)paramMap.get("pkPv"))){
			sql+=" and pe.pk_pv=:pkPv";
		}
		if (!StringUtil.isBlank((String)paramMap.get("pkPvdoc"))){
			sql+=" and eaa.pk_rec=:pkPvdoc";
		}
		if (!StringUtil.isBlank((String)paramMap.get("pkPi"))){
			sql+=" and eaa.PK_PI=:pkPi";
		}
		List<EmrAuthApplyVo> emrAuthApplyVoList = DataBaseHelper.queryForList(sql,EmrAuthApplyVo.class,paramMap);
		return emrAuthApplyVoList;
	}
	public void saveEmrAuthList(String param, IUser user){
		List<EmrAuthApply> list = JsonUtil.readValue(param,
				new TypeReference<List<EmrAuthApply>>() {
				});
		int count = 0;
		//DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(EmrAuthApply.class), list);
		for (EmrAuthApply emrAuthApply : list) {
			if (emrAuthApply.getPkApply() == null) {
				count = DataBaseHelper.insertBean(emrAuthApply);
			} else {
				count = DataBaseHelper.updateBeanByPk(emrAuthApply, false);
			}
		}
	}
	public int saveEmrAuth(String param, IUser user){
		EmrAuthApply emrAuthApply = JsonUtil.readValue(param, EmrAuthApply.class);
		if (emrAuthApply == null)
			throw new BusException("未获取到待保存的申请信息！");
		int count = 0;
		if (emrAuthApply.getPkApply() == null) {
			count = DataBaseHelper.insertBean(emrAuthApply);
		} else {
			count = DataBaseHelper.updateBeanByPk(emrAuthApply, false);
		}
		return count;
	}
}
