package com.zebone.nhis.ma.pub.sd.service;

import com.zebone.nhis.ma.pub.sd.dao.RisLisReportMapper;
import com.zebone.nhis.ma.pub.support.ReportUtil;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 检查检验报告服务类
 * @author shihongxin
 *
 */
@Service
public class RisLisReportService {
	@Autowired
	RisLisReportMapper risLisReportMapper;
	
	
	/**
	 * 010005003001
	 * 查询在院患者列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> qryPatientList(String param, IUser user){
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		return risLisReportMapper.qryPatientList(map);
	}

	/**
	 * 010005003002
	 * 查询出院患者列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> qryLeavePatientList(String param, IUser user){
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		Object dateBegin = map.get("dateBegin");
		if(dateBegin != null){
			String dateBeginStr = dateBegin.toString();
			map.put("dateBegin", dateBeginStr.substring(0, 8)+"000000");
		}
		Object dateEnd = map.get("dateEnd");
		if(dateEnd != null){
			String dateEndStr = dateEnd.toString();
			map.put("dateEnd", dateEndStr.substring(0, 8)+"235959");
		}
		return risLisReportMapper.qryLeavePatientList(map);
	}

	/**
	 * 010005003003
	 * 查询转科患者列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> qryChangePatientList(String param, IUser user){
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		return risLisReportMapper.qryChangePatientList(map);
	}

	/**
	 * 010005003004
	 * 根据pkPv查询检验申请单
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qrySughLisApply(String param, IUser user){
		Map<String, Object> qryMap = JsonUtil.readValue(param, Map.class);
		String pkpv = (String)qryMap.get("pkPv");//患者编码
		//获取检验申请单
		String sqlLis = "select distinct occ.date_rpt,occ.name_ord,occ.eu_type,occ.code_samp,occ.code_apply,dept.name_dept,ord.pk_dept,ord.name_emp_ord,ord.date_start,occ.pk_dept_occ"
				+ " from ex_lab_occ occ left outer join cn_order ord on occ.pk_cnord=ord.pk_cnord "
				+ " left join bd_ou_dept dept on dept.pk_dept=ord.pk_dept where occ.pk_pv=? and occ.DEL_FLAG='0' order by date_rpt desc";
		return DataBaseHelper.queryForList(sqlLis, new Object[]{pkpv});
	}

	/**
	 * 010005003005
	 * 根据pkPv查询检查申请单
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qrySughRisApply(String param, IUser user){
		Map<String, Object> qryMap = JsonUtil.readValue(param, Map.class);
		String pkpv = (String)qryMap.get("pkPv");//患者编码
		//获取检查申请单
		String sqlRis = "select distinct ris.pk_risocc,ris.date_rpt,ris.name_ord,ris.pk_dept_occ,ris.name_emp_occ,ris.code_apply,ord.pk_dept,ord.name_emp_ord,ord.date_start,ris.addr_img,ris.result_obj,ris.result_sub,dept.name_dept name_dept_occ "
				+" from ex_ris_occ ris inner join cn_order ord on ris.pk_cnord=ord.pk_cnord "
				+ " left join bd_ou_dept dept on ord.pk_dept_exec = dept.pk_dept  where ris.pk_pv=? and ris.del_flag='0' order by date_rpt desc";
		return DataBaseHelper.queryForList(sqlRis, new Object[]{pkpv});
	}

	/**
	 * 010005003006
	 * 根据申请单号查询检验报告数据
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qrySughLisReport(String param, IUser user){
		Map<String, Object> qryMap = JsonUtil.readValue(param, Map.class);
		String codeApply = (String)qryMap.get("codeApply");//申请单号
		String codeSamp = (String)qryMap.get("codeSamp");//标本号
		String euType = (String)qryMap.get("euType");//标本号
		String sql  = null;
		List<Map<String, Object>> qrySughLisReport = null;
		if("1".equals(euType)){
			//sql = "select bact.sort_no,bact.name_bact,bact.name_pd,bact.mic,bact.eu_allevel,bact.val_lab from ex_lab_occ occ "
			//		+" inner join ex_lab_occ_bact bact on occ.pk_labocc=bact.pk_labocc where occ.code_apply=? and occ.code_samp=? order by bact.sort_no";
			qrySughLisReport = risLisReportMapper.qrySughLisReportWsw(qryMap);
		}else{
			//sql = "select occ.sort_no,occ.name_index,occ.val,occ.val1,occ.unit,occ.val_max,occ.val_min,occ.eu_result from ex_lab_occ occ "
			//		+" where occ.code_apply=? and occ.code_samp=? order by sort_no";
			qrySughLisReport = risLisReportMapper.qrySughLisReportCommon(qryMap);
		}
		//return DataBaseHelper.queryForList(sql, new Object[]{codeApply,codeSamp});
		return qrySughLisReport;
	}
	
	/**
	 * 交易号：010005003011
	 * 获取第三方外院检验报告
	 * @param param
	 * @param user
	 * @return
	 */
	public String getReportList (String param, IUser user){
		String reportListInfo = ReportUtil.getReportListInfo("getReportList", param);
		return reportListInfo;
	}
}
