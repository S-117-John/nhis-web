package com.zebone.nhis.pro.zsrm.cn.vo;

import java.util.List;

/**
 * 草药相关参数
 */
public class HerbVo {
    /**查询草药明细对应的转换列表入参**/
    private List<String> pkPds; //药品主键
    /****/


    public List<String> getPkPds() {
        return pkPds;
    }

    public void setPkPds(List<String> pkPds) {
        this.pkPds = pkPds;
    }
}
