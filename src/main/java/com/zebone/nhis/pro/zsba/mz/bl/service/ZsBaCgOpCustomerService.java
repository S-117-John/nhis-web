package com.zebone.nhis.pro.zsba.mz.bl.service;

import com.google.common.collect.Lists;
import com.zebone.nhis.bl.pub.syx.service.CgOpCustomerService;
import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("zsBaCgOpCustomerService")
public class ZsBaCgOpCustomerService extends CgOpCustomerService {

    /**
     *
     * @param allPriceList
     * @return 返回科研医嘱+自备药医嘱(不走计费策略）
     */
    @Override
    protected List<ItemPriceVo> filterIterPriceCustomer(List<ItemPriceVo> allPriceList) {
        List<ItemPriceVo> items = Lists.newArrayList();
        items.addAll(allPriceList.stream().filter(v -> EnumerateParameter.ONE.equals(v.getEuOrdtype())).collect(Collectors.toList()));
        return items;
    }

    @Override
    public Double calcAmountPi(ItemPriceVo priceVo, BlOpDt bod) {
        if (priceVo.getMaxQuan() == null || priceVo.getMaxQuan() == 0
                || bod.getQuan().compareTo(priceVo.getMaxQuan()) <= 0) {
            return super.calcAmountPi(priceVo, bod);
        }

        //超过最大数量限制的部分，不享受优惠算法
        double diff = MathUtils.sub(bod.getQuan(), priceVo.getMaxQuan());
        if (!"1".equals(priceVo.getFlagHppi())){
            bod.setAmountHppi(MathUtils.add(MathUtils.mul(MathUtils.mul(bod.getPrice(), priceVo.getMaxQuan()), bod.getRatioSelf()), MathUtils.mul(bod.getPrice(),diff)));
        }
        double amount = super.calcAmountPi(priceVo, bod);
        bod.setRatioSelf(MathUtils.div(amount, MathUtils.mul(bod.getPriceOrg(), bod.getQuan())));
        return amount;
    }
}
