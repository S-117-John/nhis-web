package com.zebone.nhis.cn.ipdw.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.ipdw.dao.CpExpMapper;
import com.zebone.nhis.cn.ipdw.support.Constants;
import com.zebone.nhis.cn.ipdw.vo.CpExpVo;
import com.zebone.nhis.common.module.cn.cp.BdCpExp;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class CpExpService {
	
	@Autowired      
	private CpExpMapper cpExp;
	
	public List<BdCpExp> getDictCpExp(String param,IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		String pkExp = paramMap.get("pkExp");
		if(pkExp == null)
			pkExp = "%";
		else
			pkExp = "%" + pkExp + "%";
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("pkExp", pkExp);
		List<BdCpExp> ret = cpExp.getDictCpExp(m);
		return ret;
	}
	
	public void saveDictCpExp(String param,IUser user){
		CpExpVo cpexpListAll = JsonUtil.readValue(param, new TypeReference<CpExpVo>(){} );
	
		List<BdCpExp> cpexpList = cpexpListAll.getCpexpList();
		if  (cpexpList == null)
			return;
		List<BdCpExp> cpexpListDel = cpexpListAll.getCpexpListDel();
		if(cpexpListDel!=null&&cpexpListDel.size()>0){
			List<String> pkExp = new ArrayList<String>();
			for(BdCpExp cpExp : cpexpListDel){
				pkExp.add(cpExp.getPkCpexp());
			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("pkCpexp",pkExp);
			List<Map<String,Object>> useExp = DataBaseHelper.queryForList("select distinct(bdexp.code_exp),bdexp.name_exp "+ 
				" from cp_rec_exp  recexp "+
				    " inner join bd_cp_exp bdexp on bdexp.pk_cpexp = recexp.pk_cpexp and bdexp.del_flag='0' "+
				" where recexp.del_flag='0' and recexp.pk_cpexp in(:pkCpexp)", map);
		    if(useExp!=null&&useExp.size()>0){
		    	String expName="";
		    	for(Map<String,Object> expMap : useExp){
		    		expName+=(String)expMap.get("nameExp")+"("+(String)expMap.get("codeExp")+")"+"\n";
		    	}
		    	throw new BusException(expName+"已被使用，不能删除，请刷新！");
		    }
		}
		String sql = "select count(*) from bd_cp_exp where pk_org = ? and (code_exp = ? or name_exp=? )";
		for(int i=0; i<cpexpList.size(); i++){
			BdCpExp act = cpexpList.get(i);
			//try
			{
				int count = 0;
				String sCode = act.getCodeExp();
				String sOrg = "";
				if	((sCode == null) || (sCode == ""))
					throw new BusException("编码不能为空！");
				String sName = act.getNameExp();
				if	((sName == null) || (sName.trim() == ""))
					throw new BusException("名称不能为空！");
				if(Constants.RT_NEW.equals(act.getRowStatus()))
				{
					//sOrg = ((User)user).getPkOrg();
					sOrg = "~";
					count = DataBaseHelper.queryForScalar(sql, Integer.class, sOrg, sCode,sName );
					if (count > 0)
					{
						throw new BusException("新增["+sCode+"]或["+sName+"]存在重复数据！");
					}
					act.setDelFlag("0");
					act.setPkOrg(sOrg);
					DataBaseHelper.insertBean(act);
				}
				else if(Constants.RT_UPDATE.equals(act.getRowStatus()))
				{
					sOrg = act.getPkOrg();
					count = DataBaseHelper.queryForScalar(sql+" and pk_cpexp!=? ", Integer.class, sOrg, sCode,sName,act.getPkCpexp());
					if (count > 0)
					{
						throw new BusException("修改["+sCode+"]或["+sName+"]存在重复数据！");
					}
					act.setDelFlag("0");
					DataBaseHelper.updateBeanByPk(act, false);
				}
				else if(Constants.RT_REMOVE.equals(act.getRowStatus()))
					DataBaseHelper.deleteBeanByPk(act);	
			}
//			catch(Exception)
//			{
//				
//			}
		}
		for(int i=0; i<cpexpListDel.size(); i++){
			BdCpExp act = cpexpListDel.get(i);
		if(Constants.RT_REMOVE.equals(act.getRowStatus()))
			DataBaseHelper.deleteBeanByPk(act);	
		}
}	
	
}

