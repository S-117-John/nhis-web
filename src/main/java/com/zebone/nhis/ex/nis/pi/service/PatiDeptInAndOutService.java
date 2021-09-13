package com.zebone.nhis.ex.nis.pi.service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.nis.pi.vo.BdDaycgSetItemVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 入科及转科患者业务操作类
 * @author yangxue
 *
 */
@Service
public class PatiDeptInAndOutService {
	
    /**
     * 查询科室定义下的固定费用
     * @param param{pkDeptNs的值}
     * @param user
     * @return
     */
    public List<BdDaycgSetItemVo> queryDayCgItem(String param,IUser user){
    	Map map = JsonUtil.readValue(param, Map.class);
    	String pkDeptNs = CommonUtils.getString(map.get("pkDeptNs"));
    	String hpcode = CommonUtils.getString(map.get("hpcode"));		
    	User u = (User)user;
    	if(CommonUtils.isEmptyString(pkDeptNs)) throw new BusException("未获取到科室主键！");
    	StringBuilder sql = new StringBuilder("select item.*,bdit.name,bdit.price,bdit.code from bd_daycg_set_item item inner join bd_daycg_set daycg on daycg.pk_daycgset = item.pk_daycgset ");
    	sql.append(" inner join bd_item bdit on bdit.pk_item = item.pk_item ");
    	sql.append(" where daycg.pk_dept = ? and daycg.eu_type = '1' and daycg.pk_org = '");
    	sql.append(u.getPkOrg());
    	sql.append("' and daycg.del_flag='0' and item.del_flag='0' ");
    	sql.append(" and (daycg.code_hps is null or (daycg.code_hps like  '%,"+hpcode+",%' ))");
    	List<BdDaycgSetItemVo> result = DataBaseHelper.queryForList(sql.toString(), BdDaycgSetItemVo.class, new Object[]{pkDeptNs});
    	if(result == null || result.size()<=0){
    		sql = new StringBuilder("select item.*,bdit.name,bdit.price,bdit.code  from bd_daycg_set_item item inner join bd_daycg_set daycg on daycg.pk_daycgset = item.pk_daycgset ");
    		sql.append(" inner join bd_item bdit on bdit.pk_item = item.pk_item ");
    		sql.append(" where daycg.eu_type = '0' and daycg.pk_org = '");
    		sql.append(u.getPkOrg());
    		sql.append("' and daycg.del_flag='0' and item.del_flag='0' ");
    		sql.append(" and (daycg.code_hps is null or (daycg.code_hps like '%,"+hpcode+",%' )) ");
    		result = DataBaseHelper.queryForList(sql.toString(),BdDaycgSetItemVo.class,new Object[]{});
    	}
    	return result;
    }
   
   
}
