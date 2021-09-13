package com.zebone.nhis.scm.pub.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.scm.pub.vo.PdOutDtParamVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface DeDrugExtPubMapper {

	/**
	 * 查询物品库存价格
	 * @param paramMap
	 * @return
	 */
    public List<PdOutDtParamVo> getPdStorePrice(Map<String,Object> paramMap);
	/**
	 * 根据医嘱主键查询医嘱信息
	 * @param paramMap
	 * @return
	 */
    public List<CnOrder> getOrderInfo(List<String> pklist);
    /**
     * 根据物品主键查询物品名称
     * @param pdlist
     * @return
     */
    public List<BdPd> getPdInfo(List<String> pdlist);
	
    /**
     * 根据处方主键查询中草药的煎药费用
     * @param pkPreses
     * @return
     */
    public List<BlPubParamVo> qryBoilBl(List<String> pkPreses);
}
