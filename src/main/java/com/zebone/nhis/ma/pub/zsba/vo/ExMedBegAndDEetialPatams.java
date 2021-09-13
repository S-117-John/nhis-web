package com.zebone.nhis.ma.pub.zsba.vo;

import java.util.List;

/**
 * @Classname ExMedBegAndDEetialPatams
 * 药袋和药袋明细
 * @Description TODO
 * @Date 2020-04-24 11:27
 * @Created by wuqiang
 */
public class ExMedBegAndDEetialPatams {
    /**
     * 药袋
     */

    private    List<ExMedBag> exMedBags;

    /**
     * 药袋明细
     */
    private  List<ExMedBagDetail > exMedBagDetails;

    public ExMedBegAndDEetialPatams(List<ExMedBag> exMedBags,List<ExMedBagDetail> exMedBagDetails){

        this.exMedBags=exMedBags;
        this.exMedBagDetails=exMedBagDetails;
    }

    public List<ExMedBag> getExMedBags() {
        return exMedBags;
    }

    public void setExMedBags(List<ExMedBag> exMedBags) {
        this.exMedBags = exMedBags;
    }

    public List<ExMedBagDetail> getExMedBagDetails() {
        return exMedBagDetails;
    }

    public void setExMedBagDetails(List<ExMedBagDetail> exMedBagDetails) {
        this.exMedBagDetails = exMedBagDetails;
    }





}
