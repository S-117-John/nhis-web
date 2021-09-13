package com.zebone.nhis.base.drg.service;

import com.zebone.nhis.common.module.base.bd.mk.BdEhpQcrule;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BdEhpQcruleService {

	/**
	 * 保存，修改操作
	 * @param param
	 * @param user
	 */
	public int saveBdEhpQcrule(String param, IUser user){
		BdEhpQcrule bdEhpQcrule = JsonUtil.readValue(param,BdEhpQcrule.class);
		int count = 0;
		if(bdEhpQcrule.getPkQcrule() == null){
			ApplicationUtils.setDefaultValue(bdEhpQcrule, true);
			bdEhpQcrule.setPkOrg("~");
			count = DataBaseHelper.insertBean(bdEhpQcrule);
		}else{
			ApplicationUtils.setDefaultValue(bdEhpQcrule, false);
			count = DataBaseHelper.updateBeanByPk(bdEhpQcrule,false);
		}
		return  count;
	}
	
	/**
	 * 删除操作
	 * @param param
	 * @param user
	 */
	public void delBdEhpQcrule(String param, IUser user){
		String pkQcrule = JsonUtil.getFieldValue(param, "pkQcrule");
		if("null".equals(pkQcrule) || StringUtils.isBlank(pkQcrule)){
			return;
		}
		String sql = "SELECT COUNT(1) num from BD_EHP_QCITEM_RULE qcle left JOIN BD_EHP_QCITEM qcit on qcle.PK_QCITEM = qcit.PK_QCITEM where qcle.pk_qcrule = ? and qcit.DEL_FLAG ='0' ";
		Map<String, Object> data = DataBaseHelper.queryForMap(sql, pkQcrule);
		if(!data.isEmpty() && !data.get("num").toString().equals("0")){
			throw new BusException("该数据已经被质控项目引用，需要先删除质控项目！");
		}
		DataBaseHelper.update("update BD_EHP_QCRULE set del_flag='1' where pk_qcrule=? and del_flag='0'", new Object[]{pkQcrule});
	}

	/**
	 * 恢复操作
	 * @param param
	 * @param user
	 */
	public void recBdEhpQcrule(String param, IUser user){
		String pkQcrule = JsonUtil.getFieldValue(param, "pkQcrule");
		if("null".equals(pkQcrule) || StringUtils.isBlank(pkQcrule)){
			return;
		}
		DataBaseHelper.update("update BD_EHP_QCRULE set del_flag='0' where pk_qcrule=? and del_flag='1'", new Object[]{pkQcrule});
	}
	/**
	 * 查询
	 * @param param
	 * @param user
	 */
	public List<BdEhpQcrule> qryBdEhpQcruleList(String param, IUser user){
		String nameRule = JsonUtil.getFieldValue(param, "nameRule");
		String delFlag = JsonUtil.getFieldValue(param, "delFlag");
		StringBuilder sql = new StringBuilder(" select * from BD_EHP_QCRULE where 1=1 ");
		if(StringUtils.isNotBlank(delFlag)){
			if("0".equals(delFlag)){
				sql.append(" and DEL_FLAG = '0'");
			}else if("1".equals(delFlag)){
				sql.append(" and DEL_FLAG = '1'");
			}

		}
		if(!"null".equals(nameRule) && StringUtils.isNotEmpty(nameRule)) {
			sql.append(" and ( spcode like '%"+nameRule+"' or NAME_RULE like '%"+nameRule+"%')");
		}
		sql.append(" ORDER BY SORTNO,CODE_RULE");
		List<BdEhpQcrule> bdEhpQcruleList = DataBaseHelper.queryForList(sql.toString(),BdEhpQcrule.class);
		return bdEhpQcruleList;
		
	}
	


}
