package com.zebone.nhis.bl.pub.syx.service;

import com.google.common.collect.Lists;
import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.support.MathUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "cgOpCustomerService")
public class CgOpCustomerService {

    /**
     * 通用计费前置过滤方法,永远不返回null
     * @param allPriceList
     * @return
     */
    public List<ItemPriceVo> filterIterPrice(List<ItemPriceVo> allPriceList){
        //a. do common filter here
        List<ItemPriceVo> changed = Lists.newArrayList();
        //自备药价格全部设置为0，也不会走计费策略
        changed.addAll(allPriceList.stream().filter(v -> "0".equals(v.getEuAdditem()) && "1".equals(v.getFlagPd()) && "1".equals(v.getFlagSelf()))
                .map(v -> {
                    v.setPrice(0D);
                    v.setPriceOrg(0D);
                    v.setPriceCs(0D);
                    return v;
                }).collect(Collectors.toList()));
        //b. invoke subMethod
        changed.addAll(filterIterPriceCustomer(allPriceList));
        return changed;
    }

    /**
     *<b>subclass rewrite this method <b/>
     * @param allPriceList
     * @return
     */
    protected List<ItemPriceVo> filterIterPriceCustomer(List<ItemPriceVo> allPriceList){
        return Lists.newArrayList();
    }

    /**
     * 计算单个计费-患者自费金额
     * @param priceVo
     * @param bod
     * @return
     */
    public Double calcAmountPi(ItemPriceVo priceVo,BlOpDt bod) {
        return MathUtils.sub(bod.getAmountHppi(), MathUtils.mul(MathUtils.mul(bod.getPriceOrg(), MathUtils.sub(1D, bod.getRatioDisc())), bod.getQuan()));
    }
}
