package com.zebone.nhis.pi.acc.vo;

import com.zebone.nhis.common.module.bl.BlDepositPi;

import java.util.List;

/**
 * @Classname ChangeCardParam
 * @Description 用来接收销卡退卡数据
 * * @Date 2019-11-28 16:25
 * @Created by wuqiang
 */
public class ChangeCardParam {

    /** 卡号*/
    public String  pkPiCard ;
    /** 动作状态*/
    public String status;

    public String getPkPiCard() {
        return pkPiCard;
    }

    public void setPkPiCard(String pkPiCard) {
        this.pkPiCard = pkPiCard;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BlDepositPi> getBlDepositPiList() {
        return blDepositPiList;
    }

    public void setBlDepositPiList(List<BlDepositPi> blDepositPiList) {
        this.blDepositPiList = blDepositPiList;
    }

    /**  组装好的退款数据*/
    public List<BlDepositPi> blDepositPiList;

}
