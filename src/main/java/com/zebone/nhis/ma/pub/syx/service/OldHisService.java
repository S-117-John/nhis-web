package com.zebone.nhis.ma.pub.syx.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;
import com.zebone.nhis.common.module.cn.ipdw.ExOpSch;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.syx.dao.OldHisMapper;
import com.zebone.nhis.ma.pub.syx.vo.ExOpSchVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class OldHisService {
	@Autowired
	private OldHisMapper oldHisMapper;

	// 查询老系统的手术申请
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<ExOpSchVo> qryOpApplys(Map<String, Object> paramMap) throws Exception {
		return oldHisMapper.qryOpApplys(paramMap);
	}
	// 查询HIS手术申请
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<ExOpSchVo> qryOpApplysHIS(Map<String, Object> paramMap) throws Exception {
		return oldHisMapper.qryOpApplysHIS(paramMap);
	}
	
	public void updateInfectedFlag(Map<String, Object> paramMap) throws Exception {
		DataBaseHelper.update("update PV_IP set flag_infected='1' where PK_PV=?",new Object[] { paramMap.get("pkPv") });
	}

	// 保存手术安排
	public void saveOpSch(String param, IUser user) {
		List<ExOpSchVo> exOpSchList = JsonUtil.readValue(param, new TypeReference<List<ExOpSchVo>>() {
		});
		List<ExOpSch> exOpSchLists = JsonUtil.readValue(param, new TypeReference<List<ExOpSch>>() {
		});
		Date dateNow = new Date();
		for (ExOpSchVo opSch : exOpSchList) {
			if (StringUtils.isNotBlank(opSch.getPkCnord())) {
				DataBaseHelper.execute("update cn_op_apply set eu_status = '2',pk_emp_anae=?,name_emp_anae=?,pk_emp_circul=?,name_emp_cricul=?,pk_emp_scrub=?,name_emp_scrub=? where pk_cnord =? and eu_status <'2' ", opSch.getPkEmpAnae(),opSch.getNameEmpAnae(),opSch.getPkEmpCircul(),opSch.getNameEmpCircul(),opSch.getPkEmpScrub(),opSch.getNameEmpScrub(),opSch.getPkCnord());
				DataBaseHelper.execute("update CN_ORDER set PK_DEPT_EXEC =? where pk_cnord =?",opSch.getPkDeptExec(),opSch.getPkCnord());
			}
		}
		for (ExOpSch opSchs : exOpSchLists) {
			if (StringUtils.isBlank(opSchs.getPkOpsch())) {
				opSchs.setEuStatus("1");
				opSchs.setTs(dateNow);
				opSchs.setCreateTime(dateNow);
				opSchs.setModityTime(dateNow);
				DataBaseHelper.insertBean(opSchs);
			} else {
				DataBaseHelper.updateBeanByPk(opSchs, false);
			}
		}
	}

	// 取消手术安排
	public void cancleOpSch(String param, IUser user) throws Exception {
		List<ExOpSch> exOpSchList = JsonUtil.readValue(param, new TypeReference<List<ExOpSch>>() {
		});
		// 查询申请单不在排班状态的手术
		Set<String> pkCnOrds = Sets.newHashSet();
		for (ExOpSch sch : exOpSchList) {
			pkCnOrds.add(sch.getPkCnord());
		}
		String sql = "select ex.* from CN_OP_APPLY opa inner join EX_OP_SCH ex on ex.PK_CNORD=opa.PK_CNORD and ex.EU_STATUS<>9 where (opa.EU_STATUS='3' or opa.EU_STATUS='5') and ex.PK_CNORD in (" + CommonUtils.convertSetToSqlInPart(pkCnOrds, "pk_cnord") + ")";
		List<ExOpSch> finOp = DataBaseHelper.queryForList(sql, ExOpSch.class);
		if (finOp != null && finOp.size() > 0) {
			String exInfo = "";
			for (ExOpSch op : finOp) {
				exInfo += "住院号：" + op.getCodeIp() + "，患者：" + op.getNamePi() + "\n";
			}
			exInfo += "手术已完成，不可取消排班！";
			throw new BusException(exInfo);
		}

		Map<String, Object> mapParam = new HashMap<String, Object>();
		Date d = new Date();
		User u = (User) user;
		mapParam.put("dateCanc", d);
		mapParam.put("pkEmpCanc", u.getPkEmp());
		mapParam.put("nameEmpCanc", u.getNameEmp());

		for (ExOpSch opSch : exOpSchList) {
			mapParam.put("pkOpsch", opSch.getPkOpsch());
			DataBaseHelper.update("update ex_op_sch set eu_status='9',date_canc= :dateCanc,pk_emp_canc= :pkEmpCanc,name_emp_canc= :nameEmpCanc where pk_opsch= :pkOpsch and eu_status<='2' ", mapParam);
			if (StringUtils.isNotBlank(opSch.getPkCnord())) {
				DataBaseHelper.update("update cn_op_apply set eu_status = '1' where pk_cnord =? ", new Object[] { opSch.getPkCnord() });
			}
		}

	}


}
