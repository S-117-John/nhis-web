package com.zebone.nhis.base.bd.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zebone.nhis.base.bd.dao.BdDiagLabruleMapper;
import com.zebone.nhis.common.module.base.bd.mk.BdDiagLabrule;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class BdDiagLabruleService {
	
	@Autowired
	private BdDiagLabruleMapper bdDiagLabruleMapper;
	
	/**
	 * 保存，修改操作
	 * @param param
	 * @param user
	 */
	public void saveBdDiagLabrule(String param, IUser user){
		BdDiagLabrule bdDiagLabrule = JsonUtil.readValue(param,BdDiagLabrule.class);
		User user1 = (User)user;
		if(bdDiagLabrule.getPkLabrule() == null){
			bdDiagLabrule.setPkOrg(user1.getPkOrg());
			DataBaseHelper.insertBean(bdDiagLabrule);			
		}else{
			DataBaseHelper.updateBeanByPk(bdDiagLabrule,false);
		}
		
	}
	
	/**
	 * 删除操作
	 * @param param
	 * @param user
	 */
	public void delBdDiagLabrule(String param, IUser user){
		String pkLabrule = JsonUtil.getFieldValue(param, "pkLabrule");
		DataBaseHelper.execute("Delete BD_DIAG_LABRULE where PK_LABRULE=?", pkLabrule);
	}
	
	/**
	 * 查询
	 * @param param
	 * @param user
	 */
	public List qryBdDiagLabruleList(String param, IUser user){
		String name = JsonUtil.getFieldValue(param, "name");
		String value = "";						
		if(name != null && name.length()>0){
			value = "%"+name+"%";
		}	
		List<Map<String,Object>> bdDiagLabruleList = bdDiagLabruleMapper.getBdDiagLabruleList(value);
		return bdDiagLabruleList;
		
	}
	
	

}
