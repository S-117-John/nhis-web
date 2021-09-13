package com.zebone.nhis.base.bd.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.bd.vo.BdTicketAddVo;
import com.zebone.nhis.common.module.base.bd.price.BdTicketaddCs;
import com.zebone.nhis.common.module.base.bd.price.BdTicketaddCsdt;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class TicketAddService {

	/**
	 * 保存
	 */
	public void SaveTicketAdd(String param, IUser user) throws BusException {

		// 未判断VO为空的情况下的处理逻辑
		BdTicketAddVo vo = JsonUtil.readValue(param, BdTicketAddVo.class);

		// 就诊加号
		BdTicketaddCs ticketAddCs = vo.getBtacs();

		// 就诊加号明细
		List<BdTicketaddCsdt> ticketAddCsdts = vo.getBtacsdts();
		
		//删除的行主键
		List<String> delPkCsdtList = vo.getDelPkCsdtList();

		//删除明细
		if (delPkCsdtList != null && delPkCsdtList.size() > 0) {
			for(String pk:delPkCsdtList){
				DataBaseHelper.execute("update bd_ticketadd_csdt set del_flag='1' where pk_ticketaddcsdt=? ", new Object[]{pk});
			}
		}

		// 修改
		if (!StringUtils.isBlank(ticketAddCs.getPkTicketaddcs()))
		{
			// 校验重复值
			checkRepeatValue(ticketAddCs, param, user);
			
			DataBaseHelper.updateBeanByPk(ticketAddCs,false);
			for(BdTicketaddCsdt csdt : ticketAddCsdts){
				if(StringUtils.isEmpty(csdt.getPkTicketaddcsdt())){
					DataBaseHelper.insertBean(csdt);
				}else if("1".equals(csdt.getStatus()) && !StringUtils.isEmpty(csdt.getPkTicketaddcsdt())){
					DataBaseHelper.updateBeanByPk(csdt,false);
				}
			}
		} else {// 新增
			// 校验重复值
			checkRepeatValue(ticketAddCs, param, user);

			DataBaseHelper.insertBean(ticketAddCs);
			for (BdTicketaddCsdt dt : ticketAddCsdts) {
				dt.setPkTicketaddcs(ticketAddCs.getPkTicketaddcs());
				DataBaseHelper.insertBean(dt);
			}
		}
	}

	/**
	 * 删除
	 */
	public void delTicketAdd(String param, IUser user) {
		BdTicketaddCs bdTicketaddCs = JsonUtil.readValue(param, BdTicketaddCs.class);
		List<Map<String, Object>> execute = DataBaseHelper.queryForList("select count(1) as num from sch_srv where pk_ticketaddcs=? and del_flag='0'", new Object[] { bdTicketaddCs.getPkTicketaddcs() });
		if (((BigDecimal) execute.get(0).get("num")).intValue() > 0) {
			throw new BusException("该策略已被使用，请先取消该策略的使用!");
		}
		DataBaseHelper.execute("update bd_ticketadd_csdt set del_flag='1' where pk_ticketaddcs=? and del_flag='0'", new Object[] { bdTicketaddCs.getPkTicketaddcs() });
		DataBaseHelper.execute("update bd_ticketadd_cs set del_flag='1' where pk_ticketaddcs=? and del_flag='0'", new Object[] { bdTicketaddCs.getPkTicketaddcs() });
	}

	/**
	 * 查询左侧树
	 */
	public List<Map<String, Object>> findBdTicketaddCs(String param, IUser user) {
		List<Map<String, Object>> list = DataBaseHelper.queryForList("select acs.pk_ticketaddcs,acs.code_cs,acs.name_cs,acs.note,acs.pk_org from bd_ticketadd_cs acs where acs.pk_org=? and acs.del_flag='0' order by create_time asc", new Object[] { ((User) user).getPkOrg() });
		return list;
	}

	/**
	 * 查询右侧详细
	 */
	public List<Map<String, Object>> findBdTicketaddCsdt(String param, IUser user) {
		BdTicketaddCs bdTicketaddCs = JsonUtil.readValue(param, BdTicketaddCs.class);
		List<Map<String, Object>> queryForList = DataBaseHelper.queryForList("select dt.pk_ticketaddcsdt,dt.beginno, dt.endno,dt.pk_item,dt.date_begin,dt.date_end,item.price,dt.note from bd_ticketadd_csdt dt inner join bd_item item on dt.pk_item=item.pk_item where dt.pk_ticketaddcs=? and dt.del_flag='0'", new Object[] { bdTicketaddCs.getPkTicketaddcs() });
		return queryForList;
	}

	/**
	 * 校验重复值
	 * 
	 * @param ticketAddCs
	 * @param param
	 * @param user
	 */
	private void checkRepeatValue(BdTicketaddCs ticketAddCs, String param, IUser user) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (!StringUtils.isBlank(ticketAddCs.getPkTicketaddcs())) {// 修改校验
			list = DataBaseHelper.queryForList("select acs.pk_ticketaddcs,acs.code_cs,acs.name_cs,acs.note,acs.pk_org from bd_ticketadd_cs acs where acs.pk_org=? and acs.del_flag='0' and acs.pk_ticketaddcs!=? ", new Object[] { ((User) user).getPkOrg(), ticketAddCs.getPkTicketaddcs() });
		} else {// 新增校验
			list = findBdTicketaddCs(param, user);
		}
		for (Map<String, Object> m : list) {
			if (ticketAddCs.getCodeCs().equals(m.get("codeCs"))) {
				throw new BusException("您的规则编码重复!");
			}
			if (ticketAddCs.getNameCs().equals(m.get("nameCs"))) {
				throw new BusException("您的规则名称重复!");
			}
		}
	}
}
