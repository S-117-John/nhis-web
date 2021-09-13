package com.zebone.nhis.bl.pub.service;

import java.util.List;

import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.platform.modules.exception.BusException;

/**
 * 记费服务通用接口(各项目可添加个性化实现)
 * @author yangxue
 *
 */
public interface ICgService {
     /**
      * 住院记费服务
      * @param blCgPubParamVos
      * @param isAllowQF
      * @return
      * @throws BusException
      */
	 public BlPubReturnVo chargeIpBatch(List<BlPubParamVo> blCgPubParamVos,boolean isAllowQF) throws BusException; 
	 /**
	  * 住院退费服务
	  * @param params
	  * @return
	  */
	 public BlPubReturnVo refundInBatch(List<RefundVo> params,List<BlIpDt> dtlist)throws BusException;
	 /**
	  * 门诊记费服务
	  * @param blOpCgPubParamVos
	  * @return
	  * @throws BusException
	  */
	 public BlPubReturnVo chargeOpBatch(List<BlPubParamVo> blOpCgPubParamVos) throws BusException;
	 /**
	  * 根据医保或患者分类更新住院记费明细
	  * @param pkPv
	  * @param pkHp
	  * @param pkPicate
	  * @throws BusException
	  */
	 public void updateBlIpDt(String pkPv,String pkHp,String pkPicate,String oldPkInsu,String oldPkPicate) throws BusException;
	 /**
	  * 根据医保或患者分类更新门诊记费明细
	  * @param pkPv
	  * @param pkHp
	  * @param pkPicate
	  * @throws BusException
	  */
	 public void updateBlOpDt(String pkPv,String pkHp,String pkPicate,String oldPkInsu,String oldPkPicate) throws BusException;
	
}
