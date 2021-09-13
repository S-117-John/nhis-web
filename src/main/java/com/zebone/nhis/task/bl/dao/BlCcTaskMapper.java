package com.zebone.nhis.task.bl.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.task.bl.vo.OthStaMonthUnstRec;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface BlCcTaskMapper {

	/**
	 * 依据拓展属性，查询操作员信息
	 *
	 * @param codeAttr
	 * @return
	 */
	List<BdOuEmployee> queryEmployees(Map<String, Object> params);

	/**
	 * 记录月底未结账患者费用信息
	 */
	List<OthStaMonthUnstRec> queryOthStaMonthUnstRec();
}
