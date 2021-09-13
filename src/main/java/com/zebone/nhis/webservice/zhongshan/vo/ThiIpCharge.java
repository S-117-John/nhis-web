package com.zebone.nhis.webservice.zhongshan.vo;

import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * @Classname ThiIpCharge
 * @Description 06 号接口业务入参
 * @Date 2021-04-08 10:32
 * @Created by wuqiang
 */
public class ThiIpCharge {

    /**
     *记费部门编码
     */
    @NotBlank(message = "记费部门编码不能为空")
    private  String pkDeptCg;
    /**
     *记费人编码
     */
    @NotBlank(message = "记费人编码不能为空")
    private  String pkEmpCg;
    /**
     *计费详情
     */
    @NotBlank(message = "计费详情不能为空")
    private List< ThiCgItem > cgItems;



    public List<ThiCgItem> getCgItems() {
        return cgItems;
    }

    public void setCgItems(List<ThiCgItem> cgItems) {
        this.cgItems = cgItems;
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

