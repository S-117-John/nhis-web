package com.zebone.nhis.pi.acc.vo;

import java.util.List;
import java.util.Map;

/**
 * @Classname PagePiaccVo
 * @Description 用作患者建档患者信息查询分页使用
 * @Date 2019-11-06 1:04
 * @Created by wuqiang
 */
public class PagePiaccVo {
    public List<Map<String, Object>> getPiccPiList() {
        return piccPiList;
    }

    public void setPiccPiList(List<Map<String, Object>> piccPiList) {
        this.piccPiList = piccPiList;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 页面所需数据，picc整个表，pi_master部分数据
     */
    private List<Map<String, Object>> piccPiList;
    /**
     * 分页总数
     */
    private int totalCount;

}
