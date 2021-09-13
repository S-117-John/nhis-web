package com.zebone.nhis.base.bd.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.bd.dao.VenousLiquorRuleMapper;
import com.zebone.nhis.base.bd.vo.BdPivasruleDeptVO;
import com.zebone.nhis.common.module.base.bd.code.BdAdminDivision;
import com.zebone.nhis.common.module.base.bd.wf.BdPivasrule;
import com.zebone.nhis.common.module.base.bd.wf.BdPivasruleDept;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class VenousLiquorRuleService {
	
	@Autowired
	private VenousLiquorRuleMapper venousLiquorRuleMapper;
	
	//查询静配规则列表:001002006020
	public List<Map<String, Object>> qryRule(String param , IUser user){
		
		String pkOrg = JsonUtil.getFieldValue(param, "pkOrg");
		
		return venousLiquorRuleMapper.qryRule(pkOrg);
	}
	
	//保存静配规则列表:001002006021
	public void saveRule(String param , IUser user){
		
		BdPivasrule bdPivasrule = JsonUtil.readValue(param, BdPivasrule.class);
		
		if(bdPivasrule != null){		
			if(StringUtils.isNotBlank(bdPivasrule.getPkPivasrule())){
				DataBaseHelper.updateBeanByPk(bdPivasrule,false);
			}else{
				DataBaseHelper.insertBean(bdPivasrule);
			}
		}
	}
	
	//查询使用科室列表:001002006022
	public List<Map<String, Object>> qryDept(String param , IUser user){
		
		String pkPivasrule = JsonUtil.getFieldValue(param, "pkPivasrule");
		
		return venousLiquorRuleMapper.qryDept(pkPivasrule);
	}
	
	//查询当前机构所有护理单元列表:001002006023
	public List<Map<String, Object>> qryDeptNs(String param , IUser user){
		
		String pkOrg = JsonUtil.getFieldValue(param, "pkOrg");
		
		return venousLiquorRuleMapper.qryDeptNs(pkOrg);
	}
	
	//保存使用科室列表:001002006024
	public void saveDept(String param , IUser user){
		
		BdPivasruleDeptVO vo = JsonUtil.readValue(param, BdPivasruleDeptVO.class);
		
		//先进行删除，再重新插入
		String sql = "delete from bd_pivasrule_dept where pk_pivasrule = ? and pk_org = ? ";
		DataBaseHelper.execute(sql, vo.getPkPivasrule(),vo.getPkOrg());
		
		List<BdPivasruleDept> list = vo.getList();
		if(list != null && list.size() > 0){
			for (BdPivasruleDept dept : list) {
				dept.setPkPivasruledept(null);
				DataBaseHelper.insertBean(dept);
			}
		}
	}
	
	//删除静配规则:001002006025
	public void delRules(String param , IUser user){
		
		String pkPivasrule = JsonUtil.getFieldValue(param, "pkPivasrule");
		if(StringUtils.isNotBlank(pkPivasrule)){
			DataBaseHelper.execute("delete from bd_pivasrule_dept where pk_pivasrule = ?", pkPivasrule);
			DataBaseHelper.execute("delete from bd_pivasrule where pk_pivasrule = ?", pkPivasrule);
		}
	}
}
