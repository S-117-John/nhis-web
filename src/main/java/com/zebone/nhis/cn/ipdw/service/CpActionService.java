package com.zebone.nhis.cn.ipdw.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.ipdw.dao.CpActionMapper;
import com.zebone.nhis.cn.ipdw.support.Constants;
import com.zebone.nhis.cn.ipdw.vo.CpActionVo;
import com.zebone.nhis.common.module.cn.cp.BdCpAction;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class CpActionService {
	
	@Autowired      
	private CpActionMapper cpAction;
	
	public List<BdCpAction> getDictAction_1(String param,IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		String pkAction = paramMap.get("pkAction");
		if(pkAction == null)
			pkAction = "%";
		//List<BdCpAction> ret = DataBaseHelper.queryForList("", BdCpAction.class, new Object[]{pkAction});
		List<BdCpAction> ret = this.cpAction.getDictAction_1(pkAction);
		return ret;
	}
	
	public List<BdCpAction> getDictAction(String param,IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		String pkAction = paramMap.get("pkAction");
		if(pkAction == null)
			pkAction = "%";
		else
			pkAction = "%" + pkAction + "%";
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("pkAction", pkAction);
		List<BdCpAction> ret = cpAction.getDictAction(m);
		return ret;
	}
	
	public void saveDictAction(String param,IUser user){
		CpActionVo actListAll = JsonUtil.readValue(param, new TypeReference<CpActionVo>(){} );
		//JOptionPane.showMessageDialog(null, "提示信息");
		List<BdCpAction> actionList = actListAll.getActList();
		if  (actionList == null)
			return;
		List<BdCpAction> actionListDel = actListAll.getActListDel();
		if(actionListDel!=null&&actionListDel.size()>0){
			List<String> pkaction = new ArrayList<String>();
			for(BdCpAction cpAction : actionListDel){
				pkaction.add(cpAction.getPkCpaction());
			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("pkCpaction",pkaction);
			List<Map<String,Object>> useAction = DataBaseHelper.queryForList("select distinct(action.code_act),action.name_act "+
					" from cp_temp_work twork "+
					" inner join bd_cp_action action on action.pk_cpaction = twork.pk_cpaction and action.del_flag='0'"+
					" where twork.del_flag='0' and twork.pk_cpaction in(:pkCpaction)", map);
		    if(useAction!=null&&useAction.size()>0){
		    	String actName="";
		    	for(Map<String,Object> actionMap : useAction){
		    		actName+=(String)actionMap.get("nameAct")+"("+(String)actionMap.get("codeAct")+")"+"\n";
		    	}
		    	throw new BusException(actName+"已被使用，不能删除，请刷新！");
		    }
		}
		String sql = "select count(*) from bd_cp_action where pk_org = ? and (code_act = ? or name_act=? )";
		for(int i=0; i<actionList.size(); i++){
			BdCpAction act = actionList.get(i);
			int count = 0;
			String sCode = act.getCodeAct();	
			String sOrg = "";
			if	((sCode == null) || (sCode == ""))
				throw new BusException("编码不能为空！");
			String sName = act.getNameAct();
			if	((sName == null) || (sName.trim() == ""))
				throw new BusException("名称不能为空！");
			String sEuType = act.getEuType();
			String sFunc = act.getFunc();
			if (sFunc == null)
				sFunc = "";			
			if ((sEuType == "1") && (sFunc == ""))
				throw new BusException("当类型是'文档'时，文档类型必填！");
			if(Constants.RT_NEW.equals(act.getRowStatus()))
			{
				sOrg = ((User)user).getPkOrg();
				count = DataBaseHelper.queryForScalar(sql, Integer.class, sOrg, sCode,sName);
				if (count > 0)
				{
					throw new BusException("新增["+sCode+"]或["+sName+"]存在重复数据！");
				}
				act.setDelFlag("0");
				DataBaseHelper.insertBean(act);
			}
			else if(Constants.RT_UPDATE.equals(act.getRowStatus()))
			{
				sOrg = act.getPkOrg();
				count = DataBaseHelper.queryForScalar(sql+" and pk_cpaction!=? ", Integer.class, sOrg, sCode,sName,act.getPkCpaction());
				if (count > 0)
				{
					throw new BusException("修改["+sCode+"]或["+sName+"]存在重复数据！");
				}
				act.setDelFlag("0");
				if(StringUtils.isBlank(act.getEuCalltype()))act.setEuCalltype(""); //null更新不了
				DataBaseHelper.updateBeanByPk(act, false);
				
			}
			else if(Constants.RT_REMOVE.equals(act.getRowStatus()))
				DataBaseHelper.deleteBeanByPk(act);	
		}
		for(int i=0; i<actionListDel.size(); i++){
			BdCpAction act = actionListDel.get(i);
		if(Constants.RT_REMOVE.equals(act.getRowStatus()))
			DataBaseHelper.deleteBeanByPk(act);	
		}
    }
	
}
