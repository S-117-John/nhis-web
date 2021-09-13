package com.zebone.nhis.ma.lb.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.bd.price.BdInvcate;
import com.zebone.nhis.ma.lb.dao.InvLbMapper;
import com.zebone.nhis.ma.lb.vo.InvLbVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * lb使用的发票查询相关操作
 * @author dell
 *
 */

@Service
public class InvLbService {

	@Resource
	private InvLbMapper invLbMapper;
	
	public List<InvLbVo> getEmpInvoices(String param, IUser user) {
		@SuppressWarnings("unchecked")
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		paramMap.put("pkOrg", ((User)user).getPkOrg());
		return invLbMapper.queryInv(paramMap);
	}
	
	public void returnedEmpInvoice(String param, IUser user){
		DataBaseHelper.update("update bl_emp_invoice set end_no=cur_no-1,flag_active='0',flag_use='0',cnt_use=0,modifier=? where pk_empinv=? and cnt_use>0", 
				new Object[]{UserContext.getUser().getPkEmp(),JsonUtil.getFieldValue(param, "pkEmpinv")});
	}
	
	public void delEmpInvoice(String param, IUser user) {

		String pkEmpinv = JsonUtil.getFieldValue(param, "pkEmpinv");
		Map<String, Object> empinv = DataBaseHelper.queryForMap("select * from bl_emp_invoice where del_flag = '0' and pk_empinv = ?", pkEmpinv);
		Long invCount = Long.parseLong(empinv.get("invCount").toString());
		Long cntUse = Long.parseLong(empinv.get("cntUse").toString());
		String flagActive = empinv.get("flagActive").toString();
		int count = DataBaseHelper.queryForScalar("select count(1) from bl_invoice where del_flag = '0' and pk_empinvoice = ?", Integer.class, pkEmpinv);
		if (invCount.equals(cntUse) && "1".equals(flagActive) && count == 0) {
			DataBaseHelper.update("delete from bl_emp_invoice where pk_empinv=? and begin_no=cur_no", new Object[] { pkEmpinv });
		} else {
			if (!(invCount.equals(cntUse))) {
				throw new BusException("该票据领用已被使用过！");
			}
			if ("0".equals(flagActive)) {
				throw new BusException("该票据领用当前不可用！");
			}
			if (count != 0) {
				throw new BusException("该票据领用被发票记录引用！");
			}
		}

	}
	
	public List<Map<String,Object>> getEmpInvHistory(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		BdInvcate invcate = DataBaseHelper.queryForBean(
				"select i.eu_type from bl_emp_invoice ei inner join bd_invcate i on i.pk_invcate = ei.pk_invcate where ei.pk_empinv=?",
				BdInvcate.class, new Object[] {MapUtils.getString(paramMap, "pkEmpinv")});
		if(invcate == null){
			throw new BusException("没有查到对应票据分类");
		}
		
		if(Arrays.asList("0","1","3").contains(invcate.getEuType())) {
			return invLbMapper.queryInvHistory(paramMap);
		}else if("5".equals(invcate.getEuType())){
			return invLbMapper.queryInvHistoryByDeposit(paramMap);
		}else{
			throw new BusException("对应票据分类EuType不是[0,1,3,5]");
		}
	}
}
