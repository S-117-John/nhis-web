package com.zebone.nhis.ma.pub.zsba.vo;

/**
 * @Classname BagEuStatus 药袋类型枚举
 * @Description TODO
 * @Date 2020-04-08 17:42
 * @Created by wuqiang
 */
public enum BagEuStatus {
    /** 摆药机药袋 */
    MACH("01"),
    /** 人工药袋 */
    ARTI("02");

    private String status;

    private BagEuStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static BagEuStatus getEuStatuValue(String status) {
        if(status == null || "".equals(status))
        {return null;}
        if(MACH.getStatus().equals(status))
        {return MACH;}
        if(ARTI.getStatus().equals(status))
        {return ARTI;}

        return null;
    }


}
