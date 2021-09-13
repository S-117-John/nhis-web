package com.zebone.nhis.webservice.zhongshan.vo;

import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * @Classname ThiCancelIpCharge
 * @Description 07号接口业务入参
 * @Date 2021-04-08 11:27
 * @Created by wuqiang
 */
public class ThiCancelIpCharge {
    /**
     *退费人编码
     */
    @NotBlank(message = "退费人不能为空")

    private  String pkEmpCg;
    /**
     *记费部门编码
     */
    @NotBlank(message = "退费部门不能为空")
    private  String pkDeptCg;

    /**
     *退费详情
     */
    @NotBlank(message = "退费详情不能为空")
    private List<ThiIpChargeRet> dataCgs;

    @Override
    public String toString() {
        return "ThiCancelIpCharge{" +
                "pkEmpCg='" + pkEmpCg + '\'' +
                ", pkDeptCg='" + pkDeptCg + '\'' +
                ", dataCgs=" + dataCgs +
                '}';
    }

    public List<ThiIpChargeRet> getDataCgs() {
        return dataCgs;
    }

    public void setDataCgs(List<ThiIpChargeRet> dataCgs) {
        this.dataCgs = dataCgs;
    }


    public String getPkDeptCg() {
        return pkDeptCg;
    }

    public void setPkDeptCg(String pkDeptCg) {
        this.pkDeptCg = pkDeptCg;
    }

    public String getPkEmpCg() {
        return pkEmpCg;
    }

    public void setPkEmpCg(String pkEmpCg) {
        this.pkEmpCg = pkEmpCg;
    }
}
