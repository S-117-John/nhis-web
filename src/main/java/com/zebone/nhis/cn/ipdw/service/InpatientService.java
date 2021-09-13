package com.zebone.nhis.cn.ipdw.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;





import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;






import com.zebone.nhis.cn.ipdw.dao.InpatientMapper;
import com.zebone.nhis.cn.ipdw.vo.BloodApplyVO;
import com.zebone.nhis.cn.ipdw.vo.InpatientVo;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnTransApply;
import com.zebone.nhis.common.module.cn.ipdw.PvIpRep;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InpatientService {
	
	@Autowired
	private InpatientMapper inpatientMapper;
	
	/**
	 * 查询左侧列表
	 * 004004001013
	 * @param param
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selInThrDayLeftList(String param, IUser user) throws Exception{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String pkpv = JsonUtil.getFieldValue(param, "pkpv");
		String sql = "select rep.pk_iprep,rep.date_rep,rep.flag_canc,rep.desc_adv,rep.name_emp_rep "
				+" ,dept.name_dept,rep.desc_soc,rep.desc_oth,rep.flag_oth,rep.flag_over,rep.flag_delay "
				+" ,rep.flag_inf,rep.flag_comp_med,rep.flag_rej,rep.flag_comp_dise,rep.flag_dise,rep.flag_pat,rep.flag_hp,rep.pk_diag"
				+" from pv_ip_rep rep , bd_ou_dept dept"
				+" where rep.pk_pv='"+pkpv+"' and rep.pk_dept = dept.pk_dept order by date_rep desc";	
		list = DataBaseHelper.queryForList(sql);

		return list;
		
	}
	
	/**
	 * 查询右侧明细
	 * 004004001014
	 * @param param
	 * @param user
	 * @return
	 * @throws Exception
	 */
	/*public InpatientVo selThrDayRightDetail(String param, IUser user) throws Exception{
		Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		InpatientVo inpatientVo = inpatientMapper.selThrDayRightDetail(params);
		return inpatientVo;
		
	}*/
	
	/**
	 * 新增
	 * 004004001015
	 * @param param
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public void addThrDayDetail(String param,IUser user) throws Exception{
		Map params = JsonUtil.readValue(param, Map.class);
		String pkpv = params.get("pk_pv").toString();
		String dateins = DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date());
		String sql = "insert into pk_ip_rep(date_rep) values ('"+dateins+"') "+
					 "where pk_pv ='"+pkpv+"'"; 
		int k = DataBaseHelper.update(sql, new Object(){});
		
	}
	
	/**
	 * 保存
	 * 004004001011
	 * @param param
	 * @param user
	 * @return 
	 * @throws Exception
	 */
	public void upThrDayDetail(String param,IUser user) throws Exception{
		PvIpRep inpatient = JsonUtil.readValue(param, new TypeReference<PvIpRep>(){});
		User u = (User)user;
		inpatient.setPkEmpRep(u.getPkEmp());
		inpatient.setNameEmpRep(u.getNameEmp());
		inpatient.setPkDept(u.getPkDept());
		if (inpatient.getPkIprep() == null) {
			DataBaseHelper.insertBean(inpatient);
		}else {
			DataBaseHelper.updateBeanByPk(inpatient, false);
		}
	}
	
	

	/**
	 * 作废功能
	 * 004004001012
	 * @param param
	 * @param user
	 */
	public void deleteThyDay(String param,IUser user){
		PvIpRep listC = JsonUtil.readValue(param, new TypeReference<PvIpRep>() {});
		User u = (User) user;
		listC.setFlagCanc("1");
		if(listC.getPkIprep() != null){
		DataBaseHelper.updateBeanByPk(listC, false);
		}
	
	}
	
	/**
	 * 查询当前诊断
	 * 004004001017
	 * @param param
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryDiagNow(String param, IUser user) throws Exception{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String pkpv = JsonUtil.getFieldValue(param, "pkpv");
		String sql = "SELECT * FROM PV_DIAG WHERE 1=1 and  PK_PV = '"+pkpv+"'"+
		"  AND FLAG_MAJ = '1'  AND "+ 
		" DEL_FLAG = '0'";	
		list = DataBaseHelper.queryForList(sql);
		return list;
		
	}

}
