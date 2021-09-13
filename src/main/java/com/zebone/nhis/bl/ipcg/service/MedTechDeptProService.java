package com.zebone.nhis.bl.ipcg.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.ipcg.dao.MedTechDeptProMapper;
import com.zebone.nhis.common.module.base.bd.srv.BdOrd;
import com.zebone.nhis.common.module.base.bd.srv.BdOrdDept;
import com.zebone.nhis.common.module.base.bd.srv.BdOrdtype;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class MedTechDeptProService {

	@Autowired
	private MedTechDeptProMapper medTechDeptProMapper;
	
	/**
	 * 查询当前科室的项目
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdOrd> qryPro(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, new TypeReference<Map<String,Object>>(){		
		});
		User u = UserContext.getUser();
		String pkDept = u.getPkDept();
		map.put("pkDept", pkDept);
		List<BdOrd> qryOrd = medTechDeptProMapper.qryPro(map);
		return qryOrd;
	}
	
	/**
	 * 删除当前科室项目
	 * @param param
	 * @param user
	 */
	public void delByOrdDept(String param,IUser user){
		List<BdOrd> map = JsonUtil.readValue(param, new TypeReference<List<BdOrd>>(){});
		if(map == null){
			throw new BusException("请勾选一条记录");
		}
		else{
		Map<String,Object> pkMap = new HashMap<String,Object>();
		List<String> poList = new ArrayList<>();
		User u = UserContext.getUser();
		String pkDept = u.getPkDept();
		for (BdOrd m : map) {
			String pkOrd = m.getPkOrd();
			poList.add(pkOrd);
		}
		pkMap.put("pkDept", pkDept);
		pkMap.put("poList", poList);
		int delByOrdDept = medTechDeptProMapper.delByOrdDept(pkMap);
		}
	}
	
	/**
	 * 查询医嘱类型
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdOrdtype> qryOrdtype(String param,IUser user){
		
		List<BdOrdtype> qryOrdtype = medTechDeptProMapper.qryOrdtype();
		return qryOrdtype;
	}
	
	/**
	 * 查询可导入的医嘱项目
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdOrd> qryOrd(String param,IUser user){
		Map<String, Object> map = JsonUtil.readValue(param, new TypeReference<Map<String,Object>>(){		
		});
		Map<String,Object> m = new HashMap<String,Object>();
		String pkOrdorg = (String)map.get("pkOrdorg");
		BdOrdtype queryForBean = DataBaseHelper.queryForBean("select * from bd_ordtype where pk_ordtype = ?", BdOrdtype.class, new Object[]{pkOrdorg});
		User u = UserContext.getUser();
		String pkDept = u.getPkDept();
		String pkOrg = u.getPkOrg();
		String codeOrdtype = queryForBean.getCode();
		m.put("pkDept", pkDept);
		m.put("pkOrg", pkOrg);
		m.put("codeOrdtype", codeOrdtype);
		List<BdOrd> qryOrd = medTechDeptProMapper.qryOrd(m);
		return qryOrd;
	}
	
	/**
	 * 导入项目
	 * @param param
	 * @param user
	 */
	public void saveOrdDept(String param,IUser user){
		List<BdOrd> bod = JsonUtil.readValue(param, new TypeReference<List<BdOrd>>(){});
		User u = UserContext.getUser();
		for (BdOrd bdOrd : bod) {
			BdOrdDept bdOrdDept = new BdOrdDept();
			Map<String, Object> queryForMap = DataBaseHelper.queryForMap("select PK_ORDORG pkOrdorg from BD_ORD_ORG inner join bd_ord bo on bo.pk_ord = BD_ORD_ORG.pk_ord where bo.pk_ord = ?", new Object[]{bdOrd.getPkOrd()});
			bdOrdDept.setPkOrd(bdOrd.getPkOrd());
			bdOrdDept.setPkOrg(u.getPkOrg());
			bdOrdDept.setPkOrgExec(u.getPkOrg());
			bdOrdDept.setPkDept(u.getPkDept());
			bdOrdDept.setFlagDef("0");
			String pkOrdorg = (String)queryForMap.get("pkordorg");
			bdOrdDept.setPkOrdorg(pkOrdorg);
			DataBaseHelper.insertBean(bdOrdDept);
		}	
	}
}
