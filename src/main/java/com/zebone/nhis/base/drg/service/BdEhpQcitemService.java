package com.zebone.nhis.base.drg.service;

import com.zebone.nhis.base.drg.vo.BdEhpQcitemSaveParam;
import com.zebone.nhis.common.module.base.bd.drg.BdTermCchiDept;
import com.zebone.nhis.common.module.base.bd.mk.BdEhpQcitem;
import com.zebone.nhis.common.module.base.bd.mk.BdEhpQcitemRule;
import com.zebone.nhis.common.module.base.bd.mk.BdEhpQcrule;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BdEhpQcitemService {

	/**
	 * 保存，修改操作
	 * @param param
	 * @param user
	 */
	public int saveBdEhpQcitem(String param, IUser user){
		BdEhpQcitemSaveParam saveParam = JsonUtil.readValue(param,BdEhpQcitemSaveParam.class);
		BdEhpQcitem bdEhpQcitem=saveParam.getBdEhpQcitem();
		List<BdEhpQcitemRule> addList = saveParam.getAddBdEhpQcitemRuleList();
		List<BdEhpQcitemRule> delList = saveParam.getDelBdEhpQcitemRuleList();
		List<BdEhpQcitemRule> addListSave =new ArrayList<>();
		List<BdEhpQcitemRule> updateListSave =new ArrayList<>();
		//先删除list中的数据
		this.delBdEhpQcitemRuleList(delList);
		int count = 0;
		if(bdEhpQcitem.getPkQcitem() == null){
			ApplicationUtils.setDefaultValue(bdEhpQcitem, true);
			bdEhpQcitem.setPkOrg("~");
			count = DataBaseHelper.insertBean(bdEhpQcitem);
			for (BdEhpQcitemRule b : addList) {
				ApplicationUtils.setDefaultValue(b, true);
				b.setPkOrg("~");
				b.setPkQcitem(bdEhpQcitem.getPkQcitem());
				addListSave.add(b);
			}
		}else{
			ApplicationUtils.setDefaultValue(bdEhpQcitem, false);
			count = DataBaseHelper.updateBeanByPk(bdEhpQcitem,false);
			for (BdEhpQcitemRule b : addList) {
				if(StringUtils.isEmpty(b.getPkQcitemrule())){
					ApplicationUtils.setDefaultValue(b, true);
					b.setPkOrg("~");
					b.setPkQcitem(bdEhpQcitem.getPkQcitem());
					addListSave.add(b);
				}else{
					updateListSave.add(b);
				}
			}
		}
		if(addListSave.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdEhpQcitemRule.class), addListSave);
		}
		if(updateListSave.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BdEhpQcitemRule.class), updateListSave);
		}
		return  count;
	}
	/**
	 * 删除列表
	 * @param delQcitems
	 */
	private void delBdEhpQcitemRuleList(List<BdEhpQcitemRule> delQcitems){
		if(null!=delQcitems && delQcitems.size()>0){
			Set<String> pkQcitemrules = new HashSet<>();
			for(BdEhpQcitemRule b:delQcitems){
				if(!StringUtils.isEmpty(b.getPkQcitemrule())){
					pkQcitemrules.add(b.getPkQcitemrule());
				}
			}
			if(pkQcitemrules.size() > 0){
				DataBaseHelper.execute(
						"delete from BD_EHP_QCITEM_RULE  where PK_QCITEMRULE in ("+ CommonUtils.convertSetToSqlInPart(pkQcitemrules, "PK_QCITEMRULE")+")");
			}
		}

	}
	/**
	 * 删除操作
	 * @param param
	 * @param user
	 */
	public void delBdEhpQcitem(String param, IUser user){
		String pkQcitem = JsonUtil.getFieldValue(param, "pkQcitem");
		if("null".equals(pkQcitem) || StringUtils.isBlank(pkQcitem)){
			return;
		}
		DataBaseHelper.update("update BD_EHP_QCITEM set del_flag='1' where PK_QCITEM=? and del_flag='0'", new Object[]{pkQcitem});
	}

	/**
	 * 恢复操作
	 * @param param
	 * @param user
	 */
	public void recBdEhpQcitem(String param, IUser user){
		String pkQcitem = JsonUtil.getFieldValue(param, "pkQcitem");
		if("null".equals(pkQcitem) || StringUtils.isBlank(pkQcitem)){
			return;
		}
		DataBaseHelper.update("update BD_EHP_QCITEM set del_flag='0' where PK_QCITEM=? and del_flag='1'", new Object[]{pkQcitem});
	}
	/**
	 * 查询
	 * @param param
	 * @param user
	 */
	public List<BdEhpQcitem> qryBdEhpQcitemList(String param, IUser user){
		String nameQc = JsonUtil.getFieldValue(param, "nameQc");
		String delFlag = JsonUtil.getFieldValue(param, "delFlag");
		StringBuilder sql = new StringBuilder(" select * from BD_EHP_QCITEM where 1=1 ");
		if(StringUtils.isNotBlank(delFlag)){
			if("0".equals(delFlag)){
				sql.append(" and DEL_FLAG = '0'");
			}else if("1".equals(delFlag)){
				sql.append(" and DEL_FLAG = '1'");
			}

		}
		if(!"null".equals(nameQc) && StringUtils.isNotEmpty(nameQc)) {
			sql.append(" and ( spcode like '%"+nameQc+"' or NAME_QC like '%"+nameQc+"%')");
		}
		sql.append(" ORDER BY SORTNO,CODE_QC");
		List<BdEhpQcitem> bdEhpQcruleList = DataBaseHelper.queryForList(sql.toString(),BdEhpQcitem.class);
		return bdEhpQcruleList;
		
	}
	/**
	 * 查询
	 * @param param
	 * @param user
	 */
	public List<Map<String,Object>> qryBdEhpQcitemRuleByPkQcitem(String param, IUser user){
		String  pkQcitem = JsonUtil.getFieldValue(param, "pkQcitem");
		List<Map<String,Object>> bdList = DataBaseHelper.queryForList(" select bd.*,qcg.FLAG_ACTIVE from BD_EHP_QCITEM_RULE bd left join BD_EHP_QCRULE qcg on qcg.PK_QCRULE =bd.PK_QCRULE  where bd.PK_QCITEM = '"+pkQcitem+"' and bd.DEL_FLAG = '0' ");
		return bdList;
	}
	

}
