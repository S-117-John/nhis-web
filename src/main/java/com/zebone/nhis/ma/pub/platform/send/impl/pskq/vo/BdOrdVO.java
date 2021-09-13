package com.zebone.nhis.ma.pub.platform.send.impl.pskq.vo;

import com.zebone.nhis.base.bd.vo.BdItemVO;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.base.bd.srv.BdOrd;

import java.util.List;

public class BdOrdVO extends BdOrd{



    private List<BdItemVO> bdItemVOList;



    public List<BdItemVO> getBdItemVOList() {
        return bdItemVOList;
    }

    public void setBdItemVOList(List<BdItemVO> bdItemVOList) {
        this.bdItemVOList = bdItemVOList;
    }
}
