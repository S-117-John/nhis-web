package com.zebone.nhis.arch.service;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.arch.vo.ArchBorrowVo;
import com.zebone.nhis.common.module.arch.PvArchBorrow;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 病历借阅申请，审批服务类
 * @author Roger
 *
 */
@Service
public class ArchBorrowService {
	
	
	/**
	 * 申请借阅
	 * @param param
	 * @param user
	 */
	public void Apply(String param, IUser user) {
		
		List<ArchBorrowVo>  vos =JsonUtil.readValue(param, new TypeReference<List<ArchBorrowVo>>(){});
		if(vos!=null && vos.size()>0){
			for(ArchBorrowVo vo : vos){
				
				String sql = " select count(1) from pv_arch_borrow bo where bo.pk_archive=? and bo.flag_canc='0' and bo.pk_emp_ap=? and bo.date_end>?";
				int cnt = DataBaseHelper.queryForScalar(sql, Integer.class, vo.getPkArchive(),UserContext.getUser().getPkEmp(),new Date ());
				if(cnt>0){
					throw new BusException("存在有效的病历借阅申请，则不能再次申请!");
				}
				
				PvArchBorrow bo = new PvArchBorrow();
				bo.setPkOrg(UserContext.getUser().getPkOrg());
				bo.setPkArchive(vo.getPkArchive());
				bo.setDateAp(new Date());
				bo.setDateEnd(vo.getDateExpire());
				bo.setFlagCanc("0");
				bo.setFlagChk("0");
				bo.setFlagDel("0");
				bo.setEuStatus("0");
				bo.setDays(vo.getDays()+"");
				bo.setPkDept(UserContext.getUser().getPkDept());
				bo.setPkEmpAp(UserContext.getUser().getPkEmp());
				bo.setNameEmpAp(UserContext.getUser().getNameEmp());
				ApplicationUtils.setDefaultValue(bo, true);
				DataBaseHelper.insertBean(bo);				
			}

		}
		
	}
	
	/**
	 * 取消申请
	 * @param param
	 * @param user
	 */
	public void cancelApply(String param, IUser user) {
		List<String>  vo =JsonUtil.readValue(param, new TypeReference<List<String>>(){});
		if(vo!=null && vo.size()>0){
			String sql = " update pv_arch_borrow  set eu_status='9', flag_canc='1',date_canc=? where pk_borrow=?";
			for(String pk : vo){
				DataBaseHelper.execute(sql, new Date(),pk);
			}
		}
		
	}
	
	/**
	 * 借阅审核
	 * @param param
	 * @param user
	 */
	public void Approval(String param,IUser user){
		
		List<ArchBorrowVo>  vos =JsonUtil.readValue(param, new TypeReference<List<ArchBorrowVo>>(){});
		if(vos!=null && vos.size()>0){
			String sql = " update pv_arch_borrow  set eu_status='1',flag_chk='1',"
					+ "date_chk=?,pk_emp_chk=?, name_emp_chk=? where pk_borrow=?";
			Object[] params = new Object[4];
			params[0] = new Date();
			params[1] = UserContext.getUser().getPkEmp();
			params[2] = UserContext.getUser().getNameEmp();
			for(ArchBorrowVo vo : vos){
 				params[3] = vo.getPkBorrow();
 				DataBaseHelper.execute(sql, params);
			}
		}
	}
	
	/**
	 * 取消审核
	 * @param param
	 * @param user
	 */
    public void CancelApproval(String param,IUser user){
		
		List<ArchBorrowVo>  vos =JsonUtil.readValue(param, new TypeReference<List<ArchBorrowVo>>(){});
		if(vos!=null && vos.size()>0){
			String sql = " update pv_arch_borrow  set eu_status='0',flag_chk='0',"
					+ "date_chk=null,pk_emp_chk=null, name_emp_chk=null where pk_borrow=?";
			for(ArchBorrowVo vo : vos){
 				DataBaseHelper.execute(sql, vo.getPkBorrow());
			}
		}
	}

}
