package com.zebone.nhis.bl.pub.service.impl;

import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.service.IYBSettleService;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.bl.pub.vo.SettleInfo;
@Service("lbYBSettleService")
public class LbYBSettleService implements IYBSettleService {

	/**
	 * 灵璧结算暂时无特殊需求
	 */
	@Override
	public void dealYBSettleMethod(SettleInfo settlevo, BlSettle stVo) {
		
		
	}

}
