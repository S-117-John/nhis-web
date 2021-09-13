package com.zebone.nhis.cn.ipdw.vo;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * 适应症--参数列表
 */
public class CnIndicationVo {

    private List<CnOrder> order;

    public List<CnOrder> getOrder() {
        return order;
    }

    public void setOrder(List<CnOrder> order) {
        this.order = order;
    }
}
