package com.zebone.nhis.bl.pub.service;

import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.ItemPriceVo;

import java.util.List;

/**
 * 检验计费通用接口
 */
public interface ICgLabService {

    /**
     * 处理检验标本、容器费用记录
     * @param blCgPubParamVos
     * @return
     */
    void dealSampAndTubeItem(List<BlPubParamVo> blCgPubParamVos);

    /**
     * 检验医嘱收费特殊处理
     * @param priceList
     * @param itemList
     */
    List<ItemPriceVo> filterOrdItem(List<ItemPriceVo> priceList,List<ItemPriceVo> itemList);

    /**
     * 检查医嘱特殊处理
     * @param priceList
     */
    void filterExOrdItem(List<ItemPriceVo> priceList);
}
