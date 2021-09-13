package com.zebone.nhis.bl.pub.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.zebone.nhis.bl.pub.service.ICgLabService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.service.ICgService;
import com.zebone.nhis.bl.pub.syx.service.IpCgPubSyxService;
import com.zebone.nhis.bl.pub.syx.service.OpCgPubSyxService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.platform.modules.exception.BusException;
/**
 * 中山二院记费公共服务
 * @author yangxue
 *
 */
@Service("SyxCgService")
public  class SyxCgPubService implements ICgService {
    @Resource
	private IpCgPubSyxService ipCgPubSyxService;
    @Resource
    private OpCgPubSyxService opCgPubSyxService;
	@Override
	public  BlPubReturnVo chargeIpBatch(List<BlPubParamVo> blCgPubParamVos,boolean isAllowQF)
			throws BusException{
		return ipCgPubSyxService.chargeIpBatch(blCgPubParamVos,isAllowQF);
	}

	@Override
	public  BlPubReturnVo chargeOpBatch(List<BlPubParamVo> blOpCgPubParamVos)
			throws BusException{
		ICgLabService cgLabService = ServiceLocator.getInstance().getBean(ApplicationUtils.getPropertyValue("cg.processLabClass","defaultCgLabService")
				, ICgLabService.class);
		cgLabService.dealSampAndTubeItem(blOpCgPubParamVos);
		return opCgPubSyxService.chargeOpBatch(blOpCgPubParamVos);
	}

	@Override
	public void updateBlIpDt(String pkPv, String pkHp, String pkPicate,String oldPkInsu,String oldPkPicate)
			throws BusException {
		ipCgPubSyxService.updateBlIpDtCgRate(pkPv,pkHp,pkPicate,oldPkInsu,oldPkPicate);
	}

	@Override
	public void updateBlOpDt(String pkPv, String pkHp, String pkPicate,String oldPkInsu,String oldPkPicate)
			throws BusException {
		opCgPubSyxService.updateBlOpDtCgRate(pkPv, pkHp, pkPicate, oldPkInsu, oldPkPicate);		
	}

	@Override
	public BlPubReturnVo refundInBatch(List<RefundVo> params,List<BlIpDt> dtlist)throws BusException {
		return ipCgPubSyxService.refundInBatch(params, dtlist);
	}

}
