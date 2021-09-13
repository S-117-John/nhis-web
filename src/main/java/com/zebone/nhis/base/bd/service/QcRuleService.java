package com.zebone.nhis.base.bd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.bd.dao.QcRuleMapper;
import com.zebone.nhis.common.module.base.bd.srv.BdQcRule;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 分诊规则
 * 
 * @author haohan
 * 
 */
@Service
public class QcRuleService {

	@Autowired
	private QcRuleMapper qcRuleMapper;

	/**
	 * 保存分诊规则 003003001001
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public BdQcRule saveQcRule(String param, IUser user) {
		BdQcRule bdQcRule = JsonUtil.readValue(param, BdQcRule.class);

		Integer count = -1;
		if (bdQcRule.getPkQcrule() == null || "".equals(bdQcRule.getPkQcrule())) { // 新增
			count = DataBaseHelper.queryForScalar("select count(1) from bd_qc_rule where code_rule=? or name_rule=?", Integer.class, new Object[] { bdQcRule.getCodeRule(), bdQcRule.getNameRule() });
			DataBaseHelper.insertBean(bdQcRule);
		} else {// 修改
			count = DataBaseHelper.queryForScalar("select count(1) from bd_qc_rule where (code_rule=? or name_rule=?) and pk_qcrule <> ?", Integer.class, new Object[] { bdQcRule.getCodeRule(), bdQcRule.getNameRule(), bdQcRule.getPkQcrule() });
			DataBaseHelper.updateBeanByPk(bdQcRule, false);
		}
		if (count > 0) {
			throw new BusException("编码或名称重复！");
		}
		return bdQcRule;
	}

	/**
	 * 查询分诊规则 003003001002
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdQcRule> qryQcRule(String param, IUser user) {
		List<BdQcRule> bdQcRules = qcRuleMapper.qryQcRule(UserContext.getUser().getPkOrg());
		return bdQcRules;
	}

	/**
	 * 删除分诊规则 003003001003
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public void delQcRule(String param, IUser user) {
		String pkQcrule = JsonUtil.getFieldValue(param, "pkQcrule");
		// 数据校验，返回值大于0不可删除
		Integer count = DataBaseHelper.queryForScalar("select count(1) as count from bd_qc_que where pk_qcrule=?", Integer.class, pkQcrule);
		if (count != null && count > 0) {
			throw new BusException("该规则已被使用，请先取消使用再删除！");
		}

		DataBaseHelper.execute("delete from bd_qc_rule where pk_qcrule=?", pkQcrule);
	}
}
